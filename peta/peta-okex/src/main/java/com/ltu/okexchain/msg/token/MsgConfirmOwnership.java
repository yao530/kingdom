package com.ltu.okexchain.msg.token;

import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;

public class MsgConfirmOwnership extends MsgBase {

    public MsgConfirmOwnership() { setMsgType("okexchain/token/MsgConfirmOwnership"); }

    public Message produceConfirmOwnershipMsg(String address, String symbol) {

        MsgConfirmOwnershipValue value = new MsgConfirmOwnershipValue();
        value.setAddress(address);
        value.setSymbol(symbol);

        Message<MsgConfirmOwnershipValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);

        return msg;
    }

}