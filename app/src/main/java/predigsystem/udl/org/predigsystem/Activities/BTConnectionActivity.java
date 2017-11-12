package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import predigsystem.udl.org.predigsystem.R;

public class BTConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnection);

        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_host);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                if (tabId == R.id.tab_favourites) {
                    intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (tabId == R.id.tab_home) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
