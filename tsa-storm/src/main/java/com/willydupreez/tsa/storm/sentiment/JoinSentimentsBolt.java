package com.willydupreez.tsa.storm.sentiment;

import java.util.HashMap;

import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class JoinSentimentsBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(JoinSentimentsBolt.class);

	private HashMap<Long, TweetData> tweets;

	public JoinSentimentsBolt() {
		this.tweets = new HashMap<Long, TweetData>();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "pos_score", "neg_score", "tweet_text", "tweet_weight"));
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Long id = input.getLong(input.fieldIndex("tweet_id"));
		String text = input.getString(input.fieldIndex("tweet_text"));
		Long weight = input.getLong(input.fieldIndex("tweet_weight"));

		if (input.contains("pos_score")) {
			Float pos = input.getFloat(input.fieldIndex("pos_score"));
			if (this.tweets.containsKey(id)) {
				TweetData data = this.tweets.get(id);
				if ("neg".equals(data.getType())) {
					emit(collector, id, data.getText(), pos, data.getScore(), weight);
				}
				else {
					log.warn("one sided join attempted");
					this.tweets.remove(id);
				}
			}
			else {
				this.tweets.put(id, new TweetData(text, "pos", pos));
			}
		}
		else if (input.contains("neg_score")) {
			Float neg = input.getFloat(input.fieldIndex("neg_score"));
			if (this.tweets.containsKey(id)) {
				TweetData data = this.tweets.get(id);
				if ("pos".equals(data.getType())) {
					emit(collector, id, data.getText(), neg, data.getScore(), weight);
				}
				else {
					log.warn("one sided join attempted");
					this.tweets.remove(id);
				}
			}
			else {
				this.tweets.put(id, new TweetData(text, "neg", neg));
			}
		}
	}

	private void emit(BasicOutputCollector collector, Long id, String text, float pos, float neg, long weight) {
		collector.emit(new Values(id, pos, neg, text, weight));
		this.tweets.remove(id);
	}

}