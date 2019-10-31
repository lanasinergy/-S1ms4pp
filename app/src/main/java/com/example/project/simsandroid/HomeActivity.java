package com.example.project.simsandroid;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.simsandroid.ui.home.AddLeadFragment;
import com.example.project.simsandroid.ui.home.DetailLeadFragment;

public class HomeActivity extends AppCompatActivity implements AddLeadFragment.OnFragmentInteractionListener, DetailLeadFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String addlead = getIntent().getStringExtra("newLead");
        String detaillead = getIntent().getStringExtra("get_id");
        String edit = getIntent().getStringExtra("get_id_edit");
        if (addlead != null) {
            Fragment androidFragment = new AddLeadFragment();
            replaceFragment(androidFragment);
        } else if (detaillead != null) {
            Fragment androidFragment2 = new DetailLeadFragment();
            replaceFragment(androidFragment2);
        } else if (edit != null) {
            Fragment androidFragment3 = new AddLeadFragment();
            replaceFragment(androidFragment3);
        }
    }

    public void replaceFragment(Fragment destFragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.add_lead_fragment, destFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
