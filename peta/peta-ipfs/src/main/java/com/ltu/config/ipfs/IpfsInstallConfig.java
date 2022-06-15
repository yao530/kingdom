package com.ltu.config.ipfs;

import io.ipfs.api.IPFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.ipfs.multiaddr.MultiAddress;

@Configuration
public class IpfsInstallConfig {

	@Value("${ipfs-remote}")
	private String ipfsRemote="/ip4/47.112.108.183/tcp/10053";
//	private String ipfsRemote="http://47.112.108.183:10053";
	
	@Bean
	public IPFS ipfs() {
		//输入整个地址
		MultiAddress multiaddr = new MultiAddress(ipfsRemote);
		IPFS ipfs = new IPFS(multiaddr);
		return ipfs;
	}
}
