package com.willydupreez.tsa.storm.sentiment;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class WordsRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> stopWords;
	private List<String> negativeWords;
	private List<String> positiveWords;

	public WordsRepository() {
		this.stopWords = Collections.unmodifiableList(loadWords("/nlp/stop-words.txt"));
		this.negativeWords = Collections.unmodifiableList(loadWords("/nlp/negative-words.txt"));
		this.positiveWords = Collections.unmodifiableList(loadWords("/nlp/positive-words.txt"));
	}

	public List<String> findStopWords() {
		return this.stopWords;
	}

	public List<String> findNegativeWords() {
		return this.negativeWords;
	}

	public List<String> findPositiveWords() {
		return this.positiveWords;
	}

	private List<String> loadWords(String resource) {
		try (InputStream in = this.getClass().getResourceAsStream(resource)) {
			return IOUtils.readLines(in);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load words from resource: " + resource, e);
		}
	}

}
