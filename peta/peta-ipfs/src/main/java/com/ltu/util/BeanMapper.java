package com.ltu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.response.base.CodeDataResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @Description: 资源拷贝工具，忽略空值或者指定某些key不拷贝
 * @author: 若尘  
 * @date 2019年3月28日 下午6:17:04
 * @version V1.0
 */
@Slf4j
public class BeanMapper {

	//source中的非空属性复制到target中
	/**
	 * @Description 忽略掉空值
	 * @param source 映射资源对象
	 * @param target 接收对象
	 * void
	 */
	public static <T> void copy(T source, T target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}

	/**
	 * 复制ArrayList里面的所有实体
	 * @param sourceList
	 * @param beanClass
	 * @return
	 */
	public static List copyList(List sourceList, Class beanClass) {
		List resultList = new ArrayList();
		if (sourceList == null || sourceList.size() < 1) {
			return resultList;
		}

		for (Object obj : sourceList) {
			try {
				Object newObj = beanClass.getConstructor().newInstance();
				BeanMapper.copy(obj, newObj);
				resultList.add(newObj);
			}
			catch (Exception e) {
				log.error("BeanMapper.copyList Exception:" + e.toString() + "\nobj:" + obj.toString());
			}
		}
		return resultList;
	}

	/**
	 * 复制CodeDataResp.data里面的所有实体，注意data里面的数据为Page
	 * @param data
	 * @param beanClass
	 * @return
	 */
	public static CodeDataResp copyRespDataList(CodeDataResp data, Class beanClass) {
		Page page = (Page)data.getData();
		if (page == null || page.getRecords() == null)
			return CodeDataResp.valueOfFailed("BeanMapper.copyRespDataList null exception.");

		page.setRecords(BeanMapper.copyList(page.getRecords(), beanClass));
		return CodeDataResp.valueOfSuccess(page);
	}

	/**
	 * 复制CodeDataResp.data里面的所有实体
	 * @param data
	 * @param targetClassName
	 * @return
	 */
	public static CodeDataResp copyRespDataItem(CodeDataResp data, String targetClassName) {
		Object obj = data.getData();
		if (obj == null) {
			//无数据的情况，原封不动返回
			return data;
		}

		try {
			Object newObj = Class.forName(targetClassName).getConstructor().newInstance();
			BeanMapper.copy(obj, newObj);
			return CodeDataResp.valueOfSuccess(newObj);
		}
		catch (Exception e) {
			log.error("BeanMapper.copyRespDataItem Exception:" + e.toString());
			return CodeDataResp.valueOfFailed("BeanMapper.copyRespDataItem Exception" + e.toString());
		}
	}

	/**
	 * @Description source中的非空属性复制到target中，但是忽略指定的属性，也就是说有些属性是不可修改的（个人业务需要）
	 * @param source 映射资源对象
	 * @param target 接收对象
	 * void
	 */
	public static <T> void beanCopyWithIngore(T source, T target, String... ignoreProperties) {
		String[] pns = getNullAndIgnorePropertyNames(source, ignoreProperties);
		BeanUtils.copyProperties(source, target, pns);
	}

	private static String[] getNullAndIgnorePropertyNames(Object source, String... ignoreProperties) {
		Set<String> emptyNames = getNullPropertyNameSet(source);
		for (String s : ignoreProperties) {
			emptyNames.add(s);
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	private static String[] getNullPropertyNames(Object source) {
		Set<String> emptyNames = getNullPropertyNameSet(source);
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	private static Set<String> getNullPropertyNameSet(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) emptyNames.add(pd.getName());
		}
		return emptyNames;
	}
	
	/**
	 * <p>
	 * 数组深拷贝
	 * </p>
	 * 
	 * @param <T>
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}

	/**
	 * <p>
	 * 单个bean深拷贝
	 * </p>
	 * @param <T>
	 * @param src
	 * @return
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		T dest = (T) in.readObject();
		return dest;
	}
}
