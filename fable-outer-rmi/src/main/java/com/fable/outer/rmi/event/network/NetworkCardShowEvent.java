package com.fable.outer.rmi.event.network;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;

public class NetworkCardShowEvent extends Event {
	protected EventType type = CommonEventTypes.SHOWCARD;
	
	public NetworkCardShowEvent(){
	    
		super(CommonEventTypes.SHOWCARD);
	}
	
	
}
