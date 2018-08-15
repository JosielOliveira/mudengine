package com.jpinfo.mudengine.world.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.jpinfo.mudengine.common.utils.BaseServiceClient;

@Component
public class ItemServiceClientImpl extends BaseServiceClient implements ItemServiceClient {

	@Value("${item.endpoint}")
	private String itemEndpoint;
	
	@Autowired
	private RestTemplate restTemplate;

	
	@Override
	public void destroyAllFromPlace(String worldName, Integer placeCode) {
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("worldName", worldName);
		urlVariables.put("placeCode", placeCode);
	
		try {
			restTemplate.exchange(itemEndpoint + "/item/place/{worldName}/{placeCode}", 
					HttpMethod.DELETE, getEmptyHttpEntity(), Void.class, urlVariables);
			
		} catch(RestClientResponseException e) {
			handleError(e);
		}

		
	}

}
