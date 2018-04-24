package com.pritesh.androidappratingtogooglestore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kila.apprater_dialog.lars.AppRater;
import com.kobakei.ratethisapp.RateThisApp;
import com.sbstrm.appirater.Appirater;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity
{

    RelativeLayout rootLayout;
    //https://github.com/kobakei/Android-RateThisApp

    //OTHER :   https://android-arsenal.com/tag/84
    //          1) https://github.com/Angtrim/Android-Five-Stars-Library
    //          2) https://android-arsenal.com/details/1/5449
    //          3) https://github.com/hotchemi/Android-Rate
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //showApplicationRatingDialog(this);
        setContentView(R.layout.activity_main);
        //myAppRate();
        //myNewAppRate();
        newAppRate();

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        //new AppRater.DefaultBuilder(this, "com.kila.addnotification.lars").showDefault().appLaunched();


        rootLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                //startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));

                boolean flag = NotificationsUtils.isNotificationEnabled(MainActivity.this);
                if(flag)
                {
                    Log.d("PUSHNotifications","ON-1");
                }
                else
                {
                    Log.d("PUSHNotifications","OFF-1");
                    openNotificationScreen();
                }

                if(NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled())
                {
                    Log.d("PUSHNotifications","ON-2");
                }
                else
                {
                    Log.d("PUSHNotifications","OFF-2");
                }
            }
        });
    }

    private void openNotificationScreen()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void myAppRate()
    {
        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);

        // Custom condition: 3 days and 5 launches
        //RateThisApp.Config config = new RateThisApp.Config(3, 5);
        //RateThisApp.init(config);

        RateThisApp.Config config = new RateThisApp.Config(3,2);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        config.setUrl("http://www.google.com");
        RateThisApp.init(config);

        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                Toast.makeText(MainActivity.this, "Yes event", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoClicked() {
                Toast.makeText(MainActivity.this, "No event", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClicked() {
                Toast.makeText(MainActivity.this, "Cancel event", Toast.LENGTH_SHORT).show();
            }
        });

        //If you want to stop showing the rate dialog, use this method in your code.
        //RateThisApp.stopRateDialog(this);
    }

    public void showApplicationRatingDialog(Context context)
    {
        Log.d("APP_RATE", context.getPackageName());
        new AppRater.DefaultBuilder(context, context.getPackageName())
                .showDefault()
                .timesToLaunch(4)
                .daysToWait(3)
                .title("Rate Notify me")
                .notNowButton(null)
                .rateButton("I want to rate")
                .appLaunched();
    }

    private void myNewAppRate()
    {
        //https://android-arsenal.com/details/1/846
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    private void newAppRate()
    {
        Appirater.appLaunched(this);
    }
}
