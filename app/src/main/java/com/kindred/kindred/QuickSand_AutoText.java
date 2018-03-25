package com.kindred.kindred;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Fahad Saleem on 3/21/2018.
 */

@SuppressLint("AppCompatCustomView")
public class QuickSand_AutoText extends AutoCompleteTextView {
    public QuickSand_AutoText(Context context) {
        super(context);
        init();
    }

    public QuickSand_AutoText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickSand_AutoText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QuickSand_AutoText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public QuickSand_AutoText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
        init();
    }
    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.otf");
        setTypeface(tf ,1);

    }

}
