package org.rpc.storm.bolt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.rpc.constants.Constants;
import org.rpc.object.OneTrace;

public class StoreBolt implements IBasicBolt{

	private static final long serialVersionUID = -4857164080831287407L;
	
	private static Logger LOG = LoggerFactory.getLogger(StoreBolt.class);
	
	private Connection conn = null;
	
	private PreparedStatement statement = null;
	
	private String sql = "INSERT INTO RPC_DATA (trace_id,handle_time,transport_time,total_time,create_time) VALUES (?,?,?,?,?)";

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		OneTrace oneTrace = (OneTrace)tuple.getValueByField(Constants.CALCULATE_FIELD);
		if(oneTrace != null){
			try {
				statement = conn.prepareStatement(sql);
				statement.setInt(1, oneTrace.getTrace_id());
				statement.setInt(2, oneTrace.getHandle_time());
				statement.setInt(3, oneTrace.gettransport_time());
				statement.setInt(4, oneTrace.getTotal_time());
				statement.setLong(5, System.currentTimeMillis());
				int result = statement.executeUpdate();
				if(result == 0) {
					LOG.error("didn't insert into DB");
				} else {
					LOG.info("insert success");
				}
			} catch (SQLTimeoutException e) {
				LOG.error("Sql execute timeout");
				e.printStackTrace();
			} catch (SQLException e) {
				LOG.error("Sql execute error");
				e.printStackTrace();
			} finally {
				try{
					if(statement != null) statement.close();
				} catch (Exception e){
				}
			}
		} else {
			LOG.error("store bolt ddin't get data from calculate bolt");
		}
	}

	public void prepare(Map arg0, TopologyContext context) {
		try {
			getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			LOG.error("get connetction error");
			e.printStackTrace();
		}
	}
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.DB_USER, Constants.DB_PAWD);
	}
	
	public void cleanup() {}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {}
	
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
