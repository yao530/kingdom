package com.ltu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.domain.mp_entity.VerifySmsCode;
import com.ltu.model.request.smscode.GetSmsCodeReq;
import com.ltu.model.request.smscode.VerifySmsCodeReq;
import com.ltu.model.response.base.CodeDataResp;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vk@rongding123
 * @since 2020-08-25
 */

public interface VerifySmsCodeService extends IService<VerifySmsCode> {

    /**
     * 获取短信验证码
     * @param req
     * @return
     */
    boolean getSmsCode(GetSmsCodeReq req);

    /**
     * 校验短信码
     * @param verifySmsCodeReq
     * @return
     */
    boolean verifySmsCode(VerifySmsCodeReq verifySmsCodeReq);


}
