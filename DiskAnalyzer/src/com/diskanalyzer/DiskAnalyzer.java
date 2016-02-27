package com.diskanalyzer;

import com.diskanalyzer.helper.AnalyzerRunnable;
import com.diskanalyzer.helper.IndexerCallback;

public class DiskAnalyzer {
	
	public void analyzePath(String path,IndexerCallback onComplete){
		new Thread(new AnalyzerRunnable(path, onComplete)).start();
	}
}
