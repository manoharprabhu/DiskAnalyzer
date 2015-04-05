package com.diskanalyzer;

import com.diskanalyzer.helper.AnalyzerResult;
import com.diskanalyzer.helper.IndexerCallback;

public class Main {
	
	public static String INPUT_PATH;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out
					.println("USAGE: java -jar DiskAnalyzer.jar <FOLDER_OR_DRIVE_TO_BE_ANALYZED>");
			return;
		}
		
		INPUT_PATH = args[0];

		final DiskAnalyzer analyzer = new DiskAnalyzer();
		analyzer.analyzePath(args[0], new IndexerCallback() {

			@Override
			public void onIndexComplete(AnalyzerResult analyzerResult) {
				System.out.println();
				System.out.println("Finshed indexing " + DiskAnalyzer.bytesToGigabytes(analyzerResult.getTotalSizeAnalyzed()) + " gigabytes of data. Generating report in your  browser.");
				analyzerResult.generateHTMLReport();
			}

			@Override
			public void updateIndexProgress(long progress) {
				System.out.print("Indexed " + progress + " files");
				System.out.print("\r");
			}
		});

	}

}
