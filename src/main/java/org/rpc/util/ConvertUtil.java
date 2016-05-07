package org.rpc.util;

import org.rpc.object.OneCall;

public class ConvertUtil {
	public static OneCall convertToCall(String string) throws Exception{
		OneCall call;
		if (string == null)
			return null;
		else {
			String[] args = string.split(",");
			if(args.length != 7) throw new Exception("The number of arguments wrong");
			else {
				call = new OneCall();
				call.setTrace_id(Integer.parseInt(args[0]));
				call.setCall_id(Integer.parseInt(args[1]));
				call.setTree_id(args[2]);
				call.setApp_name(args[3]);
				call.setService_name(args[4]);
				call.setCur_time(Long.parseLong(args[5]));
				call.setK_type(args[6]);
				return call;
			}
		}
	}
}
