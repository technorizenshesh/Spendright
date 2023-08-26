package com.my.spendright.act;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;

import com.my.spendright.biomatriclogin.Utilitiesss;
import com.my.spendright.databinding.ActivityLoginBinding;
import com.my.spendright.utils.Constant;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity /*implements GoogleApiClient.OnConnectionFailedListener*/{

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
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;
    String keyvalue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);

        sessionManager = new SessionManager(LoginActivity.this);

       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            newToken = runnable.getToken();
            Log.e( "Tokennnn" ,newToken);
        });*/

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
         //   PreferenceConnector.writeString(Splash.this, PreferenceConnector.Firebash_Token, token);
            newToken = token;
            Log.e( "Tokennnn" ,newToken);
        });

/*
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
                .build();*/

     /*   //FaceBook
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
        });*/

        setUi();
    }

    private void setUi() {
      /*  binding.RRGoogle.setOnClickListener(v -> {
            Intent intent1 = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent1, RC_SIGN_IN);
        });*/

        binding.txtForgot.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,ForogtPassword.class));
        });


     /*   binding.RRFacebook.setOnClickListener(v -> {
            loginButton.performClick();
        });*/



        binding.llFingerPrint.setOnClickListener(v -> {
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

        executor = ContextCompat.getMainExecutor(LoginActivity.this);

        setPrompt();

        if(Utilitiesss.getInstance().isBiometricHardWareAvailable(LoginActivity.this)){
            binding.llFingerPrint.setVisibility(View.VISIBLE);
            initBiometricPrompt(
                    Constant.BIOMETRIC_AUTHENTICATION,
                    Constant.BIOMETRIC_AUTHENTICATION_SUBTITLE,
                    Constant.BIOMETRIC_AUTHENTICATION_DESCRIPTION,
                    false
            );
        }
        else {
            binding.llFingerPrint.setVisibility(View.GONE);
        }
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
                loginMethod("normal","");
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginMethod(String type,String biomatric){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<LoginModel> call = null;
             if(type.equalsIgnoreCase("normal"))  call = RetrofitClients.getInstance().getApi()
                .Api_login(binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString(),newToken);
               else call = RetrofitClients.getInstance().getApi()
                     .Api_login_biomatric(biomatric);
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

   private void initBiometricPrompt( String title, String subtitle, String description, Boolean setDeviceCred){
       if (setDeviceCred) {
            /*For API level > 30
              Newer API setAllowedAuthenticators is used*/
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              // int authFlag =  ;
               promptInfo = new BiometricPrompt.PromptInfo.Builder()
                       .setTitle(title)
                       .setSubtitle(subtitle)
                       .setDescription(description)
                       .setAllowedAuthenticators(BIOMETRIC_STRONG|DEVICE_CREDENTIAL )
                       .setNegativeButtonText(Constant.CANCEL)

                       .build();
           } else {
                /*SetDeviceCredentials method deprecation is ignored here
                  as this block is for API level<30*/
               promptInfo = new BiometricPrompt.PromptInfo.Builder()
                       .setTitle(title)
                       .setSubtitle(subtitle)
                       .setDescription(description)
                       .setDeviceCredentialAllowed(true)
                       .build();
           }
       } else {
           promptInfo = new  BiometricPrompt.PromptInfo.Builder()
                   .setTitle(title)
                   .setSubtitle(subtitle)
                   .setDescription(description)
                   .setNegativeButtonText(Constant.CANCEL)
                   .build();

       }

       binding.llFingerPrint.setOnClickListener(view -> {
        //  biometricPrompt.authenticate(promptInfo);

           BiometricPrompt.CryptoObject cryptoObject = null;
           try {
               cryptoObject = new BiometricPrompt.CryptoObject(getEncryptCipher(createKey()));
           } catch (NoSuchPaddingException e) {
               throw new RuntimeException(e);
           } catch (NoSuchAlgorithmException e) {
               throw new RuntimeException(e);
           } catch (InvalidKeyException e) {
               throw new RuntimeException(e);
           } catch (NoSuchProviderException e) {
               throw new RuntimeException(e);
           } catch (InvalidAlgorithmParameterException e) {
               throw new RuntimeException(e);
           }
           biometricPrompt.authenticate(promptInfo, cryptoObject);
       } );


   }

    private SecretKey createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String provider = "AndroidKeyStore";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
        KeyGenParameterSpec keyGenParameterSpec = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenParameterSpec = new KeyGenParameterSpec.Builder("MY_KEY", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(keyGenParameterSpec);
        }
       // Log.e("ciphrKey=====",keyGenerator.generateKey()+"");
        return keyGenerator.generateKey();
    }

    private Cipher getEncryptCipher(Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String blockMode = KeyProperties.BLOCK_MODE_CBC;
        String padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        Cipher cipher = Cipher.getInstance(algorithm+"/"+blockMode+"/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        Log.e("ciphrKey=====",key+"");
        Log.e("ciphrKey2=====",cipher+"");
        keyvalue = key+"";
        return cipher;
    }




    private void setPrompt() {
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this,Constant.AUTHENTICATION_ERROR + " " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
               // Toast.makeText(LoginActivity.this, Constant.AUTHENTICATION_SUCCEEDED , Toast.LENGTH_SHORT).show();
                //binding.textViewAuthResult.visibility = View.VISIBLE
               // Toast.makeText(LoginActivity.this, Constant.AUTHENTICATION_SUCCEEDED , Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Log.e("authresult=====", result.getCryptoObject().getCipher().getParameters().getProvider().keys() + "");
                    if (sessionManager.isNetworkAvailable()) {
                        loginMethod("finger", keyvalue);

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                    Log.e("authresult=====",result.getAuthenticationType()+"");
                  /*  byte[] encryptedInfo = new byte[0];
                    try {
                        encryptedInfo = result.getCryptoObject().getCipher().doFinal(
                                // plaintext-string text is whatever data the developer would like
                                // to encrypt. It happens to be plain-text in this example, but it
                                // can be anything
                                "sfsfsffssfs".getBytes(Charset.defaultCharset()));
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("MY_APP_TAG", "Encrypted information: " +
                            Arrays.toString(encryptedInfo));
                }*/

                }
                }




            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, Constant.AUTHENTICATION_FAILED , Toast.LENGTH_SHORT).show();


            }
        });

    }


/*    public void faceLogin(){
        String title ="";





        BiometricAuthRequest authFace    = new BiometricAuthRequest(BiometricApi.AUTO,
                BiometricType.BIOMETRIC_FACE,
                BiometricConfirmation.ANY);



        if (BiometricManagerCompat.isHardwareDetected(authFace) && BiometricManagerCompat.hasEnrolled(authFace)) {
            title = "Use your smiling face to enter the app" + authFace ;
        }
        else title ="";

        authFace.
    }*/



   /* //Google Login
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

                         *//*   Socilal_FirstName=user.getDisplayName();
                            Socilal_last_name="";
                            Socilal_email=user.getEmail();
                            Socilal_mobile="";
                            Socilal_city="";
                            Socilal_address="";
                            Socilal_address2="";
                            Socilal_type="";
                            social_id=user.getUid();

                            social_image= String.valueOf(user.getPhotoUrl());
*//*
                          *//*  if (sessionManager.isNetworkAvailable()) {

                                binding.progressBar.setVisibility(View.VISIBLE);

                                ApISocialogin("Manager");

                            }else {

                                Toast.makeText(LoginActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }
*//*
                           // startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        } else {

                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/




/*
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
*/

}
