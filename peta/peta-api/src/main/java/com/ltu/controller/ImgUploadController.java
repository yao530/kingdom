package com.ltu.controller;


import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.base.ImgUploadResp;


import com.ltu.service.impl.AlyUploadServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 *
 */
@Api(tags="图片上传")
@Controller
@RequestMapping(value = "/imageUpload")
public class ImgUploadController {
	

	@Autowired
	private com.ltu.service.impl.ImgUploadServiceImpl imgUploadService;

	@Autowired
	private AlyUploadServiceImpl alyUploadService;



	@RequestMapping(value = "/imgUploadFile",method = RequestMethod.POST)
	@ResponseBody
	public CodeDataResp<?> imgUploadIO(MultipartFile file, HttpServletRequest request) throws IOException{
		ImgUploadResp state;
		System.out.println("七牛图片上传。。。。");
		try {
			state = imgUploadService.uploadFile(file, request);
			return CodeDataResp.valueOfSuccess(state);
		} catch (IOException e) {
			return  CodeDataResp.valueOfFailed("上传的图片失败");
		}

	}

	@RequestMapping(value = "/imgUploadAl",method = RequestMethod.POST)
	@ResponseBody
	public CodeDataResp<?> imgUploadAl(MultipartFile file, HttpServletRequest request) throws IOException{
		ImgUploadResp state;
		System.out.println("阿里云图片上传。。。。");
		try {
			state = alyUploadService.uploadFile(file, request);
			return CodeDataResp.valueOfSuccess(state);
		} catch (IOException e) {
			return  CodeDataResp.valueOfFailed("上传的图片失败");
		}


	}



}