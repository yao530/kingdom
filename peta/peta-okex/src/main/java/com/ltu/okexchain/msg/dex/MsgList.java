package com.ltu.okexchain.msg.dex;

import com.ltu.okexchain.env.EnvInstance;
import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.utils.Utils;

public class MsgList extends MsgBase {

    public MsgList() {
        setMsgType("okexchain/dex/MsgList");
    }

    public static void main(String[] args) {
        EnvInstance.getEnv().setChainID("okexchainevm-8");
        EnvInstance.getEnv().setRestServerUrl("http://localhost:8545");

        MsgList msg = new MsgList();
        msg.initMnemonic("giggle sibling fun arrow elevator spoon blood grocery laugh tortoise culture tool");

        Message messages = msg.produceListMsg(
                "usdk-017",
                "okt",
                "1.00000000");

        // okexchaincli tx dex list --from captain --base-asset eos-a99 --quote-asset okt -y -b block --fees 0.01okt
        msg.submit(messages, "0.05000000", "500000", "okexchain dex list!");
    }

    public Message produceListMsg(String listAsset, String quoteAsset, String initPrice) {

        MsgListValue value = new MsgListValue();
        value.setOwner(this.address);
        value.setListAsset(listAsset);
        value.setQuoteAsset(quoteAsset);
        value.setInitPrice(Utils.NewDecString(initPrice));

        Message<MsgListValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }
}
