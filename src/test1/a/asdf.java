package test1.a;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class asdf extends Activity {
	private Unhosted unhosted;
	private String[] getSandwichIngredients(String userAddress) {
		Sandwich sandwich = new Sandwich();
		sandwich.fromJson(this.unhosted.get(userAddress, "myfavouritesandwich.org", "favSandwich"));
		return sandwich.ingredients;
	}

	private void setSandwichIngredients(String[] ingredients) {
		String json = new Sandwich(ingredients).toJson();
		this.unhosted.set("myfavouritesandwich.org", "favSandwich", json);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		String userAddress = "mich@myfavouritesandwich.org";
		String userPass = "asdf";
		this.unhosted = new Unhosted(userAddress, userPass);
		String[] ingr = {"and", "roid"};
		this.setSandwichIngredients(ingr);
		String[] ingredients = this.getSandwichIngredients(userAddress);
		tv.setText("Logged in as "+userAddress+" - your sandwich has "+ingredients[0]+" and "+ingredients[1]+" on it");
		setContentView(tv);
	}
}