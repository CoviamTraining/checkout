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
		int size = cart.size();			
		for( int i = 0;i<size;i++){
			 OrderDTO crt = cart.get(i);
			 String uri = paths.getInventory() +"/validateOrder/"+crt.getProductId()+"/"+crt.getMerchantId()+"/"+crt.getOrderQuantity(); 
			 String response = restTemplate.getForObject(uri, String.class);
			 System.out.println("response is :" + response);
			 if(response.equals("Not Success")){
				 System.out.println("Order out of stock");
				 System.out.println("outside"+i);
				 for (int j=0;j<i;j++){
					 System.out.println("inside"+i);
					 OrderDTO toRollBack = cart.get(j);
					 String rollbacUuri = paths.getInventory() +"/rollbackUpdatedStockAndSoldUpdate/"
				 +toRollBack.getProductId()+"/"+toRollBack.getMerchantId()+"/"+toRollBack.getOrderQuantity();
					 
					 String responseRollback=restTemplate.getForObject(rollbacUuri, String.class);
			     System.out.println(responseRollback+ " "+crt.getProductId());
				 }
				 return "The Product "+ crt.getProductName() + "is not available";
			 }
			
		}
		
		return "Success";
	}
}
