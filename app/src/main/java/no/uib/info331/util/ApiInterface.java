package no.uib.info331.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @FormUrlEncoded
    @GET("/users")
    Call<ResponseBody> allUsers(@Field("username") String username, @Field("password") String password);


}
