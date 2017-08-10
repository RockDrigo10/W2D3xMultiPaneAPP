package com.example.admin.multipane;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener,ListFragment.OnFragmentInteractionListener {
    private static final String LIST_FRAGMENT_TAG = "ListFragment";
    private static final String DETAIL_FRAGMENT_TAG = "DetailFragment";
    private static final String TAG = "main";
    ListFragment listFragment;
    FrameLayout flList,flDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        flList = (FrameLayout) findViewById(R.id.flList);
        flDetails = (FrameLayout) findViewById(R.id.flDetails);

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flList,listFragment,LIST_FRAGMENT_TAG)
                .addToBackStack(LIST_FRAGMENT_TAG)
                .commit();
    }
    @Override
    public void onFragmentInteraction(String string) {
        DetailFragment detailFragment = DetailFragment.newInstance(string,"");
        getSupportFragmentManager().beginTransaction()
            .add(R.id.flDetails,detailFragment,DETAIL_FRAGMENT_TAG)
            .addToBackStack(DETAIL_FRAGMENT_TAG)
            .commit();
        Log.d(TAG, "onFragmentInteraction: " + string);
    }
    @Override
    public void onFragmentInteraction(int inter) {
        Log.d(TAG, "onFragmentInteraction: "+ inter);
        listFragment.NotificationChanged();
    }
}
