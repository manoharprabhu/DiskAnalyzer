package com.diskanalyzer.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileTypeHelper {
	public  HashMap<String,Set<String>> getFileTypeSets(){
		
		
		HashMap<String,Set<String>> superSet = new HashMap<String,Set<String>>();
		
		try {
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(getClass().getClassLoader().getResourceAsStream("FileTypes.xml"));
		
		document.getDocumentElement().normalize();
		
		int length = document.getElementsByTagName("file").getLength();
		for(int i=0;i<length;i++){
			if(superSet.get(((Element)document.getElementsByTagName("file").item(i)).getAttribute("type")) == null){
				superSet.put((((Element)document.getElementsByTagName("file").item(i)).getAttribute("type")),new HashSet<String>());
			}
			superSet.get(((Element)document.getElementsByTagName("file").item(i)).getAttribute("type")).add(((Element)document.getElementsByTagName("file").item(i)).getAttribute("value"));
		}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return superSet;
	}
}
