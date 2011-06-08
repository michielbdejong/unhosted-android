package test1.a;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HostMetaParser extends DefaultHandler{
	private String lrddTemplate = null;
	@Override
	public void startElement(String namespaceURI, String localName,
	                         String qName, Attributes atts) throws SAXException {
		if (localName.equals("Link")) {
			String rel = atts.getValue("rel");
			if(rel.equals("lrdd")) {
				this.lrddTemplate = atts.getValue("template");
			}
		}
	}
	public String getLrddTemplate() {
		return this.lrddTemplate;
	}
}
