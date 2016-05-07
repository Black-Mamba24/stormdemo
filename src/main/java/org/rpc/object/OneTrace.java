package org.rpc.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OneTrace implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6998372353586455172L;
	public int trace_id;
	public boolean isConpleted;
	public int app_num;
	// public Map<String, String> apps = new HashMap<String, String>();
	// public Map<String, String> services = new HashMap<String, String>();
	public Set<String> apps = new HashSet<String>();
	public Set<String> services = new HashSet<String>();
	public int handle_time;
	public List<OneCall> callList = new ArrayList<OneCall>();
	public int transport_time;
	public int total_time;
	public long baseTime;

	public long getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(long baseTime) {
		this.baseTime = baseTime;
	}
	
	public int getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(int handle_time) {
		this.handle_time = handle_time;
	}

	public Set<String> getServices() {
		return services;
	}

	public void addServices(String k) {
		this.services.add(k);
	}

	public int getTrace_id() {
		return trace_id;
	}

	public List<OneCall> getCallList() {
		return callList;
	}

	public void setCallList(List<OneCall> callList) {
		this.callList = callList;
	}

	public void setTrace_id(int trace_id) {
		this.trace_id = trace_id;
	}

	public boolean isConpleted() {
		return isConpleted;
	}

	public void setConpleted(boolean isConpleted) {
		this.isConpleted = isConpleted;
	}

	public int getApp_num() {
		return app_num;
	}

	public void setApp_num(int app_num) {
		this.app_num = app_num;
	}

	public Set<String> getApps() {
		return apps;
	}

	public void addApps(String k) {
		apps.add(k);
		setApp_num(apps.size());
	}

	public int gettransport_time() {
		return transport_time;
	}

	public void settransport_time(int transport_time) {
		this.transport_time = transport_time;
	}

	public int getTotal_time() {
		return total_time;
	}

	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}

	@Override
	public String toString() {
		return "OneTrace [trace_id=" + trace_id + ", isConpleted=" + isConpleted + ", app_num=" + app_num + ", apps="
				+ apps + ", services=" + services + ", callList=" + callList
				+ ", transport_time=" + transport_time + ", handle_time=" + handle_time + ", total_time=" + total_time + "]";
	}

}
