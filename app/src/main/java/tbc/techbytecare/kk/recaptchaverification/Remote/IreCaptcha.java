package tbc.techbytecare.kk.recaptchaverification.Remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tbc.techbytecare.kk.recaptchaverification.Model.MyResponse;

public interface IreCaptcha {

    @FormUrlEncoded
    @POST("google_recaptcha.php")
    Call<MyResponse> validate(@Field("recaptcha-response") String response);
}
