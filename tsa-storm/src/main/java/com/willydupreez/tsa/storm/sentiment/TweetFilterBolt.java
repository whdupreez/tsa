package com.willydupreez.tsa.storm.sentiment;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TweetFilterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(TweetFilterBolt.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text", "tweet_weight"));
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		log.debug("filttering incoming tweets");
		String json = input.getString(0);
		try {
			JsonNode root = mapper.readValue(json, JsonNode.class);
			if ("en".equals(root.path("lang").asText().toLowerCase())) {
				if (root.get("id") != null
					&& root.get("text") != null) {

					long id = root.get("id").longValue();
					String text = root.get("text").textValue();
					int followers = root.path("user").path("followersCount").asInt();
					boolean favorited = root.path("favorited").asBoolean();

					long weight = followers > 0 ? followers * (favorited ? 2 : 1) : 1;

					collector.emit(new Values(id, text, weight));
				}
				else {
					log.debug("tweet id and/ or text was null");
				}
			}
			else {
				log.debug("Ignoring non-english tweet");
			}
		}
		catch (IOException ex) {
			log.error("IO error while filtering tweets", ex);
			log.trace(null, ex);
		}
	}

	public Map<String, Object> getComponenetConfiguration() {
		return null;
	}

}
