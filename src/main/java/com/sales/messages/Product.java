package com.sales.messages;

import com.sales.Adjustment;

/**
 * Message object
 * @author Manga
 *
 */
public class Product {

    private String name;
    private int quantity;
    private double price;
    private Adjustment adjustment;
    private boolean isAdjusted;

    public Product(String name, int quantity, double price, Adjustment adjustment) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.adjustment = adjustment;
	}

	public boolean isAdjusted() {
		return isAdjusted;
	}

	public void setAdjusted(boolean isAdjusted) {
		this.isAdjusted = isAdjusted;
	}

	public Product() {
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Adjustment getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Adjustment adjustment) {
		this.adjustment = adjustment;
	}
	
	

}
