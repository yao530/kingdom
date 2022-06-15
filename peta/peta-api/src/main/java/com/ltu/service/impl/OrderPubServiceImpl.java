package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.enums.EnumUtils;
import com.ltu.enums.OrderDict;
import com.ltu.mapper.OrderMapper;
import com.ltu.mapper.PayRecordMapper;
import com.ltu.model.request.collectionOrder.CreateOrderReq;
import com.ltu.model.request.collectionOrder.OrderPageReq;
import com.ltu.model.request.collectionOrder.PayReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.payment.PubPayService;
import com.ltu.service.*;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.exception.ApiException;
import com.ltu.util.redis.RedistUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 公共服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPubServiceImpl implements OrderPubService {



}
