package no.uib.info331.queries;

import android.util.Base64;

import java.util.List;

import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moled on 05.10.2017.
 */

public class UserQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public void getUsersByStringFromDb(String query, String username, String password) {

        //username:password
        String credentials = username + ":" + password;

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        Call<List<User>> call = apiService.userSearch(basic, query);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(response.code()==200) {
                    for(User user: response.body()) {
                        System.out.println(user.getUsername());
                    }

                } else {
                    System.out.println("APA: " + response.body());
                    System.out.println("response: " + response);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

    }

    private void getAllUsersFromDb() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //username:password
        String credentials = "edd:edd";

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        Call<List<User>> call = apiService.allUsers(basic);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(response.code()==200) {
                    for(User user: response.body()) {
                        System.out.println(user.getUsername());
                    }

                } else {
                    System.out.println("APA: " + response.body());
                    System.out.println("response: " + response);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

    }


}
