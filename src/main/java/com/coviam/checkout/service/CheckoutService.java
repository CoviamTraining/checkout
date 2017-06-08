package com.coviam.checkout.service;

import java.util.List;

import com.coviam.checkout.entity.OrderDTO;

public interface CheckoutService {
	public String ifProductAvailable(List<OrderDTO> cart);
}
