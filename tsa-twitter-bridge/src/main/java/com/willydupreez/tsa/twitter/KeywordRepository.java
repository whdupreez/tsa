package com.willydupreez.tsa.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;

@Repository
public class KeywordRepository {

	public List<String> findAll() {
		try (InputStream in = this.getClass().getResourceAsStream("/keywords.txt")) {
			return IOUtils.readLines(in);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load keywords from resource.", e);
		}
	}

}
