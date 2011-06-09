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
	private Unhosted unhosted;
	private String[] getSandwichIngredients(String userAddress) {
		Sandwich sandwich = new Sandwich();
		sandwich.fromJson(this.unhosted.get(userAddress, "myfavouritesandwich.org", "favSandwich"));
		return sandwich.ingredients;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		String userAddress = "mich@myfavouritesandwich.org";
		String userPass = "asdf";
		this.unhosted = new Unhosted(userAddress, userPass);
		String[] ingredients = this.getSandwichIngredients(userAddress);
		tv.setText("Logged in as "+userAddress+" - your sandwich has "+ingredients[0]+" and "+ingredients[1]+" on it");
		setContentView(tv);
	}
}