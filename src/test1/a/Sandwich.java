package test1.a;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sandwich {
	public String[] ingredients;

	public Sandwich(String[] ingredients) {
		this.ingredients = ingredients;
	}
	public Sandwich() {
		String[] emptyIngredients ={"",""};
		this.ingredients = emptyIngredients;
	}
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
	public String toJson() {
		return "{\"ingredients\":[\""+this.ingredients[0]+"\", \""+this.ingredients[1]+"\"]}";
	    
	    /*
	    JSONObject json = new JSONObject();
	    JSONArray jsonIngredients = new JSONArray();
	    try {
            jsonIngredients.put(new JSONObject(this.ingredients[0]));
            jsonIngredients.put(new JSONObject(this.ingredients[1]));
	        json.put("ingredients", jsonIngredients);;
	    } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return json.toString();
	    */
	}
}
