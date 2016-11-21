package com.piter.piterdiplomna3;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Piter on 20/11/2016.
 */

public class MyRelativeLayout extends RelativeLayout {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public MyRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
                if (getChildCount() != oldCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            oldCount = getChildCount();
            params = getLayoutParams();
            params.height = (getChildCount() * height)+80;
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }

}
