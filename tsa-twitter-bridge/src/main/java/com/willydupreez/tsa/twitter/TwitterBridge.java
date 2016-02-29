package com.willydupreez.tsa.twitter;

import java.util.stream.Collectors;

import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.twitter.TwitterComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TwitterBridge extends SpringRouteBuilder {

	private TwitterProperties twitter;
	private KafkaProperties kafka;
	private KeywordRepository keywords;

	@Autowired
	public TwitterBridge(
			TwitterProperties twitter,
			KafkaProperties kafka,
			KeywordRepository keywords) {
		Assert.notNull(twitter);
		Assert.notNull(kafka);
		Assert.notNull(keywords);

		this.twitter = twitter;
		this.kafka = kafka;
		this.keywords = keywords;
	}

	@Override
	public void configure() throws Exception {
		TwitterComponent tc = getContext().getComponent("twitter", TwitterComponent.class);
		tc.setAccessToken(this.twitter.getAccessToken());
		tc.setAccessTokenSecret(this.twitter.getAccessTokenSecret());
		tc.setConsumerKey(this.twitter.getConsumerKey());
		tc.setConsumerSecret(this.twitter.getConsumerSecret());

		String csKeywords = this.keywords.findAll()
				.stream()
				.collect(Collectors.joining(","));

		String twitterEndpoint = "twitter://streaming/filter?type=EVENT&keywords=" + csKeywords;
		String kafkaEndpoint = String.format("kafka:%s:%d?topic=%s&zookeeperHost=%s&zookeeperPort=%d&groupId=bridge",
				this.kafka.getHost(),
				this.kafka.getPort(),
				this.kafka.getTopic(),
				this.kafka.getZookeeperHost(),
				this.kafka.getZookeeperPort());

		from(twitterEndpoint)
			.marshal().json(JsonLibrary.Jackson)
			.wireTap("direct:tap")
			.setHeader(KafkaConstants.PARTITION_KEY).constant("0".getBytes())
			.to(kafkaEndpoint);

		from("direct:tap")
			.to("bean:twitterWireTap");
	}

}
