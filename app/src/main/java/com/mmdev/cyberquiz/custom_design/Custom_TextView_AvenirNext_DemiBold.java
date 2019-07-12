package com.mmdev.cyberquiz.custom_design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Custom_TextView_AvenirNext_DemiBold extends androidx.appcompat.widget.AppCompatTextView {


    public Custom_TextView_AvenirNext_DemiBold (Context context) {
        super(context);
        applyCustomFont(context);
    }

    public Custom_TextView_AvenirNext_DemiBold (Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public Custom_TextView_AvenirNext_DemiBold (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/AvenirNext-DemiBold.otf", context);
        setTypeface(customFont);
    }
}