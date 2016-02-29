package com.willydupreez.tsa.sentiment;

public class Sentiment {

	private Long id;
	private String text;
	private Float pos;
	private Float neg;
	private Float net;
	private Long weight;
	private String score;

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getPos() {
		return this.pos;
	}

	public void setPos(Float pos) {
		this.pos = pos;
	}

	public Float getNeg() {
		return this.neg;
	}

	public void setNeg(Float neg) {
		this.neg = neg;
	}

	public Float getNet() {
		return this.net;
	}

	public void setNet(Float net) {
		this.net = net;
	}

	public Long getWeight() {
		return this.weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
