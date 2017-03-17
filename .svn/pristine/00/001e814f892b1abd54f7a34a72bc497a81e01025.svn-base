package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpMappingUpdateEvent extends Event {

    protected EventType ftpUserUpdateEvent = CommonEventTypes.FTPMAPPING_UPDATE;
    
    public FtpMappingUpdateEvent(FtpMapping ftpMapping){
        
        super(CommonEventTypes.FTPMAPPING_UPDATE);
        setData(ftpMapping);
    }
    
}
