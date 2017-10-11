package no.uib.info331.util;

import java.util.List;

import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by fredrik on 10/1/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/register")
    Call<User> register(@Field("username") String username, @Field("password") String password);

    @GET ("/v1/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String credentials);

    @GET ("/v1/search/{username}")
    Call<List<User>> searchUserByUsername(@Header("Authorization") String credentials, @Path("username") String query);

    @GET ("/v1/user/{id}")
    Call<User> getUserById(@Header("Authorization") String credentials, @Path("id") int id);

    @FormUrlEncoded
    @PUT ("/v1/user/{id}")
    Call<User> updateUserById(@Header("Authorization") String credentials, @Field("user") User user); //Don't know how put works yet. This may be wrong

    @GET ("/v1/groups")
    Call<List<Group>> getAllGroups(@Header("Authorization") String credentials);

    @GET ("/v1/group/{id}")
    Call<Group> getGroupById(@Header("Authorization") String credentials, @Field("id") int id);

    @FormUrlEncoded
    @POST ("/v1/group")
    Call<Group> createGroup(@Header("Authorization") String credentials, @Field("Name") String groupName); //Maybe this works

    @FormUrlEncoded
    @PUT ("/v1/group/{id}")
    Call<Group> updateGroupById(@Header("Authorization") String credentials, @Field("group") Group group); //Don't know how put works yet. This may be wrong

    @DELETE ("/v1/group/{id}")
    Call<ResponseBody> deleteGroup(@Header("Authorization") String credentials, @Path("id") int id);

    @GET ("/v1/group/{id}/users")
    Call<List<User>> getGroupMembers(@Header("Authorization") String credentials, @Field("id") int id);

    @POST ("/v1/group/{groupid}/user/{userid}")
    Call<ResponseBody> addUserToGroup(@Header("Authorization") String credentials, @Path("groupid") int groupId, @Path("userid") int userId);

    @DELETE ("/v1/group/{groupid}/user/{userid}")
    Call<ResponseBody> removeUserFromGroup(@Header("Authorization") String credentials, @Path("groupid") int groupId, @Path("userid") int userId);








}
