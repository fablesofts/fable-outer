package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpUserDeleteEvent extends Event {

protected EventType ftpUserDeleteEvent = CommonEventTypes.FTPUSER_DELETE; 
    
    public FtpUserDeleteEvent(FtpUser ftpUser){
        super(CommonEventTypes.FTPUSER_DELETE);
        setData(ftpUser);
    }
    
}
