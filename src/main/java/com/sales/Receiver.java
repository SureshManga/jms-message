package com.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.sales.messages.Product;
import com.sales.vo.ReportItem;

@Component
public class Receiver {
	
	private static AtomicInteger salesCount = new AtomicInteger();
	private List<Product> productList = new ArrayList<>();
	private static final int MAX_MESSAGES = 50;
	private static final int LOG_COUNT = 10;
	private Map<String, ReportItem> reportMap = new HashMap<>();

    @JmsListener(destination = "salesmessage", containerFactory = "myFactory")
    public void receiveMessage(Product product) {
    	salesCount.incrementAndGet();
        if(salesCount.intValue() <= MAX_MESSAGES)
        {
        	// make adjustment
        	applyAdjustment(product);
        	
        	productList.add(product);
        	
        	// create report item
        	ReportItem reportItem = reportMap.get(product.getName());
        	if(reportItem == null)
        	{
        		reportItem = new ReportItem();
        	}
        	reportItem.setNoOfSales(reportItem.getNoOfSales()+1);
    		reportItem.setSalesTotal(reportItem.getSalesTotal() + (product.getQuantity() * product.getPrice()));
        	reportMap.put(product.getName(), reportItem);
        	
        	//log sales
        	if((salesCount.intValue()%LOG_COUNT) == 0)
        	{
        		System.out.println("#####Sales Report Start#####");
        		for(String key : reportMap.keySet())
        		{
        			ReportItem item = reportMap.get(key);
        			System.out.println("Product: "+key +" NoOfSales: "+item.getNoOfSales()+" OrderTotal: "+item.getSalesTotal());
        		}
        		System.out.println("#####Sales Report End#####");
        		reportMap = new HashMap<>();
        	}
        	
        	// log adjusted sales
        	if(salesCount.intValue() == MAX_MESSAGES)
        	{
        		if(!productList.isEmpty())
        		{
        			System.out.println("#####Adjusted Products Starts#####");
        			for(Product prod : productList)
        			{
        				if(prod != null && prod.isAdjusted())
        				{
        					System.out.println("Product: "+prod.getName() +" QtyTotal: "+prod.getQuantity()+" OrderTotal: "+prod.getPrice());
        				}
        			}
        			System.out.println("#####Adjusted Products End#####");
        		}
        	}
        }
        else
        {
        	System.out.println("#####Sales can't be processed as the limit exceeds#####");
        }
        
    }

    /**
     * This method is used to adjust the sales that are recorded
     * @param newProduct
     */
	private void applyAdjustment(Product newProduct) 
	{
		if(!productList.isEmpty() && newProduct != null && newProduct.getName() != null)
		{
			Adjustment adjustment = newProduct.getAdjustment();
			if( adjustment != null)
			{
				for(Product product : productList)
				{
					if(product != null && newProduct.getName().equals(product.getName()))
					{
						if(adjustment == Adjustment.ADD)
						{
							product.setPrice(product.getPrice()+newProduct.getPrice());
						}
						else if(adjustment == Adjustment.MULTIPLY)
						{
							product.setPrice(product.getPrice()* newProduct.getPrice());
						}
						else if(adjustment == Adjustment.SUBTRACT)
						{
							product.setPrice(product.getPrice()- newProduct.getPrice());
						}
						product.setAdjusted(true);
					}
				}
			}
		}
	}

}
