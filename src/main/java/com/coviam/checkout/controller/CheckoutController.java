package com.coviam.checkout.controller;



import java.io.IOException;
import java.util.List;

import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.coviam.checkout.entity.OrderDTO;
import com.coviam.checkout.entity.OrderListDTO;
import com.coviam.checkout.service.CheckoutService;
import com.coviam.checkout.PathProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class CheckoutController {
	@Autowired
	CheckoutService checkout;
	 @Autowired
	    private JavaMailSender javaMailSender;
	 private PathProperties paths;
	 @Autowired
	    public void setProduct(PathProperties paths) {
	        this.paths = paths;
	    }
	    
	 @RequestMapping(value = "/sendMail", method = RequestMethod.GET)
	    public String sendMail(String to,String productName,double productPrice,int productQuantity,int productId,int merchantId) {
	        SimpleMailMessage message = new SimpleMailMessage();
	       
	        // to="shripati.bhat@coviam.com";
	        String subject="Order Confirmation - Your Order " +productName+" with Doodle.com has been successfully placed!";
	        String body="Hi "+to +",\n"
	        		+ "Your order has been Successfully placed\n\n"
	        		+ "Name: "+ productName +"\n"
	        		+ "Price :"+ productPrice +"\n"
	        		+ "Quantity : "+ productQuantity +"\n\n\n"
	        		+ "Please click on the link to confirm the order "
	        		+ "http://172.16.20.28:9020/confirmOrder/"+productId+"/"+merchantId+"/"+to
	        		+ "\n\nThanks and Regards \nDOODLE E-Commerce"
	        		+"\n\nFor any help contact us at "
	        		+ "\nsupport@doodle.com";
	        
	        message.setTo(to);
	       
	        message.setSubject(subject);
	        message.setText(body);
	        javaMailSender.send(message);
	        return "mail Sent";
	    }
	
	
	
	@RequestMapping(value = "/addOrder", method = RequestMethod.POST)
	public String getOrderDTO(@RequestBody List<OrderDTO> list)
			throws IOException {
		System.out.println(list.toString());
		//List<OrderDTO> list = getOrderDTOFromJson(jsonOrderDTO);
		System.out.println(list.size() + "=====" + list.getClass());
		for( int i=0; i<list.size();i++){
			System.out.println(list.get(i));
		}
		String flag=checkout.ifProductAvailable(list);
		if(flag.equals("Success")){
			String placeorder="placeorder:"+list;
			String uri = paths.getOrder()+"/placeorder";
			
			//System.out.println("JSON" + placeorder);
			//OrderListDTO orderListObject = new OrderListDTO(list);
			RestTemplate restTemplate = new RestTemplate();
			String result =  restTemplate.postForObject(uri, list, String.class);
			 if(result.equals("Success")){
				 for(OrderDTO product : list){
					 sendMail(product.getUserEmail(),product.getProductName(),product.getProductPrice(),product.getOrderQuantity(),product.getProductId(),product.getMerchantId());
						
				 }
				
				 return "Success";
			 }
			//order place
			
		}
			return flag;
		}
//	 private List<OrderDTO> getOrderDTOFromJson(final List<OrderDTO> jsonOrderDTO)
//			 throws IOException {
//		 ObjectMapper objectMapper = new ObjectMapper();
//		 
//		 List<OrderDTO> orderDTOs =  objectMapper.readValue((JsonParser) jsonOrderDTO,
//				 	new TypeReference<List<OrderDTO>>(){});
//		 return orderDTOs;
//		 }
	 
}
