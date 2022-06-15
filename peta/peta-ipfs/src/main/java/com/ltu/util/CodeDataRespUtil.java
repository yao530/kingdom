package com.ltu.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.response.base.CodeDataResp;

import java.util.ArrayList;

public class CodeDataRespUtil {

    /**
     * 从CodeDataResp的page数据中获取数组
     * @param codeDataResp
     * @return
     */
    public static ArrayList getListFromPageData(CodeDataResp codeDataResp) {
        Page page = (Page)codeDataResp.getData();
        if (page != null && page.getRecords().size() > 0)
            return (ArrayList)page.getRecords();

        return null;
    }
}
