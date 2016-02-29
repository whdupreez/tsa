package com.willydupreez.tsa.storm.sentiment;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class StemmingBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(StemmingBolt.class);

	private WordsRepository words = new WordsRepository();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text", "tweet_weight"));
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		log.debug("Removing stop words");
		Long id = input.getLong(input.fieldIndex("tweet_id"));
		String text = input.getString(input.fieldIndex("tweet_text"));
		Long weight = input.getLong(input.fieldIndex("tweet_weight"));
		for (String word : this.words.findStopWords()) {
			text = text.replaceAll("\\b" + word + "\\b", "");
		}
		collector.emit(new Values(id, text, weight));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}