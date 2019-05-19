package com.devaom.limjg.gproj;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "NanumBarunpenR.ttf")).addBold(Typekit.createFromAsset(this, "NanumBarunpenB.ttf"));
    }
}
