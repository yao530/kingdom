package com.ltu.service.impl;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.okexchain.env.EnvInstance;
import com.ltu.okexchain.utils.crypto.AddressUtil;
import com.ltu.okexchain.utils.crypto.Crypto;
import com.ltu.okexchain.utils.evm.ContractUtil;
import com.ltu.service.BlockchainLogsInfoService;
import com.ltu.service.BlockchainPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.crypto.*;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vk@rongding123
 * @since 2020-08-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlockchainPubServiceImpl implements BlockchainPubService {

    private static Web3j web3j;

    private static Admin admin;

    private static String emptyAddress = "0x0000000000000000000000000000000000000000";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String KEYSTORE_SAVEURL="/";

    private static final String READ_KEYSTORE_URL="keystore/keystore_";


    /**
     * @desc 生成12位助记词
     * @param
     * </p>
     */
    public String generateMemoryMonic(){
        return Crypto.generateMnemonic();
    }

    /**
     * @desc 根据12位助记词，生成私钥
     * @param
     * </p>
     */
    public String generatePrivateKeyFromMnemonic(String mnemoic){
        System.out.println(Crypto.generatePrivateKeyFromMnemonic(mnemoic));
        return Crypto.generatePrivateKeyFromMnemonic(mnemoic);
    }

    /**
     * @desc 根据12位助记词，生成加密信息
     * @param
     * </p>
     */
    private ECKeyPair createEcKeyPair(String mnemoic){

        List<String> words = Splitter.on(" ").splitToList(mnemoic);

        byte[] seed = MnemonicCode.INSTANCE.toSeed(words, "");
        DeterministicKey key = HDKeyDerivation.createMasterPrivateKey(seed);

        List<ChildNumber> childNumbers = HDUtils.parsePath(EnvInstance.getEnv().GetHDPath());
        for (ChildNumber cn : childNumbers) {
            key = HDKeyDerivation.deriveChildKey(key, cn);
        }

        return ECKeyPair.create(key.getPrivKey());
    }

    private ECKeyPair createEcKeyPairByPri(){
        String priKey = Crypto.generatePrivateKeyFromMnemonic(generateMemoryMonic());
        System.out.println("pri-key-is:"+priKey);
        Credentials credentials = Credentials.create(priKey);
        return credentials.getEcKeyPair();
    }

    /**
     * @desc 根据用户密码、加密信息生成keystore
     * @param
     * </p>
     */
    public Credentials createWalletFile(String password) throws Exception{
        ECKeyPair ecKeyPair = createEcKeyPairByPri();
        String outPath = System.getProperty("user.dir") + KEYSTORE_SAVEURL;
        WalletFile walletFile = Wallet.createLight(password,ecKeyPair);

        String keystorePath = READ_KEYSTORE_URL+ walletFile.getAddress() + ".json";
        File destination = new File(outPath, keystorePath);
        Credentials credentials = Credentials.create(Wallet.decrypt(password, walletFile));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(destination, walletFile);
        return credentials;
    }

    /**
     * @desc 根据用户密码与钱包地址、查找keystore，获取加密授权
     * @param address
     * @param password
     * </p>
     */
    public Credentials getCredentials(String address,String password) throws IOException, CipherException {

        try {
            String keystorePath = KEYSTORE_SAVEURL+READ_KEYSTORE_URL+ StrUtil.subSuf(address,2)+ ".json";

            File file =  new File( System.getProperty("user.dir")+keystorePath);

            String keyStoreJson = keyStoreJson(file);
            if (keyStoreJson!=null){
                WalletFile walletFile = new ObjectMapper().readValue(keyStoreJson, WalletFile.class);
                Credentials credentials = Credentials.create(Wallet.decrypt(password, walletFile));
                System.out.print(credentials.getAddress());
                return credentials;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @desc 读取keystore的内容
     * @param file
     * </p>
     */
    private String keyStoreJson(File file) throws IOException {

        //创建一个输入流对象
        InputStream is = new FileInputStream(file);

        if (is!=null){
            try {
                //定义一个缓冲区
                byte[] bytes = new byte[1024];// 1kb
                //通过输入流使用read方法读取数据
                int len = is.read(bytes);
                //System.out.println("字节数:"+len);
                String str = null;
                while(len!=-1){
                    //把数据转换为字符串
                    str = new String(bytes, 0, len);
                    //System.out.println(str);
                    //继续进行读取
                    len = is.read(bytes);
                }
                //释放资源
                is.close();
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * @desc 根据私钥,解析公钥
     * @param
     * </p>
     */
    public String generatePublicKey(String prikey){
        return Hex.toHexString(Crypto.generatePubKeyFromPriv(prikey));
    }

    /**
     * @desc 根据私钥,解析钱包地址
     * @param
     * </p>
     */
    public String generateAddress(String prikey){
        return Crypto.generateAddressFromPriv(prikey);
    }

    public String converEthAddress(String bechAddress){
        String ethAddr = AddressUtil.convertAddressFromBech32ToHex(bechAddress);
        System.out.println("ethAddr is : " + ethAddr);
        return ethAddr;
    }

    /**
     * balanceOf
     * read contract with params
     */
    public  BigInteger getTotalSupply(String contractAddress) {
        web3j = Web3j.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        String methodName = "totalSupply";

        List<Type> inputParameters = new ArrayList<>();

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {};
        outputParameters.add(typeReference);

        List<Type> results = ContractUtil.ethCall(web3j, emptyAddress, contractAddress, methodName, inputParameters, outputParameters);
        BigInteger balanceValue = (BigInteger) results.get(0).getValue();
        System.out.println(balanceValue);
        return balanceValue;
    }

    /**
     * balanceOf
     * read contract with params
     */
    public  BigInteger getTokenBalance(String fromAddress, String contractAddress) {
        web3j = Web3j.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        String methodName = "balanceOf";

        List<Type> inputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);


        List<Type> results = ContractUtil.ethCall(web3j, fromAddress, contractAddress, methodName, inputParameters, outputParameters);
        BigInteger balanceValue = (BigInteger) results.get(0).getValue();
        System.out.println(balanceValue);
        return balanceValue;
    }

    /**
     * name
     * read contract with out params
     * @param web3j
     * @param contractAddress
     * @return
     */
    public  String getTokenName( String contractAddress) {
        web3j = Web3j.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        String fromAddr = emptyAddress;
        String methodName = "name";
        List<Type> inputParameters = new ArrayList<>();

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        List<Type> results = ContractUtil.ethCall(web3j, fromAddr, contractAddress, methodName, inputParameters, outputParameters);
        String name = results.get(0).getValue().toString();
        System.out.println(name);
        return name;
    }



    /**
     * @desc 调合约方法
     * @param contractAddress 合约地址
     * @param methodName 合约pubilc方法名称
     * @param inputParameters 输入参数
     * @param outputParameters 输出参数
     * @param credentials 调合约者的私钥
     * </p>
     */
    public TransactionReceipt sendContractMethmod(String contractAddress, String methodName, List<Type> inputParameters, List<TypeReference<?>> outputParameters,Credentials credentials) throws Exception {
        web3j = Web3j.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        admin = Admin.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        try {
            Function function = new Function(methodName, inputParameters, outputParameters);
            String txData = FunctionEncoder.encode(function);
            RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, Byte.valueOf(web3j.netVersion().send().getNetVersion()));//注意传入 chain-id；

            String txHash = txManager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddress,
                    txData,
                    BigInteger.ZERO
            ).getTransactionHash();

            PollingTransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                    web3j,
                    TransactionManager.DEFAULT_POLLING_FREQUENCY,
                    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

            boolean result= receiptProcessor.waitForTransactionReceipt(txHash).isStatusOK();
            System.out.println(result);
            web3j.shutdown();
            return receiptProcessor.waitForTransactionReceipt(txHash);
        }catch (Exception e){
            throw new Exception(e);
        }


    }


}
