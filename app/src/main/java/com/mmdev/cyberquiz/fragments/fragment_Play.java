package com.mmdev.cyberquiz.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.adapters.play_page_adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Anderson on 28.10.2017.
 */

public class fragment_Play extends Fragment
{

    private Activity_Main activity;
    public ViewPager viewPager;
    private play_page_adapter adapter;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //todo: DIALOG "CHOOSE CATEGORY TO BEGIN PLAY"
        return inflater.inflate(R.layout.fragment_play,
                container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity()!=null)
            activity = (Activity_Main)getActivity();
        toolbar = activity.findViewById(R.id.toolbar);

        if (toolbar.getVisibility()==View.GONE)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    toolbar.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.toolbar_down));
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setTitle("");
                }
            }, 100);

        }

        FragmentManager fm = activity.getSupportFragmentManager();
        adapter = new play_page_adapter(getContext(), fm);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        activity.setGame("csgo");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected (int position)
            {
                switch (position)
                {
                    case 0:
                        activity.setGame("csgo");
                        break;
                    case 1:
                        activity.setGame("dota");
                        break;
                    case 2:
                        activity.setGame("pubg");
                        break;
                    case 3:
                        activity.setGame("lol");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged (int state)
            {

            }
        });
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


}
