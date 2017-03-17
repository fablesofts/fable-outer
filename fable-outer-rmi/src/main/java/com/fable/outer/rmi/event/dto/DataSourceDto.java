package com.fable.outer.rmi.event.dto;

import java.io.Serializable;

import com.fable.outer.rmi.type.DataBaseTypes;



/**
 * 数据源信息表
 * 
 * @author 吴浩
 * 
 */
public class DataSourceDto  implements Serializable {
	
	
    private static final long serialVersionUID = 2599353606041056073L;

    /**
	 * 数据资源名称
	 */
	protected String name;
	
	/**
	 * 数据资源IP地址 
	 */
	protected String server_ip;
	
	/**
	 * 数据资源端口
	 */
	protected Long port;
	
	/**
	 * 数据资源用户名
	 */
	protected String username;
	
	/**
	 * 数据资源密码
	 */
	protected String password;
	
	/**
	 * 数据资源逻辑删除
	 */
	protected Long del_flag;
	
	/**
	 * 数据资源描述
	 */
	protected String description;
	
	/**
	 * FTP资源路径
	 */
	protected String root_path;
	
	/**
	 * 数据资源类型
	 */
	protected String source_type;
	
	/**
	 * 数据库类型
	 */
	protected DataBaseTypes db_type;
	
	/**
	 * 数据库名称
	 */
	protected String db_name;
	
	/**
	 * 数据库连接字符串
	 */
	protected String connect_url;
	
	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
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

	public Long getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(Long del_flag) {
		this.del_flag = del_flag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer_ip() {
		return server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public String getRoot_path() {
		return root_path;
	}

	public void setRoot_path(String root_path) {
		this.root_path = root_path;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

    public DataBaseTypes getDb_type() {
        return db_type;
    }

    
    public void setDb_type(DataBaseTypes db_type) {
        this.db_type = db_type;
    }

    public String getConnect_url() {
		return connect_url;
	}

	public void setConnect_url(String connect_url) {
		this.connect_url = connect_url;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}


	
	
}
