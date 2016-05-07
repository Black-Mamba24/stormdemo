package org.rpc;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import org.rpc.constants.Constants;
import org.rpc.storm.bolt.CalculateBolt;
import org.rpc.storm.bolt.StoreBolt;
import org.rpc.storm.spout.DataSpout;

public class Topology {

	public static void main(String[] args) {
		Config conf = new Config();
		conf.setNumWorkers(2);
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(Constants.Spout_ID, new DataSpout(), 1);
		builder.setBolt(Constants.CalculateBolt_ID, new CalculateBolt(), 2).directGrouping(Constants.Spout_ID);
		builder.setBolt(Constants.StoreBolt_ID, new StoreBolt(), 2).shuffleGrouping(Constants.CalculateBolt_ID);
		try {
			StormSubmitter.submitTopology(Constants.Topology_ID, conf, builder.createTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		}
	}
}
