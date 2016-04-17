package cisco.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends AppCompatActivity {
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        username = (EditText)findViewById(R.id.editText);

    }

    public void Register (View view){
        Toast.makeText(RegisterUser.this, "credentials of user: "+username.getText() + " added", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, LoadCredentials.class);
        startActivity(i);

    }

}
