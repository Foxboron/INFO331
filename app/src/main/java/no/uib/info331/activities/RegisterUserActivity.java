package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

    //UI references
    private EditText editTextRegisterUsername;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterRepeatPassword;
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
        editTextRegisterUsername = (EditText) findViewById(R.id.register_username);
        editTextRegisterPassword = (EditText) findViewById(R.id.password_register);
        editTextRegisterRepeatPassword = (EditText) findViewById(R.id.repeat_password_register);
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
     * Registers a new currentUser.
     */
    public void registerUser() {
        String username = editTextRegisterUsername.getText().toString();
        final String password = editTextRegisterPassword.getText().toString();
        final String repeatPassword = editTextRegisterRepeatPassword.getText().toString();

        // Reset errors.
        editTextRegisterUsername.setError(null);
        editTextRegisterPassword.setError(null);
        editTextRegisterRepeatPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(username)) {
            editTextRegisterUsername.setError(getString(R.string.error_field_required));
            focusView = editTextRegisterUsername;
            cancel = true;
        } else if(TextUtils.isEmpty(password)) {
            editTextRegisterPassword.setError(getString(R.string.error_field_required));
            focusView = editTextRegisterPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(repeatPassword)) {
            editTextRegisterRepeatPassword.setError(getString(R.string.error_field_required));
            focusView = editTextRegisterRepeatPassword;
            cancel = true;
        } else if (!password.equals(repeatPassword)) {
            editTextRegisterRepeatPassword.setError(getString(R.string.error_password_match));
            focusView = editTextRegisterRepeatPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<User> call = apiService.register(username, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code()==200){
                        User currentUser = response.body();
                        currentUser.setPassword(password);
                        DataManager dataManager = new DataManager();
                        dataManager.storeObjectInSharedPref(getApplicationContext(), "currentlySignedInUser", currentUser);
                        Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        showProgress(false);
                        editTextRegisterUsername.setError(getString(R.string.error_username_exists));
                        editTextRegisterUsername.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
