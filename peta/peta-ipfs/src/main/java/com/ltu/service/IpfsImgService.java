package com.ltu.service;

import java.util.List;

import com.ltu.domain.mp_entity.IpfsImgEntity;

import io.ipfs.api.MerkleNode;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-18
 */

public interface IpfsImgService extends BaseTService<IpfsImgEntity> {
	
	void  asyncSave(List<MerkleNode> result,String ipfsDir,Integer collectionId);
 
}
