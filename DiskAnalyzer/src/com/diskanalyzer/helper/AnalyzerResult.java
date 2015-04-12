package com.diskanalyzer.helper;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

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

	private static HashMap<String, Set<String>> superSet;

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

	public void incrementInaccessibleFiles() {
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

	private AnalyzerResult() {
		topEntries = new PriorityQueue<IndexEntry>();
		numberOfEntries = 10;
		totalSizeAnalyzed = 0;
		inaccessibleFiles = 0;
		populateMediaList();

	}

	private void populateMediaList() {
		superSet = new HashMap<String, Set<String>>();
		FileTypeHelper helper = new FileTypeHelper();
		superSet = helper.getFileTypeSets();

	}

	public static AnalyzerResult getInstance() {
		return instance;
	}

	public void addEntry(IndexEntry entry) {
		topEntries.add(entry);
		totalSizeAnalyzed += entry.getFileSize();
		if (topEntries.size() > numberOfEntries) {
			topEntries.poll();
		}
		// System.out.println(entry.getFilePath());
		String[] temp = entry.getFilePath().split("\\.");

		String extension = "";
		if (temp.length != 0) {
			extension = temp[temp.length - 1].toLowerCase();
		}

		if (superSet.get("picture").contains(extension)) {
			pictureFilesCount++;
		} else if (superSet.get("audio").contains(extension)) {
			audioFilesCount++;
		} else if (superSet.get("video").contains(extension)) {
			videoFilesCount++;
		} else if (superSet.get("document").contains(extension)) {
			documentFilesCount++;
		} else {
			otherFilesCount++;
		}

	}

	public void generateHTMLReport() {

		try {
			File temp = File.createTempFile("diskanalyzer", ".html");
			// File template = new File(getClass().getClassLoader().);
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("/result_template.html")));
			String t;
			while ((t = reader.readLine()) != null) {
				builder.append(t);
			}
			reader.close();

			t = builder.toString();

			t = t.replaceFirst("TOTAL_PICTURES_FOUND",
					String.valueOf(this.getPictureFilesCount()));
			t = t.replaceFirst("TOTAL_MUSIC_FOUND",
					String.valueOf(this.getAudioFilesCount()));
			t = t.replaceFirst("TOTAL_VIDEO_FOUND",
					String.valueOf(this.getVideoFilesCount()));
			t = t.replaceFirst("TOTAL_DOCUMENT_FOUND",
					String.valueOf(this.getDocumentFilesCount()));
			t = t.replaceFirst("TOTAL_OTHER_FOUND",
					String.valueOf(this.getOtherFilesCount()));

			
			String[] result = this.getAppropriateSizeWithUnit(this.getTotalSizeAnalyzed(),false).split(" ");

			t = t.replaceFirst("TOTAL_DATA_ANALYZED",
					result[0] + "<br />" + result[1]);

			builder = new StringBuilder();
			builder.append("<table class=\"table table-responsive table-condensed\">");
			builder.append("<thead><tr><td  colspan=\"2\" class=\"center\">");
			builder.append("<strong>Analysis of <code>");
			builder.append(StringEscapeUtils.escapeJava(Main.INPUT_PATH));
			builder.append("</code></strong><br />Top 10 space occupying files</td></tr><tr><td>Filename</td><td>Size on disk</td></tr></thead>");

			List<IndexEntry> sortedEntries = new ArrayList<IndexEntry>();
			PriorityQueue<IndexEntry> tempQueue = new PriorityQueue<IndexEntry>();
			tempQueue.addAll(this.getTopEntries());

			while (tempQueue.size() > 0) {
				sortedEntries.add(tempQueue.poll());
			}

			for (int i = sortedEntries.size() - 1; i >= 0; i--) {
				IndexEntry entry = sortedEntries.get(i);
				String fileName = StringEscapeUtils.escapeJava(entry
						.getFilePath());
				if (fileName.length() > 60) {
					fileName = fileName.substring(0, 59);
					fileName = fileName + "...";
				}
				builder.append("<tr>");
				builder.append("<td><code title=\"");
				builder.append(StringEscapeUtils.escapeJava(entry
						.getFullFilePath()));
				builder.append("\">");
				builder.append(fileName);
				builder.append("</code></td>");
				builder.append("<td>");
				builder.append(this.getAppropriateSizeWithUnit(entry.getFileSize(),true));
				builder.append("</td>");
				builder.append("</tr>");
			}
			builder.append("</table>");
			t = t.replaceFirst("TOP_TABLE_ENTRIES", builder.toString());

			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
			writer.write(t);
			writer.close();

			Desktop.getDesktop().open(temp);
			;

		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public String getAppropriateSizeWithUnit(long size,boolean isShortUnit){
		String unit = "";
		double result = 0;
		if (size >= 1000 * 1000 * 1000) {
			result = DiskAnalyzer.bytesToGigabytes(size);
			unit = isShortUnit?"GB":"Gigabytes";
		} else if (size >= 1000 * 1000) {
			result = DiskAnalyzer.bytesToMegabytes(size);
			unit = isShortUnit?"MB":"Megabytes";
		} else if (size >= 1000) {
			result = DiskAnalyzer.bytesToKilobytes(size);
			unit = isShortUnit?"KB":"Kilobytes";
		} else {
			result = size;
			unit = isShortUnit?"B":"Bytes";
		}
		return String.format("%.2f", result) +" " + unit;
	}
	

}
