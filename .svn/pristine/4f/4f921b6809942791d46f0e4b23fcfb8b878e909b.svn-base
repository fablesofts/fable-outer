package com.fable.outer.rmi.event.handler.ftpserver;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;
import com.fable.outer.service.ftpserver.intf.FtpUserService;

@Service
public class FtpUserEventHandler implements EventHandler {

    @Autowired
    FtpUserService ftpUserService;
    
    @PostConstruct
    public void init() {
        System.out.println("##############CommonEventTypes.FTPUSER##################");
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER_LIST, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER_ONE, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER_UPDATE, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER_UPDATE_PASS, this);
        EventRegisterCenter.regist(CommonEventTypes.FTPUSER_DELETE, this);
        
    }
    
    public Object handle(Event event) {
        
        EventType type = event.getType();
        
        if(CommonEventTypes.FTPUSER.equals(type)){
            
            return ftpUserService.insertFtpUser((FtpUser)event.getData());
            
        }else if(CommonEventTypes.FTPUSER_LIST.equals(type)){
            return ftpUserService.getFtpUserList();
            
        }else if(CommonEventTypes.FTPUSER_UPDATE.equals(type)){
            return ftpUserService.updateFtpUser((FtpUser)event.getData());
            
        }else if(CommonEventTypes.FTPUSER_UPDATE_PASS.equals(type)){
            return ftpUserService.updateFtpUserPass((FtpUser)event.getData());
            
        }else if(CommonEventTypes.FTPUSER_DELETE.equals(type)){
            return ftpUserService.deleteFtpUser((FtpUser)event.getData());
            
        }else if(CommonEventTypes.FTPUSER_ONE.equals(type)){
            return ftpUserService.getFtpUser((String)event.getData());
        }
        
        return null;
    }

}
