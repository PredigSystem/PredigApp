package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.LogIn;
import predigsystem.udl.org.predigsystem.JavaClasses.Session;
import predigsystem.udl.org.predigsystem.JavaClasses.UserID;
import predigsystem.udl.org.predigsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LogInActivity extends AppCompatActivity {
    private final static String USER_INFO = "userInfo";
    EditText user;
    EditText password;
    SQLiteDatabase db;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        db = predigAppDB.getReadableDatabase();

        Button btn = findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = findViewById(R.id.input_email);
                password = findViewById(R.id.input_password);
                authorize(user, password);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void authorize (EditText puser, EditText ppassword) {
        String suser = puser.getText().toString();
        String spassword = ppassword.getText().toString();
        LogIn logIn = new LogIn(suser, spassword);

        PredigAPIService service = APIConnector.getConnection();

        service.logIn(logIn).enqueue(new Callback<UserID>() {
            @Override
            public void onResponse(Call<UserID> call, Response<UserID> response) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                SharedPreferences.Editor editor = getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
                editor.putString("uid", response.body().getUserid());
                editor.apply();

                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserID> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "User or password incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
