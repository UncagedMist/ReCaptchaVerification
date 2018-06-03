package tbc.techbytecare.kk.recaptchaverification;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tbc.techbytecare.kk.recaptchaverification.Model.MyResponse;
import tbc.techbytecare.kk.recaptchaverification.Remote.IreCaptcha;
import tbc.techbytecare.kk.recaptchaverification.Remote.RetrofitClient;

public class MainActivity extends AppCompatActivity {


    Button btn_post;
    EditText edt_comment;

    IreCaptcha mService;

    private static final String SITE_KEY_CAPTCHA = "6LcEGVgUAAAAAONEsBNGLa7e1xvYFuIIWretkx4x";

    private static final String SECRET_KEY_CAPTCHA = "6LcEGVgUAAAAANsh-Hf4UAR9LKUgXJtMFIfM9uo2";

    private IreCaptcha getAPI() {
        return RetrofitClient.getClient("https://10.0.2.2/server_validate/").create(IreCaptcha.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = getAPI();

        btn_post = findViewById(R.id.btn_post);
        edt_comment = findViewById(R.id.edt_comment);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(edt_comment.getText().toString()))   {

                    SafetyNet .getClient(MainActivity.this)
                            .verifyWithRecaptcha(SITE_KEY_CAPTCHA)
                            .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                @Override
                                public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {

                                    if (!recaptchaTokenResponse.getTokenResult().isEmpty()) {
                                        verifyTokenOnServer(recaptchaTokenResponse.getTokenResult());
                                    }


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof ApiException)  {

                                        ApiException apiException = (ApiException)e;

                                        Log.d("ERROR", "Error : "+ CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                                    }
                                    else    {
                                        Log.d("ERROR", "Unknown Error");
                                    }
                                }
                            });

                }
                else    {
                    Toast.makeText(MainActivity.this, "Plz enter a comment to proceed!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyTokenOnServer(String tokenResult) {

        final AlertDialog dialog = new SpotsDialog(MainActivity.this);
        dialog.show();
        dialog.setMessage("Please wait..");

        mService.validate(tokenResult)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                        dialog.dismiss();

                        if (response.body().isSuccess())    {
                            Toast.makeText(MainActivity.this, "Comment Posted!!!", Toast.LENGTH_SHORT).show();
                        }
                        else    {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        dialog.dismiss();

                        Log.d("ERROR", t.getMessage());
                    }
                });
    }
}
