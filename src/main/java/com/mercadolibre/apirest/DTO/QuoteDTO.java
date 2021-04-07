package com.mercadolibre.apirest.DTO;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDTO 
{
	private String success;
	private Timestamp timestamp;
	private String base;
	private Date date;
	private HashMap<String, String> rates; 	
}