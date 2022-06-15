package com.ltu.service;

import com.ltu.domain.mp_entity.CharacterRoleEntity;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.character.CharacterRoleListReq;
import com.ltu.model.request.character.CharacterRoleReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 人物角色类型 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface CharacterRoleService extends BaseTService<CharacterRoleEntity> {


    CodeDataResp saveOrUpdate(CharacterRoleReq roleReq);

    CodeDataResp getPage(PageReqData pageReqData);

    List<CharacterRoleEntity> getList();
    List<CharacterRoleEntity> getOpenRoleList();

}
