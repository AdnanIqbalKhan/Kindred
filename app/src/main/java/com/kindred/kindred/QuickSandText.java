package com.kindred.kindred;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * Created by Fahad Saleem on 3/21/2018.
 */

public class QuickSandText extends TextInputEditText {
    public QuickSandText(Context context) {
        super(context);
        init();
    }
    public  QuickSandText(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public  QuickSandText(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs, defStyle);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.otf");
        setTypeface(tf ,1);
        setTextColor(getResources().getColor(R.color.colorPrimary));
        setTextSize(14);

    }


}
