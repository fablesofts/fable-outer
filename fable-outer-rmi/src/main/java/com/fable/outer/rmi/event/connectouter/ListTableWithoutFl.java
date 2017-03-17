package com.fable.outer.rmi.event.connectouter;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;
public class ListTableWithoutFl extends Event{
	private static final long serialVersionUID = 5402935828152095380L;

	public ListTableWithoutFl(DataSourceDto ds) {
		super(CommonEventTypes.LISTTABLEWITHOUTFL);
		setData(ds);
	}
	
	
}
