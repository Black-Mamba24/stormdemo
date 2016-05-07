package org.rpc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.rpc.constants.Constants;

public class HdfsGet {

	public static void main(String[] args) throws IOException {
		OutputStream outputStream = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dir = sdf.format(date);
		String fileURI = Constants.DATA_ADDRESS + dir;
		System.out.println(fileURI);
		//System.out.println(new Date().getTime());
		Configuration configuration = new Configuration();
		FileSystem fileSystem = FileSystem.get(URI.create(fileURI), configuration);
		//long startTime = new Date().getTime();
		Path fileName = new Path(Constants.FILE_PREFIX + "1461920876688");
		System.out.println(fileName.toString());
		if (!fileSystem.exists(fileName)) {
			//continue;
			System.out.println("not exist");
		} else {
			InputStream inputStream = null;
			//try {
				inputStream = fileSystem.open(new Path(fileURI + fileName));
				// file name
				outputStream = new BufferedOutputStream(new FileOutputStream(new File("~/log.txt")));
				IOUtils.copy(inputStream, outputStream);
				//reader = new BufferedReader(new InputStreamReader(
						//new FileInputStream(new File("/home/hadoop/log.txt")), Constants.UTF_8));
//			} finally {
//				IOUtils.closeStream(inputStream);
//				IOUtils.closeStream(outputStream);
//			}
		}
	}

}
