package com.example.mongo.config;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liwenbo
 * @Date 2019/11/25 10:56
 * @Description
 */
@Configuration
public class MqProducerConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(MqProducerConfiguration.class);

    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Bean
    public DefaultMQProducer getRocketMQProducer() {

//        DefaultMQProducer producer = new DefaultMQProducer("my-group");
//        producer.setNamesrvAddr("10.10.65.18:9876");
//        producer.setInstanceName("rmq-instance1");
//        producer.start();
//        try {
//            Message message = new Message("demo-topic", "demo-tag", "这是一条测试消息".getBytes());
//            producer.send(message);
//
//            while (true) {
//                Thread.sleep(3000);
//                String text = String.valueOf(System.currentTimeMillis());
//                Message msg = new Message("demo-topic",// topic
//                        "demo-tag1",// tag
//                        text.getBytes() // body
//                );
//                SendResult sendResult = producer.send(msg);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        producer.shutdown();
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);

        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setInstanceName("rmq-instance1");

        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if (this.retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }

        try {
            producer.start();

            LOGGER.info(String.format("_________________producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));
        } catch (MQClientException e) {
            LOGGER.error(String.format("producer is error {}"
                    , e.getMessage(), e));
        }
        return producer;

    }

}
