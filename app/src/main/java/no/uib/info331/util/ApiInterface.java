package no.uib.info331.util;

import java.util.List;

import no.uib.info331.models.Beacon;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Fredrik V. Heimsæter
 */

public interface ApiInterface {

    /**
     * Authenticate the user and return all the info about the user.
     * @param username The user's username
     * @param password The user's password
     * @return The user-object for this user
     */
    @FormUrlEncoded
    @POST("/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    /**
     * Register a user and return the user-object for that user
     * @param username The user's username
     * @param password The user's password
     * @return The user-object for the registered user
     */
    @FormUrlEncoded
    @POST("/register")
    Call<User> register(@Field("username") String username, @Field("password") String password);

    /**
     * Get all the users from the database
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @return A list of all users
     */
    @GET ("/v1/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String credentials);

    /**
     * Search for a user by username
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param query What to search for
     * @return A list of user where username matches your query
     */
    @GET ("/v1/search/users/{username}")
    Call<List<User>> searchUserByUsername(@Header("Authorization") String credentials, @Path("username") String query);

    /**
     * Search for a group by name
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param query What to search for
     * @return A list of groups where the name matches your query
     */
    @GET ("/v1/search/groups/{groupname}")
    Call<List<Group>> searchGroupByName(@Header("Authorization") String credentials, @Path("groupname") String query);

    /**
     * Get a user by id
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param userId The user-id of the user to get
     * @return The user
     */
    @GET ("/v1/user/{id}")
    Call<User> getUserById(@Header("Authorization") String credentials, @Path("id") int userId);

    /**
     * Update a user. Might not work as intended.
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param user The updated user-object
     * @return The updated user-object from the database
     */
    @FormUrlEncoded
    @PUT ("/v1/user/{id}")
    Call<User> updateUserById(@Header("Authorization") String credentials, @Field("user") User user); //Don't know how put works yet. This may be wrong

    /**
     * Get all the groups from the database
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @return A list of all the groups
     */
    @GET ("/v1/groups")
    Call<List<Group>> getAllGroups(@Header("Authorization") String credentials);

    /**
     * Get a group by id
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupId The group-id of the group to get
     * @return The group
     */
    @GET ("/v1/group/{id}")
    Call<Group> getGroupById(@Header("Authorization") String credentials, @Field("id") int groupId);

    /**
     * Create a new group
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupName The name of the new group
     * @return The new Group-object from the database
     */
    @FormUrlEncoded
    @POST ("/v1/group")
    Call<Group> createGroup(@Header("Authorization") String credentials, @Field("Name") String groupName);

    /**
     * Update a group. Not shure if this works
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param group The update group object
     * @return The updated group-object from the database
     */
    @PUT ("/v1/group/{id}")
    Call<Group> updateGroupById(@Header("Authorization") String credentials, @Body Group group, @Path("id") int groupId); //Don't know how put works yet. This may be wrong

    /**
     * Delete a group
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupId The id of the group to delete
     * @return The response from the server
     */
    @DELETE ("/v1/group/{id}")
    Call<ResponseBody> deleteGroup(@Header("Authorization") String credentials, @Path("id") int groupId);

    /**
     * Get all the members of a group
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupId The id of the group
     * @return A list of users
     */
    @GET ("/v1/group/{id}/users")
    Call<List<User>> getGroupMembers(@Header("Authorization") String credentials, @Field("id") int groupId);

    /**
     * Add a user to a group
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupId The id of the group
     * @param userId The id of the user
     * @return The response from the server
     */
    @POST ("/v1/group/{groupid}/user/{userid}")
    Call<ResponseBody> addUserToGroup(@Header("Authorization") String credentials, @Path("groupid") int groupId, @Path("userid") int userId);

    /**
     * Remove a user from a group
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param groupId The id of the group
     * @param userId The id of the user
     * @return The response from the server
     */
    @DELETE ("/v1/group/{groupid}/user/{userid}")
    Call<ResponseBody> removeUserFromGroup(@Header("Authorization") String credentials, @Path("groupid") int groupId, @Path("userid") int userId);

    /**
     * Search for beacons by name
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param query What to search for.
     * @return A list of beacons where the name matches your query
     */
    @GET ("/v1/search/beacons/{beaconname}")
    Call<List<Beacon>> searchBeaconByName(@Header("Authorization") String credentials, @Path("beaconname") String query);

    /**
     * Get all the beacons
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @return The list of all beacons
     */
    @GET ("/v1/beacons")
    Call<List<Beacon>> getAllBeacons(@Header("Authorization") String credentials);

    /**
     * Get a beacon by it's id
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param beaconId The id of the beacon to get
     * @return The beacon
     */
    @GET ("/v1/beacon/{beaconid}")
    Call<Beacon> getBeaconById(@Header("Authorization") String credentials, @Path("beaconid") int beaconId);

    /**
     * Create a beacon
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param beacon The beacon to create
     * @return The new beacon from the server
     */
    @POST ("v1/beacons")
    Call<Beacon> createBeacon(@Header("Authorization") String credentials, @Field("beacon") Beacon beacon);

    /**
     * Update a beacon
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param beaconId The id of the beacon to update
     * @param beacon The updated beacon
     * @return The updated beacon from the server
     */
    @PUT ("v1/beacon/{beaconid}")
    Call<Beacon> updateBeacon(@Header("Authorization") String credentials, @Field("beaconid") int beaconId, @Body Beacon beacon);

    /**
     * Delete a beacon
     * @param credentials Base64 encoded credentials on the form "username:password"
     * @param beaconId The id of the beacon to delete
     * @return The response from the server
     */
    @DELETE ("/v1/beacon/{beaconid}")
    Call<ResponseBody> deleteBeaconById(@Header("Authorization") String credentials, @Field("beaconid") int beaconId);










}
