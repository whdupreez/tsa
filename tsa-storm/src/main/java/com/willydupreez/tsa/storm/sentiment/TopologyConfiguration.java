package com.willydupreez.tsa.storm.sentiment;

import java.util.Arrays;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import backtype.storm.Config;

public class TopologyConfiguration {

	private Configuration config;

	public TopologyConfiguration() {
		this.config = new CompositeConfiguration(Arrays.asList(
				new EnvironmentConfiguration(),
				new SystemConfiguration()));
	}

	public String getKafkaTopic() {
		return "twitter";
	}

	public String getZookeeperHosts() {
		return "192.168.8.105:2181";
	}

	public Config getStormConfig(boolean local) {
		Config conf = new Config();
		conf.setDebug(false);
		if (local) {
			conf.setMaxTaskParallelism(getWorkers());
		}
		else {
			conf.setNumWorkers(getWorkers());
		}
		return conf;
	}

	public int getWorkers() {
		return 3;
	}

	public String getNotificationEndpoint() {
		return "http://localhost:8080/twitter/sentiment";
	}

}
