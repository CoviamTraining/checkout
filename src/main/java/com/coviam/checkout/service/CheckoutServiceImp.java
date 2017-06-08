package com.coviam.checkout.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.coviam.checkout.entity.OrderDTO;

import com.coviam.checkout.PathProperties;

@Component
public class CheckoutServiceImp implements CheckoutService{
	
	private PathProperties paths;
	RestTemplate restTemplate = new RestTemplate();
	@Autowired
    public void setProduct(PathProperties paths) {
        this.paths = paths;
    }
	public String ifProductAvailable(List<OrderDTO> cart){
				
		for( OrderDTO crt : cart){
			 String uri = paths.getInventory() +"/getstock/"+crt.getProductId()+"/"+crt.getMerchantId(); 
			 int stock = restTemplate.getForObject(uri, Integer.class);
			 if(stock<=0){
				 return "The Product "+ crt.getProductId() + "is not available";
			 }
			
		}
		
		return "Success";
	}
}
