package com.willydupreez.tsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.willydupreez.tsa.twitter.KafkaProperties;
import com.willydupreez.tsa.twitter.TwitterProperties;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev");
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "tsa.twitter")
	public TwitterProperties twitterProperties() {
		return new TwitterProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "tsa.kafka")
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

}
