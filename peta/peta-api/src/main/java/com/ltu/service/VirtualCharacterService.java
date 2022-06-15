package com.ltu.service;

import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.ApplyCreatorEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.VirtualCharacterEntity;
import com.ltu.model.request.account.AccountPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.character.VirtualCharacterPageReq;
import com.ltu.model.request.character.VirtualCharacterReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 虚拟人物 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface VirtualCharacterService extends BaseTService<VirtualCharacterEntity> {

    CodeDataResp saveOrUpdate(VirtualCharacterReq req);

    List<VirtualCharacterEntity> getList(VirtualCharacterPageReq req);

    CodeDataResp getPage(VirtualCharacterPageReq req);

    List<VirtualCharacterEntity> getCreatorsByArt(BaseIdReq baseIdReq);

    /**
     * @desc 根据后台账号类型：作家、艺术创作者，获取对应的虚拟人物IP信息
     **/
    VirtualCharacterEntity getCharacterByAccount(AccountEntity accountEntity);

    void createVirtualCharacter(UserEntity userEntity, ApplyCreatorEntity applyCreatorEntity,Integer metaId);
}
