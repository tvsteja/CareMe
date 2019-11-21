package com.careme.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class CommonUtils {

    private static final String GILL_SANS_TYPE_FACE			= "Gill Sans";
    private static final String MONTSERRAT_MEDIUM_TYPE_FACE  = "montserrat_medium";

    public static void applyFont(Context context, final View root) {
        try {
            String fontName = "";
            fontName = fontStyleName(fontName);
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i));
            }
            else if (root instanceof TextView){
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
            }
        } catch (Exception e) {
            Log.e("Care Me", String.format("Error occured when trying to apply %s font for %s view", root));
            e.printStackTrace();
        }
    }

    private static String fontStyleName(String name){
        String fontStyleName = "";
        switch (name){
            case GILL_SANS_TYPE_FACE:
                fontStyleName =  "gillsansstd.otf";
                break;
            case MONTSERRAT_MEDIUM_TYPE_FACE:
                fontStyleName =  "montserrat_medium.ttf";
                break;
            case "":
                fontStyleName =  "gillsansstd.otf";
                break;
        }
        fontStyleName =  "montserrat_medium.ttf";
        return fontStyleName;
    }

    public static void showSoftKeyboard(View view) {
        try {
            if (view.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
