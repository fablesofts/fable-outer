package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpUserUpdate extends Event {

    
    protected EventType ftpUserUpdateEvent = CommonEventTypes.FTPUSER_UPDATE; 
    
    public FtpUserUpdate(FtpUser ftpUser){
        super(CommonEventTypes.FTPUSER_UPDATE);
        setData(ftpUser);
    }
    
}
