package predigsystem.udl.org.predigsystem.Api;

import java.util.List;

import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.JavaClasses.LogIn;
import predigsystem.udl.org.predigsystem.JavaClasses.UserID;
import predigsystem.udl.org.predigsystem.JavaClasses.VisitsDoctor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Pau on 10/12/17.
 */

public interface PredigAPIService {

    @GET("bloodPressure/{user}")
    Call<List<BloodPressure>> bloodPressureByUser(@Path("user") String user);

    @GET("bloodPressure/{user}/last")
    Call<BloodPressure> lastBloodPressureByUser(@Path("user") String user);

    @POST("bloodPressure")
    Call<BloodPressure> newBloodPressureToUser(@Body BloodPressure bloodPressure);

    @GET("visitsDoctor/{user}")
    Call<List<VisitsDoctor>> visitsDoctorByUser(@Path("user") String user);

    @GET("visitsDoctor/{user}/last")
    Call<VisitsDoctor> lastVisitsDoctorByUser(@Path("user") String user);

    @POST("user/logIn")
    Call<UserID> logIn(@Body LogIn logIn);
}
