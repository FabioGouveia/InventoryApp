package com.example.android.inventoryapp.model;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

public class TouchableInputEditText extends TextInputEditText {

    public TouchableInputEditText(Context context) {
        super(context);
    }

    public TouchableInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
