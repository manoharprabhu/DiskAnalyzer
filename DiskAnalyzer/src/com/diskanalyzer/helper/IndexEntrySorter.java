package com.diskanalyzer.helper;

import java.util.Comparator;

public class IndexEntrySorter  implements Comparator<IndexEntry>{
	private boolean sortFlag;
	
	public  IndexEntrySorter(boolean asc) {
		this.sortFlag = asc;
	}

	@Override
	public int compare(IndexEntry e1, IndexEntry e2) {
		if(sortFlag) {
			if(e1.getFileSize() > e2.getFileSize()){
				return 1;
			} else if(e1.getFileSize() > e2.getFileSize()){
				return -1;
			} else {
				return 0;
			}
		} else {
			if(e1.getFileSize() < e2.getFileSize()){
				return 1;
			} else if(e1.getFileSize() > e2.getFileSize()){
				return -1;
			} else {
				return 0;
			}
		}
	}
}
