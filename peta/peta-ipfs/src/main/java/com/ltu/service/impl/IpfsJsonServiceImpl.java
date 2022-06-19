package com.ltu.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ltu.constant.enums.FileBusibessdict;
import com.ltu.domain.mp_entity.IpfsImgEntity;
import com.ltu.domain.mp_entity.IpfsJsonEntity;
import com.ltu.mapper.IpfsJsonMapper;
import com.ltu.service.IpfsJsonService;
import com.ltu.util.datetime.DateTimeUtil;

import io.ipfs.api.MerkleNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-18
 */
@Slf4j
@Service
public class IpfsJsonServiceImpl extends BaseServiceImpl<IpfsJsonMapper, IpfsJsonEntity> implements IpfsJsonService {


//	@Async("customServiceExecutor")
	@Override
	public void asyncSave(List<MerkleNode> result, String ipfsDir, Integer collectionId) {
		if (CollectionUtils.isEmpty(result))
			return;
		String currentDate = DateTimeUtil.getNowDate();
		List<IpfsJsonEntity> saveDatas = new ArrayList<IpfsJsonEntity>(1000);
		for (MerkleNode merkleNode : result) {
			String ipfsImgCid = merkleNode.hash.toBase58();
			if(ipfsDir.equals(ipfsImgCid))  // 排除目录 hash
				continue;
			String[] fileInfo = merkleNode.name.get().split("/");
			IpfsJsonEntity item = new IpfsJsonEntity();
			item.setCollectionId(collectionId)
				.setCreateTime(currentDate)
				.setFileName(fileInfo[1])
				.setIpfsDir(ipfsDir)
				.setIpfsJsCid(ipfsImgCid)
				.setIpfsJsUrl(FileBusibessdict.IpfsPrefix.concat(ipfsImgCid).concat(FileBusibessdict.IpfsSuffix).concat(fileInfo[1]))
				.setLocalJsUrl(FileBusibessdict.BrowseImgUrl.concat(merkleNode.name.get()))
				.setLocalDir(fileInfo[0]);
			saveDatas.add(item);
			if(saveDatas.size()==1000 || (result.size()<1000 && (result.size()-1)==saveDatas.size() )) {
			Boolean saveFlag =	 super.saveBatch(saveDatas);
			if(saveFlag==false)
				log.error("\n 保存失败的数据：\n {}" , JSONArray.toJSONString(saveDatas));
			saveDatas.clear();
			}

		}

	}
}
