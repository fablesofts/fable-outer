package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class JudgdbconnectEvent extends Event {
	
	
    private static final long serialVersionUID = 9156070895849131109L;

    /**
	 * 判断外网数据库连接
	 * @param ds 数据源实体
	 */
	public JudgdbconnectEvent(DataSourceDto ds){
		super(CommonEventTypes.JUDGDBCONNECT);
		setData(ds);
	}
}
