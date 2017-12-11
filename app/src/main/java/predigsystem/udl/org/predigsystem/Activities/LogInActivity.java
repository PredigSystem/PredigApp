package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.Session;
import predigsystem.udl.org.predigsystem.R;


public class LogInActivity extends AppCompatActivity {

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


                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("nif", user.getText().toString());
                if (authorize(user, password)){
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "User or password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean authorize (EditText puser, EditText ppassword) {
        String suser = puser.getText().toString();
        String spassword = ppassword.getText().toString();

        if (suser.equals(""))suser="''";
        if (spassword.equals(""))spassword="''";

        Cursor consult = db.rawQuery("SELECT Email, Password FROM User WHERE NIF='"+suser+"' AND Password='"+spassword+"';", null);
        if(consult.moveToFirst()) return true;
        else return false;
    }
}
