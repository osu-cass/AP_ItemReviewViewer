/**
 * 
 */
package org.smarterbalanced.irv.services;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * @author kthotti
 *
 */
public class XMLReaderWithoutNamespace extends StreamReaderDelegate {

	/**
	 * 
	 */
	public XMLReaderWithoutNamespace() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reader
	 */
	public XMLReaderWithoutNamespace(XMLStreamReader reader) {
		super(reader);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public String getAttributeNamespace(int arg0) {
      return "";
    }
    @Override
    public String getNamespaceURI() {
      return "";
    }

}
