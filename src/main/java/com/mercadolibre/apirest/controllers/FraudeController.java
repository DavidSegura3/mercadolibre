package com.mercadolibre.apirest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mercadolibre.apirest.DTO.CurrencyDTO;
import com.mercadolibre.apirest.services.IFraudeService;

@RestController
@RequestMapping("/api")
public class FraudeController 
{
	@Autowired
	IFraudeService fraudeService;
	
	/**
	 * Endpoint para consultar una IP.
	 * @param ip Parámetro de entrada para buscar la IP.
	 * @return Retorna un objeto DTO con la siguiente información: nombrePais, moneda del pais y valor actual 
	 * de la moneda en Euros.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @author DavidSegura - 02/04/2021.
	 */
	@GetMapping("/fraude/ip/{ip}")
	public ResponseEntity<?> findIP(@PathVariable String ip, @RequestParam(required = false, defaultValue = "false") Boolean blackList) throws JsonMappingException, JsonProcessingException
	{
		Map<String, Object> response = new HashMap<String, Object>();
		try
		{
			if(blackList != true)
			{
				CurrencyDTO currencyDTO = fraudeService.consultIP(ip);
				if(currencyDTO != null)
				{
					response.put("country", currencyDTO);
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
				}
				else
				{
					response.put("mensaje", "La ip No: " + ip + " no existe");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
			}
			else
			{
				response.put("mensaje", "La ip : " + ip + " se encuentra en una lista negra y no es posible consultar su información.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
			}
		}
		catch (Exception e) 
		{
			response.put("error", "Failed to insert in database.");
			response.put("cause", e.getMessage().toString());
			response.put("action", "Please validate the data and try again.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}
}