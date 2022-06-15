package com.ltu.okexchain.msg.farm;

import com.alibaba.fastjson.JSONObject;
import com.ltu.okexchain.env.EnvInstance;
import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.msg.common.Token;
import com.ltu.okexchain.utils.Utils;
import com.ltu.okexchain.utils.crypto.PrivateKey;

public class MsgLock extends MsgBase {

    public MsgLock() { setMsgType("okexchain/farm/MsgLock"); }

    public Message produceMsg(String amount, String denom, String poolName) {

        Token token = new Token();
        token.setAmount(Utils.NewDecString(amount));
        token.setDenom(denom);

        MsgLockValue value = new MsgLockValue();

        value.setAddress(this.address);
        value.setAmount(token);
        value.setPoolName(poolName);

        Message<MsgLockValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }

    public static void main(String[] args) {

        EnvInstance.getEnv().setChainID("okexchainevm-8");
        EnvInstance.getEnv().setRestServerUrl("http://localhost:8545");

        PrivateKey key = new PrivateKey("EA6D97F31E4B70663594DD6AFC3E3550AAB5FDD9C44305E8F8F2003023B27FDA");

        MsgLock msg = new MsgLock();
        msg.init(key);

        Message messages = msg.produceMsg(
                "200",
                "okt",
                "turing-pool-1"
        );
        JSONObject res = msg.submit(messages, "0.05", "500000", "lock asset!");

        try {
            boolean succeed = msg.isTxSucceed(res);
            System.out.println("tx " + (succeed ? "succeed": "failed"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
