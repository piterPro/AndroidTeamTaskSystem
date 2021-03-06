package com.piter.piterdiplomna.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Piter on 20/11/2016.
 */
//used for comments relative height
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
            Log.d("TAG", "onDraw: params.height="+params.height);
            params.height = (getChildCount() * height)+230;//TODO: can i calculate exact height of all children?
            Log.d("TAG", "onDraw: params.height="+params.height);
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }

}
