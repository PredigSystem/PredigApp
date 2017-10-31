package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import predigsystem.udl.org.predigsystem.R;

public class BTConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnection);

        Button btn = findViewById(R.id.btnSynchronize);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BloodPressureMeasurementActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
