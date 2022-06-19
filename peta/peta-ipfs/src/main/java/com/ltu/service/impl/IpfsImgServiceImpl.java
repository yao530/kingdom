package com.ltu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ltu.constant.enums.FileBusibessdict;
import com.ltu.domain.mp_entity.IpfsImgEntity;
import com.ltu.mapper.ArtCollectionMapper;
import com.ltu.mapper.IpfsImgMapper;
import com.ltu.service.IpfsImgService;
import com.ltu.util.datetime.DateTimeUtil;

import io.ipfs.api.MerkleNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IpfsImgServiceImpl extends BaseServiceImpl<IpfsImgMapper, IpfsImgEntity> implements IpfsImgService {

//	@Async("customServiceExecutor")
	@Override
	public void asyncSave(List<MerkleNode> result, String ipfsDir, Integer collectionId) {
		if (CollectionUtils.isEmpty(result))
			return;
		String currentDate = DateTimeUtil.getNowDate();
		List<IpfsImgEntity> saveDatas = new ArrayList<IpfsImgEntity>(1000);
		
		for (MerkleNode merkleNode : result) {
			String ipfsImgCid = merkleNode.hash.toBase58();
			if(ipfsDir.equals(ipfsImgCid))  // 排除目录 hash
				continue;
			String[] fileInfo = merkleNode.name.get().split("/");
			IpfsImgEntity item = new IpfsImgEntity();
			item.setCollectionId(collectionId)
				.setCreateTime(currentDate)
				.setFileName(fileInfo[1])
				.setIpfsDir(ipfsDir)
				.setIpfsImgCid(ipfsImgCid)
				.setIpfsImgUrl(FileBusibessdict.IpfsPrefix.concat(ipfsImgCid).concat(FileBusibessdict.IpfsSuffix).concat(fileInfo[1]))
				.setLocalImgUrl(FileBusibessdict.BrowseImgUrl.concat(merkleNode.name.get()))
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
