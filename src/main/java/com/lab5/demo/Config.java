package com.lab5.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class Config {

    @Resource
    private Environment environment;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String pass;

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public DriverManagerDataSource dataSource(RedisConnectionFactory connectionFactory) {
        final DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
        source.setUsername(redisTemplate(connectionFactory).opsForValue().get(this.username));
        source.setPassword(redisTemplate(connectionFactory).opsForValue().get(this.pass));
        source.setUrl(this.url);
        log.info(redisTemplate(connectionFactory).opsForValue().get(this.username));
        return source;
    }
}
