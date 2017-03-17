package com.fable.outer.rmi.event.handler.ftpserver;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;
import com.fable.outer.service.ftpserver.intf.FtpMappingService;

@Service
public class FtpMappingEventHandler implements EventHandler {

    @Autowired
    FtpMappingService ftpMappingService;

    @PostConstruct
    public void init() {

        EventRegisterCenter.regist(CommonEventTypes.FTPMAPPING, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPMAPPING_UPDATE, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPMAPPING_DELETE, this);
    }



    public Object handle(Event event) {

        EventType type = event.getType();

        if (CommonEventTypes.FTPMAPPING.equals(type)) {

            return ftpMappingService.insertFtpMapping((FtpMapping)event.getData());
            
        }else if(CommonEventTypes.FTPMAPPING_UPDATE.equals(type)){
            
            return ftpMappingService.updateFtpMapping((FtpMapping)event.getData());
            
        }else if(CommonEventTypes.FTPMAPPING_DELETE.equals(type)){
         
            return ftpMappingService.deleteFtpMapping((FtpMapping)event.getData());
            
        }

        return null;
    }

}
