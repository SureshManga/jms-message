
package com.sales;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.sales.messages.Product;


@SpringBootApplication
@EnableJms
public class Application {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    public static void main(String[] args) {
   
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        System.out.println("#####Sending Sales messages.######");
        List<Product> productList = getProductList();
        if(!productList.isEmpty())
        {
        	for(Product product : productList)
        	{
        		jmsTemplate.convertAndSend("salesmessage", product);
        	}
        }
    }
    
    public static List<Product> getProductList()
    {
    	 List<Product> products = new ArrayList<>();
    	for(int i=0;i<51;i++)
    	{
    		if(i%10 != 0)
    		{
    			products.add(new Product("item"+i,i,i,null));
    		}
    		else
    		{
    			products.add(new Product("item"+(i/10),i,i,Adjustment.ADD));
    		}
    	}
    	return products;
    }

}
