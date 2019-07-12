package com.mmdev.cyberquiz.custom_design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Anderson on 24.09.2017.
 */

public class Custom_EditText_AvenirNext_Regular extends EditText
{
    //private int myEditTextDrawable = R.drawable.main_buttons;
    public Custom_EditText_AvenirNext_Regular (Context context) {
        super(context);
        applyCustomFont(context);
    }

    public Custom_EditText_AvenirNext_Regular (Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundResource(myButtonDrawable);
        applyCustomFont(context);
    }

    public Custom_EditText_AvenirNext_Regular (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/AvenirNext-Regular.otf", context);

        setTypeface(customFont);
    }
}

