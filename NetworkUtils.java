 /**
 * @(#) NetworkUtils.java Feb 16, 2019 1:44:06 PM
 * Copyright 2018 wzk Software Co., Ltd. All Rights Reserved
 */
package com.wzk.common.tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/** 
 * 获取本机所有ip地址工具类
 * 方案一：通过主机名
 * 对应的方法
 * 	getFirstLocalHostIP()
 *  getAllLocalHostIP()
 * 获取逻辑：
 * 1，获取主机名
 * 2，通过主机名获取所有ip
 * 3，取第一个（如果ip中包含10开头的ip取10）
 * 该类是为了在实际环境中存在多网卡的情况获取外网ip使用，本人所在环境对外ip一般为10开头，所以加了一个判断
 * 如果有10开头的优先取，这里并不保险，如果存在多网卡又没有规律，最好是增加配置文件来配置外网ip，结合本工具
 * 类来确定外网ip
 * 
 * 方案二：通过网络接口
 * 对应的方法
 * 	getLocalLANIP()
 * 获取逻辑
 * 1，获取所有网络接口
 * 2，循环所有网络接口，并循环网络接口下的所有ip地址
 * 3，取isSiteLocalAddress 即：本地连接地址
 * 4，获取的可能是多个，可以根据实际修改这里的逻辑，默认是只要找到一个满足条件就返回
 * 5，如果以上循环都没有找到就直接使用：InetAddress.getLocalHost() 获取，该方法
 *    在win下可正常获取，在linux下会返回127.0.0.1
 * <code> NetworkUtils </code>
 * @Package com.yonyou.ycm.common.tools 
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
	private String[] getAllLocalHostIP() {
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
	/**
	 * 获取本地ip，通过网络接口获取，通过内层循环可以获取到所有ip
	 * 该方法获取ip地址最全，最准确，可通过该方法获取，如果没有获取到再使用通过hostname的方法
	 * 获取ip即:getFirstLocalHostIP
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getLocalLANIP(){
	    try {
	        String targetIP = "127.0.0.1";
	        // 遍历所有的网络接口
	        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	        while(networkInterfaces.hasMoreElements()) {
	            NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
	            // 在所有的接口下再遍历IP
	            Enumeration<InetAddress> inetAddrs = networkInterface.getInetAddresses();
	            while (inetAddrs.hasMoreElements()) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                // 排除loopback类型地址 
	                // 本地环回接口(或地址)，亦称回送地址(loopback address)
	                // loopback地址就是代表本机的IP地址。IPv4的loopback地址的范围是127.0.0.0 ~ 127.255.255.255
	                // IPv6的loopback地址是0：0：0：0：0：0：0：1，也可以简写成：：1
	                if (!inetAddr.isLoopbackAddress()) {//isLoopbackAddress
	                    if (inetAddr.isSiteLocalAddress()) {//当IP地址是地区本地地址（SiteLocalAddress）时返回true
	                        // 如果是site-local地址，就是它了
	                    		/* 
	                    		IPv4的地址本地地址分为三段：
	                    			10.0.0.0 ~ 10.255.255.255、
	                    			172.16.0.0 ~ 172.31.255.255、
	                    			192.168.0.0 ~ 192.168.255.255
	                    		IPv6的地区本地地址的前12位是FEC，其他的位可以是任意取值，如FED0：：、FEF1：：都是地区本地地址
	                        */
	                        return inetAddr.getHostAddress();
	                    }
	                }
	            }
	        }
	        // 如果以上都没有找到，那就直接通过最简单的方式获取
	        // 之所以说这个方法不推荐直接用主要因为
	        // 我们在windows系统，我们可以获取正确的IP
	        // 但是放在linux，可能是获取的IP地址为：127.0.0.1
	        targetIP = InetAddress.getLocalHost().getHostAddress();
	        return targetIP;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	//测试
	public static void main(String[] args){
		
		String ip0 = getInstance().getFirstLocalHostIP();
		System.out.println(ip0);
		
		String localLANIP = getInstance().getLocalLANIP();
		System.out.println(localLANIP);
	}
}
