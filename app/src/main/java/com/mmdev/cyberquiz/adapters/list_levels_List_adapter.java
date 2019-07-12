package com.mmdev.cyberquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.fragments.fragment_Level_view;
import com.mmdev.cyberquiz.utils.TinyDB;
import com.mmdev.cyberquiz.utils.common;

import java.util.Locale;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//todo:holder pattern
public class list_levels_List_adapter extends BaseAdapter
{
    private Context context;
    private int number_of_levels;
    //int[] score;
    private static LayoutInflater inflater = null;
    private FragmentManager fm;


    public list_levels_List_adapter (Context context, int number_of_levels, FragmentManager fm)
    {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.number_of_levels = number_of_levels;
        this.fm = fm;

        //score = _score;
        inflater = (LayoutInflater) this.context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount () { return number_of_levels; } //count elements

    @Override
    public Object getItem (int position) { return position; } //_position

    @Override
    public long getItemId (int position) { return position; } //id

    private int lastPosition = -1;
    @Override //returning view of item in list
    public View getView (final int position, View childView, ViewGroup parent)
    {
        View view = childView;
        if (view == null) view = inflater.inflate(R.layout.item_list, parent, false);
        AbsListView.LayoutParams childLayoutParams = (AbsListView.LayoutParams) view.getLayoutParams();
        childLayoutParams.height = (parent.getHeight() / 3);
        view.setLayoutParams(childLayoutParams);
        ((TextView) view.findViewById(R.id.tvLevel_num)).setText(String.valueOf("Level " + (position+1)));
        final Activity_Main activity = (Activity_Main) context;
        TinyDB tinydb = activity.getTinyDb();

        //filling in tvCompleted

        //count of completed images per level
        int count_completed_per_level = tinydb.getListInt(activity.getGame() + "_level_" + (position+1)).size();
        //count of total images per level
        int total_images = common.levelContent(context,activity.getGame(),(position+1)).getPlayersNames().length;
        ((TextView) view.findViewById(R.id.tvCompleted)).setText(String.valueOf(count_completed_per_level +"/"+ total_images));
        //.tv

        //Toast.makeText(context, String.valueOf(total_images_all_levels), Toast.LENGTH_SHORT).show(); //test
        //count of summary images from all levels
        int total_images_all_levels = 0;
        for (int i = 0; i<number_of_levels;i++)
            total_images_all_levels += common.levelContent(context,activity.getGame(),(i+1)).getPlayersNames().length;
        int[] require_to_unlock = new int[number_of_levels];
        for (int i=0; i< require_to_unlock.length;i++)
            require_to_unlock[i] = Math.round(total_images_all_levels/10*i);
        int completed_count = tinydb.getInt(activity.getGame()+"completed_count");


        if ((completed_count < require_to_unlock[position]))
        {
            (view.findViewById(R.id.main_body)).setVisibility(View.INVISIBLE);
            int complete_to_unlock = require_to_unlock[position] - completed_count;
            ((TextView)(view.findViewById(R.id.tv_Locked)))
                    .setText(String.format(Locale.getDefault(),"Complete %d more to unlock",  complete_to_unlock));
            view.setEnabled(false);
        }
        else
        {
            (view.findViewById(R.id.main_body)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.frame_main_body)).setBackground(context.getResources().getDrawable(R.drawable.list_item_background_unlocked));
            (view.findViewById(R.id.iv_Locked)).setVisibility(View.GONE);
            (view.findViewById(R.id.tv_Locked)).setVisibility(View.GONE);
            view.findViewById(R.id.bStart).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view)
                {
                    activity.setLevel(position + 1);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_left, R.anim.fragment_enter_from_left, R.anim.fragment_exit_to_right);
                    ft.replace(R.id.main_container,new fragment_Level_view());
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
        }
	    //((TextView) view.findViewById(R.id.tvScore)).setText(String.valueOf(score[_position]));
        //Toast.makeText(context, String.valueOf(activity.getGame() + "_level_" + (_position+1)), Toast.LENGTH_SHORT).show(); //test
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.list_up_from_bottom : R.anim.list_down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
        return view;
    }


}
