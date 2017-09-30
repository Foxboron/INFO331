package no.uib.info331.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import no.uib.info331.R;

public class Register_User extends AppCompatActivity {

    //UI references
    private AutoCompleteTextView autoCompleteTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button btnRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    private void initGUI() {
        autoCompleteTextEmail = (AutoCompleteTextView) findViewById(R.id.email_register);
        editTextName = (EditText) findViewById(R.id.name_register);
        editTextPassword = (EditText) findViewById(R.id.password_register);
        editTextRepeatPassword = (EditText) findViewById(R.id.repeat_password_register);
        btnRegisterUser = (Button) findViewById(R.id.register_button);

    }

    private void btnClickListeners() {
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    /**
     * Registers a new user.
     */
    public void registerUser() {
        //TODO: Add the magic here.
    }
}
