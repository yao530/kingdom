package com.ltu.service;


import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.domain.mp_entity.UserEntity;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 区块链服务
 * </p>
 *
 * @author ruochen@cxmx123
 * @since 2021-10-08
 */

public interface BlockchainPubService {
    /**
     * @desc 生成12位助记词
     * @param
     * </p>
     */
    String generateMemoryMonic();
    /**
     * @desc 根据12位助记词，生成私钥
     * @param
     * </p>
     */
    String generatePrivateKeyFromMnemonic(String mnemoic);


    /**
     * @desc 根据私钥,解析公钥
     * @param
     * </p>
     */
     String generatePublicKey(String prikey);

    Credentials createWalletFile(String password) throws Exception;
    Credentials getCredentials(String address,String password) throws IOException, CipherException;

    /**
     * @desc 根据私钥,解析钱包地址
     * @param
     * </p>
     */
    public String generateAddress(String prikey);

    String converEthAddress(String bechAddress);
    BigInteger getTokenBalance(String fromAddress, String contractAddress);
    String getTokenName(String contractAddress);
    BigInteger getTotalSupply(String contractAddress);

    TransactionReceipt sendContractMethmod(String contractAddress, String methodName, List<Type> inputParameters, List<TypeReference<?>> outputParameters, Credentials credentials) throws Exception;
}
