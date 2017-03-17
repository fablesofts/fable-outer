package com.fable.outer.rmi.event.ftpserver;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;


public class FtpUserUpdatePassEvent extends Event {

    
protected EventType ftpUserUpdatePassEvent = CommonEventTypes.FTPUSER_UPDATE_PASS; 
    
    public FtpUserUpdatePassEvent(FtpUser ftpUser){
        super(CommonEventTypes.FTPUSER_UPDATE_PASS);
        setData(ftpUser);
    }
    
}
