package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.event.dto.ListColumnDataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;

public class ListDateColumnEvent extends Event{
	public ListDateColumnEvent(ListColumnDataSourceDto columnDataSourceDto) {
		super(CommonEventTypes.LISTDATECOLUMNNAME);
		setData(columnDataSourceDto);
	}
}
