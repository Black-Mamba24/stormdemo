package org.rpc.storm.spout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.rpc.constants.Constants;
import org.rpc.object.OneCall;
import org.rpc.util.ConvertUtil;

public class DataSpout extends BaseRichSpout {

	private static final long serialVersionUID = 2757091808155368326L;

	private static Logger LOG = LoggerFactory.getLogger(DataSpout.class);

	private SpoutOutputCollector collector;

	private BufferedReader reader;

	private List<Integer> calculateBoltTasks = null;

	private HashSet<String> alreadyRead;

	public void nextTuple() {
		readAndEmit();
	}

	public void open(Map arg0, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		calculateBoltTasks = context.getComponentTasks(Constants.CalculateBolt_ID);
		LOG.info("calculateBoltTasks : "+calculateBoltTasks.get(0)+"  "+calculateBoltTasks.get(1));
		if (calculateBoltTasks.size() != 2) {
			LOG.error("calculateBoltTasks size wrong");
			return;
		}
		alreadyRead = new HashSet<String>();
		try {
			getReader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(true, new Fields(Constants.SPOUT_FIELD));
	}

	public void readAndEmit() {
		String str;
		OneCall call;
		try {
			if (reader != null && (str = reader.readLine()) != null) {
				try {
					call = ConvertUtil.convertToCall(str);
					if (call.getTrace_id() < 2500) {
						collector.emitDirect(calculateBoltTasks.get(0), new Values(call));
					} else {
						collector.emitDirect(calculateBoltTasks.get(1), new Values(call));
					}
						
				} catch (InterruptedException e) {/* ignore */
				} catch (Exception e) {
					LOG.error("The number of arguments wrong");
				}
			} else {
				reader = null;
				getReader();
				if (reader != null) {
					readAndEmit();
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				LOG.error("reader is null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getReader() throws IOException {
		FileSystem fileSystem;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dir = sdf.format(date);
		String fileURI = Constants.DATA_ADDRESS + dir + "/";
		Configuration configuration = new Configuration();
		configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		fileSystem = FileSystem.get(URI.create(fileURI), configuration);
		FileStatus files[] = fileSystem.listStatus(new Path(fileURI));
		for(FileStatus file : files) {
			if(alreadyRead.contains(file.getPath().toString()))
				continue;
			else {
				alreadyRead.add(file.getPath().toString());
				LOG.info("The path of this file : "+file.getPath().toString());
				InputStream inputStream = fileSystem.open(file.getPath());
				reader = new BufferedReader(
						new InputStreamReader(inputStream, Constants.UTF_8));
				return;
			}
		}
	}

}
