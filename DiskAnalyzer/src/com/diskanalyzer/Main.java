package com.diskanalyzer;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.diskanalyzer.helper.AnalyzerResult;
import com.diskanalyzer.helper.FileTypeHelper;
import com.diskanalyzer.helper.IndexerCallback;

public class Main {
	
	public static String INPUT_PATH;

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		if (args.length != 1) {
			System.out
					.println("USAGE: java -jar DiskAnalyzer.jar <FOLDER_OR_DRIVE_TO_BE_ANALYZED>");
			return;
		}
		
		new FileTypeHelper().getFileTypeSets();
		
		INPUT_PATH = args[0];

		final DiskAnalyzer analyzer = new DiskAnalyzer();
		analyzer.analyzePath(args[0], new IndexerCallback() {

			@Override
			public void onIndexComplete(AnalyzerResult analyzerResult) {
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
