package com.mercadolibre.apirest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.apirest.DTO.GeolocationDTO;
import com.mercadolibre.apirest.controllers.FraudeController;
import com.mercadolibre.apirest.services.FraudeServiceImpl;

@SpringBootTest
class MeliRestApiApplicationTests 
{
	private static final String ACCESS_KEY = "3dc157ffc9f5ab951849fe356611d5a6";
	
	@Value("${path.external.geolocation}")
	private String pathGeolocation;
	
	@Value("${path.external.country}")
	private String pathCountry;
	
	@Value("${path.external.currency}")
	private String pathCurrency;
	
	@Mock
	private RestTemplate restTemplate;
	
	RestTemplate rest = new RestTemplate() ;
	
	private FraudeServiceImpl fraudeService;
	
	@Autowired
	FraudeController fraudeController;
	
	@Test
	@DisplayName("Testeando que el controlador tenga por lo menos un método")
	void contextLoads() 
	{
		assertThat(fraudeController).isNotNull();
	}

	@BeforeEach
	void inicializacion()
	{
		fraudeService = spy(new FraudeServiceImpl("pathGeo", "pathIso", "pathCoti", restTemplate));
	}
	
	@Test
	@DisplayName("Testeando que el método consultIP no genere una excepción de tipo HttpClientErrorException.")
	void testConsultIP()
	{
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		assertThrows(HttpClientErrorException.class, () -> fraudeService.consultIP(null), " Error al consultar el servicio de moneda");
	}
	
	@Test
	@DisplayName("Probando que la access_key para el método de consultQuote.")
	void validateAccessKey()
	{
		assertEquals(ACCESS_KEY, FraudeServiceImpl.ACCESS_KEY);
	}
	
	@Test
	@DisplayName("Testeando que el servicio me retorne código HttpStatus 200 y adicional traiga la información necesaria para"
			+ " que los otros servicios funcionen correctamente.")
	void testEndpointGeolocation()
	{
		rest = new RestTemplate() ;
		String ip = "5.6.7.8";
		String url = String.format(pathGeolocation, ip);
		ResponseEntity<String> geolocation = rest.getForEntity(url, String.class);
		
		assertAll(
				() -> assertEquals(200, geolocation.getStatusCodeValue(), () -> "El código HttpStatus no es el esperado"),
				() -> assertEquals(true, geolocation.getBody().contains("countryCode"), () -> "countryCode no viene en el body y no es el esperado"),
				() -> assertEquals(true, geolocation.getBody().contains("countryName"), () -> "countryName no viene en el body y no es el esperado") 
		);
	}
	
	@Test
	@Disabled
	void testEndpointCountry()
	{
		rest = new RestTemplate() ;
		String countryCode = "DE";
		String url = String.format(pathCountry, countryCode);
		ResponseEntity<String> countryCurrency = rest.getForEntity(url, String.class);
		assertEquals(200, countryCurrency.getStatusCodeValue());
		fail();
	}
	
	@Test
	@DisplayName("Testeando que el objeto no sea nulo, también que los valores que vengan en el objeto no sean vacios,"
			+ " o por lo menos countryCode y countryName")
	void testEndpointGeolocationEmpty()
	{
		GeolocationDTO geolocation = null;
		try 
		{
			String url = String.format(pathGeolocation, "5.6.7.8");
			String geo = rest.getForObject(url, String.class);
			ObjectMapper mapper = new ObjectMapper();
			geolocation = mapper.readValue(geo, GeolocationDTO.class);
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
		
		assertNotNull(geolocation);
		assertTrue(!geolocation.getCountryCode().isEmpty());
		assertFalse(geolocation.getCountryName().isEmpty());
	}
	
	@Test
	@DisplayName("Testeando que el servicio de ip, reciba cómo parametro la ip a consultar.")
	public void testBadRequest() throws URISyntaxException 
	{
	    rest = new RestTemplate();
	    String url = "https://api.ip2country.info/ip?5.6.7.8";
		URI uri = new URI(url);
	    
	    try
	    {
	    	rest.getForEntity(uri, String.class);
	    }
	    catch(HttpClientErrorException ex) 
	    {
	        assertEquals(400, ex.getRawStatusCode());
	        assertEquals(true, ex.getResponseBodyAsString().contains("Bad Request"));
	    }
	}
}
