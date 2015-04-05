package com.diskanalyzer;

import com.diskanalyzer.helper.AnalyzerResult;
import com.diskanalyzer.helper.AnalyzerRunnable;
import com.diskanalyzer.helper.IndexerCallback;

public class DiskAnalyzer {
	
	private static final long STORAGE_UNIT = 1000;
	
	public void analyzePath(String path,IndexerCallback onComplete){
		new Thread(new AnalyzerRunnable(path, onComplete)).start();
	}
	
	public static long bytesToKilobytes(long bytes){
		return bytes/STORAGE_UNIT;
	}
	
	public static long bytesToMegabytes(long bytes){
		return bytesToKilobytes(bytes)/STORAGE_UNIT;
	}
	
	public static long bytesToGigabytes(long bytes){
		return bytesToMegabytes(bytes)/STORAGE_UNIT;
	}
	
	
}
