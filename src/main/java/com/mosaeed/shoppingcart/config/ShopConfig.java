package com.mosaeed.shoppingcart.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // to set a bean
public class ShopConfig {

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

}
