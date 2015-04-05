package com.diskanalyzer.helper;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.diskanalyzer.DiskAnalyzer;
import com.diskanalyzer.Main;

public class AnalyzerResult {
	
	private static AnalyzerResult instance = new AnalyzerResult();
	private PriorityQueue<IndexEntry> topEntries;
	
	private long numberOfEntries;
	private long totalSizeAnalyzed;
	private long videoFilesCount;
	private long audioFilesCount;
	private long documentFilesCount;
	private long pictureFilesCount;
	private long otherFilesCount;
	
	public long getOtherFilesCount() {
		return otherFilesCount;
	}

	public void setOtherFilesCount(long otherFilesCount) {
		this.otherFilesCount = otherFilesCount;
	}

	private static Set<String> audioMediaList;
	private static Set<String> pictureMediaList;
	private static Set<String> videoMediaList;
	private static Set<String> documentMediaList;
	
	public long getNumberOfEntries() {
		return numberOfEntries;
	}

	public void setNumberOfEntries(long numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public long getVideoFilesCount() {
		return videoFilesCount;
	}

	public void setVideoFilesCount(long videoFilesCount) {
		this.videoFilesCount = videoFilesCount;
	}

	public long getAudioFilesCount() {
		return audioFilesCount;
	}

	public void setAudioFilesCount(long audioFilesCount) {
		this.audioFilesCount = audioFilesCount;
	}

	public long getDocumentFilesCount() {
		return documentFilesCount;
	}

	public void setDocumentFilesCount(long documentFilesCount) {
		this.documentFilesCount = documentFilesCount;
	}

	public long getPictureFilesCount() {
		return pictureFilesCount;
	}

	public void setPictureFilesCount(long pictureFilesCount) {
		this.pictureFilesCount = pictureFilesCount;
	}

	public long getInaccessibleFiles() {
		return inaccessibleFiles;
	}

	public void setInaccessibleFiles(long inaccessibleFiles) {
		this.inaccessibleFiles = inaccessibleFiles;
	}
	
	public void incrementInaccessibleFiles(){
		inaccessibleFiles++;
	}

	private long inaccessibleFiles;
	
	public long getTotalSizeAnalyzed() {
		return totalSizeAnalyzed;
	}

	public void setTotalSizeAnalyzed(long totalSizeAnalyzed) {
		this.totalSizeAnalyzed = totalSizeAnalyzed;
	}

	public PriorityQueue<IndexEntry> getTopEntries() {
		return topEntries;
	}

	public void setTopEntries(PriorityQueue<IndexEntry> topEntries) {
		this.topEntries = topEntries;
	}

	private AnalyzerResult(){
		topEntries = new PriorityQueue<IndexEntry>();
		numberOfEntries = 10;
		totalSizeAnalyzed = 0;
		inaccessibleFiles = 0;
		populateMediaList();
	}
	
	private void populateMediaList(){
		
		audioMediaList = new HashSet<String>();
		pictureMediaList = new HashSet<String>();
		videoMediaList = new HashSet<String>();
		documentMediaList = new HashSet<String>();
		
		
		pictureMediaList.add("jpeg");
		pictureMediaList.add("jpg");
		pictureMediaList.add("png");
		pictureMediaList.add("gif");
		pictureMediaList.add("tiff");
		
		
		videoMediaList.add("avi");
		videoMediaList.add("mkv");
		videoMediaList.add("flv");
		videoMediaList.add("mp4");
		videoMediaList.add("mov");
		videoMediaList.add("3gp");
		
		
		audioMediaList.add("mp3");
		audioMediaList.add("ogg");
		audioMediaList.add("raw");
		audioMediaList.add("midi");
		
		documentMediaList.add("doc");
		documentMediaList.add("docx");
		documentMediaList.add("xls");
		documentMediaList.add("xlsx");
		documentMediaList.add("ppt");
		documentMediaList.add("pptx");
		documentMediaList.add("txt");
		documentMediaList.add("rtf");
		
	}
	
	public static AnalyzerResult getInstance(){
		return instance;
	}
	
	public void addEntry(IndexEntry entry){
		topEntries.add(entry);
		totalSizeAnalyzed += entry.getFileSize();
		if(topEntries.size() > numberOfEntries){
			topEntries.poll();
		}
		//System.out.println(entry.getFilePath());
		String[] temp = entry.getFilePath().split("\\.");
		
		String extension = "";
		if(temp.length!=0){
			extension =  temp[temp.length-1].toLowerCase();
		}
		
		if(pictureMediaList.contains(extension)){
			pictureFilesCount++;
		} else if(audioMediaList.contains(extension)){
			audioFilesCount++;
		} else if(videoMediaList.contains(extension)){
			videoFilesCount++;
		} else if(documentMediaList.contains(extension)){
			documentFilesCount++;
		} else {
			otherFilesCount++;
		}
		
	}
	
	public  void generateHTMLReport(){
		
		try {
		File temp = File.createTempFile("diskanalyzer", ".html");
		//File template = new File(getClass().getClassLoader().);
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/result_template.html")));
		String t;
		while(( t= reader.readLine()) != null){
			builder.append(t);
		}
		reader.close();
		
		t = builder.toString();
		
		t = t.replaceFirst("TOTAL_PICTURES_FOUND", String.valueOf(this.getPictureFilesCount()));
		t = t.replaceFirst("TOTAL_MUSIC_FOUND", String.valueOf(this.getAudioFilesCount()));
		t = t.replaceFirst("TOTAL_VIDEO_FOUND", String.valueOf(this.getVideoFilesCount()));
		t = t.replaceFirst("TOTAL_DOCUMENT_FOUND", String.valueOf(this.getDocumentFilesCount()));
		t = t.replaceFirst("TOTAL_OTHER_FOUND", String.valueOf(this.getOtherFilesCount()));
		t = t.replaceFirst("TOTAL_DATA_ANALYZED", String.format("%.2f", DiskAnalyzer.bytesToGigabytes(this.getTotalSizeAnalyzed()))  + "<br />Gigabytes" );
		
		
		builder = new StringBuilder();
		builder.append("<table class=\"top-entries-table table table-bordered\">");
		builder.append("<thead><tr><td  colspan=\"2\" class=\"center\">");
		builder.append("<strong>Analysis of <code>");
		builder.append(StringEscapeUtils.escapeJava(Main.INPUT_PATH));
		builder.append("</code></strong><br />Top 10 space occupying files</td></tr><tr><td>Filename</td><td>Size in MB</td></tr></thead>");
		
		List<IndexEntry> sortedEntries = new ArrayList<IndexEntry>();
		PriorityQueue<IndexEntry> tempQueue = new PriorityQueue<IndexEntry>();
		tempQueue.addAll(this.getTopEntries());
		
		while(tempQueue.size() > 0){
			sortedEntries.add(tempQueue.poll());
		}
		
		for(int i=sortedEntries.size()-1;i>=0;i--){
			IndexEntry entry = sortedEntries.get(i);
			String fileName = StringEscapeUtils.escapeJava(entry.getFilePath());
			if(fileName.length() > 60){
				fileName = fileName.substring(0, 59);
				fileName = fileName + "...";
			}
			builder.append("<tr>");
			builder.append("<td><code title=\"");
			builder.append(StringEscapeUtils.escapeJava(entry.getFullFilePath()));
			builder.append("\">");
			builder.append(fileName);
			builder.append("</code></td>");
			builder.append("<td>");
			builder.append(DiskAnalyzer.bytesToMegabytes(entry.getFileSize()));
			builder.append("</td>");
			builder.append("</tr>");
		}
		builder.append("</table>");
		t = t.replaceFirst("TOP_TABLE_ENTRIES", builder.toString());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
		writer.write(t);
		writer.close();
		
		Desktop.getDesktop().open(temp);;
		
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
