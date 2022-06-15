package com.ltu.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ltu.constant.enums.FileBusibessdict;
import com.ltu.constant.enums.FileBusibessdict.BusibessType;
import com.ltu.enums.EnumUtils;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.IpfsFileUtil;

import io.ipfs.api.IPFS;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/file") 
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "统一文件上传接口")
public class FileUploadController {
	Logger  log=org.slf4j.LoggerFactory.getLogger(FileUploadController.class);
	private final IPFS  ipfs;
	private final IpfsFileUtil  ipfsFileUtil;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="businessType",value="0:上传图片1：上传json文件",required=true,  dataType = " ", paramType = "query")
	})
	@ApiOperation(value="业务文件上传")
    @PostMapping("/upload/{businessType}")
    public CodeDataResp<String>  upload(@RequestParam("files") MultipartFile[] uploadFiles,
                        @PathVariable(name = "businessType") Integer businessType) {
    	BusibessType dict= 	 EnumUtils.indexToEnum(FileBusibessdict.BusibessType.class, businessType);
    	if(dict==null)
    		return CodeDataResp.valueOfFailed("请传入业务编号");
		 Date  date= new Date();

		//获取当前项目所在的根目录
		String userDir = System.getProperties().getProperty("user.dir").concat(File.separator).concat("static");
		String folderDay= String.valueOf( System.currentTimeMillis()/100); //每次上传创建的目录 批次目录
		String  browsePath=("ipfs").concat(File.separator).concat(dict.getValue())  .concat(File.separator).concat(folderDay); //浏览器访问地址：
		String uploadPath  = userDir.concat(File.separator).concat(browsePath);		//绝对路径
		String  returnPath = "";   //上传到ipfs目录下返回的CID  拼接 https://ipfs.io/ipfs/QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn/文件名即可访问
		log.info("文件上传目录：{}",uploadPath);
		
		 try {
			 File folder = new File(uploadPath);
        	 if (!folder.isDirectory()) //如果文件夹不存在则创建
                  folder.mkdirs();
			 
			 for (MultipartFile uploadFile : uploadFiles) {
				 //获取文件名
				   String oldName = uploadFile.getOriginalFilename();
			        String suffix = oldName.substring(oldName.lastIndexOf("."), oldName.length());//文件名后缀
					//生成文件名        
			     	String fileName = oldName;
			            // 文件保存
			            uploadFile.transferTo(new File(folder, fileName));
			}
        } catch (IOException e) {
          e.printStackTrace();
        }
		 returnPath = ipfsFileUtil.uploadFileToIpfs(folderDay,uploadPath, dict, ipfs);
	     	log.info("返回的文件路径：{}",returnPath);
		return CodeDataResp.valueOfSuccess(returnPath);
    }
	
	

}
