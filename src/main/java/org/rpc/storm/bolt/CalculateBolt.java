package org.rpc.storm.bolt;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.rpc.constants.Constants;
import org.rpc.object.OneCall;
import org.rpc.object.OneTrace;

public class CalculateBolt implements IBasicBolt{

	private static final long serialVersionUID = -3929838799766274573L;
	
	private static Logger LOG = LoggerFactory.getLogger(CalculateBolt.class);
	
	private LinkedHashMap<Integer, OneTrace> current;
	
	private LinkedHashMap<Integer, WeakReference<OneTrace>> backup;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.CALCULATE_FIELD));
	}

	public void prepare(Map arg0, TopologyContext context) {
		backup = new LinkedHashMap<Integer, WeakReference<OneTrace>>();
		current = new LinkedHashMap<Integer, OneTrace>(Constants.CURRENT_LEN){
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<Integer, OneTrace> eldest) {
				if (size() > Constants.CURRENT_LEN * 5 / 6) {
					backup.put(eldest.getKey(), new WeakReference<OneTrace>(eldest.getValue()));
					return true;
				}
				return false;
			}
		};
	}

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		OneCall call = (OneCall)tuple.getValueByField(Constants.SPOUT_FIELD);
		OneTrace oneTrace;
		if(call != null) {
			int trace_id = call.getTrace_id();
			if(current.containsKey(trace_id)){
				oneTrace = current.get(trace_id);
				oneTrace.getCallList().add(call);
				oneTrace.addApps(call.getApp_name());
				oneTrace.addServices(call.getService_name());
				if(completed(oneTrace, oneTrace.getCallList().size(), oneTrace.getApp_num())){
					oneTrace.setConpleted(true);
					computeTime(oneTrace);
					collector.emit(new Values(oneTrace));
					current.remove(trace_id);
				}
			} else if(backup.containsKey(trace_id)){
				WeakReference<OneTrace> weakOneTrace = backup.get(trace_id);
				if(weakOneTrace != null){
					oneTrace = weakOneTrace.get();
					oneTrace.getCallList().add(call);
					oneTrace.addApps(call.getApp_name());
					oneTrace.addServices(call.getService_name());
					if(completed(oneTrace, oneTrace.getCallList().size(), oneTrace.getApp_num())){
						oneTrace.setConpleted(true);
						computeTime(oneTrace);
						collector.emit(new Values(oneTrace));
						backup.remove(trace_id);
					}
				}
			} else {
				oneTrace = new OneTrace();
				oneTrace.setTrace_id(call.getTrace_id());
				ArrayList<OneCall> callList = new ArrayList<OneCall>();
				callList.add(call);
				oneTrace.setCallList(callList);
				oneTrace.setConpleted(false);
				oneTrace.addApps(call.getApp_name());
				oneTrace.addServices(call.getService_name());
				current.put(trace_id, oneTrace);
			}
		} //end of if != null
	}
	
	private void computeTime(OneTrace trace){
		List<OneCall> callList = trace.getCallList();
		int handle_time = 0;
		int transport_time = 0;
		for(int i=0, j=1; j < callList.size(); i++, j++){
			if(i % 2 ==0) transport_time += callList.get(j).getCur_time() - callList.get(i).getCur_time();
			else handle_time += callList.get(j).getCur_time() - callList.get(i).getCur_time();
		}
		trace.setHandle_time(handle_time);
		trace.settransport_time(transport_time);
		trace.setTotal_time(handle_time+transport_time);
	}
	
	public boolean completed(OneTrace oneTrace, int listLen, int setLen){
		if(setLen <= 1) {
			return false;
		}
		int total = (int)Math.pow(2, setLen);
		if(total != listLen) {
			return false;
		} else {
			List<OneCall> callList = oneTrace.getCallList();
			Collections.sort(callList);
			for(int i=0 ; i<setLen ; i++){
				if(i != callList.get(i).getCall_id()) return false;
			}
			if(callList.get(setLen - 1).getTree_id().equals("1.1.1")) return false;
			return true;
		}
	}
	
	public void cleanup() {}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
