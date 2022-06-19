package com.ltu.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ltu.constant.enums.FileBusibessdict;
import com.ltu.constant.enums.FileBusibessdict.BusibessType;
import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.CollectionDetailEntity;
import com.ltu.mapper.ArtCollectionMapper;
import com.ltu.mapper.CollectionDetailMapper;
import com.ltu.model.metadata.ArtMetaData;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IpfsFileUtil {
	private final IpfsImgService ipfsImgService;
	private final IpfsJsonService ipfsJsonService;
	/**
	 * @param lastDir 对应IPFS的目录
	 * @param filePath 待上传的文件目录绝对路径
	 * @param ipfs
	 * @param collectionDetailMapper
	 * @param entity
	 * @param dict
	 */
//	@Async("customServiceExecutor")
	public void  uploadFileToIpfs(String lastDir,String filePath,IPFS ipfs,CollectionDetailMapper collectionDetailMapper,CollectionDetailEntity entity,FileBusibessdict.BusibessType dict) {
		File  source = new File(filePath);
		if(!source.exists()) {
			log.error("目录不存在");
			return ;
		}
		NamedStreamable file = new NamedStreamable.FileWrapper(source);
		try {
			List<MerkleNode> result =	ipfs.add(file);
			log.info(" 上传文件到IPFS结果：{} ",JSONObject.toJSONString(result));
			if(result.size()>0) {
				MerkleNode   folder =	  result.stream().filter( a -> a.name.get().equals(lastDir)).collect(Collectors.toList()).get(0);
				String	dir = folder.hash.toBase58();
				log.info("目录哈希：{}",folder.hash.toBase58());
				if(dict.equals( BusibessType.IMAGE)) {
						entity.setImgDirCid(dir);
						entity.setImgDirLocal(FileBusibessdict.BrowseImgUrl.concat(folder.name.get()) );
						ipfsImgService.asyncSave(result, dir, entity.getCollectionId());//保存图片信息
				}
				collectionDetailMapper.updateById(entity);
				
				uploadJsonToIpfs(entity,result,lastDir,ipfs,collectionDetailMapper);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private final ArtCollectionMapper artCollectionMapper; 
	public  void  uploadJsonToIpfs(CollectionDetailEntity c,List<MerkleNode> imgIpfs,String  lastDir,IPFS ipfs,CollectionDetailMapper collectionDetailMapper) {
		//https://ipfs.io/ipfs/QmYqd4b2Jrswi5eaBXkKwCDZv47dekYcvYjjYpRuQjtpFa?filename=one.jpg
		final String  perxUrl="https://ipfs.io/ipfs/";
		ArtCollectionEntity artC = artCollectionMapper.selectById(c.getCollectionId());
		JSONArray  arr = new JSONArray();
		String userDir = System.getProperties().getProperty("user.dir").concat(File.separator).concat("static");
		BusibessType dict =	FileBusibessdict.BusibessType.JSON;
		String browsePath = ("ipfs").concat(File.separator).concat(dict.getValue()).concat(File.separator).concat(lastDir); // 浏览器访问地址：
		String uploadPath = userDir.concat(File.separator).concat(browsePath); // 绝对路径
		File folder = new File(uploadPath);
		if (!folder.isDirectory()) // 如果文件夹不存在则创建
			folder.mkdirs();
		try {
		for (MerkleNode merkleNode : imgIpfs) {
			if( lastDir.equals(merkleNode.name.get()))
				continue;
			String ipfsImgCid = merkleNode.hash.toBase58();
			String[] fileInfo = merkleNode.name.get().split("/");
			String fileName= fileInfo[1].substring(0,fileInfo[1].lastIndexOf(".") );
			String imgurl = (FileBusibessdict.IpfsPrefix.concat(ipfsImgCid).concat(FileBusibessdict.IpfsSuffix).concat(fileInfo[1]));
			ArtMetaData  m = new ArtMetaData();
			m.setArtTitle(artC.getCollectionName());
			m.setCreatorAddress(m.getCreatorAddress());
			m.setSmartContractAddress(artC.getSmartContractAddress());
			m.setTokenAmount(1);
			m.setTokenId( fileName );
			m.setImageUri(imgurl);
			String  json = JSONObject.toJSONString(m);
			String filePath = uploadPath.concat(File.separator).concat(fileName).concat(".json");
			File  file = new File(filePath);
			if(file.exists())
				file.delete();
			file.createNewFile();
			FileWriter  fileWrite = new FileWriter(file, false);
			BufferedWriter  bufWriter = new BufferedWriter(fileWrite);
			bufWriter.write(json);
			bufWriter.close();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NamedStreamable files = new NamedStreamable.FileWrapper(folder);
		
		try {
		List<MerkleNode> result  = ipfs.add(files);
		log.info(" 上传JSON文件到IPFS结果：{} ",JSONObject.toJSONString(result));
		if(result.size()>0) {
			MerkleNode   folderNode =	  result.stream().filter( a -> a.name.get().equals(lastDir)).collect(Collectors.toList()).get(0);
			String	dir = folderNode.hash.toBase58();
			log.info("目录哈希：{}",dir);
			if(dict.equals( BusibessType.JSON)) {
				c.setJsonDirCid(dir);
				c.setJsonDirLocal(FileBusibessdict.BrowseJsUrl.concat(folderNode.name.get()) );
				ipfsJsonService.asyncSave(result, dir, c.getCollectionId());//保存图片信息
		}
		collectionDetailMapper.updateById(c);
			
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
