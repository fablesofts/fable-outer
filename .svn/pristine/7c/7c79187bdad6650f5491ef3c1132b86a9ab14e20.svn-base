package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpMappingEvent extends Event {

    protected EventType ftpUserEvent = CommonEventTypes.FTPMAPPING;
    
    public FtpMappingEvent(FtpMapping ftpMapping){
        
        super(CommonEventTypes.FTPMAPPING);
        setData(ftpMapping);
    }
    
}
