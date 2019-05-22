package mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq生产者
 * created by XUAN on 2019/05/05
 */
public class RabbitMqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "exchange_name";

        channel.exchangeDeclare(exchangeName,"direct", true);

        String routingKey = "routingKey";

        byte[] message = "hello".getBytes();

        channel.basicPublish(exchangeName,routingKey, null,message);

        channel.close();
        connection.close();
    }


}
