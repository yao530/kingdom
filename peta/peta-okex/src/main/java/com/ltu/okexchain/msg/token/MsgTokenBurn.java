package com.ltu.okexchain.msg.token;

import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.msg.common.Token;
import com.ltu.okexchain.utils.Utils;

public class MsgTokenBurn extends MsgBase {

    public MsgTokenBurn() { setMsgType("okexchain/token/MsgBurn");}

    public Message produceTokenBurnMsg(String denom, String amountDenom, String owner) {

        Token amount = new Token();
        amount.setDenom(denom);
        amount.setAmount(Utils.NewDecString(amountDenom));

        MsgTokenBurnValue value = new MsgTokenBurnValue();
        value.setAmount(amount);
        value.setOwner(owner);

        Message<MsgTokenBurnValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);

        return msg;
    }

}