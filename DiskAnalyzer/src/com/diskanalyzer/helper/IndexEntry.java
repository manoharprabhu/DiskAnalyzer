package com.diskanalyzer.helper;


public class IndexEntry implements Comparable<IndexEntry> {
	
	private String filePath;
	
	private String fullFilePath;
	
	public String getFullFilePath() {
		return fullFilePath;
	}

	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}

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
	
	public IndexEntry(String filePath,long fileSize,String fullFilePath){
		this.filePath = filePath;
		this.fileSize  = fileSize;
		this.fullFilePath = fullFilePath;
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




