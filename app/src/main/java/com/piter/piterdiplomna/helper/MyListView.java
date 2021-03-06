package com.piter.piterdiplomna.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by Piter on 20/11/2016.
 */

public class MyListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public MyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            if(getChildCount()>0) {
                int height = getChildAt(0).getHeight() + 1;
                oldCount = getChildCount();//getCount();
                params = getLayoutParams();
                params.height = (getCount() * height);
                setLayoutParams(params);
                Log.d("TAG", "onDraw: iz4isliha se decata");
            }
            else{
                oldCount = getChildCount();//getCount();
                params = getLayoutParams();
                params.height = 1;
                setLayoutParams(params);
            }
        }

        super.onDraw(canvas);
    }

}