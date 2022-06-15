package com.ltu.controller.base;

import com.fasterxml.jackson.databind.ObjectMapper;


public class BaseRestControllerTest {


	public final static String devUrl = "http://127.0.0.1:8888/";

	public final static String testUrl = "http://47.95.204.49/api";


	public final String token = "12221";
	
	public void sendDevUrlObject(String path, Object data){
		String url = devUrl + path;
		send(url, data);
	}
	
	public void sendTestUrlObject(String path, Object data){
		String url = testUrl + path;
		send(url, data);
	}


//	public void sendDevUrl(String path, PageReqData data) {
//		String url = devUrl + path;
//		this.send(url, data);
//	}
//
//	public void sendTestUrl(String path, BaseAccountReqData data) {
//		String url = testUrl + path;
//		this.send(url, data);
//	}
//
//	public void send(String url, BaseAccountReqData data){
//		send(url, data);
//
//	}
	
	public void send(String url, Object data){
		try {
			long beginTimeMills = System.currentTimeMillis();
			System.out.println(url);
			String requestBody =new ObjectMapper().writeValueAsString(data);
			System.out.println("-----------------");
			System.out.println(formatJson(requestBody));
			System.out.println("-----------------");
			RequestHttpClient.post(url, requestBody);
			System.out.println((System.currentTimeMillis() - beginTimeMills) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void sendString(String url, String body) {
		System.out.println(url);
		RequestHttpClient.post(url, body);
	}

	/**
	 * 格式化
	 * 
	 * @param jsonStr
	 * @return
	 * @Date 2015-10-14 下午1:17:35
	 */
	public static String formatJson(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				sb.append('\n');
				indent++;
				addIndentBlank(sb, indent);
				break;
			case '}':
			case ']':
				sb.append('\n');
				indent--;
				addIndentBlank(sb, indent);
				sb.append(current);
				break;
			case ',':
				sb.append(current);
				if (last != '\\') {
					sb.append('\n');
					addIndentBlank(sb, indent);
				}
				break;
			default:
				sb.append(current);
			}
		}

		return sb.toString();
	}

	/**
	 * 添加space
	 * 
	 * @param sb
	 * @param indent
	 * @Date 2015-10-14 上午10:38:04
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

}
