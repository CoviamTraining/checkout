package com.coviam.checkout.entity;

import java.util.List;

public class OrderListDTO {
	public List<OrderDTO> placeorder;

	public OrderListDTO(List<OrderDTO> list) {
		
		this.placeorder = list;
	}
	
}
