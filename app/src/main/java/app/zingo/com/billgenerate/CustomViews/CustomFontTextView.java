/*
package app.zingo.com.billgenerate.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import app.zingo.com.billgenerate.R;


*/
/**
 * Created by CSC on 11/2/2017.
 *//*


public class CustomFontTextView extends android.support.v7.widget.AppCompatTextView {
    String customFont;

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_fontName, 0);
        int fontName = 0;
        switch (cf)
        {
            case 1:
                fontName = R.string.Roboto_Bold;
                break;
            case 2:
                fontName = R.string.Roboto_Italic;
                break;
            case 3:
                fontName = R.string.Roboto_Light;
                break;
            case 4:
                fontName = R.string.Roboto_Medium;
                break;
            case 5:
                fontName = R.string.Roboto_Regular;
                break;
            case 6:
                fontName = R.string.Roboto_Thin;
            break;
            case 7:
                fontName = R.string.arial;
                break;
            case 8:
                fontName = R.string.ostrich_regular;
                break;
            case 9:
                fontName = R.string.arial_bold;
                break;
            case 10:
                fontName = R.string.arial_rounded_mt;
                break;
            default:
                fontName = R.string.Roboto_Regular;
                break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + customFont + ".ttf");
        if(tf == null)
        {
            tf = Typeface.createFromAsset(context.getAssets(),
                    "font/" + customFont + ".TTF");
        }
        setTypeface(tf);
        a.recycle();
    }
}
*/
