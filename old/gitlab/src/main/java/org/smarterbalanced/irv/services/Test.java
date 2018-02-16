/**
 * 
 */
package org.smarterbalanced.irv.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.smarterbalanced.irv.metadata.Metadata;;

/**
 * @author kthotti
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    try{
	    	FileInputStream fis = new FileInputStream("/Users/kthotti/sbac/metadata.xml");
	    	XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(fis);
	    	XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);
	    	
			JAXBContext jc = JAXBContext.newInstance(Metadata.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Metadata metaData = (Metadata) unmarshaller.unmarshal(xr);
			fis.close();
			System.out.println(metaData.getSmarterAppMetadata().getIdentifier());

	    }catch(Exception ex){
	      ex.printStackTrace(); //handle an exception here
	    }

	}

}
