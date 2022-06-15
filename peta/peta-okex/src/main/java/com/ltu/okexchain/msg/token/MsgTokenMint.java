package com.ltu.okexchain.msg.token;

import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.msg.common.Token;
import com.ltu.okexchain.utils.Utils;

public class MsgTokenMint extends MsgBase {

    public MsgTokenMint() { setMsgType("okexchain/token/MsgMint");}

    public Message produceTokenMintMsg(String denom, String amountDenom, String owner) {

        Token amount = new Token();
        amount.setDenom(denom);
        amount.setAmount(Utils.NewDecString(amountDenom));

        MsgTokenMintValue value = new MsgTokenMintValue();
        value.setAmount(amount);
        value.setOwner(owner);

        Message<MsgTokenMintValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);

        return msg;
    }

}