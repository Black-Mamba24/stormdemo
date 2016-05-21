package org.rpc.constants;

public class Constants {
	public static String DATA_ADDRESS = "hdfs://10.0.0.33:10000/flume/data/";
	public static String FILE_PREFIX = "data.";
	public static String FILE_SUFFIX = ".log";
	public static String DB_IP = "172.18.113.32";
	public static String DB_PORT = "3306";
	public static String DB_NAME="storm";
	public static String JDBC_URL = "jdbc:mysql://"+DB_IP+":"+DB_PORT+"/"+DB_NAME;
	public static String DB_USER = "root";
	public static String DB_PAWD = "123456";
	
	
	public static String UTF_8 = "utf-8";
	public static String END = "end";
	public static String SPOUT_FIELD = "spout_field";
	public static String CALCULATE_FIELD = "calculate_field";

	public static String Topology_ID = "Topology_ID";
	public static String Spout_ID = "DataSpout_ID";
	public static String CalculateBolt_ID = "CalculateBolt_ID";
	public static String StoreBolt_ID = "StoreBolt_ID";
	public static int CURRENT_LEN = 4000;
	public static int TWO_HOUR = 2*60*60*1000;
	public static int HALF_HOUR = 30*60*1000;
	public static int TEN_MIN = 10*60*1000;
	public static int FIVE_MIN = 5*60*1000;
	public static int ONE_HALF_MIN = 90*1000;
	public static int ONE_MIN = 60*1000;
	// tree_id
	public static String TREE_ID_1 = "1";
	public static String TREE_ID_2 = "1.1";
	public static String TREE_ID_3 = "1.1.1";
}
