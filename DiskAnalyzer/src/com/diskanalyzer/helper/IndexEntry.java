package com.diskanalyzer.helper;


public class IndexEntry implements Comparable<IndexEntry> {
	
	private String filePath;
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	private long fileSize;
	
	public IndexEntry(String filePath,long fileSize){
		this.filePath = filePath;
		this.fileSize  = fileSize;
	}

	@Override
	public int compareTo(IndexEntry arg0) {
		if(this.getFileSize() > arg0.getFileSize()){
			return 1;
		} else if(this.getFileSize() < arg0.getFileSize()){
			return -1;
		}  else {
			return 0;
		}
	}

}




