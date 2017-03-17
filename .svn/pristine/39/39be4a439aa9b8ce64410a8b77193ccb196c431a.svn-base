package com.fable.outer.rmi.event.dto;

import java.io.Serializable;

import com.fable.outer.rmi.type.DataBaseTypes;


public class AddTableDto implements Serializable{

    
    /**
     * 
     */
    private static final long serialVersionUID = -4262971695422455024L;

    /**
     * 数据资源用户名
     */
    private String username;
    
    /**
     * 数据资源密码
     */
    private String password;
    
    /**
     * 数据库连接字符串
     */
    private String connect_url;
    
    /**
     * 表名
     */
    private String tablename;
    
    /**
     * 数据库名
     */
    private String dbname;

    /**
     * 是否重建触发器
     */
    private boolean rebuildTrigger;
    
    
    public boolean isRebuildTrigger() {
        return rebuildTrigger;
    }


    
    public void setRebuildTrigger(boolean rebuildTrigger) {
        this.rebuildTrigger = rebuildTrigger;
    }


    public String getDbname() {
        return dbname;
    }

    
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
    private String dbType;//用于查询从表信息的String类型dbType
    
    public String getDbType() {
		return dbType;
	}



	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	private DataBaseTypes dbtype;

    public DataBaseTypes getDbtype() {
        return dbtype;
    }

    public void setDbtype(DataBaseTypes dbtype) {
        this.dbtype = dbtype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnect_url() {
        return connect_url;
    }

    public void setConnect_url(String connect_url) {
        this.connect_url = connect_url;
    }

    public String getTablename() {
        return tablename;
    }
    
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    
}
