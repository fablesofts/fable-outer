package com.fable.outer.rmi.event.network;

import com.fable.hamal.shuttle.communication.a.example.HelloEventType;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;

public class NetworkCardCountEvent extends Event {

	protected EventType type = CommonEventTypes.GETCARDCOUNT;
	
	public NetworkCardCountEvent(){
		super(CommonEventTypes.GETCARDCOUNT);
	}
	
}
