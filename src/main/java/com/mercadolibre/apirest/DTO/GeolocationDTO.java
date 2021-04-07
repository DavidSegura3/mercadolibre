package com.mercadolibre.apirest.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeolocationDTO 
{
	private String countryCode;
	
	@JsonIgnore
	private String countryCode3;
	
	private String countryName;
	
	@JsonIgnore
	private String countryEmoji;
}