package com.willydupreez.tsa.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.willydupreez.tsa.sentiment.Sentiment;
import com.willydupreez.tsa.sentiment.SentimentListener;
import com.willydupreez.tsa.sentiment.SentimentService;
import com.willydupreez.tsa.twitter.KeywordRepository;
import com.willydupreez.tsa.twitter.TweetListener;
import com.willydupreez.tsa.twitter.TwitterWireTap;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

	private static final Logger log = LoggerFactory.getLogger(TwitterController.class);

	private TwitterWireTap wireTap;
	private KeywordRepository keywords;
	private SentimentService sentiment;
	private ObjectMapper mapper;

	@Autowired
	public TwitterController(
			TwitterWireTap wireTap,
			KeywordRepository keywords,
			SentimentService sentiment,
			ObjectMapper mapper) {
		Assert.notNull(wireTap);
		Assert.notNull(keywords);
		Assert.notNull(sentiment);
		Assert.notNull(mapper);

		this.wireTap = wireTap;
		this.keywords = keywords;
		this.sentiment = sentiment;
		this.mapper = mapper;
	}

	@RequestMapping(path = "/sse", produces = "text/event-stream")
	public ResponseBodyEmitter sse() {
		SseEmitter emitter = new SseEmitter();
		TweetListener listener = t -> {
			try {
				emitter.send(SseEmitter.event().data(t));
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		};
		this.wireTap.addListener(listener);
		emitter.onCompletion(() -> this.wireTap.removeListener(listener));
		return emitter;
	}

	@RequestMapping(path = "/keywords", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> keywords() {
		return this.keywords.findAll();
	}

	@RequestMapping(path = "/sentiment", method = RequestMethod.POST)
	public void updateSentiment(HttpServletRequest request) {
		try {
			String data = IOUtils.toString(request.getInputStream());
			Sentiment update = this.mapper.readValue(data, Sentiment.class);
			this.sentiment.updateSentiment(update);
		} catch (IOException e) {
			log.error("Failed to deserialize sentiment update", e);
		}
	}

	@RequestMapping(path = "/sentiment/sse", produces = "text/event-stream")
	public ResponseBodyEmitter sentiment() {
		SseEmitter emitter = new SseEmitter();
		SentimentListener listener = view -> {
			try {
				emitter.send(SseEmitter.event().data(this.mapper.writeValueAsString(view)));
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		};
		this.sentiment.addSentimentListener(listener);
		emitter.onCompletion(() -> this.sentiment.removeSentimentListener(listener));
		return emitter;
	}

}
