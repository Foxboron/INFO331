package no.uib.info331.util;

import java.util.List;

import no.uib.info331.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by fredrik on 10/1/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/register")
    Call<ResponseBody> register(@Field("username") String username, @Field("password") String password);

    @GET ("/v1/users")
    Call<List<User>> users(@Header("Authorization") String credentials);


}
