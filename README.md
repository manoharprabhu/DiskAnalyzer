# DiskAnalyzer
A Java application that analyzes your disk and generates useful HTML report about the stats.

Generate HTML reports about your files with this tool.

![Alt text](screenshot.PNG?raw=true "DiskAnalyzer report screenshot")

Usage
======
    java -jar DiskAnalyzer.java <PATH_TO_BE_ANALYZED>

The DiskAnalyzer API is also available in  *com.diskanalyzer.DiskAnalyzer*
#####Example

```java
DiskAnalyzer analyzer = new DiskAnalyzer();
analyzer.analyzePath("E:\\",new IndexerCallback(){
/*This method will be called upon completion of analysys.
*The AnalyzerResult object will contain details about the stats found during anaysis
*/
@Override
public void onIndexComplete(AnalyzerResult analyzerResult) {
		
}

/* This method will be called on analysis of every 1000th files.
* You can perform the progress update here.
*/
@Override
public void updateIndexProgress(long progress) {
			
}
});
```
