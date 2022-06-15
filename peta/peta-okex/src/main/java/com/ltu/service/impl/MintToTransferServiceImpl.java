package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.constant.NFTConstant;
import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.domain.mp_entity.MintToTransferEntity;

import com.ltu.mapper.MintToTransferMapper;
import com.ltu.model.response.base.MintItemDto;
import com.ltu.service.MintToTransferService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 平台交易nft记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-11
 */
@Service
public class MintToTransferServiceImpl extends BaseServiceImpl<MintToTransferMapper, MintToTransferEntity> implements MintToTransferService {


    private final static String LOCK_CONTRACT="Lock_Contract_";
    private final static String NFT_CONTRACT_SUPPLY = "Nft_Contract_Supply_";//合约nft总供应量redis存储key

      @Autowired
      RedistUtil redistUtil;
    @Autowired
    RedisTemplate redisTemplate;

   public void createMintSusscessRecord(ContractMintEntity contractMintEntity, List<MintItemDto> mintItemDtos){
       if (mintItemDtos.size()>0){
           List<MintToTransferEntity> mintToTransferEntityList = new ArrayList<>();
           if (contractMintEntity.getContractType()== NFTConstant.CONTRACT_TYPE_REC721)
               mintToTransferEntityList.addAll(createErc721MintRecord( contractMintEntity,  mintItemDtos));
           if (contractMintEntity.getContractType()==NFTConstant.CONTRACT_TYPE_REC1155)
               mintToTransferEntityList.addAll(createErc721MintRecord( contractMintEntity,  mintItemDtos));

           if (mintToTransferEntityList.size()>0)
               super.saveBatch(mintToTransferEntityList);

       }
   }


   private  List<MintToTransferEntity> createErc721MintRecord(ContractMintEntity contractMintEntity,List<MintItemDto> mintItemDtos){
       List<MintToTransferEntity> mintToTransferEntityList = new ArrayList<>();
       for (MintItemDto itemDto:mintItemDtos){
           MintToTransferEntity mint = new MintToTransferEntity();

           mint.setBlockchainType(contractMintEntity.getBlockchainType());
           mint.setSmartContractAddress(contractMintEntity.getSmartContractAddress());
           mint.setResultDesc("铸造成功");
           mint.setContractType(contractMintEntity.getContractType());
           mint.setFromAddress(itemDto.getToAddress());
           mint.setNftId(itemDto.getNftId());
           mint.setNftIdAmount(itemDto.getNftId());
           mint.setTransferStatus(0);
           mint.setStatus(1);
           mint.setCreateTime(DateUtils.currentMillis());

           mintToTransferEntityList.add(mint);

       }

       return mintToTransferEntityList;
   }

   private  List<MintToTransferEntity> createErc1155MintRecord(ContractMintEntity contractMintEntity,List<MintItemDto> mintItemDtos){
       List<MintToTransferEntity> mintToTransferEntityList = new ArrayList<>();
       MintItemDto itemDto = mintItemDtos.get(0);
       for (int i=0;i<itemDto.getMintAmount();i++){
           MintToTransferEntity mint = new MintToTransferEntity();
           mint.setBlockchainType(contractMintEntity.getBlockchainType());
           mint.setSmartContractAddress(contractMintEntity.getSmartContractAddress());
           mint.setResultDesc("铸造成功");
           mint.setContractType(contractMintEntity.getContractType());
           mint.setFromAddress(itemDto.getToAddress());
           mint.setNftId(itemDto.getNftId());
           mint.setNftIdAmount(i+1);
           mint.setTransferStatus(0);
           mint.setStatus(1);
           mint.setCreateTime(DateUtils.currentMillis());

           mintToTransferEntityList.add(mint);
       }
       return mintToTransferEntityList;
   }

   public MintToTransferEntity getNftId(ContractTransferEntity transferReq){
       Integer id = getTransferId(transferReq);
       if (StrUtils.isVaileNum(id))
           return super.getById(id);
       else
           return null;
   }

    /**
     * @desc 为保证nft的唯一性 需要根据消息队列锁定可交易的nft
     **/
    private Integer getTransferId(ContractTransferEntity transferReq){
        Integer id = 0;
        if (redistUtil.lock(LOCK_CONTRACT+transferReq.getSmartContractAddress(),3l)){
           id =(Integer) redisTemplate.opsForList().leftPop(NFT_CONTRACT_SUPPLY+transferReq.getSmartContractAddress());
           if (id==null){
                List<MintToTransferEntity> list = getContractNftIds(transferReq.getSmartContractAddress());
                if (list.size()>0){
                    List<Integer> ids = list.stream().map(t -> t.getId()).collect(Collectors.toList());
                    redisTemplate.opsForList().rightPushAll(NFT_CONTRACT_SUPPLY+transferReq.getSmartContractAddress(),ids);
                    id = (Integer) redisTemplate.opsForList().leftPop(NFT_CONTRACT_SUPPLY+transferReq.getSmartContractAddress());
                    return id;
                }
           }
        }
        return id;
    }


    private List<MintToTransferEntity> getContractNftIds(String contractAddress){
        QueryWrapper<MintToTransferEntity> condition = new QueryWrapper<>();
        condition.eq("smart_contract_address",contractAddress);
        condition.eq("transfer_status",0);
        condition.eq("status",1);
        return super.list(condition);
    }

}
