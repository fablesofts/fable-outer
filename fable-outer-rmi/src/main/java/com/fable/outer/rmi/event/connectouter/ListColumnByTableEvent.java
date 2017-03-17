package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.event.dto.ListColumnDataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class ListColumnByTableEvent extends Event{
	private static final long serialVersionUID = -4620982179018083683L;

	/**
	 * 根据相应数据源找出对应表名的所有字段信息
	 */
	public ListColumnByTableEvent(ListColumnDataSourceDto dto) {
		super(CommonEventTypes.LISTCOLUMNBYTABLE);
		setData(dto);
	}
}
