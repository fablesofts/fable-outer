package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class AddDBTableEvent extends Event {
	
	
    private static final long serialVersionUID = -3465020760003465443L;

	/**
	 * 查询外网表集合事件 
	 * @param ds 数据源实体对象
	 */
	public AddDBTableEvent(AddTableDto dto){
		super(CommonEventTypes.ADDDBTABLE);
		setData(dto);
	}
}
