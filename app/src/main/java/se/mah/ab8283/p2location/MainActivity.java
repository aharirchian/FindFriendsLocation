package se.mah.ab8283.p2location;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Controller controller;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = savedInstanceState;
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        controller = new Controller(this);
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }

    public Fragment getFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public void addFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commit();
    }
}
