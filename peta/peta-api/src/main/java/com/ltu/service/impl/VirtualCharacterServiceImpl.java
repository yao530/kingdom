package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.VirtualCharacterMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.character.VirtualCharacterPageReq;
import com.ltu.model.request.character.VirtualCharacterReq;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 虚拟人物 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class VirtualCharacterServiceImpl extends BaseServiceImpl<VirtualCharacterMapper, VirtualCharacterEntity> implements VirtualCharacterService {


   @Autowired
   CharacterRoleService characterRoleService;

   @Autowired
   UserService userService;

   @Autowired
   ContractSquService contractSquService;

   @Autowired
   ArtCollectionService artCollectionService;
   @Autowired
   AccountService accountService;

   @Autowired
   MetaService metaService;


    @Override
    public CodeDataResp saveOrUpdate(VirtualCharacterReq req){

        MetaEntity metaEntity = metaService.getMeta(req.getMetaId());
        if (StrUtils.isVaileNum(req.getId())){
            VirtualCharacterEntity virtualCharacterEntity = super.getById(req.getId());
            if (virtualCharacterEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            BeanMapper.copy(req,virtualCharacterEntity);
            virtualCharacterEntity.setUpdateTime(DateUtils.currentSecs());
            virtualCharacterEntity.setMetaName(metaEntity.getMetaName());
            super.saveOrUpdate(virtualCharacterEntity);
//            if (StrUtils.isVaileNum(virtualCharacterEntity.getMintStatus())&&!StrUtils.isTrimNull(virtualCharacterEntity.getBlockSmartContractAddress()))
//                contractSquService.mintCharacterAvatar(virtualCharacterEntity);
        }
        else {
            VirtualCharacterEntity virtualCharacterEntity = new VirtualCharacterEntity();
            BeanMapper.copy(req,virtualCharacterEntity);
            virtualCharacterEntity.setCreateTime(DateUtils.currentSecs());
            virtualCharacterEntity.setMetaName(metaEntity.getMetaName());
            if (StrUtils.isVaileNum(req.getCharacterRoleId())){
                CharacterRoleEntity characterRoleEntity = characterRoleService.getById(req.getCharacterRoleId());
                if (characterRoleEntity!=null){
                    virtualCharacterEntity.setCharacterRoleId(characterRoleEntity.getId());
                    virtualCharacterEntity.setCharacterRoleName(characterRoleEntity.getCharacterRoleName());
                    characterRoleEntity.setCharacterNumber(characterRoleEntity.getCharacterNumber()+1);
                    characterRoleEntity.setUpdateTime(DateUtils.currentSecs());
                    characterRoleService.saveOrUpdate(characterRoleEntity);
                }
            }
            super.saveOrUpdate(virtualCharacterEntity);
        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    public void createVirtualCharacter(UserEntity userEntity, ApplyCreatorEntity applyCreatorEntity,Integer metaId){
        MetaEntity metaEntity = metaService.getMeta(metaId);
        VirtualCharacterEntity virtualCharacterEntity = new VirtualCharacterEntity();
        virtualCharacterEntity.setUserId(userEntity.getId());
        virtualCharacterEntity.setUserAvatar(userEntity.getUserAvatar());
        virtualCharacterEntity.setCreateTime(DateUtils.currentSecs());
        virtualCharacterEntity.setCharacterRoleId(applyCreatorEntity.getCharacterRoleId());
        virtualCharacterEntity.setCharacterRoleName(applyCreatorEntity.getCharacterRoleName());
        virtualCharacterEntity.setCharacterName(applyCreatorEntity.getApplyCharacterName());
        virtualCharacterEntity.setCharacterAvatar(userEntity.getUserAvatar());
        virtualCharacterEntity.setCreateTime(DateUtils.currentSecs());
        virtualCharacterEntity.setMetaId(metaId);
        virtualCharacterEntity.setMetaName(metaEntity.getMetaName());
        super.saveOrUpdate(virtualCharacterEntity);

    }


    public  CodeDataResp getPage(VirtualCharacterPageReq req){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());
        UserEntity userEntity = userService.getUserByPhone(accountEntity.getMobilePhone());
        Page<VirtualCharacterEntity> page = MpPageUtil.getCommonPage(req);
        QueryWrapper<VirtualCharacterEntity> condition = new QueryWrapper<>();
        if (StrUtils.isVaileNum(req.getCharacterRoleId()))
            condition.eq("character_role_id",req.getCharacterRoleId());
        if (!StrUtils.isTrimNull(req.getCharacterName()))
            condition.like("character_role_name",req.getCharacterName());
        if (accountEntity.getAccountType()!= RoleType.ADMIN_AUTH)
            condition.eq("user_id",userEntity.getId());
        return CodeDataResp.valueOfSuccess(super.page(page,condition));

    }

    public List<VirtualCharacterEntity> getList(VirtualCharacterPageReq req){
        QueryWrapper<VirtualCharacterEntity> condition = new QueryWrapper<>();
         condition.eq("status",1);
        if (StrUtils.isVaileNum(req.getCharacterRoleId()))
            condition.eq("character_role_id",req.getCharacterRoleId());

        return super.list(condition);
    }

    public List<VirtualCharacterEntity> getCreatorsByArt(BaseIdReq baseIdReq) {
        List<VirtualCharacterEntity> virtualCharacterEntityList = new ArrayList<>();
        ArtCollectionEntity artCollectionEntity = artCollectionService.getById(baseIdReq.getId());
        if (artCollectionEntity != null) {
            QueryWrapper<VirtualCharacterEntity> condition = new QueryWrapper<>();
            condition.eq("status", 1);
            List<Integer> ids = new ArrayList<>();
            ids.add(artCollectionEntity.getWriterId());
            if (!artCollectionEntity.getWriterId().equals(artCollectionEntity.getArtCreatorId()))
                ids.add(artCollectionEntity.getArtCreatorId());
            if (ids.size() > 0) {
                condition.in("id", ids);
                virtualCharacterEntityList = super.list(condition);
            }
        }
        return virtualCharacterEntityList;
    }

    private VirtualCharacterEntity getCharacterByUser(UserEntity userEntity){
        QueryWrapper<VirtualCharacterEntity> condition = new QueryWrapper<>();
            condition.eq("status",1);
            condition.eq("user_id",userEntity.getId());
            return super.getOne(condition);

    }

    /**
     * @desc 根据后台账号类型：作家、艺术创作者，获取对应的虚拟人物IP信息
     **/
    public VirtualCharacterEntity getCharacterByAccount(AccountEntity accountEntity){
      UserEntity userEntity = userService.getUserByPhone(accountEntity.getMobilePhone());
      VirtualCharacterEntity virtualCharacterEntity = null;
      if (userEntity!=null){
          virtualCharacterEntity = new VirtualCharacterEntity();
          virtualCharacterEntity = getCharacterByUser(userEntity);
      }

      return virtualCharacterEntity;
    }

}
