package com.lam.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("Fanout_Blog_exchange",true,false);
    }

    @Bean
    public Queue useQueue(){
        return new Queue("Blog_Use_queue",true);
    }

    @Bean
    public Queue VodQueue(){
        return new Queue("Blog_Vod_queue",true);
    }

    @Bean
    public Queue pageQueue(){
        return new Queue("Blog_Page_queue",true);
    }

    @Bean
    public Binding useBingding(){
        return BindingBuilder.bind(useQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding vodBingding(){
        return BindingBuilder.bind(VodQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding pageBingding(){
        return BindingBuilder.bind(pageQueue()).to(fanoutExchange());
    }
}
