package com.ltu.service.impl;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.domain.BaseEntity;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.BaseTService;
import com.ltu.util.common.MpPageUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@CrossOrigin(origins="*",allowedHeaders="*")
@ApiIgnore
	public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity<T>>  extends ServiceImpl<M, T>  implements BaseTService<T>{
//	public class BaseController<M extends BaseMapper<T>, T>  extends ServiceImpl<M, T>  implements BaseTService<T>{

	@Override
	public CodeDataResp<Page<T>> getMePage(BaseFilterPageReq<T> req) {
		Page<T> page = MpPageUtil.getCommonPage(req);
    	Page<T>	 result= super.page(page, null);
		return CodeDataResp.valueOfSuccess(result);
	}

	@Override
	public CodeDataResp<List<T>> getMeList(BaseFilterPageReq<T> req) {
		return CodeDataResp.valueOfSuccess(super.list(null));
	}

	@Override
	public CodeDataResp edit(T t) {
		super.saveOrUpdate(t);
		return   CodeDataResp.valueOfSuccessEmptyData();
	}

	@Override
	public CodeDataResp<T> getDetail(BaseIdReq req) {
		return CodeDataResp.valueOfSuccess(super.getById(req.getId()));
	}

	@Override
	public CodeDataResp remove(BaseIdReq req) {
		// TODO Auto-generated method stub
		return CodeDataResp.valueOfSuccess(super.removeById(req.getId()));
	}
	
	
}
