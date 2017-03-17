package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpMappingDeleteEvent extends Event {

protected EventType ftpUserDeleteEvent = CommonEventTypes.FTPMAPPING_DELETE;
    
    public FtpMappingDeleteEvent(FtpMapping ftpMapping){
        
        super(CommonEventTypes.FTPMAPPING_DELETE);
        setData(ftpMapping);
    }
    
}
