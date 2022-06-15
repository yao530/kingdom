package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.FlowRecordMapper;
import com.ltu.model.request.base.BaseIdAndStatusReq;
import com.ltu.model.request.character.FlowRecordPageReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.FlowRecordService;
import com.ltu.service.UserService;
import com.ltu.service.VirtualCharacterService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关注人物记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class FlowRecordServiceImpl extends BaseServiceImpl<FlowRecordMapper, FlowRecordEntity> implements FlowRecordService {


   @Autowired
   VirtualCharacterService virtualCharacterService;
   @Autowired
   UserService userService;


    public CodeDataResp saveOrUpdate(BaseIdAndStatusReq baseIdReq){

        UserDto userDto = ShiroUtil.getPrincipalUser();
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户非法");
        if (StrUtils.isVaileNum(baseIdReq.getId())){
            VirtualCharacterEntity virtualCharacterEntity = virtualCharacterService.getById(baseIdReq.getId());
            if (virtualCharacterEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            FlowRecordEntity flowRecordEntity = getFlowRecord(userDto.getId(),virtualCharacterEntity.getId());
            if (flowRecordEntity!=null){
                flowRecordEntity.setStatus(baseIdReq.getStatus());
                flowRecordEntity.setUpdateTime(DateUtils.currentSecs());
                super.saveOrUpdate(flowRecordEntity);

                virtualCharacterEntity.setTotalFocusNumber(baseIdReq.getStatus()==1?virtualCharacterEntity.getTotalFocusNumber()+1:virtualCharacterEntity.getTotalFocusNumber()-1);
            }
            else {
                flowRecordEntity = new FlowRecordEntity();
                BeanMapper.copy(baseIdReq,flowRecordEntity);
                flowRecordEntity.setCreateTime(DateUtils.currentSecs());
                flowRecordEntity.setCharacterName(virtualCharacterEntity.getCharacterName());
                flowRecordEntity.setCharacterAvatar(virtualCharacterEntity.getCharacterAvatar());
                flowRecordEntity.setCharacterId(virtualCharacterEntity.getId());
                flowRecordEntity.setStatus(1);
                flowRecordEntity.setUserId(userEntity.getId());
                flowRecordEntity.setUserAvatar(userEntity.getUserAvatar());
                flowRecordEntity.setUserNick(userEntity.getUserNick());
                flowRecordEntity.setFlowTime(DateUtils.currentSecs());

                super.saveOrUpdate(flowRecordEntity);

                virtualCharacterEntity.setTotalFocusNumber(virtualCharacterEntity.getTotalFocusNumber()+1);


            }

            virtualCharacterService.saveOrUpdate(virtualCharacterEntity);

        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }


    private FlowRecordEntity getFlowRecord(Integer userId,Integer virtualCharacterId){
        QueryWrapper<FlowRecordEntity> condition = new QueryWrapper<>();
            condition.eq("user_id", userId);
            condition.eq("character_id", virtualCharacterId);
        return super.getOne(condition);
    }


    public CodeDataResp findVirtualChapracterFuns(FlowRecordPageReq pageReq){
        Page<FlowRecordEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<FlowRecordEntity> condition = new QueryWrapper<>();
         condition.eq("character_id", pageReq.getCharacterId());
         condition.eq("status", 1);
        return CodeDataResp.valueOfSuccess(this.page(page,condition));

    }

    public CodeDataResp findUserFlows(FlowRecordPageReq pageReq){
        Page<FlowRecordEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<FlowRecordEntity> condition = new QueryWrapper<>();
        condition.eq("user_id", pageReq.getUserId());
        condition.eq("status", 1);
        return CodeDataResp.valueOfSuccess(this.page(page,condition));
    }


}
