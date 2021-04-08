package com.mercadolibre.apirest.DTO;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO 
{
	private String name;
	@JsonIgnore
	private List<String> topLevelDomain;
	private String alpha2Code;
	@JsonIgnore
	private String alpha3Code;
	@JsonIgnore
	private List<String> callingCodes;
	@JsonIgnore
	private String capital;
	@JsonIgnore
	private List<String> altSpellings;
	@JsonIgnore
	private String region;
	@JsonIgnore
	private String subregion;
	@JsonIgnore
	private String population;
	@JsonIgnore
	private List<Integer> latlng;
	@JsonIgnore
	private String demonym;
	@JsonIgnore
	private Integer area;
	@JsonIgnore
	private Integer gini;
	@JsonIgnore
	private List<String> timezones;
	@JsonIgnore
	private List<String> borders;
	@JsonIgnore
	private String nativeName;
	@JsonIgnore
	private Integer numericCode;
	private List<Currency> currencies;
	@JsonIgnore
	private List<String> languages;
	@JsonIgnore
	private List<String> translations;
	@JsonIgnore
	private String flag;
	@JsonIgnore
	private List<String> regionalBlocs;
	@JsonIgnore
	private String cioc;
	private String currentValue;
	private Date date;
}