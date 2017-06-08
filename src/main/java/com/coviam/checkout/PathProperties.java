package com.coviam.checkout;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class PathProperties {
	private String inventory;
	private String order;

	public String getInventory() {
		return inventory;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	
}

