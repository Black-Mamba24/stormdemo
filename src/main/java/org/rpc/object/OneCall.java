package org.rpc.object;

import java.io.Serializable;

public class OneCall implements Serializable,Comparable<OneCall>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4236558980045039146L;
	public int trace_id;//
	public int call_id;//
	public String tree_id;//
	public String app_name;//
	public String service_name;//
	public long cur_time;
	public String k_type;//
	
	public OneCall() {
	}
	public OneCall(int call_id) {
		this.call_id = call_id;
	}
	
	@Override
	public String toString() {
		return trace_id + "," + call_id + "," + tree_id + "," + app_name + "," + service_name + "," + cur_time + "," + k_type;
	}
	public int getCall_id() {
		return call_id;
	}
	public void setCall_id(int call_id) {
		this.call_id = call_id;
	}
	public int getTrace_id() {
		return trace_id;
	}
	public void setTrace_id(int trace_id) {
		this.trace_id = trace_id;
	}
	public String getTree_id() {
		return tree_id;
	}
	public void setTree_id(String tree_id) {
		this.tree_id = tree_id;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public long getCur_time() {
		return cur_time;
	}
	public void setCur_time(long cur_time) {
		this.cur_time = cur_time;
	}
	public String getK_type() {
		return k_type;
	}
	public void setK_type(String k_type) {
		this.k_type = k_type;
	}
	public int compareTo(OneCall o) {
		if(this.getCall_id() > o.getCall_id()) return 1;
		else if(this.getCall_id() < o.getCall_id()) return -1;
		return 0;
	}
	
}
