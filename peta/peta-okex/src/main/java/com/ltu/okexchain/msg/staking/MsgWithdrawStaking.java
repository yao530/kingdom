package com.ltu.okexchain.msg.staking;

import com.ltu.okexchain.env.EnvInstance;
import com.ltu.okexchain.msg.MsgBase;
import com.ltu.okexchain.msg.common.DecCoin;
import com.ltu.okexchain.msg.common.Message;
import com.ltu.okexchain.utils.Utils;

public class MsgWithdrawStaking  extends MsgBase {
    public MsgWithdrawStaking() {
        setMsgType("okexchain/staking/MsgWithdraw");
    }
    public static void main(String[] args) {
        EnvInstance.setEnv("okq");

        MsgWithdrawStaking msg = new MsgWithdrawStaking();
        msg.initMnemonic("puzzle glide follow cruel say burst deliver wild tragic galaxy lumber offer");

        Message messages = msg.produceMsg("okt", "10.00000000", "okexchain10q0rk5qnyag7wfvvt7rtphlw589m7frsku8qc9");

        msg.submit(messages, "6.00000000", "200000", "");
    }

    public Message produceMsg(String denom, String amountDenom, String delegrator) {

        DecCoin d = new DecCoin();
        d.setDenom(denom);
        d.setAmount(Utils.NewDecString(amountDenom));

        MsgWithdrawStakingValue value = new MsgWithdrawStakingValue();

        System.out.println("this.operAddress:");
        System.out.println(this.operAddress);

        value.setAmount(d);
        value.setDelegatorAddress(delegrator);

        System.out.println("println this.operAdddress");

        Message<MsgWithdrawStakingValue> msg = new Message<>();
        msg.setType(msgType);
        msg.setValue(value);
        return msg;
    }
}
