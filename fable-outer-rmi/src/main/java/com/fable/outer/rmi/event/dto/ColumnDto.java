package com.fable.outer.rmi.event.dto;

import java.io.Serializable;

/**
 * 
 * @author 邱爽
 *
 */
public class ColumnDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6936532247563089281L;
	private String columnName;
	private int columnIndex;
	private int columnType;
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	public int getColumnType() {
		return columnType;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public ColumnDto(String columnName, int columnIndex, int columnType) {
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.columnType = columnType;
	}
	public ColumnDto() {
	}
	
	
}
