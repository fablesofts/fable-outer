package com.fable.outer.dmo.ftpserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fable.dsp.core.entity.AuditEntity;

/**
 * FTP路径映射表
 * 
 * @author 邱爽
 * 
 */
@Entity
@Table(name = "DSP_FTP_MAPPING")
public class FtpMapping extends AuditEntity {

	private static final long serialVersionUID = 5038646986481494413L;

	@Id
	@TableGenerator(name = "dspFTPMappingGen", table = "SYS_ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "DSP_FTP_MAPPING_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "dspFTPMappingGen")
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "USER_FLAG", length = 1, nullable = false)
	private Character userFlag;
	/**
	 * 内交换用户名
	 */
	@Column(name = "INNER_USERNAME", length = 32)
	private String innerUserName;
	/**
	 * 外交换用户名
	 */
	@Column(name = "OUTER_USERNAME", length = 32)
	private String outerUserName;
	/**
	 * 内交换路径
	 */
	@Column(name = "INNER_ADDRESS", length = 255)
	private String innerAddress;
	/**
	 * 外交换路径
	 */
	@Column(name = "OUTER_ADDRESS", length = 255)
	private String outerAddress;

	public Character getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(Character userFlag) {
		this.userFlag = userFlag;
	}

	public String getInnerUserName() {
		return innerUserName;
	}

	public void setInnerUserName(String innerUserName) {
		this.innerUserName = innerUserName;
	}

	public String getOuterUserName() {
		return outerUserName;
	}

	public void setOuterUserName(String outerUserName) {
		this.outerUserName = outerUserName;
	}

	public String getInnerAddress() {
		return innerAddress;
	}

	public void setInnerAddress(String innerAddress) {
		this.innerAddress = innerAddress;
	}

	public String getOuterAddress() {
		return outerAddress;
	}

	public void setOuterAddress(String outerAddress) {
		this.outerAddress = outerAddress;
	}

}
