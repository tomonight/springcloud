package com.cm;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver {  
    private final static String QUEUE_NAME = "MyQueue";  
      
    public static void main(String[] args) throws IOException, TimeoutException {  
        receive();  
    }  
      
    public static void receive() throws IOException, TimeoutException  
    {  
    	ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("127.0.0.1");  
        // 打开连接和创建频道，与发送端一样  
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
  
        // 声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");  
          
        // 创建队列消费者  
        final Consumer consumer = new DefaultConsumer(channel) {  
              @Override  
              public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {  
                String message = new String(body, "UTF-8");  
  
                System.out.println(" [x] Received '" + message + "'");  
                System.out.println(" [x] Proccessing... at " +new Date().toLocaleString());  
              }  
            };  
            channel.basicConsume(QUEUE_NAME, true, consumer);  
    }  
}  