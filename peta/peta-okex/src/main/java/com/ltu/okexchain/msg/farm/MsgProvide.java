package com.ltu.okexchain.msg.farm;

import com.alibaba.fastjson.JSONObject;
import com.ltu.okexchain.env.EnvInstance;
import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.msg.common.Token;
import com.ltu.okexchain.utils.Utils;
import com.ltu.okexchain.utils.crypto.PrivateKey;

public class MsgProvide extends MsgBase {

    public MsgProvide() { setMsgType("okexchain/farm/MsgProvide"); }

    public Message produceMsg(String amount, String denom, String amountYieldedPerBlock, String poolName, String startHeightToYield) {

        Token token = new Token();
        token.setAmount(Utils.NewDecString(amount));
        token.setDenom(denom);

        MsgProvideValue value = new MsgProvideValue();

        value.setAddress(this.address);
        value.setAmount(token);
        value.setAmountYieldedPerBlock(Utils.NewDecString(amountYieldedPerBlock));
        value.setPoolName(poolName);
        value.setStartHeightToYield(startHeightToYield);

        Message<MsgProvideValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }

    public static void main(String[] args) {

        EnvInstance.getEnv().setChainID("okexchainevm-8");
        EnvInstance.getEnv().setRestServerUrl("http://localhost:8545");

        PrivateKey key = new PrivateKey("EA6D97F31E4B70663594DD6AFC3E3550AAB5FDD9C44305E8F8F2003023B27FDA");

        MsgProvide msg = new MsgProvide();
        msg.init(key);

        Message messages = msg.produceMsg(
                "10",
                "okt",
                "5",
                "turing-pool-1",
                "4270"
        );
        JSONObject res = msg.submit(messages, "0.05", "500000", "provide");

        try {
            boolean succeed = msg.isTxSucceed(res);
            System.out.println("tx " + (succeed ? "succeed": "failed"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
