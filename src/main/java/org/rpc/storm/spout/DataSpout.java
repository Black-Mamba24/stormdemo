package org.rpc.storm.spout;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.rpc.constants.Constants;
import org.rpc.object.OneCall;
import org.rpc.util.ConvertUtil;

public class DataSpout extends BaseRichSpout {

	private static final long serialVersionUID = 2757091808155368326L;

	private static Logger LOG = LoggerFactory.getLogger(DataSpout.class);

	private SpoutOutputCollector collector;

	private OutputStream outputStream;

	private BufferedReader reader;

	private List<Integer> calculateBoltTasks = null;

	private long startTime = 0;

	public void nextTuple() {
		readAndEmit();
	}

	public void open(Map arg0, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		calculateBoltTasks = context.getComponentTasks(Constants.CalculateBolt_ID);
		if (calculateBoltTasks.size() != 2) {
			LOG.error("calculateBoltTasks size wrong");
			return;
		}
		startTime = new Date().getTime() - Constants.ONE_HALF_MIN;
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
					if (call.getTrace_id() < 5000)
						collector.emitDirect(calculateBoltTasks.get(0), new Values(call));
					else
						collector.emitDirect(calculateBoltTasks.get(1), new Values(call));
					LOG.info("spout emit done");
					System.out.println(call);
				} catch (InterruptedException e) {/* ignore */
				} catch (Exception e) {
					LOG.error("The number of arguments wrong");
				}
			} else {
				getReader();
				if (reader != null) {
					readAndEmit();
				}
				LOG.error("reader is null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getReader() throws IOException {
		FileSystem fileSystem;
		while (true) {
			startTime++;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String dir = sdf.format(date);
			String fileURI = Constants.DATA_ADDRESS + dir + "/";
			Configuration configuration = new Configuration();
			configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			fileSystem = FileSystem.get(URI.create(fileURI), configuration);
			//Path filePath = new Path(fileURI+Constants.FILE_PREFIX + startTime);
			Path filePath = new Path(fileURI+"data.1462615838014");
			if (!fileSystem.exists(filePath)) {
				LOG.error(filePath.getName()+" not exist");
				continue;
			} else {
				LOG.info(filePath.getName()+"  exist");
				InputStream inputStream = null;
				inputStream = fileSystem.open(filePath);
				outputStream = new BufferedOutputStream(new FileOutputStream(new File("/home/ecust/log.txt")));
				IOUtils.copy(inputStream, outputStream);
				reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File("/home/ecust/log.txt")), Constants.UTF_8));
				return;
			}
		}
	}

}
