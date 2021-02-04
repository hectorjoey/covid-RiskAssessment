package hector.developers.covid19riskassessment.Api;

import java.util.List;

import hector.developers.covid19riskassessment.model.Supervisor;
import hector.developers.covid19riskassessment.model.UserHealthData;
import hector.developers.covid19riskassessment.model.Users;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("users")
    Call<Users> createUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("age") String age,
            @Field("designation") String designation,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password,
            @Field("userType") String userType,
            @Field("gender") String gender,
            @Field("address") String address,
            @Field("state") String state,
            @Field("lga") String lga,
            @Field("supervisedBy") String supervisedBy);

    @FormUrlEncoded
    @POST("userHealthData")
    Call<UserHealthData> createUserHealthData(
            @Field("date") String date,

            @Field("feverSymptom") String feverSymptom,
            @Field("headacheSymptom") String headacheSymptom,
            @Field("sneezingSymptom") String sneezingSymptom,
            @Field("chestPainSymptom") String chestPainSymptom,
            @Field("bodyPainSymptom") String bodyPainSymptom,
            @Field("nauseaOrVomitingSymptom") String nauseaOrVomitingSymptom,
            @Field("diarrhoeaSymptom") String diarrhoeaSymptom,
            @Field("fluSymptom") String fluSymptom,
            @Field("soreThroatSymptom") String soreThroatSymptom,
            @Field("fatigueSymptom") String fatigueSymptom,

            @Field("newOrWorseningCough") String newOrWorseningCough,
            @Field("difficultyInBreathing") String difficultyInBreathing,
            @Field("lossOfOrDiminishedSenseOfSmell") String lossOfOrDiminishedSenseOfSmell,
            @Field("lossOfOrDiminishedSenseOfTaste") String lossOfOrDiminishedSenseOfTaste,

            @Field("contactWithFamily") String contactWithFamily,

            @Field("userId") Long userId,
            @Field("firstname") String firstname,
            @Field("phone") String phone,
            @Field("risk") String risk

    );

    //the users login call
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("login")
    Call<Users> login(
            @Field("email") String email,
            @Field("password") String password
    );

    //fetching all users
    @GET("users")
    Call<List<Users>> getUsers();

    //fetching all user health data
    @GET("userHealthData")
    Call<List<UserHealthData>> getUserHealthData();

    //fetching users by Id
    @GET("users/{id}")
    Call<List<Users>> getUsersById(@Path("id") Long id);

    //supervisors Login
    @FormUrlEncoded
    @POST("supLogin")
    Call<Supervisor> supLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    

    //create supervisor
    @FormUrlEncoded
    @POST("supervisors")
    Call<ResponseBody> createSupervisor(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password,
            @Field("userType") String userType,
            @Field("state") String state,
            @Field("designation") String designation);

    //fetching users by supervisor Id
    @GET("user/{supervisorId}")
    Call<List<Users>> getUsersBySupervisorId(@Path("supervisorId") String supervisorId);

    //fetching user health data
    @GET("userHealthData/{userId}")
    Call<List<UserHealthData>> getUserHealthByUserId(@Path("userId") Long userId);

    //fetching all supervisors
    @GET("supervisors")
    Call<List<Supervisor>> getSupervisors();

    @GET("userHealthdata/{userId}")
    Call<List<UserHealthData>> getUserHealthDataBySupervisorId(@Path("userId") String userId);

}