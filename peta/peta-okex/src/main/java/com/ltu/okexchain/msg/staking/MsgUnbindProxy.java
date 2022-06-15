package com.ltu.okexchain.msg.staking;

import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.Message;

public class MsgUnbindProxy extends MsgBase {

    public MsgUnbindProxy() { setMsgType("okexchain/staking/MsgUnbindProxy"); }

    public Message produce() {

        MsgUnbindProxyValue value = new MsgUnbindProxyValue();

        value.setDelegatorAddress(this.address);

        Message<MsgUnbindProxyValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }
}
