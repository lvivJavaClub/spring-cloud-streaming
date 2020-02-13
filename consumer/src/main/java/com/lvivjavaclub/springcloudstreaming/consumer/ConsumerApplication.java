package com.lvivjavaclub.springcloudstreaming.consumer;

import java.util.function.Consumer;
import com.javaclub.Sensor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	public Consumer<Sensor> process() {
		return System.out::println;
	}

	@Bean
	public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.bindings.schemaRegistryClient.endpoint}") String endpoint){
		ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
		client.setEndpoint(endpoint);
		return client;
	}
}
