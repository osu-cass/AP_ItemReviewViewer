/**
 * 
 */
package org.smarterbalanced.irv.services;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.smarterbalanced.irv.model.MetaData;

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
			JAXBContext jc = JAXBContext.newInstance(MetaData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Path path = Paths.get("/Users/kthotti/sbac/gitlab/item-187-3212/metadata.xml");
	    	BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
			MetaData metaData = (MetaData) unmarshaller.unmarshal(reader);
			reader.close();
			System.out.println(metaData.getSmarterAppMetadata().getAllowCalculator());

	    }catch(Exception ex){
	      ex.printStackTrace(); //handle an exception here
	    }

	}

}
