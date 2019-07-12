package com.mmdev.cyberquiz.gameplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.utils.OnSwipeTouchListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Recycler_adapter_answer
        extends RecyclerView.Adapter<Recycler_adapter_answer.MyHolder>
{
    private char[] _submitted_answer;
    static private OnItemClickListener mClickListener;

    public Recycler_adapter_answer (char[] submitted_answer) { _submitted_answer = submitted_answer; }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_grid_textview, parent, false);
        ViewGroup.MarginLayoutParams childLayoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        childLayoutParams.width = (parent.getWidth() / 6);
        itemView.setLayoutParams(childLayoutParams);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder (@NonNull MyHolder holder, int position)
    {
        holder.textView.setText(String.format("%c", _submitted_answer[position]));
    }

    @Override
    public int getItemCount ()
    {
        return _submitted_answer.length;
    }

    static class MyHolder extends RecyclerView.ViewHolder
    {
        private final TextView textView;

        private MyHolder (@NonNull final View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_Letters);
            itemView.setTag(itemView);
            itemView.setOnTouchListener(new OnSwipeTouchListener(itemView.getContext())
            {
                public void onSwipeTop () { Toast.makeText(itemView.getContext(), "bottom", Toast.LENGTH_SHORT).show(); }

                public void onSwipeRight() { itemView.getRootView().findViewById(R.id.button_left).performClick();}
                public void onSwipeLeft() {itemView.getRootView().findViewById(R.id.button_right).performClick();}

                public void onSwipeBottom () { Toast.makeText(itemView.getContext(), "bottom", Toast.LENGTH_SHORT).show(); }

                @Override
                public void onClick ()
                {
                    super.onClick();
                    if (mClickListener != null) mClickListener.onItemClick(itemView.getRootView(), getAdapterPosition());
                }
            });
        }
    }

    // convenience method for getting data at click position
    char getLetter(int position) {
        return _submitted_answer[position];
    }

    // allows clicks events to be caught
    void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
