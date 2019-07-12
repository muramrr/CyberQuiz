package com.mmdev.cyberquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.adapters.list_levels_List_adapter;
import com.mmdev.cyberquiz.utils.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class fragment_List_levels extends Fragment
{
    private Activity_Main activity;
    private list_levels_List_adapter list_levelsListAdapter;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_levels,
                container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity()!=null)
            activity = (Activity_Main) getActivity();
        String game = activity.getGame();
        ((TextView) activity.findViewById(R.id.tv_CurrentWindow)).setText(game.toUpperCase());
        fm = activity.getSupportFragmentManager();
        list_levelsListAdapter = new list_levels_List_adapter(activity,common.jsonTotalLevels(getContext(),game),fm);
        ListView listView = view.findViewById(R.id.list_levels);
        listView.setAdapter(list_levelsListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                if (view.isEnabled())
                {
                    activity.setLevel(position + 1);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_left, R.anim.fragment_enter_from_left, R.anim.fragment_exit_to_right);
                    ft.replace(R.id.main_container, new fragment_Level_view());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        list_levelsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        ((TextView) activity.findViewById(R.id.tv_CurrentWindow)).setText("CHOOSE GAME");
        (activity.findViewById(R.id.tv_Hints)).setVisibility(View.GONE);
    }
}
