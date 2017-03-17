package com.fable.outer.rmi.event.dto;

import java.io.Serializable;

public class ListColumnDataSourceDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3930525048019026545L;
	private DataSourceDto sourceDto;
	private String sourceTable;
	private DataSourceDto targetDto;
	private String targetTable;
	public DataSourceDto getSourceDto() {
		return sourceDto;
	}
	public void setSourceDto(DataSourceDto sourceDto) {
		this.sourceDto = sourceDto;
	}
	public String getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}
	public DataSourceDto getTargetDto() {
		return targetDto;
	}
	public void setTargetDto(DataSourceDto targetDto) {
		this.targetDto = targetDto;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	
	
	
}
