package com.willydupreez.tsa.storm.sentiment;

import java.io.Serializable;

public class TweetData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIELD_NAME = "tweet_data";

	private String text;
	private String type;
	private Float score;

	public TweetData(String text, String type, Float score) {
		this.text = text;
		this.type = type;
		this.score = score;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
