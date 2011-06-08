package test1.a;

import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

public class asdf extends Activity {
	private String userAddressToDavUrl(String userAddress) {
		try {
			String[] userAddressParts = userAddress.split("@");
			if(userAddressParts.length != 2) {
				return "invalid user address";
			}
			URL hostMeta = new URL("http://"+ userAddressParts[1] + "/.well-known/host-meta");
			XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			HostMetaParser myHostMetaParser = new HostMetaParser();
			xr.setContentHandler(myHostMetaParser);
			xr.parse(new InputSource(hostMeta.openStream()));
			String template = myHostMetaParser.getLrddTemplate();
			String[] templateParts = template.split("\\{uri\\}");
			URL lrdd;
			if(templateParts.length == 1) {
				lrdd = new URL(templateParts[0] + userAddress);
			} else if(templateParts.length == 2) {
				lrdd = new URL(templateParts[0] + userAddress + templateParts[1]);
			} else {
				return "failed to parse lrdd template!";
			}
			LrddParser myLrddParser = new LrddParser();
			xr.setContentHandler(myLrddParser);
			xr.parse(new InputSource(lrdd.openStream()));
			String davUrl = myLrddParser.getDavUrl();
			return davUrl;
		} catch (Exception e) {
			return "Error!";
		}
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TextView tv = new TextView(this);
        tv.setText(this.userAddressToDavUrl("mich@myfavouritesandwich.org"));
        setContentView(tv);
	}
}