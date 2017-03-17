package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpUserEvent extends Event {

    protected EventType ftpUserEvent = CommonEventTypes.FTPUSER; 
    
    public FtpUserEvent(FtpUser ftpUser){
        super(CommonEventTypes.FTPUSER);
        setData(ftpUser);
    }
}
