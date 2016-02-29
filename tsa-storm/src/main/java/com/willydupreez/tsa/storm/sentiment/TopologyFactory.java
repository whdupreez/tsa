package com.willydupreez.tsa.storm.sentiment;

import org.apache.log4j.BasicConfigurator;

import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class TopologyFactory {

//	private static final Logger log = Logger.getLogger(TopologyFactory.class);

	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		TopologyFactory factory = new TopologyFactory();
		StormTopology topology = factory.createTopology();

		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], factory.config.getStormConfig(false), topology);
		} else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("sentiment-analysis", factory.config.getStormConfig(true), topology);
//			Thread.sleep(60000);
			System.in.read();
			cluster.shutdown();
		}
	}

	private TopologyConfiguration config;

	public TopologyFactory() {
		this.config = new TopologyConfiguration();
	}

	private StormTopology createTopology() {
		int workers = this.config.getWorkers();
		ZkHosts zkHosts = new ZkHosts(this.config.getZookeeperHosts());

		SpoutConfig kafkaConf = new SpoutConfig(
				zkHosts,
				this.config.getKafkaTopic(),
				"/kafka",
				"KafkaSpout");
		kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());

		TopologyBuilder topology = new TopologyBuilder();
		topology.setSpout("KafkaSpout", new KafkaSpout(kafkaConf), workers);

		topology.setBolt("TweetFilter", new TweetFilterBolt(), workers).shuffleGrouping("KafkaSpout");

		topology.setBolt("TextFilter", new TextFilterBolt(), workers).shuffleGrouping("TweetFilter");

		topology.setBolt("Stemming", new StemmingBolt(), workers).shuffleGrouping("TextFilter");

		topology.setBolt("PositiveSentiment", new PositiveSentimentBolt(), workers).shuffleGrouping("Stemming");
		topology.setBolt("NegativeSentiment", new NegativeSentimentBolt(), workers).shuffleGrouping("Stemming");

		topology.setBolt("JoinSentiment", new JoinSentimentsBolt(), workers)
				.fieldsGrouping("PositiveSentiment", new Fields("tweet_id"))
				.fieldsGrouping("NegativeSentiment", new Fields("tweet_id"));

		topology.setBolt("ScoreSentiment", new ScoringBolt(), workers).shuffleGrouping("JoinSentiment");

		topology.setBolt("Notifier", new NotifierBolt(), workers).shuffleGrouping("ScoreSentiment");
		return topology.createTopology();
	}

}