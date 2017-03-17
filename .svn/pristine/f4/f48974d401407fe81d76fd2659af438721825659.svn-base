package com.fable.outer.service.ftpserver.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.outer.dao.ftpserver.intf.FtpMappingDao;
import com.fable.outer.service.ftpserver.intf.FtpMappingService;

@Service("ftpMappingService")
public class FtpMappingServiceImpl implements FtpMappingService {

    @Autowired
    FtpMappingDao ftpMappingDao;
    
    
    /**
     * 插入外交换FTP映射表
     */
    @Override
    public boolean insertFtpMapping(FtpMapping ftpMapping) {
        
        return ftpMappingDao.insertFtpMapping(ftpMapping);
    }


    @Override
    public boolean updateFtpMapping(FtpMapping ftpMapping) {
        
        return ftpMappingDao.updateFtpMapping(ftpMapping);
    }


    @Override
    public boolean deleteFtpMapping(FtpMapping ftpMapping) {
        
        return ftpMappingDao.deleteFtpMapping(ftpMapping);
    }

}
