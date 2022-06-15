package com.ltu.okexchain.msg.tx;

import com.ltu.okexchain.msg.common.TxValue;
import com.ltu.okexchain.utils.Utils;


public class BroadcastTx {

    private String mode;
    private String type = "cosmos-sdk/StdTx";
    private TxValue tx;

    public BroadcastTx() {
    }

    public TxValue getTx() {
        return tx;
    }

    public void setTx(TxValue value) {
        this.tx = value;
    }

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static BroadcastTx fromJson(String json) {
        return Utils.serializer.fromJson(json, BroadcastTx.class);
    }
}
