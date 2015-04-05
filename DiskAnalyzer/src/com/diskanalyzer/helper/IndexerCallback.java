package com.diskanalyzer.helper;


public interface IndexerCallback {
	public void onIndexComplete(AnalyzerResult result);
	public void updateIndexProgress(long progress);
}
