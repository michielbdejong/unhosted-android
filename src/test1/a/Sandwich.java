package test1.a;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sandwich {
	public String[] ingredients = {"", ""};

	public void fromJson(String json) {
		JSONObject sandwichObj;
		try {
			sandwichObj = new JSONObject(json);
			JSONArray ingredientsArr = sandwichObj.getJSONArray("ingredients");
			this.ingredients[0] = ingredientsArr.getString(0);
			this.ingredients[1] = ingredientsArr.getString(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
