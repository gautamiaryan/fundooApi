package com.bridgelabz.fundoo.exception;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@ToString
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response implements Serializable{


	private Integer status;
	
	private String response;
	
	private Object data;
	
	
	

}
