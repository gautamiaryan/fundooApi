package com.bridgelabz.fundoo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
//@Entity
//@Table(name="note_details")
//@Getter
//@Setter
//@AllArgsConstructor
public class Note {
	
	private Integer id;
	
	private String title;
	
	private String description;
	
	private boolean isVarified;
	
	private boolean isPinned;
	
	private String colour;
	
	private LocalDateTime createdStamp;
	
	private LocalDateTime updatedStamp;
	
	

}
