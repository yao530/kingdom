package com.ltu.service.impl;


import com.alibaba.fastjson.JSON;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.rabbitmq.RabbitConstants;
import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.domain.mp_entity.MintToTransferEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.MintItemDto;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.HexUtil;
import com.ltu.util.common.MathUtils;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import javax.crypto.Mac;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 合约交互 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContractServiceImpl implements ContractService {

    private final static String prikey = "6d0a8c73351b61da879fcde1393c8d39ea16b3d1e34805a5f6237955cee7a5c7";//测试管理员账号

    private final static String MINT_METHORD="mint";//铸造方法

    private final static String TRANSFER_METHORD="safeTransferFrom";//交易方法

    private final static Integer MAX_MINT = 20;//每批铸造数量 与合约对应

    private final static String MINT_CONTRACT = "Mint_Contract_";//mint合约redis存储key



    private final static String TRANSFER_ID_COUNT = "Transfer_id_";//交易nft记录ID 重复检查计数 重复交易3次 后不在挂起交易任务

    private final static Integer TRANSFER_FAIL_COUNT = 3;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    BlockchainPubService blockchainPubService;

    @Autowired
    BlockchainLogsInfoService blockchainLogsInfoService;

    @Autowired
    ContractMintService contractMintService;

    @Autowired
    ContractTransferService contractTransferService;

    @Autowired
    RedistUtil redistUtil;

    @Autowired
    MintToTransferService mintToTransferService;

    /**
     * @desc 按批铸造 如10000份 分200批铸造  每批50个
     * 铸造完成后，加入到redis list
     **/
    public void mint(ContractMintEntity contractMintEntity) throws Exception {

        ContractMintEntity mintReq = new ContractMintEntity();
        mintReq.setSmartContractAddress(contractMintEntity.getSmartContractAddress());
        mintReq.setContractType(contractMintEntity.getContractType());
        mintReq.setMainMint(0);
        mintReq.setBlockchainType(contractMintEntity.getBlockchainType());
        mintReq.setCreateTime(DateUtils.currentMillis());
        mintReq.setThingId(mintReq.getThingId());
        mintReq.setMintStartTime(DateUtils.currentMillis());
        try {
            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            Uint256 numberOfTokens = new Uint256(contractMintEntity.getMintAmount()>=MAX_MINT?MAX_MINT:contractMintEntity.getMintAmount());
            inputParameters.add(numberOfTokens);
            TypeReference<Bool> typeReference = new TypeReference<Bool>() {};
            outputParameters.add(typeReference);
            Credentials credentials =Credentials.create(prikey);
            TransactionReceipt receipt=  blockchainPubService.sendContractMethmod(contractMintEntity.getSmartContractAddress(),
                    MINT_METHORD,inputParameters,outputParameters,credentials);
            blockchainLogsInfoService.createTransationInfo(MINT_METHORD,contractMintEntity.getSmartContractAddress(),receipt);
            if (receipt.isStatusOK()){
                List<Log> logs = receipt.getLogs();
                List<MintItemDto> mintItemDtos = getMintLogs(logs);

                System.out.println("铸造结果-------"+mintItemDtos);
                mintReq.setMintStatus(2);
                mintReq.setMintDesc("铸造成功");
                mintReq.setTotalMint(mintItemDtos.size());
                mintReq.setMintLogs(JSON.toJSONString(mintItemDtos));
                mintReq.setMintEndTime(DateUtils.currentMillis());
                contractMintEntity.setTotalMint(contractMintEntity.getTotalMint()+mintItemDtos.size());
                contractMintEntity.setUpdateTime(DateUtils.currentMillis());
                contractMintService.saveOrUpdate(mintReq);
                mintToTransferService.createMintSusscessRecord(mintReq,mintItemDtos);
                //给API回调 铸造完成
                if (contractMintEntity.getTotalMint()>=contractMintEntity.getMintAmount()){
                    contractMintEntity.setMintStatus(2);
                    rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_MINT_RETURN_QUEUE4, RabbitConstants.MINT_RETURN_DEAD_QUEUE , JSON.toJSONString(mintReq));
                }
                contractMintService.saveOrUpdate(contractMintEntity);
            }else {
                mintReq.setMintAmount(3);
            }
        }catch (Exception e){

        }
    }


    /**
     * @desc nft交易  平台首次交易给用户 、用户转赠
     * 1、平台交易给用户 nftID 由系统维护
     * 2、用户转赠需要指定转赠的nftIDMintToTransferEntity getNftId(ContractTransferEntity transferReq)
     **/
    public void transfer(ContractTransferEntity transferReq) throws Exception {
        try {
            Credentials credentials =null;//转赠用被转赠用户的签名
            MintToTransferEntity mint = null;//p2c可交易的记录
            Integer nftId=transferReq.getNftId();//首次交易nftid 由平台提供
            if (transferReq.getTransferType()== NFTConstant.TRANSFER_TYPE_P2C){
                credentials=Credentials.create(prikey);
                transferReq.setFromAddress(credentials.getAddress());
                mint =mintToTransferService.getNftId(transferReq);
                if (mint!=null)
                    nftId = mint.getNftId();
            }
            if (transferReq.getTransferType()==NFTConstant.TRANSFER_TYPE_C2C){
                credentials = blockchainPubService.getCredentials(transferReq.getFromAddress(),transferReq.getFromUserPhone().substring(3,9));
            }
            transferReq.setResultDesc("交易nft的ID是："+nftId);
            if (StrUtils.isVaileNum(nftId)){
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();
                Address fromAddress = new Address(credentials.getAddress());
                Address tAddress = new Address(transferReq.getToAddress());
                Uint256 tokenId = new Uint256(nftId);
                inputParameters.add(fromAddress);
                inputParameters.add(tAddress);
                inputParameters.add(tokenId);

                TypeReference<Bool> typeReference = new TypeReference<Bool>() {};
                outputParameters.add(typeReference);

                TransactionReceipt receipt=  blockchainPubService.sendContractMethmod(transferReq.getSmartContractAddress(),
                        TRANSFER_METHORD,inputParameters,outputParameters,credentials);
                blockchainLogsInfoService.createTransationInfo(TRANSFER_METHORD,transferReq.getSmartContractAddress(),receipt);
                if (receipt.isStatusOK()){
                    List<Log> logs = receipt.getLogs();
                    List<MintItemDto> mintItemDtos = getMintLogs(logs);
                    System.out.println(mintItemDtos);
                    transferReq.setMintStatus(2);
                    transferReq.setNftId(nftId);
                    transferReq.setMintLogs(JSON.toJSONString(mintItemDtos));
                    transferReq.setResultDesc("交易nft成功");

                    contractTransferService.saveOrUpdate(transferReq);
                    if (mint!=null){
                        mint.setTransferStatus(1);
                        mint.setStatus(0);
                        mintToTransferService.saveOrUpdate(mint);
                    }

                    TransferReq req = new TransferReq();
                    BeanMapper.copy(transferReq,req);
                    req.setContractTransferId(transferReq.getId());
                    req.setTxHash(receipt.getTransactionHash());
                    //返回回调
                    rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_TRANSFER_RETURN_QUEUE3, RabbitConstants.TRANSFER_RETURN_DEAD_QUEUE , JSON.toJSONString(req));
                }else {
                    transferReq.setStatus(3);//挂起任务
                    Integer count =  (Integer) redistUtil.getValue(TRANSFER_ID_COUNT+transferReq.getId());
                    if (!StrUtils.isVaileNum(count)&&(StrUtils.isVaileNum(count)&&count<=TRANSFER_FAIL_COUNT)){
                        redistUtil.setValue(TRANSFER_ID_COUNT+transferReq.getId(),StrUtils.isVaileNum(count)? count+1:1,3600l);
                        rabbitTemplate.convertAndSend(null, RabbitConstants.TIME_OUT_OKEX_TRANSFER , JSON.toJSONString(transferReq));
                    }
                }
            }else {
                System.out.println("交易错误:"+transferReq);
            }

        }catch (Exception e){

        }
    }

