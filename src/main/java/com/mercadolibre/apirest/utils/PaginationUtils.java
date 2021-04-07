package com.mercadolibre.apirest.utils;

import java.util.Map;

public class PaginationUtils 
{
	public static String getValueFromMap(Map<String, String> map, String key)
	{
		if(map.containsKey(key) && !map.get(key).isEmpty())
			return map.get(key);
		return null;
	}
}
