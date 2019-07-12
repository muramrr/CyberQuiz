package com.mmdev.cyberquiz;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmdev.cyberquiz.data.GameContentViewModel;
import com.mmdev.cyberquiz.fragments.fragment_Play;
import com.mmdev.cyberquiz.utils.TinyDB;
import com.mmdev.cyberquiz.utils.common;

import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by Anderson on 25.09.2017.
 */

public class Activity_Main extends AppCompatActivity
{

    private TextView mHints_tv;
    private TinyDB tinydb;
    private FragmentManager fm;
    private String game;
    private int level;
    private Toolbar toolbar;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHints_tv = findViewById(R.id.tv_Hints);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });
        fm = getSupportFragmentManager();
        if (findViewById(R.id.main_container) != null)
        {
            if (savedInstanceState != null) return;
            fm.beginTransaction().add(R.id.main_container, new fragment_main()).commit();
        }
        tinydb = getTinyDb();
        GameContentViewModel model = ViewModelProviders.of(this).get(GameContentViewModel.class);
        model.init();
    }

    public void play_Click (View view)
    {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_left, R.anim.fragment_enter_from_left, R.anim.fragment_exit_to_right);
        ft.replace(R.id.main_container,new fragment_Play());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void options_Click (View view)
    {
        int total_completed = tinydb.getInt("total_completed_count");
        int total_players = common.playersTotal(getApplicationContext());
        int hints_used = tinydb.getInt("hints_used");
        Dialog dialog = new Dialog(this){    // Tap anywhere to close dialog.
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                this.dismiss();
                return true;
            }
        };
        dialog.setContentView(R.layout.dialog_options);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0.87f);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((TextView)dialog.findViewById(R.id.stats_completed_players)).setText(String.format(Locale.getDefault(),"Players completed: %d/%d",total_completed,total_players));
//        ((TextView)dialog.findViewById(R.id.stats_completed_levels)).setText(String.format(Locale.getDefault(),"Levels completed: %d/%d",completed,total_per_game));
        ((TextView)dialog.findViewById(R.id.stats_hints_used)).setText(String.format(Locale.getDefault(),"Hints used: %d",hints_used));
        dialog.show();
    }

//    public void toolbar_name_click (View view)
//    {
//        FragmentManager manager = getSupportFragmentManager();
//        Dialog_Profile mDialog_Profile_tb = new Dialog_Profile();
//        mDialog_Profile_tb.show(manager, "dialog");
//    }

    public void rate_Click (View view)
    {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    public void reset_progress (View view)
    {
        tinydb.clear();
        mHints_tv.setText(String.valueOf(tinydb.getInt("Hints")));

        //tinydb.remove(global.getGame()+ "_level_ + global.getLevel()); //use inside activity with game+level choosen
    }

    @Override
    protected void onResume ()
    {
        super.onResume();
        mHints_tv.setText(String.valueOf(tinydb.getInt("Hints")));
    }

    @Override
    public void onBackPressed ()
    {
        if (fm.getBackStackEntryCount()==1)
        {
            toolbar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.toolbar_up));
            Handler handler = new Handler();

            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    toolbar.setVisibility(View.GONE);
                }
            }, 200);
        }
        super.onBackPressed();
    }

    public static class fragment_main extends Fragment
    {
        @Nullable
        @Override
        public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.fragment_main,
                    container, false);
        }
    }

    public TinyDB getTinyDb ()
    { if (tinydb==null) tinydb = new TinyDB("general", getApplicationContext());
    return tinydb; }

    public String getGame ()
    { return game; }

    public void setGame (String aGame)
    { this.game = aGame; }

    public int getLevel ()
    { return level; }

    public void setLevel (int aLevel)
    { this.level = aLevel; }

    public Toolbar getToolbar(){
        return toolbar;}
}
