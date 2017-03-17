package com.fable.outer.rmi.event.handler.connectouter;


import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.biz.connectouter.impl.Web2OutNetWorkServiceImpl;
import com.fable.outer.biz.connectouter.intf.Web2OutNetWorkService;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.event.dto.ListColumnDataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;
@Service
public class Web2OutNetWorkEventHandler implements EventHandler {
	
	private Web2OutNetWorkService ws=null;
	
	/**
	 * 初始化注册Event的类型
	 * FINDLISTTABLE  查询外网所有表集合
	 * JUDGDBCONNECT  判断数据库是否可以连接、
	 * JUDGNETWORKCONNECT 判断网络是否可以连接、
	 */
	@PostConstruct
	public void init() {
		EventRegisterCenter.regist(CommonEventTypes.FINDLISTTABLE, this);
		EventRegisterCenter.regist(CommonEventTypes.JUDGDBCONNECT, this);
		EventRegisterCenter.regist(CommonEventTypes.JUDGNETWORKCONNECT, this);
		EventRegisterCenter.regist(CommonEventTypes.LISTTABLEWITHOUTFL,this);
		EventRegisterCenter.regist(CommonEventTypes.LISTCOLUMNBYTABLE,this);
		EventRegisterCenter.regist(CommonEventTypes.LISTFLTABLE,this);
		EventRegisterCenter.regist(CommonEventTypes.LISTDATECOLUMNNAME,this);
	}
	
	/**
	 * handle更具Event的type用不同的处理方法反不同的值说
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Object handle(Event event) {
		EventType type = event.getType();
		ws =new Web2OutNetWorkServiceImpl();
		//判断处理查询外网表
		if (CommonEventTypes.FINDLISTTABLE.equals(type)) {
			return ws.findListTable((DataSourceDto)event.getData());
		//判断数据库连接
		}else if(CommonEventTypes.JUDGDBCONNECT.equals(type)) {
			return ws.judgDBConnect((DataSourceDto)event.getData());
		//判断网络连接	
		}else if (CommonEventTypes.JUDGNETWORKCONNECT.equals(type)) {
			return ws.judgNetWorkLink((DataSourceDto)event.getData());
		//判断处理查询外网表（增量表除外）
		}else if(CommonEventTypes.LISTTABLEWITHOUTFL.equals(type))  {
			return ws.listTableWithoutFl((DataSourceDto)event.getData());
		}else if(CommonEventTypes.LISTCOLUMNBYTABLE.equals(type))   {
			return ws.listColumnByTable((ListColumnDataSourceDto)event.getData());
		}else if(CommonEventTypes.LISTFLTABLE.equals(type)) {
			return ws.listFTable((AddTableDto)event.getData());
		}else if(CommonEventTypes.LISTDATECOLUMNNAME.equals(type)) {
			return ws.dateColumnName((ListColumnDataSourceDto)event.getData());
		}
		return null;
	}

	
}
