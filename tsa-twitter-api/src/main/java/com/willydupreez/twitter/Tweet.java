package com.willydupreez.twitter;

import java.util.List;

public class Tweet {

	private String createdAt;
	private long id;
	private String text;
	private long userId;
	private long followersCount;
	private long friendsCount;
	private long retweetCount;
	private List<String> hashtags;

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFollowersCount() {
		return this.followersCount;
	}

	public void setFollowersCount(long followersCount) {
		this.followersCount = followersCount;
	}

	public long getFriendsCount() {
		return this.friendsCount;
	}

	public void setFriendsCount(long friendsCount) {
		this.friendsCount = friendsCount;
	}

	public long getRetweetCount() {
		return this.retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public List<String> getHashtags() {
		return this.hashtags;
	}

	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}

}
