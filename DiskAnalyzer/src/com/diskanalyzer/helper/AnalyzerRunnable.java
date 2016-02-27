package com.diskanalyzer.helper;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AnalyzerRunnable implements Runnable{

	private List<IndexEntry> fileList;
	private Queue<Path> bfsQueue;
	private String path;
	private IndexerCallback onComplete;
	private AnalyzerResult analyzerResult;
	private int progressUpdateInterval;
	public AnalyzerRunnable(String path,IndexerCallback onComplete) {
		// TODO Auto-generated constructor stub
		fileList = new ArrayList<IndexEntry>();
		bfsQueue = new LinkedList<Path>();
		this.path = path;
		this.onComplete = onComplete;
		analyzerResult = new AnalyzerResult();
		progressUpdateInterval = 1000;
	}

	
	@Override
	public void run() {
		
		fileList.clear();
		bfsQueue.clear();
		long count = 0;
		
		Path dir = FileSystems.getDefault().getPath(path, "");
		
		if(dir.toFile().isFile()){
			System.out.println("Please supply a path  to folder.");
			return;
		}
		
		bfsQueue.add(dir);
		while(!bfsQueue.isEmpty()){
			try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(bfsQueue.poll());
			
			for(Path p : stream){
				if(p.toFile().isDirectory()){
					bfsQueue.add(p);
				} else {
					//System.out.println(p.toString());
					count++;
					if(count % progressUpdateInterval == 0){
						onComplete.updateIndexProgress(count);
					}
					
					analyzerResult.addEntry(new IndexEntry(p.toFile().getName(),p.toFile().length(),p.toFile().getAbsolutePath()));
				}
			}
			} catch (AccessDeniedException e){
				//e.printStackTrace();
				analyzerResult.incrementInaccessibleFiles();
			} catch (IOException e) {
				//e.printStackTrace();
				analyzerResult.incrementInaccessibleFiles();
			}
		}
		
		onComplete.onIndexComplete(analyzerResult);
		
	}

}
