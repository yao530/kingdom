package com.ltu.model.request.account;

import com.ltu.model.request.base.PageReqData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GetAccountReq extends PageReqData {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    private String keyword;



}
