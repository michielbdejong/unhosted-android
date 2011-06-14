package test1.a;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login:
            TextView usernameView = (TextView) findViewById(R.id.username);
            TextView passwordView = (TextView) findViewById(R.id.password);
            CharSequence userAddress = usernameView.getText();
            CharSequence password = passwordView.getText();
            if (TextUtils.isEmpty(userAddress)) {
                usernameView.setError("Username must not be empty.");
            }
            if (TextUtils.isEmpty(password)) {
                passwordView.setError("Password must not be empty.");
            }

            if (!TextUtils.isEmpty(userAddress) && !TextUtils.isEmpty(password)) {
                new LoginTask().execute(userAddress.toString(), password.toString());
            }

            break;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Unhosted> {

        @Override
        protected Unhosted doInBackground(String... params) {
            Unhosted unhosted = new Unhosted(params[0], params[1]);
            try {
                unhosted.login();
            } catch (IOException e) {
                return null;
            }
            return unhosted;

        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Unhosted result) {
            if (result == null) {
                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                return;
            }
            ((UnhostedApplication)getApplication()).setUnhosted(result);

            startActivity(new Intent(Login.this, asdf.class));
        }

        
    }
}