//    private Integer getTransferTokenId(String smartContractAddress){
//        Integer tokenId = (Integer)redisTemplate.opsForList().rightPop(NFT_CONTRACT_SUPPLY+smartContractAddress);
//
//    }


    /**
     * @desc 读取mint记录，并把结果日志转换为铸造记录
     *
     * @param logs
     * @return
     */
    private  List<MintItemDto> getMintLogs(List<Log> logs){
        List<MintItemDto> list = new ArrayList<>();
        if (logs.size()>0){
            for (Log log:logs){
                MintItemDto mintItemDto = getMintDto(log.getTopics());
                list.add(mintItemDto);
            }
        }
        return list;
    }

    private static MintItemDto getMintDto(List<String> topics){
        MintItemDto itemDto = new MintItemDto();
        itemDto.setNftId(hexString2binaryString(topics.get(3)));
        String address = topics.get(2);
        itemDto.setToAddress("0x"+address.substring(26));
        return itemDto;
    }



    /**
     * 将16进制转换10进制
     *
     * @param hexString
     * @return
     */
    private static Integer hexString2binaryString(String hexString) {
        //16进制转10进制
        BigInteger sint = new BigInteger(Numeric.cleanHexPrefix(hexString), 16);
        System.out.println("nftId: " +sint);
        return sint.intValue();
    }


}
