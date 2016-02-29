package com.willydupreez.tsa.storm.sentiment;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class NotifierBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(NotifierBolt.class);
	private String endpoint = new TopologyConfiguration().getNotificationEndpoint();

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		System.out.println(input.getString(input.fieldIndex("tweet_text")));
		Long id = input.getLong(input.fieldIndex("tweet_id"));
		String tweet = input.getString(input.fieldIndex("tweet_text"));
		Float pos = input.getFloat(input.fieldIndex("pos_score"));
		Float neg = input.getFloat(input.fieldIndex("neg_score"));
		Float net = input.getFloat(input.fieldIndex("net_score"));
		String score = input.getString(input.fieldIndex("score"));
		Long weight = input.getLong(input.fieldIndex("tweet_weight"));

		String content = String.format(
				"{\"id\": \"%d\", "
						+ "\"text\": \"%s\", "
						+ "\"pos\": \"%f\", "
						+ "\"neg\": \"%f\", "
						+ "\"net\": \"%f\", "
						+ "\"weight\": \"%d\", "
						+ "\"score\": \"%s\" }",
				id, StringEscapeUtils.escapeJava(tweet), pos, neg, net, weight, score);

		postContent(content);
	}

	private void postContent(String content) {
		try {
			URL url = new URL(this.endpoint);

			HttpURLConnection urlConn = null;
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-Type", "application/json");
			urlConn.connect();

			DataOutputStream output = new DataOutputStream(urlConn.getOutputStream());
			output.writeBytes(content);
			output.flush();
			output.close();

			InputStream input = urlConn.getInputStream();
			while (input.read() != -1) {
			}
			input.close();
		} catch (Exception e) {
			log.error("Failed to post data", e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}