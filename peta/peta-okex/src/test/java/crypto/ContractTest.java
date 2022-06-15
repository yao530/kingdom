package crypto;

import com.ltu.okexchain.env.EnvInstance;

import com.ltu.service.BlockchainPubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.util.ArrayList;
import java.util.List;

public class ContractTest {


    private static Web3j web3j;

    private static Admin admin;

    private static String fromAddress = "0x7ebb2594de6902FdbAEB626fcD63Ff4b2bB0FDC9";

    private static String contractAddress = "0x34be96C774aCC8bc208250213da12dBE51960E22";

    private static String emptyAddress = "0x0000000000000000000000000000000000000000";




    public static void main(String[] args) throws Exception {

        web3j = Web3j.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));
        admin = Admin.build(new HttpService(EnvInstance.getEnv().GetRestServerUrl()));


        String methodName = "mint";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        Address tAddress = new Address("0x7ebb2594de6902FdbAEB626fcD63Ff4b2bB0FDC9");

        Uint256 value = new Uint256(10);
        inputParameters.add(tAddress);
        inputParameters.add(value);

        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);


        Function function = new Function(methodName, inputParameters, outputParameters);

        String txData = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create("8916523ebdf889a744485baf7bccc4dbd897ebb3c45ae8290fbd090c32433467");
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
    }





}
