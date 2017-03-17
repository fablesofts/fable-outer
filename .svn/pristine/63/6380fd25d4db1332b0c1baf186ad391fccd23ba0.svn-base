package com.fable.outer.service.ftpserver.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.outer.dao.ftpserver.intf.FtpUserDao;
import com.fable.outer.service.ftpserver.intf.FtpUserService;

@Service("ftpUserService")
public class FtpUserServiceImpl implements FtpUserService {

    @Autowired
    FtpUserDao ftpUserDao;
    
    @Override
    public boolean insertFtpUser(FtpUser ftpUser) {
        
        return ftpUserDao.insertFtpUser(ftpUser);
    }

    @Override
    public List<FtpUser> getFtpUserList() {
        return ftpUserDao.getFtpUserList();
    }

    @Override
    public boolean updateFtpUser(FtpUser ftpUser) {
        return ftpUserDao.updateFtpUser(ftpUser);
    }

    @Override
    public boolean updateFtpUserPass(FtpUser ftpUser) {
        return ftpUserDao.updateFtpUserPass(ftpUser);
    }

    @Override
    public boolean deleteFtpUser(FtpUser ftpUser) {
        return ftpUserDao.deleteFtpUser(ftpUser);
    }

    @Override
    public FtpUser getFtpUser(String userid) {

        
        return ftpUserDao.getFtpUser(userid);
    }

}
