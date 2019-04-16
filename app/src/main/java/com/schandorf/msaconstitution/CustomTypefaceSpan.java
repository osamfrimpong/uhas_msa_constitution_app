package com.schandorf.msaconstitution;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Schandorf on 8/24/2018.
 */

@SuppressLint("ParcelCreator")
public class CustomTypefaceSpan extends TypefaceSpan {
private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface newType) {
        super(family);
        this.newType = newType;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds,newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint,newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if(old == null)
        {
            oldStyle = 0;
        }
        else
        {
            oldStyle = old.getStyle();
        }
        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0)
        {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0)
        {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
