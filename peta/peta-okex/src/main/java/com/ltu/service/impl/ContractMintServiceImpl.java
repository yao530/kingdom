package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.mapper.ContractMintMapper;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.service.ContractMintService;
import com.ltu.service.ContractService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MathUtils;
import com.ltu.util.common.UUIDGenerator;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 铸造任务 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-09
 */
@Service
public class ContractMintServiceImpl extends BaseServiceImpl<ContractMintMapper, ContractMintEntity> implements ContractMintService {


    private final static String MINT_CONTRACT = "Mint_Contract_";//mint合约redis存储key

    @Autowired
    RedistUtil redistUtil;
    @Autowired
    ContractService contractService;

    /**
     * @desc nft铸造  需要根据总数量分批铸造
     * 铸造完成后，加入到redis list
     **/
    public void createMintJob(MintReq mintReq) throws Exception{

            ContractMintEntity newMint = new ContractMintEntity();
            BeanMapper.copy(mintReq,newMint);
            newMint.setMintCounter(1);
            newMint.setMainMint(1);
            newMint.setMintStatus(0);
            newMint.setMintDesc("铸造合约："+mintReq.getSmartContractAddress()+"批次："+newMint.getMintCounter());
            newMint.setCreateTime(DateUtils.currentMillis());

            super.saveOrUpdate(newMint);

            redistUtil.setValue(MINT_CONTRACT+mintReq.getSmartContractAddress(),mintReq);

    }

    public List<ContractMintEntity> getUnfinshMint(){
        QueryWrapper<ContractMintEntity> condition = new QueryWrapper<>();
        condition.eq("main_mint",1);
        condition.eq("mint_status",0);

        return this.list(condition);
    }

}
