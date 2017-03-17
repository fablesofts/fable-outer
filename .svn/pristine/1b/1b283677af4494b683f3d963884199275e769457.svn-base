package com.fable.outer.rmi.event.handler.sysinfo;


import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.outer.rmi.event.help.WatchSysInfo;
import com.fable.outer.rmi.type.CommonEventTypes;
@Service
public class SysInfoWatchEventHandler implements EventHandler {
	
	@PostConstruct
	public void init() {
		EventRegisterCenter.regist(CommonEventTypes.CPUINFO, this);
		EventRegisterCenter.regist(CommonEventTypes.MEMINFO, this);
		EventRegisterCenter.regist(CommonEventTypes.DISK, this);
	}
	
	public Object handle(Event event) {
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");

		if (os.startsWith("win") || os.startsWith("Win")) {
			if(event.getType().equals(CommonEventTypes.CPUINFO)){
				return WatchSysInfo.getCpuRatioForWindows();
			}else if(event.getType().equals(CommonEventTypes.MEMINFO)){
				return WatchSysInfo.getMemerypre();
			}else if(event.getType().equals(CommonEventTypes.DISK)){
				return WatchSysInfo.getDisk();
			}
		} else if (os.startsWith("lin") || os.startsWith("Lin")) {
			if(event.getType().equals(CommonEventTypes.CPUINFO)){
				try {
					return WatchSysInfo.getLinuxCpuUsage();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else if(event.getType().equals(CommonEventTypes.MEMINFO)){
				try {
					return WatchSysInfo.getLinuxMemUsage();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else if(event.getType().equals(CommonEventTypes.DISK)){
				try {
					return WatchSysInfo.getLinuxDiskUsage();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return null;
	}

}
