package com.fable.outer.rmi.event.example;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;

/**
 * 示例代码
 * 
 * @author 汪朝
 * 
 */
public class ExampleEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected EventType sysinfotype = CommonEventTypes.EXAMPLE;

	public ExampleEvent() {
		super(CommonEventTypes.EXAMPLE);

	}
}
