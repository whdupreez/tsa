package com.willydupreez.tsa.twitter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("twitterWireTap")
public class TwitterWireTap implements Processor {

	private List<TweetListener> listeners = new ArrayList<>();

	@Override
	public void process(Exchange exchange) throws Exception {
		for (TweetListener listener : this.listeners) {
			String tweet = exchange.getIn().getBody(String.class);
			listener.onTweet(tweet);
		}
	}

	public void addListener(TweetListener listener) {
		List<TweetListener> updated = new ArrayList<>(this.listeners);
		updated.add(listener);
		this.listeners = updated;
	}

	public void removeListener(TweetListener listener) {
		List<TweetListener> updated = new ArrayList<>(this.listeners);
		updated.remove(listener);
		this.listeners = updated;
	}

}
