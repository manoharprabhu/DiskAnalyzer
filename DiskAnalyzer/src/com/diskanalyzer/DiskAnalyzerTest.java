package com.diskanalyzer;

import org.junit.Assert;
import org.junit.Test;

import com.diskanalyzer.helper.AnalyzerResult;
import com.diskanalyzer.helper.AnalyzerRunnable;
import com.diskanalyzer.helper.IndexerCallback;

/**
 * 
 *
 * @author Manohar Prabhu
 */
public class DiskAnalyzerTest {

	@Test
	public void testBasicTest() throws InterruptedException {
		final Thread thread = new Thread(new AnalyzerRunnable(
				"test_resources\\basic_test", new IndexerCallback() {

					@Override
					public void updateIndexProgress(final long progress) {
						// Check for progress here
					}

					@Override
					public void onIndexComplete(final AnalyzerResult result) {
						Assert.assertEquals(6L, result.getTotalNumberOfFiles());
						Assert.assertEquals(2L, result.getDocumentFilesCount());
						Assert.assertEquals(2L, result.getAudioFilesCount());
						Assert.assertEquals(1L, result.getPictureFilesCount());
						Assert.assertEquals(1L, result.getVideoFilesCount());
						Assert.assertEquals("document2.txt",
								result.getTopEntryFileName());
					}
				}));

		thread.run(); // NOPMD by manoharprabhu on 2/29/16 12:36 AM
	}

	@Test
	public void testSizeTest() throws InterruptedException {
		Thread thread = new Thread(new AnalyzerRunnable( // NOPMD by
															// manoharprabhu on
															// 2/29/16 12:36 AM
				"test_resources\\size_test", new IndexerCallback() {

					@Override
					public void updateIndexProgress(final long progress) {
						// Check for progress here
					}

					@Override
					public void onIndexComplete(final AnalyzerResult result) {
						Assert.assertEquals(36, result.getTotalSizeAnalyzed());
					}
				}));

		thread.run(); // NOPMD by manoharprabhu on 2/29/16 12:35 AM
	}
}
