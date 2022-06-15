package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.BannerEntity;

import com.ltu.mapper.BannerMapper;

import com.ltu.model.request.banner.BannerCommonReq;
import com.ltu.model.request.banner.BannerPageReq;
import com.ltu.service.BannerService;

import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.common.UUIDGenerator;
import com.ltu.util.datetime.DateUtils;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;

/**
 * <p>
 * 广告banner 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-12-02
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, BannerEntity> implements BannerService {

    /**
     * 获取记录列表
     * @param pageReq
     * @return
     */
    public CodeDataResp getList(BannerPageReq pageReq) {
        pageReq.setSortOrder("desc");
        pageReq.setSortType("sort");
        Page<BannerEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<BannerEntity> condition = new QueryWrapper<>();
        condition.eq("status",1);
        if (StrUtils.isVaileNum(pageReq.getLocation()))
            condition.eq("location_type", pageReq.getLocation());

        return CodeDataResp.valueOfSuccess(this.page(page,condition));
    }

    /**
     * 保存或更新
     * @param req
     * @return
     */
    public CodeDataResp saveOrUpdate(BannerCommonReq req) {
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (StrUtils.isVaileNum(req.getId())){
            BannerEntity bannerEntity = this.getById(req.getId());
            BeanMapper.copy(req,bannerEntity);
                       bannerEntity.setUpdateTime(DateUtils.currentSecs());

            bannerEntity.setUpdateTime(DateUtils.currentSecs());
            this.saveOrUpdate(bannerEntity);
        }
        else {
            BannerEntity bannerEntity =new BannerEntity();
            BeanMapper.copy(req,bannerEntity);
            bannerEntity.setCreateTime(DateUtils.currentSecs());


            this.saveOrUpdate(bannerEntity);

        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }

    /**
     * 获取记录详情
     * @param req
     * @return
     */
    public CodeDataResp getDetail(BaseIdReq req) {
        ///todo ...
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    /**
     * 删除记录
     * @param req
     * @return
     */
    public CodeDataResp remove(BaseIdReq req) {
        ///todo ...
        return CodeDataResp.valueOfSuccessEmptyData();
    }
}
