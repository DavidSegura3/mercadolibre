package com.mercadolibre.apirest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mercadolibre.apirest.DTO.CurrencyDTO;

public interface IFraudService 
{
	public CurrencyDTO consultIP(String ip) throws JsonMappingException, JsonProcessingException;
}