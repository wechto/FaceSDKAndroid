package com.liu;

import java.io.Serializable;

public class InfoBack implements Serializable {

	String state;


	public InfoBack(String state){
		this.state = state;//ok
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
