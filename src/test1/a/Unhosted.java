package test1.a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Unhosted {
	private String userAddress, userName, userDomain, userPassword, davUrl;
	//private Map<String, String> davTokens;
	public Unhosted(String userAddress, String userPassword) {
		this.userAddress = userAddress;
		this.userPassword = userPassword;
		String[] userAddressParts = userAddress.split("@");
		if(userAddressParts.length != 2) {
			//error
		}
		this.userName = userAddressParts[0];
		this.userDomain = userAddressParts[1];
		this.davUrl = this.userAddressToDavUrl(this.userAddress, this.userDomain);
	}
	
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
	 
	public String get(String userAddress, String dataScope, String blobKey) {
		String[] userAddressParts = userAddress.split("@");
		if(userAddressParts.length != 2) {
			return "error";
		}
		String davUrl = this.userAddressToDavUrl(userAddress, userAddressParts[1]);
		try {
			URL blob = new URL(davUrl + "/webdav/" 
					+ userAddressParts[1] 
					+ "/" + userAddressParts[0] 
					+ "/myfavouritesandwich.org/favSandwich");
			String str = convertStreamToString(blob.openStream());
			return str;
		} catch(MalformedURLException e) {
		} catch(IOException e) {
		}
		return "error";
	}
	private String getDavToken(String dataScope) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(this.davUrl + "oauth2/auth/");//this should be without the trailing slash, but HttpPost reverts 301's to GETs, so quick hack...
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("user_address", this.userAddress));
	        nameValuePairs.add(new BasicNameValuePair("pwd", this.userPassword));
	        nameValuePairs.add(new BasicNameValuePair("scope", dataScope));
	        //nameValuePairs.add(new BasicNameValuePair("redirect_uri", "ho://me"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        Header[] a = response.getAllHeaders();
	        for(int i = 0; i < a.length; i++) {
	        	if(a[i].getName().equals("Token")) {
	        			return a[i].getValue();
	        	}	
	        }
	        Header[] tokenHeaders = response.getHeaders("Token");
	        String davToken = tokenHeaders[0].getName();
	        davToken = tokenHeaders[0].getValue();
	        //davToken = davToken.substring(14, 0);//length of "ho://me?token=" is 14
		    return davToken;
		} catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
			return "deadbeef";	
		} catch (IOException e) {
		        // TODO Auto-generated catch block
			return "deadbeef";			
		}
	}
	private String getBasicAuth(String dataScope) {
		return this.getDavToken(dataScope);
		//if (!this.davTokens.containsKey(dataScope)) {
		//	this.davTokens.put(dataScope, this.getDavToken(dataScope));
		//}
		//return (String) this.davTokens.get(dataScope);
	}
	public void set(String dataScope, String blobKey, String blob) {
		try {
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httpput = new HttpPut(this.davUrl + "/webdav/" 
					+ this.userDomain 
					+ "/" + this.userName 
					+ "/myfavouritesandwich.org/favSandwich");
			httpput.addHeader("Authorization",
					this.getBasicAuth(dataScope));
			httpput.setEntity(new StringEntity(blob));
		    try {
		        HttpResponse response = httpclient.execute(httpput);    
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		} catch(IOException e) {
		}
	}
}
