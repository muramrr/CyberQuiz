package com.mmdev.cyberquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.common.primitives.Ints;
import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.adapters.level_view_Grid_adapter_img;
import com.mmdev.cyberquiz.data.GameContentViewModel;
import com.mmdev.cyberquiz.data.PlayerContent_Object;
import com.mmdev.cyberquiz.gameplay.write_answer;
import com.mmdev.cyberquiz.utils.common;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


public class fragment_Level_view extends Fragment
{
	private static String[] playersNames = null;
	private static int[] playersImgs = null;
	private GridView mGridView_img;
    private Activity_Main activity;
    private static boolean loaded = false;
    private GameContentViewModel model;
    private PlayerContent_Object pl;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_level_view,
                container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity()!=null)
            activity = (Activity_Main)getActivity();
        String game = activity.getGame();
        int level = activity.getLevel();
        ((TextView) activity.findViewById(R.id.tv_CurrentWindow)).setText(String.format(Locale.getDefault(),"Level %d",level));
        (activity.findViewById(R.id.tv_Hints)).setVisibility(View.GONE);
        final int completed[] = Ints.toArray(activity.getTinyDb().getListInt(game + "_level_" + level));
        playersNames = common.levelContent(activity,game,level).getPlayersNames();
        playersImgs = common.levelContent(activity,game,level).getPlayersImgs();
        mGridView_img = view.findViewById(R.id.GridView_images);

        level_view_Grid_adapter_img levelView_Grid_adapter_img = new level_view_Grid_adapter_img(activity, playersNames, playersImgs);

        if (playersNames != null && playersImgs != null)
            mGridView_img.setAdapter(levelView_Grid_adapter_img);
        mGridView_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                boolean already_guessed = false;
                for (int i : completed)
                    if (i == position)
                        already_guessed=true;

                pl = new PlayerContent_Object(playersNames[position],
                        playersImgs[position],position,playersNames.length,already_guessed);

                model.setPlayerContent(pl);
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_left, R.anim.fragment_enter_from_left, R.anim.fragment_exit_to_right);
                ft.replace(R.id.main_container,new write_answer());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(activity).get(GameContentViewModel.class);
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        if (!loaded)
            loaded = true;
        else
            mGridView_img.invalidateViews();
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        ((TextView) activity.findViewById(R.id.tv_CurrentWindow)).setText(activity.getGame().toUpperCase());
    }

}