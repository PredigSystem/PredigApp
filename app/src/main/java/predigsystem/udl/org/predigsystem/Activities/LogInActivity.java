package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.R;


public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();

        if(db != null){
            db.execSQL("INSERT INTO User (NIF, Password, Name, Email, Phone, Address, createdAt) VALUES ('00000000X', '1234', 'Root', 'root@udl.cat' ,'666666666', 'C/ Root 1234', '13/11/2017')");
        }

        Button btn = findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
