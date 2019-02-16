 /**
 * @(#) NetworkUtils.java Feb 16, 2019 1:44:06 PM
 * Copyright 2018 wzk Software Co., Ltd. All Rights Reserved
 */
package com.wzk.common.tools;

import java.net.InetAddress;

/** 
 * 获取本机所有ip地址工具类
 * 获取逻辑：
 * 1，获取主机名
 * 2，通过主机名获取所有ip
 * 3，取第一个（如果ip中包含10开头的ip取10）
 * 该类是为了在实际环境中存在多网卡的情况获取外网ip使用，本人所在环境对外ip一般为10开头，所以加了一个判断
 * 如果有10开头的优先取，这里并不保险，如果存在多网卡又没有规律，最好是增加配置文件来配置外网ip，结合本工具
 * 类来确定外网ip
 * <code> NetworkUtils </code>
 * @Package com.wzk.common.tools 
 * @author wangzhikui
 * @date Feb 16, 2019 1:44:06 PM 
 * @version   1.0
 * @since jdk1.7 
 */

public class NetworkUtils {
	private static NetworkUtils instance;

	private NetworkUtils() {
	}
	//获取主机名
	public String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}
	//获取所有ip，取第一个，如果有10开头的先取10开头的，外网ip
	public String getFirstLocalHostIP(){
		String[] ips = getAllLocalHostIP();
		if(ips != null && ips.length > 0){
			for(String ip : ips){
				if(ip.startsWith("10.")){
					return ip;
				}
			}
			return ips[0];
		}
		return "127.0.0.1";
	}
	//根据主机名称获取所有ip地址
	public String[] getAllLocalHostIP() {
		String[] ret = null;
		try {
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	public static NetworkUtils getInstance() {
		if (instance == null) {
			instance = new NetworkUtils();
		}
		return instance;
	}
	
	public static void main(String[] args){
		String[] ips = getInstance().getAllLocalHostIP();
		System.out.print(ips[0]);
	}

}
