package test1.a;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;

 






public class asdf extends Activity {
	private String getHostMeta(String domain) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet("http://"+domain+"/.well-known/host-meta");
		String responseBody;
		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			responseBody = httpclient.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			responseBody = "CPE";
		} catch (IOException e) {
			responseBody = "IOE";
		}
		return responseBody;
	}

	private String userAddressToDavUrl(String userAddress) {
		try {
			String[] parts = userAddress.split("@");
			if(parts.length != 2) {
				return "invalid user address";
			}
			URL hostMeta = new URL("http://"+ parts[1] + "/.well-known/host-meta");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			HostMetaParser myHostMetaParser = new HostMetaParser();
			xr.setContentHandler(myHostMetaParser);
			xr.parse(new InputSource(hostMeta.openStream()));
			String template = myHostMetaParser.getLrddTemplate();
			return template;
		} catch (Exception e) {
			return "Error!";
		}
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TextView tv = new TextView(this);
        tv.setText(this.userAddressToDavUrl("mich@federoni.org"));
        setContentView(tv);
	}
}