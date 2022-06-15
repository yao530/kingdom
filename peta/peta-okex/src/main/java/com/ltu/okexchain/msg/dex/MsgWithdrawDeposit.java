package com.ltu.okexchain.msg.dex;

import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.msg.common.Token;
import com.ltu.okexchain.utils.Utils;

public class MsgWithdrawDeposit extends MsgBase {

    public MsgWithdrawDeposit() { setMsgType("okexchain/dex/MsgWithdraw"); }

    public Message produce(String denom, String amountDenom, String product) {

        Token amount = new Token();
        amount.setDenom(denom);
        amount.setAmount(Utils.NewDecString(amountDenom));

        MsgWithdrawDepositValue value = new MsgWithdrawDepositValue();

        value.setDepositor(this.address);
        value.setAmount(amount);
        value.setProduct(product);

        Message<MsgWithdrawDepositValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }
}
