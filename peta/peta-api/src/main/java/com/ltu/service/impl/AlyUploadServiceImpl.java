package com.ltu.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.google.gson.Gson;
import com.ltu.constant.CommonConstant;
import com.ltu.model.response.base.ImgUploadResp;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AlyUploadServiceImpl {



    /**
     * 根据给定的文件名,获取其后缀信息
     *
     * @param filename
     * @return
     */
    public static String getSuffixByFilename(String filename) {
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }


    @SuppressWarnings("unused")
    public ImgUploadResp uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {


        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        String originFileName = file.getOriginalFilename();
        String suffix = getSuffixByFilename(originFileName);
        Calendar c = Calendar.getInstance();
        ImgUploadResp img = new ImgUploadResp();
        // 要传到upyun后的文件路径
        String filePath = "image/" + c.get(Calendar.YEAR)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+ UUID.randomUUID().toString().replace("-", "")+suffix;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(CommonConstant.AL_ROOT_URL, CommonConstant.AL_ACCESSKEY, CommonConstant.AL_SECRETKEY);

        // 填写Byte数组。
        byte[] content = file.getBytes();
        try {

            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(CommonConstant.AL_BUCKETNAME, filePath, new ByteArrayInputStream(content));
            // 关闭OSSClient。
            ossClient.shutdown();
            img.setSuccess(true);
            img.setUrl(CommonConstant.PIN_URL+filePath);

        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        return img;
    }



}
