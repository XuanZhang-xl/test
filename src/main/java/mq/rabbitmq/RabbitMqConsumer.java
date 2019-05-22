package mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * created by XUAN on 2019/05/05
 */
public class RabbitMqConsumer {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "exchange_name";
        String queue = "exchange_queue";

        String routingKey = "routingKey";

        channel.exchangeDeclare(exchangeName, "direct", true);
        // 将队列和交换器绑定
        channel.queueBind(queue, exchangeName, routingKey);

        while (true) {
            boolean autoAck = true;
            String consumerTag = "";
            System.out.println("rabbitmq消费者启动");
            channel.basicConsume(queue, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费的路由键:" + envelope.getRoutingKey());
                    System.out.println("消费的内容类型:" + properties.getContentType());
                    long deliveryTag = envelope.getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    String bodyStr = new String(body, StandardCharsets.UTF_8);
                    System.out.println("消费的内容:" + bodyStr);
                }
            });
        }
    }
}
