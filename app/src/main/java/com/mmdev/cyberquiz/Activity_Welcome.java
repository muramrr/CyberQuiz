package com.mmdev.cyberquiz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.EmailAuthProvider;
//import com.google.firebase.auth.FacebookAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthUserCollisionException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.mmdev.cyberquiz.dialogs.Dialog_Register;
//
//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//
//
public class Activity_Welcome extends AppCompatActivity
{
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private FirebaseAuth mAuth;
//    private CallbackManager mCallbackManager;
//    private Button mReg_but;
//    private LoginButton mlogFb_btn;
//    private EditText mLogin_email;
//    private EditText mLogin_pass;
//    private Boolean pass_onAuth=false;
//
//    @Override
//    protected void onCreate (Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        auth();
//        setContentView(R.layout.activity_welcome);
//        mlogFb_btn = findViewById(R.id.facebook_login_button);
//        facebookAuth();
//        mReg_but = findViewById(R.id.register_but);
//        mLogin_email = findViewById(R.id.login_email);
//        mLogin_pass = findViewById(R.id.login_pass);
//
//    }
//
//    /**************************************
//     *               AUTH              *
//     ***************************************/
//    public void auth ()
//    {
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener()
//        {
//            @Override
//            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth)
//            {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null && !pass_onAuth)
//                {
//                    Toast.makeText(getApplicationContext(), "signed_in:" + user.getDisplayName(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Activity_Welcome.this, Activity_Main.class);
//                    startActivity(intent);
//                    finish();
//                } else Toast.makeText(getApplicationContext(), "signed_out", Toast.LENGTH_SHORT).show();
//            }
//        };
//    }
//
//    public void facebookAuth()
//    {
//        mCallbackManager = CallbackManager.Factory.create();
//        mlogFb_btn.setReadPermissions("email", "public_profile");
//        mlogFb_btn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
//        {
//            @Override
//            public void onSuccess (LoginResult loginResult)
//            {
//                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel ()
//            {
//                //Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError (FacebookException error)
//            {
//                //Log.d(TAG, "facebook:onError", error);
//            }
//        });
//    }
//
//    public void handleFacebookAccessToken (AccessToken token)
//    {
//        //pass_onAuth=true;
//        //Log.d(TAG, "handleFacebookAccessToken:" + token);
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
//        {
//            @Override
//            public void onComplete (@NonNull Task<AuthResult> task)
//            {
//                //Log.d(TAG, "facebook signInWithCredential:onComplete:" + task.isSuccessful());
//
//                // If sign in fails, display a message to the user. If sign in succeeds
//                // the auth state listener will be notified and logic to handle the
//                // signed in user can be handled in the listener.
//                if (!task.isSuccessful())
//                {
//                    //Log.w(TAG, "facebook signInWithCredential", task.getException());
//                    Toast.makeText(getApplicationContext(), "Facebook Authentication failed.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult (int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onStart ()
//    {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop ()
//    {
//        super.onStop();
//        if (mAuthListener != null)
//        {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//
//    }
//
//    /**************************************
//     *               CREATE USER              *
//     ***************************************/
//    public void createAccount (final String login, final String email, final String password, Boolean passonAuth)
//    {
//        pass_onAuth=passonAuth;
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
//        {
//            @Override
//            public void onComplete (@NonNull Task<AuthResult> task)
//            {
//                //if email already exist
//                if (!task.isSuccessful())
//                {
//                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
//                        Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
//                }
//                //else register it
//                else
//                {
//                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    //user.sendEmailVerification();
//                    //change login user and then reauth to apply changes
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(login).build();
//                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
//                    {
//                        @Override
//                        public void onComplete (@NonNull Task<Void> task)
//                        {
//                            if (task.isSuccessful())
//                            {
//                                reAuthenticate(email,password);
//                                pass_onAuth=false;
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    //bicycle method to apply changes in account
//    public void reAuthenticate (String email, String password)
//    {
//        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//        // Prompt the user to re-provide their sign-in credentials
//        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        Toast.makeText(getApplicationContext(), "signed_inreauth:" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void singIn (View view)
//    {
//        String email = mLogin_email.getText().toString();
//        String password = mLogin_pass.getText().toString();
//        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
//        {
//            @Override
//            public void onComplete (@NonNull Task<AuthResult> task)
//            {
//                if (!task.isSuccessful())
//                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void register_click (View view)
//    {
//        FragmentManager manager = getSupportFragmentManager();
//        Dialog_Register mDialog_Register = new Dialog_Register();
//        mDialog_Register.show(manager, "dialog");
//    }
//
//
}
//
