package com.fable.outer.rmi.event.sysinfo;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;

/**
 * @author zhangl
 */
public class DiskWatchEvent extends Event {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    protected EventType diskinfotype = CommonEventTypes.DISK;

    public DiskWatchEvent() {
        super(CommonEventTypes.DISK);

    }
}
