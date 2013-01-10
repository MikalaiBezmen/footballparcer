package com.argando.parcersample.data;

import com.argando.parcersample.R;

public enum Preferences {
    Green
            {
                @Override
                public int getResultListLayout() {
                    return R.layout.list_complex_green;
                }
            },
    Black
            {
                @Override
                public int getResultListLayout() {
                    return  R.layout.list_complex_black;
                }
            };

    public abstract int getResultListLayout();
}
