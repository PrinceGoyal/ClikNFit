package com.cliknfit.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by katrina on 21/07/17.
 */

public class RegulerText extends android.support.v7.widget.AppCompatTextView {

    public RegulerText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RegulerText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RegulerText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface face= Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.otf");
        this.setTypeface(face);
    }
}

