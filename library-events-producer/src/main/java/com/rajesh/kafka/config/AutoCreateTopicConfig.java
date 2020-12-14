package com.rajesh.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateTopicConfig {

    @Bean
    public NewTopic libraryEvents(){
        return TopicBuilder
                .name("library-events")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
