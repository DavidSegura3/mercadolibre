package com.mercadolibre.apirest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.apirest.DTO.CurrencyDTO;
import com.mercadolibre.apirest.DTO.GeolocationDTO;
import com.mercadolibre.apirest.DTO.QuoteDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FraudeServiceImpl implements IFraudeService
{
	public static final String ACCESS_KEY = "3dc157ffc9f5ab951849fe356611d5a6";
	
	@Value("${path.external.geolocation}")
	private String pathGeolocation;
	
	@Value("${path.external.country}")
	private String pathCountry;
	
	@Value("${path.external.currency}")
	private String pathCurrency;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public CurrencyDTO consultIP(String ip) throws JsonMappingException, JsonProcessingException
	{
		String url = String.format(pathGeolocation, ip);
		String geolocation = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		GeolocationDTO geolocationDTO = mapper.readValue(geolocation, GeolocationDTO.class);
		CurrencyDTO currencyDTO = consultCurrency(geolocationDTO);
		QuoteDTO quote = consultQuote();
		currencyDTO.setCurrentValue(quote.getRates().get(currencyDTO.getCurrencies().get(0).getCode()));
		return currencyDTO;
	}
	
	private CurrencyDTO consultCurrency(GeolocationDTO geolocationDTO) throws JsonMappingException, JsonProcessingException
	{
		String url = String.format(pathCountry, geolocationDTO.getCountryCode());
		String countryCurrency = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		CurrencyDTO currencyDTO = mapper.readValue(countryCurrency, CurrencyDTO.class);
		return currencyDTO;
	}
	
	private QuoteDTO consultQuote() throws JsonMappingException, JsonProcessingException
	{
		String url = String.format(pathCurrency, ACCESS_KEY);
		String quote = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		QuoteDTO quoteDTO = mapper.readValue(quote, QuoteDTO.class);
		return quoteDTO;
	}
}