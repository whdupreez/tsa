package com.willydupreez.tsa.sentiment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SentimentService {

	private List<SentimentListener> listeners = new ArrayList<>();

	public void updateSentiment(Sentiment sentiment) {
		List<SentimentListener> notified = this.listeners;
		SentimentView view = new SentimentView();
		view.setText(sentiment.getText());
		view.setNeg(sentiment.getNeg());
		view.setNet(sentiment.getNet());
		view.setPos(sentiment.getPos());
		for (SentimentListener listener : notified) {
			listener.notifySentimentUpdated(view);
		}
	}

	public synchronized void removeSentimentListener(SentimentListener listener) {
		List<SentimentListener> updated = new ArrayList<>(this.listeners);
		updated.remove(listener);
		this.listeners = updated;
	}

	public synchronized void addSentimentListener(SentimentListener listener) {
		List<SentimentListener> updated = new ArrayList<>(this.listeners);
		updated.add(listener);
		this.listeners = updated;
	}

}
