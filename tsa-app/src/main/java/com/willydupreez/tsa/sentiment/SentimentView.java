package com.willydupreez.tsa.sentiment;

public class SentimentView {

	private String text;
	private float pos;
	private float neg;
	private float net;

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getPos() {
		return this.pos;
	}

	public void setPos(float pos) {
		this.pos = pos;
	}

	public float getNeg() {
		return this.neg;
	}

	public void setNeg(float neg) {
		this.neg = neg;
	}

	public float getNet() {
		return this.net;
	}

	public void setNet(float net) {
		this.net = net;
	}

}
