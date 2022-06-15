package com.ltu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.constant.rabbitmq.RabbitConstants;
import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.mapper.UserBlockchainInfoMapper;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;
import com.ltu.service.BlockchainPubService;
import com.ltu.service.UserBlockchainInfoService;
import com.ltu.util.datetime.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import javax.annotation.Resource;


/**
 * <p>
 * 用户区块链信息 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-06
 */
@Service
public class UserBlockchainInfoServiceImpl extends BaseServiceImpl<UserBlockchainInfoMapper, UserBlockchainInfoEntity> implements UserBlockchainInfoService {



    @Autowired
    BlockchainPubService blockchainPubService;


    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @desc 给用户创建钱包地址、私钥、公钥、助记词
     * @param
     * </p>
     */
    public void saveOrUpdate(RegistBlockChainAddressReq req) {


        UserBlockchainInfoEntity infoEntity = getUserBlockchainInfo(req.getUserId());
        if (infoEntity==null)
            infoEntity = new UserBlockchainInfoEntity();
        infoEntity.setCreateTime(DateUtils.currentSecs());
        infoEntity.setUserId(req.getUserId());
        String password = req.getPhone().substring(3,9);//取手机号3～8位 13060687032
        System.out.println(password);
        try {
            Credentials credentials = blockchainPubService.createWalletFile(password);
            if (credentials!=null){
                infoEntity.setUserBlockchainAddress(credentials.getAddress());
                ECKeyPair ecKeyPair = credentials.getEcKeyPair();
                //infoEntity.setUserPrivateKey(ecKeyPair.getPrivateKey().toString());//暂时保存私钥
                System.out.println("创建用户钱包信息成功，用户是 ："+req.getPhone()+",钱包地址是："+credentials.getAddress());
                req.setSmartContractAddress(credentials.getAddress());
                req.setResultDesc("创建钱包信息成功");
                req.setStatus(1);
                //给API回调 并记录信息
        		rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_QUEUE2, RabbitConstants.DEAD_QUEUE2 , JSON.toJSONString(req));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            Credentials credentials = blockchainPubService.getCredentials(userEntity,infoEntity);
//            if (credentials!=null){
//                infoEntity.setUserBlockchainAddress(credentials.getAddress());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        super.saveOrUpdate(infoEntity);
    }

    private UserBlockchainInfoEntity getUserBlockchainInfo(Integer userId){
        QueryWrapper<UserBlockchainInfoEntity> condition = new QueryWrapper<>();
        condition.like("user_id",userId);

        return super.getOne(condition);
    }

}
