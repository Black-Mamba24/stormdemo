package org.rpc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.rpc.constants.Constants;

public class MysqlTest {
	
	private static Connection conn = null;
	
	private static PreparedStatement statement = null;
	
	private static String sql = "INSERT INTO RPC_DATA (trace_id,handle_time,transport_time,total_time,create_time) VALUES (?,?,?,?,?)";

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.DB_USER, Constants.DB_PAWD);
		statement = conn.prepareStatement(sql);
		statement.setInt(1, 0);
		statement.setInt(2, 0);
		statement.setInt(3, 0);
		statement.setInt(4, 0);
		statement.setLong(5, System.currentTimeMillis());
		int result = statement.executeUpdate();
		System.out.println(result);
	}

}
