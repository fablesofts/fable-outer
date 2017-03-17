package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class JudgnetworklinkEvent extends Event {
	
    private static final long serialVersionUID = 4327359845853633796L;

    /**
	 * 判断外网ip端口号连接
	 * @param ds 数据源实体
	 */
	public JudgnetworklinkEvent(DataSourceDto ds){
		super(CommonEventTypes.JUDGNETWORKCONNECT);
		setData(ds);
	}
}
