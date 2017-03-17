package com.fable.outer.rmi.event.dto;

import java.io.Serializable;
import java.util.List;


public class TreeDataDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1882125744894002972L;


    private String id;

    private String text;

    private List<TreeDataDto> children;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public List<TreeDataDto> getChildren() {
        return children;
    }


    public void setChildren(List<TreeDataDto> children) {
        this.children = children;
    }


	@Override
	public String toString() {
		return this.getId()+"__"+this.getText();
	}

    
}
