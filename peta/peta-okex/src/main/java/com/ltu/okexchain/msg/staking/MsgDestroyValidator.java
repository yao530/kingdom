package com.ltu.okexchain.msg.staking;


import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;

public class MsgDestroyValidator extends MsgBase {

    public MsgDestroyValidator() { setMsgType("okexchain/staking/MsgDestroyValidator"); }

    public Message produceMsg() {

        MsgDestroyValidatorValue value = new MsgDestroyValidatorValue();

        value.setDelegatorAddress(this.address);

        Message<MsgDestroyValidatorValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }
}
