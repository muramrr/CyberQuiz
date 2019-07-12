package com.mmdev.cyberquiz.custom_design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Anderson on 22.09.2017.
 */

public class Custom_Button_AvenirNext_Medium extends Button
{
    //private int myButtonDrawable = R.drawable.main_buttons;
    public Custom_Button_AvenirNext_Medium (Context context) {
        super(context);
        applyCustomFont(context);
    }

    public Custom_Button_AvenirNext_Medium (Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundResource(myButtonDrawable);
        applyCustomFont(context);
    }

    public Custom_Button_AvenirNext_Medium (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/AvenirNext-Medium.otf", context);

        setTypeface(customFont);
    }
}

