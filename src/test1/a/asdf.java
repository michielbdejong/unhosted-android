package test1.a;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class asdf extends Activity {

	private Unhosted unhosted;
	private String[] getSandwichIngredients(String userAddress) {
		Sandwich sandwich = new Sandwich();
		try {
            sandwich.fromJson(this.unhosted.get(userAddress, "myfavouritesandwich.org", "favSandwich"));
        } catch (IOException e) {
            return null;
        }
		return sandwich.ingredients;
	}

	private void setSandwichIngredients(String[] ingredients) {
		String json = new Sandwich(ingredients).toJson();
		try {
            this.unhosted.set("myfavouritesandwich.org", "favSandwich", json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);
		//String userAddress = "mich@myfavouritesandwich.org";
		//String userPass = "asdf";
		//this.unhosted = new Unhosted(userAddress, userPass);

		this.unhosted = ((UnhostedApplication) getApplication()).getUnhosted();

		String[] ingr = {"and", "roid"};
		this.setSandwichIngredients(ingr);

		String[] ingredients = this.getSandwichIngredients(this.unhosted.getUserAddress());
		tv.setText("Logged in as "+this.unhosted.getUserAddress()+" - your sandwich has "+ingredients[0]+" and "+ingredients[1]+" on it");
		setContentView(tv);

	}
}