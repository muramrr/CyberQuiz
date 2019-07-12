package com.mmdev.cyberquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.common.primitives.Ints;
import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.utils.TinyDB;

public class level_view_Grid_adapter_img extends BaseAdapter
{
    public static String[] _playersNameList;
    private Context context;
    public static int[] imageId;

    private static LayoutInflater inflater = null;


    public level_view_Grid_adapter_img (Context context, String[] playersNameList, int[] playersImgs)
    {
        _playersNameList = playersNameList;
        this.context = context;
        imageId = playersImgs;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount () { return _playersNameList.length; }

    @Override
    public Object getItem (int position) { return position; }

    @Override
    public long getItemId (int position) { return position; }

    static class ViewHolder
    {
        ImageView img;
        ImageView checker;
        Activity_Main activity;
        TinyDB tinydb;
        int completed[];
    }

    @Override
    public View getView (final int position, View childView, ViewGroup parent)
    {
        //pattern dlya luchei prokrutki grida
        ViewHolder holder;
        if (childView == null)
        {
            childView = inflater.inflate(R.layout.item_grid_image, parent,false);
            holder = new ViewHolder();
            holder.img = childView.findViewById(R.id.iv_smallpic);
            holder.checker = childView.findViewById(R.id.iv_checker);
            holder.activity = ((Activity_Main)context);
            holder.tinydb = holder.activity.getTinyDb();
            holder.completed = Ints.toArray(holder.tinydb.getListInt(holder.activity.getGame() + "_level_" + holder.activity.getLevel()));
            childView.setTag(holder);
        } else
            holder = (ViewHolder) childView.getTag();

        AbsListView.LayoutParams childLayoutParams = (AbsListView.LayoutParams) childView.getLayoutParams();
        childLayoutParams.height = (parent.getHeight() / 5);
        childLayoutParams.width = (parent.getWidth() / 3);
        childView.setLayoutParams(childLayoutParams);

        holder.img.setImageResource(imageId[position]);
        //pomechaem otgadanie
        if (ArrayUtils.contains(holder.completed,position))
        {
            holder.checker.setVisibility(View.VISIBLE);
            int highlightColor = context.getResources().getColor(R.color.transparency_for_completed);
            holder.img.setColorFilter(highlightColor);
        }
        else { holder.checker.setVisibility(View.INVISIBLE);
                holder.img.setColorFilter(null);}

        return childView;
    }

}