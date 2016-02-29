package com.willydupreez.tsa.storm.sentiment;

import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class NegativeSentimentBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(PositiveSentimentBolt.class);

	private WordsRepository words = new WordsRepository();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "neg_score", "tweet_text", "tweet_weight"));
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		log.debug("Calculating negitive score");
		Long id = input.getLong(input.fieldIndex("tweet_id"));
		String text = input.getString(input.fieldIndex("tweet_text"));
		Long weight = input.getLong(input.fieldIndex("tweet_weight"));
		List<String> negWords = this.words.findNegativeWords();
		String[] words = text.split(" ");
		int numWords = words.length;
		int numNegWords = 0;
		for (String word : words) {
			if (negWords.contains(word)) {
				numNegWords++;
			}
		}
		collector.emit(new Values(id, (float) numNegWords / numWords, text, weight));
	}

}