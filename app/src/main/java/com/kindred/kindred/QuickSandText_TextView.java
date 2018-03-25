package com.kindred.kindred;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fahad Saleem on 3/21/2018.
 */

public class QuickSandText_TextView extends TextView {
    public QuickSandText_TextView(Context context) {
        super(context);
        init();
    }

    public QuickSandText_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickSandText_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QuickSandText_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.otf");
        setTypeface(tf ,1);

    }
}
