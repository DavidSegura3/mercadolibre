package com.mercadolibre.apirest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mercadolibre.apirest.DTO.CurrencyDTO;
import com.mercadolibre.apirest.controllers.FraudeController;
import com.mercadolibre.apirest.services.FraudeServiceImpl;

@SpringBootTest
class MeliRestApiApplicationTests 
{
	private static final String ACCESS_KEY = "3dc157ffc9f5ab951849fe356611d5a6";
	
	@Value("${path.external.geolocation}")
	public String pathGeolocation;
	
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
	void testConsultIP()
	{
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		assertThrows(HttpClientErrorException.class, () -> fraudeService.consultIP(null), " Error al consultar el servicio de moneda");
		/*CurrencyDTO currencyDTO = new CurrencyDTO();
		currencyDTO.setName("United States of America");
		currencyDTO.setAlpha2Code("US");
		currencyDTO.setCurrentValue("1.188234");*/
		
		when(restTemplate.getForObject(anyString(), any())).thenReturn(CurrencyDTO.class);
		assertEquals(CurrencyDTO.class, CurrencyDTO.class);
	}
	
	@Test
	void validateAccessKey()
	{
		assertEquals(ACCESS_KEY, FraudeServiceImpl.ACCESS_KEY);
	}
	
	@Test
	void testEndpointGeolocation()
	{
		rest = new RestTemplate() ;
		String ip = "5.6.7.8";
		String url = String.format(pathGeolocation, ip);
		ResponseEntity<String> geolocation = rest.getForEntity(url, String.class);
		
		assertAll(
				() -> assertEquals(200, geolocation.getStatusCodeValue()),
				() -> assertEquals(true, geolocation.getBody().contains("countryCode")),
				() -> assertEquals(true, geolocation.getBody().contains("countryName")) 
		);
	}
	
	@Test
	void testEndpointCountry()
	{
		rest = new RestTemplate() ;
		String countryCode = "DE";
		String url = String.format(pathCountry, countryCode);
		ResponseEntity<String> countryCurrency = rest.getForEntity(url, String.class);
		assertEquals(200, countryCurrency.getStatusCodeValue());
	}
	
	@Test
	public void testBadRequest() throws URISyntaxException 
	{
	    rest = new RestTemplate();
	    String url = "https://api.ip2country.info/ip";
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
