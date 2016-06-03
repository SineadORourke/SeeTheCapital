package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;


//Splash Screen
public class SplashActivity extends Activity {

    MediaPlayer splashSound;

    Thread timer = new Thread(){
        public void run(){                                                                //create a timer
            try {
                sleep(6500);                                                                //let the splash screen run for 6 seconds
            } catch (InterruptedException e) {                                              //incase there is an error
                e.printStackTrace();
            } finally {                                                                     //when splash screen is finished
                Intent intent = new Intent("com.example.sinead.seethecapital.MainMenu");    //go to the MainMenu
                startActivity(intent);
                finish();                                                                   //close the Splash screen completely
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NotificationManager man = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //create notification manager to receive the incoming intent
        int ID = getIntent().getIntExtra("ID", 0);                                          //receive intent (0 is a default ID incase no ID was found)
        man.cancel(ID);                                                                     //get rid of the notification (as it is now finished)

        timer.start();                                                                      //start the timer when app is opened
        splashSound = MediaPlayer.create(this, R.raw.sound);                                /*Gets our sound-file from res/raw/sound.ogg */
        splashSound.start();                                                                //Starts our sound
    }
}