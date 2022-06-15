package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.CharacterRoleEntity;
import com.ltu.mapper.CharacterRoleMapper;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.character.CharacterRoleListReq;
import com.ltu.model.request.character.CharacterRoleReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.CharacterRoleService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 人物角色类型 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class CharacterRoleServiceImpl extends BaseServiceImpl<CharacterRoleMapper, CharacterRoleEntity> implements CharacterRoleService {



    public CodeDataResp saveOrUpdate(CharacterRoleReq roleReq){
        if (StrUtils.isVaileNum(roleReq.getId())){
            CharacterRoleEntity characterRoleEntity = super.getById(roleReq.getId());
            if (characterRoleEntity!=null)
                BeanMapper.copy(roleReq,characterRoleEntity);
                characterRoleEntity.setUpdateTime(DateUtils.currentSecs());
                super.saveOrUpdate(characterRoleEntity);
        }
        else {
            CharacterRoleEntity characterRoleEntity = new CharacterRoleEntity();
            BeanMapper.copy(roleReq,characterRoleEntity);
            characterRoleEntity.setCreateTime(DateUtils.currentSecs());
            super.saveOrUpdate(characterRoleEntity);
        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }

    public CodeDataResp getPage(PageReqData pageReqData){
        Page<CharacterRoleEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<CharacterRoleEntity> condition = new QueryWrapper<>();

        return CodeDataResp.valueOfSuccess( super.page(page,condition));

    }

    public List<CharacterRoleEntity> getList(){
        QueryWrapper<CharacterRoleEntity> condition=new QueryWrapper<>();
        //condition.eq("id",characterRoleListReq.getCharacterRoleId());
        return super.list(condition);
    }

    public  List<CharacterRoleEntity> getOpenRoleList(){
        QueryWrapper<CharacterRoleEntity> condition=new QueryWrapper<>();
            condition.eq("id",2).or().eq("id",3).or().eq("id",4);
        return super.list(condition);
    }
}
