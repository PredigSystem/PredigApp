package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import predigsystem.udl.org.predigsystem.Fragments.ArticleFragment;
import predigsystem.udl.org.predigsystem.Fragments.CalendarFragment;
import predigsystem.udl.org.predigsystem.Fragments.DashboardFragment;
import predigsystem.udl.org.predigsystem.Fragments.HomeFragment;
import predigsystem.udl.org.predigsystem.Fragments.SettingsFragment;
import predigsystem.udl.org.predigsystem.R;

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Menu Items
        SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(0).withName(R.string.home).withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem dashboard = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.dashboard).withIcon(FontAwesome.Icon.faw_line_chart);
        SecondaryDrawerItem map = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.map).withIcon(FontAwesome.Icon.faw_map_marker);
        SecondaryDrawerItem calendar = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.calendar).withIcon(FontAwesome.Icon.faw_calendar);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.settings).withIcon(GoogleMaterial.Icon.gmd_settings);
        SecondaryDrawerItem faq = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.faq).withIcon(FontAwesome.Icon.faw_question);
        SecondaryDrawerItem logout = new SecondaryDrawerItem().withIdentifier(6).withName(R.string.logout).withIcon(FontAwesome.Icon.faw_sign_out);

        // Menu Header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Pau Balaguer").withEmail("pbl5@alumnes.udl.cat").withIcon(this.getResources().getDrawable(R.drawable.user))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        // Menu
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        home,
                        dashboard,
                        map,
                        calendar,
                        new SectionDrawerItem().withName(R.string.other),
                        settings,
                        faq,
                        logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment;
                        switch (position - 1){
                            case 0:
                                fragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, fragment)
                                        .commit();
                                break;
                            case 1:
                                fragment = new DashboardFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, fragment)
                                        .commit();
                                break;
                            case 2:
                                Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
                                startActivity(intentMap);
                                break;
                            case 3:
                                fragment = new CalendarFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, fragment)
                                        .commit();
                                break;
                            case 5:
                                fragment = new SettingsFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, fragment)
                                        .commit();
                                break;

                            case 6:
                                fragment = new ArticleFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, fragment)
                                        .commit();
                                break;

                            case 7:
                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                startActivity(intent);
                                finish();
                        }

                        return true;
                    }
                })
                .build();

        //When the app starts, HomeFragment is showed.
        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment)
                .commit();

    }
}
