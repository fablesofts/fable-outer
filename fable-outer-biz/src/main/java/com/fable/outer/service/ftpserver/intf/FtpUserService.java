package com.fable.outer.service.ftpserver.intf;

import java.util.List;

import com.fable.hamal.shuttle.common.model.config.FtpUser;

public interface FtpUserService{
    
    /**
     * 插入一个ftp用户.
     * @param ftpUser
     * @return
     */
    boolean insertFtpUser(com.fable.hamal.shuttle.common.model.config.FtpUser ftpUser);
 
    /**
     * 查询外交换所有FTP用户.
     * @return
     */
    List<FtpUser> getFtpUserList();
    
    /**
     * 修改FTP用户
     * @param ftpUser
     * @return
     */
    boolean updateFtpUser(FtpUser ftpUser);
    
    /**
     * 修改FTP用户密码.
     * @param ftpUser
     * @return
     */
    boolean updateFtpUserPass(FtpUser ftpUser);
    
    /**
     * 删除FTP用户.
     * @param ftpUser
     * @return
     */
    boolean deleteFtpUser(FtpUser ftpUser);
    
    /**
     * 根据USERID获取数据.
     * @param userid
     * @return
     */
    FtpUser getFtpUser(String userid);
    
}