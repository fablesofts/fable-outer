package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class FindlisttableEvent extends Event {
	
	
    private static final long serialVersionUID = 5402935828152095380L;

    /**
	 * 查询外网表集合事件 
	 * @param ds 数据源实体对象
	 */
	public FindlisttableEvent(DataSourceDto ds){
		super(CommonEventTypes.FINDLISTTABLE);
		setData(ds);
	}
}
