package com.imagepros.app.display;

/**
 * Created by Zhao on 2014-05-24.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.imagepros.app.ImagePros;
import com.imagepros.app.R;

//add scrollable or fade in/out effects later
public class Launcher_Splash_Screen extends Activity{
    private Thread mSplashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_splash_layout);
        final Launcher_Splash_Screen sPlashScreen = this;

        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){

                        wait(5000);
                    }
                }
                catch(InterruptedException ex){
                }

                finish();

                Intent intent = new Intent();
                intent.setClass(sPlashScreen, ImagePros.class);
                startActivity(intent);

            }
        };

        mSplashThread.start();
    }


    @Override

    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}

