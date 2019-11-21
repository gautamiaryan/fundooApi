package com.bridgelabz.fundoo.exception;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
@ToString
@Data
public class ResponseError implements Serializable{
	
	private int status;
	
	private String response;
	
	private Object data;
	
	public ResponseError(int status,String response,Object data) {
		this.status=status;
		this.response=response;
		this.data=data;
				
	}

}
