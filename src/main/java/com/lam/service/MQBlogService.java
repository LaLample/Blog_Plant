package com.lam.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lam.Handler.GlobalException;
import com.lam.model.entity.po.Blog;
import com.lam.model.entity.po.Redundancy;
import com.lam.model.mapper.RedundancyMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class MQBlogService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedundancyMapper redundancyMapper;

    ObjectMapper objectMapper=new ObjectMapper();

    //用来修饰非静态的void方法，被@postConstruct修饰的方法会在服务器加载servlet的时候运行
    @PostConstruct
    public void regCallback(){

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println(cause);

                String blogId=correlationData.getId();

                if(!ack){
                   throw new GlobalException(-10,"队列应答失败，blogId:"+blogId);
                }
                UpdateWrapper<Redundancy> updateWrapper=new UpdateWrapper<>();
                updateWrapper.eq("isDelete",0);
                updateWrapper.eq("BlogId",blogId);
                updateWrapper.set("BlogAck",1);
              int count=  redundancyMapper.update(null,updateWrapper);
                if(count==1){
                    System.out.println("消息成功投递到消息队列中");
                }
                else {
                    System.out.println("消息投递失败");
                }

            }
        });

    }




    public void sendMessage(Blog blog) {
        String jsonString= null;
        try {
            jsonString = objectMapper.writeValueAsString(blog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        rabbitTemplate.convertAndSend("Fanout_Blog_exchange","",jsonString,new CorrelationData(blog.getTxtId().toString()));
    }
}
