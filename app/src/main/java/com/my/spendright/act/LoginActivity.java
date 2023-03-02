package com.my.spendright.act;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityLoginBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    ActivityLoginBinding binding;

    //Google SignIn
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;

    //FaceBook
    CallbackManager mCallbackManager;
    LoginButton loginButton;

    private SessionManager sessionManager;
    String newToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);

        sessionManager = new SessionManager(LoginActivity.this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            newToken = runnable.getToken();
            Log.e( "Tokennnn" ,newToken);
        });

        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }

        //Google SignIn
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //FaceBook
        loginButton = findViewById(R.id.connectWithFbButton);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "btnCancel", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onCancel");
                // ...
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Btnerrror", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });

        setUi();
    }

    private void setUi() {
        binding.RRGoogle.setOnClickListener(v -> {
            Intent intent1 = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent1, RC_SIGN_IN);
        });

        binding.txtForgot.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,ForogtPassword.class));
        });


        binding.RRFacebook.setOnClickListener(v -> {
            loginButton.performClick();
        });

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRlogin.setOnClickListener(v -> {
           Validation();
            //startActivity(new Intent(LoginActivity.this,LoginOne.class));
        });

        binding.txtRegistration.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this,Registration.class));

        });
    }

    //Google Login
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            handleSignInResult( result );
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String UserName=account.getDisplayName();
            String email=account.getEmail();
            String phone="";
            String SocialId=account.getIdToken();

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                SocialLoginMethod(SocialId,phone,email,UserName,"Google");
            }else {
                Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText( this, "Login Unsuccessful", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //   Toast.makeText(Activity_LoginOption.this, ""+token, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String UsernAME=user.getDisplayName();
                            String email=user.getEmail();
                            String SocialId=user.getUid();
                            String mobile=user.getPhoneNumber();
                            Uri Url=user.getPhotoUrl();

                            if (sessionManager.isNetworkAvailable()) {
                                binding.progressBar.setVisibility(View.VISIBLE);
                                SocialLoginMethod(SocialId,mobile,email,UsernAME,"Facebook");
                            }else {
                                Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }

                         /*   Socilal_FirstName=user.getDisplayName();
                            Socilal_last_name="";
                            Socilal_email=user.getEmail();
                            Socilal_mobile="";
                            Socilal_city="";
                            Socilal_address="";
                            Socilal_address2="";
                            Socilal_type="";
                            social_id=user.getUid();

                            social_image= String.valueOf(user.getPhotoUrl());
*/
                          /*  if (sessionManager.isNetworkAvailable()) {

                                binding.progressBar.setVisibility(View.VISIBLE);

                                ApISocialogin("Manager");

                            }else {

                                Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }
*/
                           // startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        } else {

                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Validation() {

        if(binding.edtEmail.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Email/mobile.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                loginMethod();
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginMethod(){
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_login(binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString(),newToken);
        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    LoginModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        sessionManager.saveUserId(finallyPr.getResult().getId());
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SocialLoginMethod(String SocilId,String mobile,String email,String Fname,String type){
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_social_login(SocilId,mobile,email,Fname,type,newToken,"75.00","75.00","img.png");
        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    LoginModel finallyPr = response.body();
                    String status = finallyPr.getStatus();

                    if (status.equalsIgnoreCase("1")) {
                        sessionManager.saveUserId(finallyPr.getResult().getId());
                        //startActivity(new Intent(Registration.this,RegistrationOne.class));
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, ""+finallyPr.getStatus(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
