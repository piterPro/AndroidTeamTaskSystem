package com.piter.piterdiplomna3;

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
//            Log.d("TAG", "onDraw: getCount="+getCount()+"!= oldCount ="+oldCount);
            if(getChildCount()>0) {
//                Log.d("TAG", "onDraw: getCount= "+getCount());
                int height = getChildAt(0).getHeight() + 1;
                oldCount = getCount();
                params = getLayoutParams();
                params.height = (getCount() * height);
                setLayoutParams(params);
            }
            else{
//                Log.d("TAG", "onDraw: getChildCount= "+getChildCount());
                oldCount = getCount();
                params = getLayoutParams();
                params.height = 1;
                setLayoutParams(params);
            }
        }
//        else {
//            Log.d("TAG  ", "onDraw: Ne vliza nikude");
//            oldCount = getCount();
//        }

        super.onDraw(canvas);
    }

}