package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText editTextUsername;
    private EditText editTextPassword;
    private View loginProgressView;
    private View viewLoginForm;
    private Button btnUsernameSignIn;
    private Button btnRegisterAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initGui();
        btnClickListeners();
    }

    private void btnClickListeners() {
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        btnUsernameSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btnRegisterAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void initGui() {

        editTextUsername = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        btnUsernameSignIn = (Button) findViewById(R.id.user_sign_in_button);
        btnRegisterAccount = (Button) findViewById(R.id.register_account);
        viewLoginForm = findViewById(R.id.login_form);
        loginProgressView = findViewById(R.id.login_progress);

    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        editTextUsername.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiService.login(username, password);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==200) {
                        Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        showProgress(false);
                        editTextPassword.setError(getString(R.string.error_incorrect_password));
                        editTextPassword.requestFocus();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showProgress(false);
                    btnUsernameSignIn.setError(getString(R.string.error_register));
                    btnUsernameSignIn.requestFocus();

                }
            });
        }
    }

    private void registerUser() {
        Intent intent = new Intent(getApplicationContext(), Register_User.class);
        startActivity(intent);
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

        viewLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        viewLoginForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        loginProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        loginProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}

