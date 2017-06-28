package studios.codelight.smartlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    private Button customSignInButton, custonSignUpButton;
    private CheckBox checkBox;
    private static final int REQUEST_SIGNUP = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        bindViews();
        setListeners();


    }

    private void bindViews() {
        customSignInButton = (Button) findViewById(R.id.logged_In);
        custonSignUpButton = (Button) findViewById(R.id.sign_In);
        checkBox = (CheckBox) findViewById(R.id.item_check);
    }

    private void setListeners() {
        customSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verify()) {
                    onLoginFailureSignIn();
                    return;

                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }
            }
        });
        custonSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verify()) {
                    onLoginFailureSignUp();
                    return;

                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }
            }
        });
    }
    public void onLoginFailureSignIn() {
        Toast.makeText(getBaseContext(), "You must agree to the terms and conditions", Toast.LENGTH_LONG).show();

        customSignInButton.setEnabled(true);
    }
    public void onLoginFailureSignUp()
    {
        Toast.makeText(getBaseContext(), "You must agree to the terms and conditions", Toast.LENGTH_LONG).show();

        custonSignUpButton.setEnabled(true);

    }
    public boolean verify()
    {
        boolean valid = false;
        if(checkBox.isChecked()== true)
        {
            valid = true;

        }
        return valid;
    }
}
