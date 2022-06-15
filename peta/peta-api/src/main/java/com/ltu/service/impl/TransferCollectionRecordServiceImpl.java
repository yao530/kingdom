package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.QrCodeConstant;
import com.ltu.domain.mp_entity.TransferCollectionRecordEntity;
import com.ltu.domain.mp_entity.UserCollectionsEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.mapper.TransferCollectionRecordMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.request.weixin.QrCodeReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class TransferCollectionRecordServiceImpl extends BaseServiceImpl<TransferCollectionRecordMapper, TransferCollectionRecordEntity> implements TransferCollectionRecordService {



    @Autowired
    UserCollectionsService userCollectionsService;
    @Autowired
    UserService userService;
    @Autowired
    WxPubService wxPubService;
    @Autowired
    RedistUtil redistUtil;

    @Autowired
    ContractSquService contractSquService;


    private final static Long EXPIRE_TIME = 3600*24L;
    private final static String TRANSFER_QR_CODE = "TRANSFER_QR_CODE:";


   public CodeDataResp makeTransfer(BaseIdReq baseIdReq){

       UserDto userDto = ShiroUtil.getPrincipalUser();
       if (userDto==null)
           return CodeDataResp.valueOfFailed("用户数据非法");
       UserEntity userEntity = userService.getById(userDto.getId());
       if (userEntity==null)
           return CodeDataResp.valueOfFailed("用户数据非法");

       UserCollectionsEntity collectionEntity = userCollectionsService.getById(baseIdReq.getId());
       if (collectionEntity==null)
           return CodeDataResp.valueOfFailed("藏品数据非法");
       if (collectionEntity.getStatus()!=1)
           return CodeDataResp.valueOfFailed("藏品非法");
       if (collectionEntity.getUserId()!=userEntity.getId())
           return CodeDataResp.valueOfFailed("藏品不属于你");
        TransferCollectionRecordEntity transferCollectionRecordEntity = createRecord(userEntity,userEntity,collectionEntity,NFTConstant.TRANSFER_TYPE_C2C);
        transferCollectionRecordEntity.setFromUserCollectionId(collectionEntity.getId());
        transferCollectionRecordEntity.setMintStatus(NFTConstant.MINT_STATUS_ING_MINT);
        super.saveOrUpdate(transferCollectionRecordEntity);

       contractSquService.transferNft(transferCollectionRecordEntity,userEntity);

       return CodeDataResp.valueOfSuccessEmptyData();
   }


    /**
     * @desc nft交易  平台首次交易给用户
     * 平台交易给用户 nftID 由系统维护
     **/
   public void mintUserCollection(UserEntity toUser,UserCollectionsEntity userCollectionsEntity,Integer transferType){
       TransferCollectionRecordEntity transferCollectionRecordEntity = createRecord(null,toUser,userCollectionsEntity,transferType);
       transferCollectionRecordEntity.setToUserCollectionId(userCollectionsEntity.getId());

       super.saveOrUpdate(transferCollectionRecordEntity);
       contractSquService.transferNft(transferCollectionRecordEntity,null);
   }

    /**
     * @desc nft交易记录
     * @param fromUser 交易发起用户 转赠则不为空
     * @param toUser 被交易用户
     * @param
     *
     **/
    private TransferCollectionRecordEntity createRecord(UserEntity fromUser,UserEntity toUser,UserCollectionsEntity userCollectionsEntity,Integer transferType){
        TransferCollectionRecordEntity transferCollectionRecordEntity = new TransferCollectionRecordEntity();
        transferCollectionRecordEntity.setTransferType(transferType);
        if (fromUser!=null){
            transferCollectionRecordEntity.setFromUserAvatar(fromUser.getUserAvatar());
            transferCollectionRecordEntity.setFromUserId(fromUser.getId());
            transferCollectionRecordEntity.setFromUserName(fromUser.getUserNick());
            transferCollectionRecordEntity.setFromAddress(fromUser.getUserBlockchainAddress());
        }
        transferCollectionRecordEntity.setToUserId(toUser.getId());
        transferCollectionRecordEntity.setToUserName(toUser.getUserNick());
        transferCollectionRecordEntity.setToUserAvatar(toUser.getUserAvatar());
        transferCollectionRecordEntity.setToAddress(toUser.getUserBlockchainAddress());

        transferCollectionRecordEntity.setSmartContractAddress(userCollectionsEntity.getSmartContractAddress());
        transferCollectionRecordEntity.setNftId(userCollectionsEntity.getNftId());
        transferCollectionRecordEntity.setNftIdAmount(userCollectionsEntity.getNftId());


        transferCollectionRecordEntity.setCollectionId(userCollectionsEntity.getCollectionId());
        transferCollectionRecordEntity.setCollectionName(userCollectionsEntity.getCollectionName());
        transferCollectionRecordEntity.setStatus(1);
        transferCollectionRecordEntity.setMintStatus(NFTConstant.MINT_STATUS_WAIT_MINT);
        transferCollectionRecordEntity.setCreateTime(DateUtils.currentSecs());
        transferCollectionRecordEntity.setCollectionCode(userCollectionsEntity.getCollectionCode());
        transferCollectionRecordEntity.setTransferCode(StrUtils.getRandomNumberStr(6));
        //getTransferQrCode(transferCollectionRecordEntity);

        return transferCollectionRecordEntity;
    }


    /**
     * 生成转赠的公众号二维码，需要对参数进行加密
     * @param
     * @return
     **/
    private void getTransferQrCode(TransferCollectionRecordEntity transferCollectionRecordEntity){

        QrCodeReq qrCodeReq = new QrCodeReq();
        qrCodeReq.setCodeType(QrCodeConstant.GIVEN_NFT);
        qrCodeReq.setSceneId(transferCollectionRecordEntity.getTransferCode());
        qrCodeReq.setExpireTime(EXPIRE_TIME);
        String codeUrl = wxPubService.getQrCode(qrCodeReq);
        redistUtil.setValue(TRANSFER_QR_CODE+transferCollectionRecordEntity.getTransferCode(),codeUrl,EXPIRE_TIME);
        transferCollectionRecordEntity.setTransferCodeUrl(codeUrl);
    }


   public List<TransferCollectionRecordEntity> findCollectionTransferRecord(String collectiobCode){
       QueryWrapper<TransferCollectionRecordEntity> condition = new QueryWrapper<>();
       condition.eq("collection_code", collectiobCode);
       condition.eq("status", 2);

       return super.list(condition);

   }
   
}
