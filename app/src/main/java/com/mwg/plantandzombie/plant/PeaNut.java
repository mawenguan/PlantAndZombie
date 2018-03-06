package com.mwg.plantandzombie.plant;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class PeaNut extends Plant {

    public PeaNut(Context context) {
        super(context);
        HP = 400;
        costSunCount = 125;
        plantBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plant_5);
    }
}
