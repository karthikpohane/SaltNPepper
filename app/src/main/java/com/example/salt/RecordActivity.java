package com.example.salt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class RecordActivity extends AppCompatActivity implements RecordFragment.Callbacks {
    public static final String TAG = RecordActivity.class.getSimpleName();

    private RecordingListFragment mRecordingListFragment;

    @Override
    public void onRecordingSaved() {
        // when a new recording is saved in the RecordFragment, it calls this method in the Hosting
        // Activity which notifies the soundListRecyclerView adapter of a data change
        // thereby updating the list
//        RecyclerView soundListRecyclerView = findViewById(R.id.recycler_view);
//        soundListRecyclerView.getAdapter().notifyDataSetChanged();
        if (mRecordingListFragment != null) {
            mRecordingListFragment.reloadList();
            Log.v(TAG, "Reload list called");
            Toast.makeText(this, "List Reloaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "List could not be reloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //Back Button...
        ImageView back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
                startActivity(intent);
                finish();

            }
        });

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: return new RecordFragment();
                    case 1: {
                        mRecordingListFragment = new RecordingListFragment();
                        return mRecordingListFragment;
                    }
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0: return getString(R.string.home_tab_label);
                    case 1: return getString(R.string.sound_list_tab_label);
                    default: return null;
                }
            }
        });

        // this sets the RecordFragment as the default Fragment to show
        viewPager.setCurrentItem(0);

        // reference the tabBar and setup with viewPager
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.sound_item_context_menu, menu);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
        startActivity(intent);
        finish();
    }

}

