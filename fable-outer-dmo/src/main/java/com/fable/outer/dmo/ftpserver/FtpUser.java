package com.fable.outer.dmo.ftpserver;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * FTP 用户管理
 * @author 邱爽
 *
 */
@Entity
@Table(name = "FTP_USER")
public class FtpUser implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1021617300255478396L;
    
    @Id
	@Column(name = "USERID")
	private String userid;
	
	@Column(name = "USERPASSWORD")
	private String userpassword;

	@Column(name = "ENABLEFLAG", length = 1)
	private String enableflag;
	
	@Column(name = "HOMEDIRECTORY")
	private String homeDirectory;
	
	
	public String getHomeDirectory() {
		return homeDirectory;
	}
	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getUserpassword() {
        return userpassword;
    }
    
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
    
    public String getEnableflag() {
        return enableflag;
    }
    
    public void setEnableflag(String enableflag) {
        this.enableflag = enableflag;
    }
    
}
