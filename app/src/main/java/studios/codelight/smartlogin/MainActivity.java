package studios.codelight.smartlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import studios.codelight.smartlogin.R.id;
import studios.codelight.smartlogin.R.string;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;
/**
 * Created by santh on 6/20/2017.
 */


public class MainActivity extends AppCompatActivity implements SmartLoginCallbacks {

    private Button facebookLoginButton, googleLoginButton, customSigninButton, customSignupButton, logoutButton;
    private EditText emailEditText, passwordEditText;
    private static final int REQUEST_SIGNUP = 0;
    SmartUser currentUser;
    //GoogleApiClient mGoogleApiClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setListeners();


        config = new SmartLoginConfig(this, this);
        config.setFacebookAppId(getString(string.facebook_app_id));
        config.setFacebookPermissions(null);
        config.setGoogleApiClient(null);


    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = UserSessionManager.getCurrentUser(this);
        refreshLayout();
    }

    private void refreshLayout() {
        currentUser = UserSessionManager.getCurrentUser(this);
        if (currentUser != null) {
            Log.d("ExcelForte App", "Logged in user: " + currentUser.toString());
            facebookLoginButton.setVisibility(View.GONE);
            googleLoginButton.setVisibility(View.GONE);
            customSigninButton.setVisibility(View.GONE);
            customSignupButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            passwordEditText.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            facebookLoginButton.setVisibility(View.VISIBLE);
            googleLoginButton.setVisibility(View.VISIBLE);
            customSigninButton.setVisibility(View.VISIBLE);
            customSignupButton.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        }
    }

    private void setListeners() {
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Facebook login
                smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                smartLogin.login(config);
            }
        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Google login
                smartLogin = SmartLoginFactory.build(LoginType.Google);
                smartLogin.login(config);
            }
        });

        customSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign in
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.login(config);
            }
        });

        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.signup(config);
                /*
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivityForResult(intent,REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                */

                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    if (currentUser instanceof SmartFacebookUser) {
                        smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                    } else if(currentUser instanceof SmartGoogleUser) {
                        smartLogin = SmartLoginFactory.build(LoginType.Google);
                    } else {
                        smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                    }
                    boolean result = smartLogin.logout(MainActivity.this);
                    if (result) {
                        refreshLayout();
                        Toast.makeText(MainActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void bindViews() {
        facebookLoginButton = (Button) findViewById(id.facebook_login_button);
        googleLoginButton = (Button) findViewById(id.google_login_button);
        customSigninButton = (Button) findViewById(id.custom_signin_button);
        customSignupButton = (Button) findViewById(id.custom_signup_button);
        emailEditText = (EditText) findViewById(id.email_edittext);
        passwordEditText = (EditText) findViewById(id.password_edittext);
        logoutButton = (Button) findViewById(id.logout_button);
    }

    @Override
    public void onLoginSuccess(SmartUser user) {
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        return user;
    }
}
