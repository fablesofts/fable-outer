package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class FindFlTableEvent extends Event{

	private static final long serialVersionUID = -5621098175901428581L;
	/**
	 * 根据主表查询从表集合事件
	 * @param ds
	 */
	public FindFlTableEvent(AddTableDto addTableDto) {
		super(CommonEventTypes.LISTFLTABLE);
		setData(addTableDto);
	}
}
