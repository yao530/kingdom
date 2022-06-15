package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.collectionOrder.CreateOrderReq;
import com.ltu.model.request.collectionOrder.OrderPageReq;
import com.ltu.model.request.collectionOrder.PayReq;
import com.ltu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.impl.BaseServiceImpl;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.OrderEntity;
import com.ltu.mapper.OrderMapper;


/**
 * <p>
 * 支付订单 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "支付订单")
public class OrderController extends BaseServiceImpl<OrderMapper, OrderEntity>{


    @Autowired
    OrderService orderService;


    @ApiOperation(value="后台取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<OrderEntity>> getMePage(@RequestBody OrderPageReq pageReq) {
    	return orderService.getPage(pageReq);
    }


    @ApiOperation(value="用户获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<Page<OrderEntity>> getList(@RequestBody  OrderPageReq pageReq ) {
        return orderService.getOrders(pageReq);
    }

    @ApiOperation(value="下单")
    @RequestMapping(value="/placeOrder", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody CreateOrderReq req) {
        return orderService.placeOrder(req);
    }

    @ApiOperation(value="支付")
    @RequestMapping(value="/pay", method= RequestMethod.POST)
    public CodeDataResp pay(@RequestBody PayReq req) {
        return orderService.createOrderPayRecord(req);
    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    


}

