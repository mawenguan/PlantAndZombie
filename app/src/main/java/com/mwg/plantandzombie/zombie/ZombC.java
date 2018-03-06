package com.mwg.plantandzombie.zombie;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class ZombC extends Zomb {
    public ZombC(Context context) {
        super(context);

        speed = 3;
        HP = 8;
        zombBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zomb_2);
    }
}
