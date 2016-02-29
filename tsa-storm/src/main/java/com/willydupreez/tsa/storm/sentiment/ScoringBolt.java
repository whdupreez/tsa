package com.willydupreez.tsa.storm.sentiment;

import org.apache.log4j.Logger;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ScoringBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ScoringBolt.class);

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text", "pos_score", "neg_score", "net_score", "score", "tweet_weight"));
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		log.debug("Scoring tweet");
		Long id = tuple.getLong(tuple.fieldIndex("tweet_id"));
		String text = tuple.getString(tuple.fieldIndex("tweet_text"));
		Float pos = tuple.getFloat(tuple.fieldIndex("pos_score"));
		Float neg = tuple.getFloat(tuple.fieldIndex("neg_score"));
		Long weight = tuple.getLong(tuple.fieldIndex("tweet_weight"));
		Float net = pos - neg;

		String score = pos > neg ? "positive" : "negative";
		log.debug(String.format("tweet %s: %s", id, score));
		collector.emit(new Values(id, text, pos, neg, net, score, weight));
	}

}