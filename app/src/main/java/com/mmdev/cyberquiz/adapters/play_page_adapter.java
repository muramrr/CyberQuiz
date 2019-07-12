package com.mmdev.cyberquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.fragments.fragment_List_levels;
import com.mmdev.cyberquiz.utils.TinyDB;
import com.mmdev.cyberquiz.utils.common;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

public class play_page_adapter extends PagerAdapter
{
    private Context context;
    private Button mButtonGame;
    private TextView mTVGame;
    private TextView mTVTotalPlayers;
    private RoundCornerProgressBar mProgressBar;
    private FragmentManager fm;

    public play_page_adapter (Context context, FragmentManager fm) { this.context = context; this.fm = fm; }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.pager_game, collection, false);
        mButtonGame = layout.findViewById(R.id.but_game);
        mTVGame = layout.findViewById(R.id.tv_game);
        mTVTotalPlayers = layout.findViewById(R.id.pager_tv_total_players);
        mProgressBar = layout.findViewById(R.id.progress_bar);
        final Activity_Main activity = (Activity_Main) context;
        TinyDB tinydb = activity.getTinyDb();
        updateUI(position,tinydb,context);
        collection.addView(layout);
        mButtonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_enter_from_right, R.anim.fragment_exit_to_left, R.anim.fragment_enter_from_left, R.anim.fragment_exit_to_right);
                ft.replace(R.id.main_container,new fragment_List_levels());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return layout;
    }

    private void updateUI(int position, TinyDB tinydb,Context context)
    {
        String game;
        String game_upper;
        int color;
        int completed;
        int total_per_game;
        switch (position)
        {
            case 0:
                game = "csgo";
                game_upper = game.toUpperCase();
                completed = tinydb.getInt(game+"completed_count");
                total_per_game = common.playersPerGame(context,game);
                color = context.getResources().getColor(R.color.material_red_500);
                mButtonGame.setText(game_upper);
                mProgressBar.setProgress(100*completed/total_per_game);
                mProgressBar.setProgressColor(color);
                mTVGame.setText(game.toUpperCase());
                mTVGame.setTextColor(color);
                mTVTotalPlayers.setText(String.format(Locale.getDefault(),"Completed: %d/%d",completed,total_per_game));
                break;
            case 1:
                game = "dota";
                game_upper = game.toUpperCase();
                mTVGame.setText(game_upper);
                mButtonGame.setText(game_upper);

//                completed = tinydb.getInt(game+"completed_count");
//                total_per_game = common.playersPerGame(context,game);

                color= context.getResources().getColor(R.color.material_blue_500);
                mTVGame.setTextColor(color);
                mProgressBar.setProgressColor(color);

                mButtonGame.setVisibility(View.GONE);
//                mProgressBar.setProgress(100*completed/total_per_game);

                //mTVTotalPlayers.setText(String.format(Locale.getDefault(),"Completed: %d/%d",completed,total_per_game));
                mTVTotalPlayers.setText("Coming soon");
                break;
            case 2:
                game = "pubg";
                game_upper = game.toUpperCase();
                mTVGame.setText(game_upper);
                mButtonGame.setText(game_upper);

//                completed = tinydb.getInt(game+"completed_count");
//                total_per_game = common.playersPerGame(context,game);

                color= context.getResources().getColor(R.color.material_purple_400);
                mTVGame.setTextColor(color);
                mProgressBar.setProgressColor(color);

                mButtonGame.setVisibility(View.GONE);
//                mProgressBar.setProgress(100*completed/total_per_game);


                //mTVTotalPlayers.setText(String.format(Locale.getDefault(),"Completed: %d/%d",completed,total_per_game));
                mTVTotalPlayers.setText("Coming soon");
                break;
            case 3:
                game = "lol";
                game_upper = game.toUpperCase();
                mTVGame.setText(game_upper);
                mButtonGame.setText(game_upper);

//                completed = tinydb.getInt(game+"completed_count");
//                total_per_game = common.playersPerGame(context,game);

                color = context.getResources().getColor(R.color.material_lime_500);
                mTVGame.setTextColor(color);
                mProgressBar.setProgressColor(color);

                mButtonGame.setVisibility(View.GONE);
//                mProgressBar.setProgress(100*completed/total_per_game);

                //mTVTotalPlayers.setText(String.format(Locale.getDefault(),"Completed: %d/%d",completed,total_per_game));
                mTVTotalPlayers.setText("Coming soon");
                break;
            default:
                break;
        }
    }

    @Override
    public void destroyItem(@Nullable ViewGroup collection, int position, @Nullable Object view) {
        if(collection!=null&& view!=null)
            collection.removeView((View)view);
    }

    @Override
    public int getCount ()
    { return 4; }

    @Override
    public boolean isViewFromObject(@Nullable View view, @Nullable Object object) { return view == object; }

}
