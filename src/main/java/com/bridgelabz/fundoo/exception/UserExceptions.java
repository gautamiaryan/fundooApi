package com.bridgelabz.fundoo.exception;

public class UserExceptions extends RuntimeException{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messages;
     
     public UserExceptions(String messages) {
    	 super(messages);
    	 this.messages=messages;
    	 
     }
     

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
}
