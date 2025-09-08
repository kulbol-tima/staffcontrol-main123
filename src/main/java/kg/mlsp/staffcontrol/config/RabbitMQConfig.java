package kg.mlsp.staffcontrol.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
@Slf4j
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "mlsp-logging-exchange";
    public static final String QUEUE_NAME = "mlsp-logging-queue";
    public static final String ROUTING_KEY = "staffcontrol.logging";

    @PostConstruct
    public void checkExchange() {
        try {
            log.info("Checking exchange '{}' accessibility", EXCHANGE_NAME);
        } catch (Exception e) {
            log.error("Error checking exchange '{}': {}", EXCHANGE_NAME, e.getMessage());
        }
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    public DirectExchange exchange() {
        // Используем существующий обмен, не создаем новый
        // durable=true, autoDelete=false, internal=false
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ObjectMapper rabbitObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
