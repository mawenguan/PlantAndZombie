package com.mwg.plantandzombie.plant;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class IcePea extends Pea {

    public IcePea(Context context) {
        super(context);
        costSunCount = 175;
        bulletBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bullet_1);
        plantBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plant_3);
    }
}
