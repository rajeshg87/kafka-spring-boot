package com.rajesh.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConfig {

    @Bean
    ConcurrentKafkaListenerContainerFactory<?,?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory){
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory=new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.setConcurrency(3);
        factory.setErrorHandler((thrownException, data) ->
                log.info("Exception in consumer config is {} and the record is {}",
                        thrownException.getMessage(), data));
        factory.setRetryTemplate(retryTemplate());
        return factory;
    }

    private RetryTemplate retryTemplate() {
        FixedBackOffPolicy fixedBackOffPolicy=new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000);
        RetryTemplate retryTemplate=new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        return retryTemplate;
    }

    private RetryPolicy simpleRetryPolicy() {
        SimpleRetryPolicy simpleRetryPolicy=new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(3);
        return simpleRetryPolicy;
    }
}
