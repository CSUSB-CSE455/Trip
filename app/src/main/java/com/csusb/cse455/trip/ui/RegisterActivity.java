package com.csusb.cse455.trip.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.csusb.cse455.trip.R;
import com.csusb.cse455.trip.data.FirebaseDb;
import com.csusb.cse455.trip.model.User;
import com.csusb.cse455.trip.utils.FirebaseUtil;
import com.csusb.cse455.trip.utils.Format;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    // Firebase Authentication instance.
    private FirebaseAuth mAuth;

    //Async Task
    MyAsyncTask myAsyncTask;

    // Handles initialization during view creation.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Super propagation.
        super.onCreate(savedInstanceState);
        // Set content view layout.
        setContentView(R.layout.activity_register);

        // Get Firebase Authentication instance.
        mAuth = FirebaseAuth.getInstance();

        // Get UI references.
        final EditText firstNameView = (EditText) findViewById(R.id.regFirstName);
        final EditText lastNameView = (EditText) findViewById(R.id.regLastName);
        final TextView emailView = (TextView) findViewById(R.id.regEmail);
        final EditText passwordView = (EditText) findViewById(R.id.regPassword);
        final EditText rePasswordView = (EditText) findViewById(R.id.regRePassword);
        final Button regButton = (Button) findViewById(R.id.regBtn);
        final Button backArrowButton = (Button) findViewById(R.id.backBtn);

        // Set on click listener for the back button
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set on click listener for the registration button.
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If information is properly formatted...
                if (!Format.isTextViewEmpty(firstNameView) &&
                        !Format.isTextViewEmpty(lastNameView) &&
                        Format.checkEmailFormat(emailView) &&
                        Format.checkNewPasswordFormat(passwordView, rePasswordView)) {
                    // Attempt to register.
                    tryRegister(emailView.getText().toString(),
                            passwordView.getText().toString(),
                            firstNameView.getText().toString(),
                            lastNameView.getText().toString());
                    //Set Progress Dialog Visible
                    myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();

                }
            }
        });
    }

    // Attempts to register a user.  If successful, sends out email verification.  If not,
    // notifies the user.
    protected void tryRegister(final String email, final String password,
                               final String fName, final String lName) {
        // Attempt to create a user with email and password.
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If successful, lets the user know, stores additional information
                        // provided, sends out verification email and transitions to the login
                        // screen.
                        if (task.isSuccessful()) {
                            // Store additional user information.
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                FirebaseDb.createOrUpdateUser(user.getUid(), new User(fName, lName));
                            }
                            // Send out verification email.
                            FirebaseUtil.sendEmailVerification(RegisterActivity.this, user);
                            // Finish up and transition to login screen.
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this,
                                    "Could not register using provided information.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //Set up MyAsyncTask for Progress Dialog
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        boolean running;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            int i = 10;
            while(running){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i-- == 0){
                    running = false;
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            progressDialog = ProgressDialog.show(RegisterActivity.this,"","Creating Account...",true,false);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}