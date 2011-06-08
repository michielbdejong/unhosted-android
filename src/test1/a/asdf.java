package test1.a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

public class asdf extends Activity {
	private String userAddressToDavUrl(String userAddress, String userDomain) {
		try {
			URL hostMeta = new URL("http://"+ userDomain + "/.well-known/host-meta");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error!";
	}
	
	//isn't there a cleaner way of doing this?
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	 
	private String[] getSandwichIngredients(String userAddress) {
		String[] userAddressParts = userAddress.split("@");
		if(userAddressParts.length != 2) {
			String[] error = { "invalid", "user address" };
			return error;
		}
		String davUrl = this.userAddressToDavUrl(userAddress, userAddressParts[1]);
		try {
			URL favSandwich = new URL(davUrl + "/webdav/" 
					+ userAddressParts[1] 
					+ "/" + userAddressParts[0] 
					+ "/myfavouritesandwich.org/favSandwich");
			Sandwich sandwich = new Gson().fromJson(
						convertStreamToString(favSandwich.openStream()), Sandwich.class);
			return sandwich.ingredients;
		} catch(MalformedURLException e) {
		} catch(IOException e) {
		}
		String[] unknown = { "ingredient 1", "ingredient 2" };
		return unknown;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		String userAddress = "mich@myfavouritesandwich.org";
		String[] ingredients = this.getSandwichIngredients(userAddress);
		tv.setText("Logged in as "+userAddress+" - your sandwich has "+ingredients[0]+" and "+ingredients[1]+" on it");
		setContentView(tv);
	}
}