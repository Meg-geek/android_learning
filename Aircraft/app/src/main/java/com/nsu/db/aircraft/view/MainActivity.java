package com.nsu.db.aircraft.view;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.accounting.accounting.ProductAccountingMainFragment;
import com.nsu.db.aircraft.view.accounting.stage.StageMainFragment;
import com.nsu.db.aircraft.view.company.company.CompanyFragment;
import com.nsu.db.aircraft.view.company.guild.GuildMainFragment;
import com.nsu.db.aircraft.view.company.site.SiteMainFragment;
import com.nsu.db.aircraft.view.products.ProductsMainFragment;
import com.nsu.db.aircraft.view.staff.brigade.BrigadeMainFragment;
import com.nsu.db.aircraft.view.staff.staff.StaffMainFragment;
import com.nsu.db.aircraft.view.tests.equipment.EquipmentMainFragment;
import com.nsu.db.aircraft.view.tests.range.RangeMainFragment;
import com.nsu.db.aircraft.view.tests.test.TestMainFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        startFragment(new HomeFragment());
    }

    private void startFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.company_menu_item:
                startFragment(new CompanyFragment());
                break;
            case R.id.guild_menu_item:
                startFragment(new GuildMainFragment());
                break;
            case R.id.site_menu_item:
                startFragment(new SiteMainFragment());
                break;
            case R.id.staff_menu_item:
                startFragment(new StaffMainFragment());
                break;
            case R.id.brigades_menu_item:
                startFragment(new BrigadeMainFragment());
                break;
            case R.id.products_menu_item:
                startFragment(new ProductsMainFragment());
                break;
            case R.id.test_labs_menu_item:
                startFragment(new RangeMainFragment());
                break;
            case R.id.tests_menu_item:
                startFragment(new TestMainFragment());
                break;
            case R.id.equipment_menu_item:
                startFragment(new EquipmentMainFragment());
                break;
            case R.id.stages_menu_item:
                startFragment(new StageMainFragment());
                break;
            case R.id.accounting_menu_item:
                startFragment(new ProductAccountingMainFragment());
                break;
            default:
                startFragment(new HomeFragment());
        }
        return true;
    }

}
