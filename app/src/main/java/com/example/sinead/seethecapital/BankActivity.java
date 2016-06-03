package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



//Simulated bank account.
public class BankActivity extends Activity {

    //Declare class variables so they can be used by many methods in this class
    Button addBank, subtractBank;
    EditText inputBank;                 //user input box to declare how much the user wants to add or subtract from the bank
    TextView bankText;                  //this will show the current status of the bank account


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        addBank = (Button)findViewById(R.id.addBank);
        subtractBank = (Button)findViewById(R.id.subtractBank);
        inputBank = (EditText)findViewById(R.id.inputBank);
        bankText = (TextView)findViewById(R.id.balanceOutput);

        Holiday entry = new Holiday(BankActivity.this);                 //make a new instance of the database
        entry.open();                                                   //open the database
        String amount = entry.getAmount();                              //get the amount that is currently stored in the Bank table in the database
        bankText.setText("€" + amount);                                 //output the amount to the screen
        entry.close();                                                  //close the database
    }


    //Adding money to the Bank table in the database when the add button is clicked, and send an encouraging notification to the user
    public void addToBank(View view) {
        try{
            String input = inputBank.getText().toString();              //get String version of the user input
            int input2 = Integer.parseInt(input);                       //parse the user input to an int

            Holiday entry = new Holiday(BankActivity.this);             //make new instance of the database
            entry.open();                                               //open the database
            entry.addBankAccount(input2);                               //call the addition method to update the bank total
            String amount = entry.getAmount();                          //get the new amount that is stored in the Bank table
            bankText.setText("€" + amount);                             //output the amount to the screen
            inputBank.setText("");                                      //clear the editText box incase the user would like to add/subtract more
            entry.close();                                              //close the database

        } catch (Exception e) {                                         //incase there is an error, make a dialog pop up with the error
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();

        }finally{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(BankActivity.this);    //build a notification to send when money is added to the savings account

            notification.setSmallIcon(R.mipmap.ic_launcher);                                                //add a small icon
            notification.setWhen(System.currentTimeMillis());                                               //set time that the notification is sent
            notification.setContentTitle("Congrats! You added money to your savings account");              //set the title of notification
            notification.setContentText("Check your holiday savings progress with SeeTheCapital");          //set main text of notification

            Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);                     //add a larger icon for the actual notification
            notification.setLargeIcon(picture);

            Intent myIntent = new Intent();                                                                 //create new intent
            Context myContext = getApplicationContext();                                                    //get the application's context

            myIntent.setClass(myContext, SplashActivity.class);                                             //select current context, and what activity you want opened when the notification is clicked
            myIntent.putExtra("ID", 1);                                                                     //send extra information with the intent (the notification ID)
            PendingIntent myPendingIntent = PendingIntent.getActivity(myContext, 0, myIntent, 0);           //the intent that will be sent with the notification
            notification.setContentIntent(myPendingIntent);

            Notification not = notification.build();                                                        //create a notification object and set it equal to the built notification above
            NotificationManager man = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);         //create notification manager to display the notification on the phone
            man.notify(1, not);                                                                             //send notification

        }
    }


    //Subtracting money from the Bank table in the database when the subtract button is clicked
    public void subtractFromBank(View view) {
        try{
            String input = inputBank.getText().toString();              //get String version of the user input
            int input2 = Integer.parseInt(input);                       //parse the user input to an int

            Holiday entry = new Holiday(BankActivity.this);             //make new instance of the database
            entry.open();                                               //open the database
            entry.deleteBankAccount(input2);                            //call the subtraction method to update the bank total
            String amount = entry.getAmount();                          //get the new amount that is stored in the Bank table
            bankText.setText("€" + amount);                             //output the amount to the screen
            inputBank.setText("");                                      //clear the editText box incase the user would like to add/subtract more
            entry.close();                                              //close the database

        } catch (Exception e) {                                         //incase there is an error, make a dialog pop up with the error
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }
    }


    //when the bank activity question mark is clicked, show the user some information about what to enter
    public void bankDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(BankActivity.this).create();              //create a new alert dialog
        alertDialog.setTitle("Simulated Bank Account");                                             //set the dialog title and text
        alertDialog.setMessage("Please enter an amount and click either add or subtract. Watch as your bank balance changes.");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}

