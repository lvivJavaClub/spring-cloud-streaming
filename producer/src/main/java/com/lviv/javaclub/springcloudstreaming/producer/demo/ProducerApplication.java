package com.lviv.javaclub.springcloudstreaming.producer.demo;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import com.javaclub.Sensor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableSchemaRegistryClient
@RestController
public class ProducerApplication {

    BlockingQueue<Sensor> unbounded = new LinkedBlockingQueue<>();
    private Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    private Sensor randomSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(UUID.randomUUID().toString() + "-v1");
        sensor.setAcceleration(random.nextFloat() * 10);
        sensor.setVelocity(random.nextFloat() * 100);
        sensor.setTemperature(random.nextFloat() * 50);
        return sensor;
    }

    @PostMapping(value = "/message")
    public String sendMessage() {
        unbounded.offer(randomSensor());
        return "200 ok";
    }

    @GetMapping(value = "/")
    public String getSendMessage() {
        unbounded.offer(randomSensor());
        return "200 ok";
    }

    @Bean
    public Supplier<Sensor> supplier() {
        return () -> unbounded.poll();
    }

    @Bean
    public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.bindings.schemaRegistryClient.endpoint}") String endpoint){
        ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        client.setEndpoint(endpoint);
        return client;
    }

}
