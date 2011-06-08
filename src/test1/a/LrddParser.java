package test1.a;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LrddParser extends DefaultHandler{
	private String davUrl = null;
	@Override
	public void startElement(String namespaceURI, String localName,
	                         String qName, Attributes atts) throws SAXException {
		if (localName.equals("Link")) {
			String rel = atts.getValue("rel");
			if(rel.equals("http://unhosted.org/spec/dav/0.1")) {
				this.davUrl = atts.getValue("href");
			}
		}
	}
	public String getDavUrl() {
		return this.davUrl;
	}
}