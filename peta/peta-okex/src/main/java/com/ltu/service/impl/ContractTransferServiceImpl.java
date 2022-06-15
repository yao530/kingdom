package com.ltu.service.impl;

import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.mapper.ContractTransferMapper;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.okexchain.utils.exception.BuildException;
import com.ltu.service.ContractService;
import com.ltu.service.ContractTransferService;
import com.ltu.util.BeanMapper;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nft合约交易记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-10
 */
@Service
public class ContractTransferServiceImpl extends BaseServiceImpl<ContractTransferMapper, ContractTransferEntity> implements ContractTransferService {


      @Autowired
      ContractService contractService;

    public  void saveTransferRecord(TransferReq transferReq) throws Exception{
        try {
            ContractTransferEntity contractTransferEntity = new ContractTransferEntity();
            BeanMapper.copy(transferReq,contractTransferEntity);
            contractTransferEntity.setMintStatus(1);
            contractTransferEntity.setCreateTime(DateUtils.currentMillis());
            super.save(contractTransferEntity);
            contractService.transfer(contractTransferEntity);
        }catch (Exception e){

        }
   }
}
