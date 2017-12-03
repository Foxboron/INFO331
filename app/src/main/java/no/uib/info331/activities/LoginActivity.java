package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that lets the profileUser log in and register a profileUser in the db. Also stores the profileUser's login info
 * so that there is only need ofr logging in once.
 *
 * To see how the app determines which activity to send profileUser, see SplashActivity.java
 *
 *
 * @author Edvard P. Bjørgen, Fredrik V. Heimsæter
 *
 */
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.easyflipview_login_card) EasyFlipView flipViewLoginCard;

    // UI references.
    @BindView(R.id.edittext_login_username) EditText editTextLoginUsername;
    @BindView(R.id.edittext_login_password) EditText editTextLoginPassword;
    @BindView(R.id.edittext_user_sign_in) Button btnLoginUsernameSignIn;
    @BindView(R.id.btn_register_group_button) Button btnLoginRegisterAccount;
    @BindView(R.id.view_login_form) View viewLoginForm;
    @BindView(R.id.view_login_progress) View progressViewLogin;

    //Register flip card
    @BindView(R.id.edittext_register_username) EditText editTextRegisterUsername;
    @BindView(R.id.edittext_password_register) EditText editTextRegisterPassword;
    @BindView(R.id.edittext_repeat_password_register) EditText editTextRegisterRepeatPassword;
    @BindView(R.id.button_register_button) Button btnRegisterUser;
    @BindView(R.id.view_register_form) View viewRegisterForm;
    @BindView(R.id.view_register_progress) View viewRegisterProgressView;
    @BindView(R.id.button_back_to_login) Button btnBackToLogin;

    boolean isFlipStateFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initGui();
        btnClickListeners();
    }

    private void btnClickListeners() {
        editTextLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
            }
        });

        btnLoginUsernameSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btnLoginRegisterAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            flipViewLoginCard.flipTheView(true);
            isFlipStateFront = false;
            }
        });
//Register card
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            flipViewLoginCard.flipTheView(true);
            }
        });

    }

    private void initGui() {

    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        editTextLoginUsername.setError(null);
        editTextLoginPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = editTextLoginUsername.getText().toString();
        final String password = editTextLoginPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the profileUser entered one.
        if (TextUtils.isEmpty(password)) {
            editTextLoginPassword.setError(getString(R.string.error_field_required));
            focusView = editTextLoginPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            editTextLoginUsername.setError(getString(R.string.error_field_required));
            focusView = editTextLoginUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the profileUser login attempt.
            showProgress(true, viewLoginForm, progressViewLogin);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<User> call = apiService.login(username, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code()==200) {
                        User currentUser = response.body();
                        currentUser.setPassword(password);
                        DataManager dataManager = new DataManager();
                        dataManager.storeObjectInSharedPref(getApplicationContext(), "currentlySignedInUser", currentUser);
                        if(currentUser.getGroups() == null || currentUser.getGroups().size()<1) {
                            Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } else {
                        showProgress(false, viewLoginForm, progressViewLogin);
                        editTextLoginPassword.setError(getString(R.string.error_incorrect_password));
                        editTextLoginPassword.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false, viewLoginForm, progressViewLogin);
                    btnLoginUsernameSignIn.setError(getString(R.string.error_register));
                    btnLoginUsernameSignIn.requestFocus();
                }
            });
        }
    }


    /**
     * Registers a new profileUser.
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

            showProgress(true, viewRegisterForm, viewRegisterProgressView);

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
                        showProgress(false, viewRegisterForm, viewRegisterProgressView);
                        editTextRegisterUsername.setError(getString(R.string.error_username_exists));
                        editTextRegisterUsername.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false, viewRegisterForm, viewRegisterProgressView);
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
    private void showProgress(final boolean show, final View VIEW_FORM, final View PROGRESS_VIEW) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        VIEW_FORM.setVisibility(show ? View.GONE : View.VISIBLE);
        VIEW_FORM.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                VIEW_FORM.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        PROGRESS_VIEW.setVisibility(show ? View.VISIBLE : View.GONE);
        PROGRESS_VIEW.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PROGRESS_VIEW.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void onBackPressed() {
        if(!isFlipStateFront) {
            flipViewLoginCard.flipTheView(true);
            isFlipStateFront = true;
        } else if (isFlipStateFront){
            this.finishAffinity();
        }
    }
}
