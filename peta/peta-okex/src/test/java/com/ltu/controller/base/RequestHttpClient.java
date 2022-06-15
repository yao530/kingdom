package com.ltu.controller.base;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * RequestHttpClient.java  用于测试是发送post请求
 */
public class RequestHttpClient {

	public static String post(String url, String requestBody){
		CloseableHttpClient hc = HttpClients.custom().build();
		String responseBody = null;
		try {
			// Post请求
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/json");
			// 设置参数
			if(StringUtils.isNotBlank(requestBody)) {
				System.out.println("requestBody:" + requestBody);
				httppost.setEntity(new StringEntity(requestBody,"UTF-8"));
			}
			// 发送请求
			HttpResponse httpresponse = hc.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			responseBody = EntityUtils.toString(entity);
			System.out.println("responseBody:" + responseBody);
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				hc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBody;
	}
}
