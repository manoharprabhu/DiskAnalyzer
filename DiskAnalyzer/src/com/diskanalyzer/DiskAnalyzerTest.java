package com.diskanalyzer;

import org.junit.Assert;
import org.junit.Test;

import com.diskanalyzer.helper.AnalyzerResult;
import com.diskanalyzer.helper.AnalyzerRunnable;
import com.diskanalyzer.helper.IndexerCallback;

public class DiskAnalyzerTest {

	@Test
	public void testAnalyzePath() throws InterruptedException {
		Thread t = new Thread(new AnalyzerRunnable("test_resources",
				new IndexerCallback() {

					@Override
					public void updateIndexProgress(long progress) {
						System.out.println(progress);
					}

					@Override
					public void onIndexComplete(AnalyzerResult result) {
						Assert.assertEquals(6L, result.getTotalNumberOfFiles());
						Assert.assertEquals(2L, result.getDocumentFilesCount());
						Assert.assertEquals(2L, result.getAudioFilesCount());
						Assert.assertEquals(1L, result.getPictureFilesCount());
						Assert.assertEquals(1L, result.getVideoFilesCount());
						Assert.assertEquals("document2.txt", result.getTopEntries().peek().getFilePath());
					}
				}));

		t.run();
	}
}
