package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_User extends AppCompatActivity {

    //UI references
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button btnRegisterUser;
    private View registerProgressView;
    private View viewRegisterForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        initGUI();
        btnClickListeners();
    }

    private void initGUI() {
        editTextUsername = (EditText) findViewById(R.id.register_username);
        editTextPassword = (EditText) findViewById(R.id.password_register);
        editTextRepeatPassword = (EditText) findViewById(R.id.repeat_password_register);
        btnRegisterUser = (Button) findViewById(R.id.register_button);
        registerProgressView = findViewById(R.id.register_progress);
        viewRegisterForm = findViewById(R.id.register_form);

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
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();

        // Reset errors.
        editTextUsername.setError(null);
        editTextPassword.setError(null);
        editTextRepeatPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            cancel = true;
        } else if(TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(repeatPassword)) {
            editTextRepeatPassword.setError(getString(R.string.error_field_required));
            focusView = editTextRepeatPassword;
            cancel = true;
        } else if (!password.equals(repeatPassword)) {
            editTextRepeatPassword.setError(getString(R.string.error_password_match));
            focusView = editTextRepeatPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            User user = new User(username, password);


            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiService.register(username, password);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==200){
                        Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        showProgress(false);
                        editTextUsername.setError(getString(R.string.error_username_exists));
                        editTextUsername.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showProgress(false);
                    btnRegisterUser.setError(getString(R.string.error_register));
                    btnRegisterUser.requestFocus();
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        viewRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        viewRegisterForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        registerProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        registerProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registerProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
