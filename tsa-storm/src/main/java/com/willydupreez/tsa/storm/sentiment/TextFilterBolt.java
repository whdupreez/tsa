package com.willydupreez.tsa.storm.sentiment;

import org.apache.log4j.Logger;
import java.util.Map;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TextFilterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TextFilterBolt.class);

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text", "tweet_weight"));
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		log.debug("Sanatizing text");
		Long id = input.getLong(input.fieldIndex("tweet_id"));
		String text = input.getString(input.fieldIndex("tweet_text"));
		long weight = input.getLong(input.fieldIndex("tweet_weight"));

		text = text.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
		collector.emit(new Values(id, text, weight));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}