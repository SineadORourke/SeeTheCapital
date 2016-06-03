package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//Main menu that shows the holiday database as a listview (first activity after the Splash screen)
public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final ListView listview = (ListView)findViewById(R.id.showHoliday);         //declare a listview and link it to the main menu xml file

        Holiday info = new Holiday (this);                                          //make a new instance of the database
        info.open();                                                                //open the database
        String data[] = info.getDestination();                                      //get a String array version of all the holiday names
        info.close();                                                               //close the database

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowlayout, R.id.holidayRowName, data);   //declare a new array adapter (with custom row layout)
        listview.setAdapter(adapter);                                                                              //populate the listview with the String array

        //When a holiday on the list is clicked
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String dest = String.valueOf(parent.getItemAtPosition(position));                           //get a string version of the item that was clicked (i.e. the holidayName)
                        Intent intent1 = new Intent("com.example.sinead.seethecapital.HolidayProfilePage");         //go to the HolidayProfilePage activity
                        intent1.putExtra("holidayName", dest);                                                      //send the holidayName as an extra with the intent
                        startActivity(intent1);
                        finish();                                                                                   //finish the MainMenu activity
                    }
                }
        );

    }


    //This brings the user to the simulated bank account when the "Your Bank a/c" button is pressed
    public void toBankActivity(View view) {
        Intent intent = new Intent("com.example.sinead.seethecapital.BankActivity");
        startActivity(intent);
    }

    //This brings the user to AddHolidayActvity.java so they can add a new holiday to the listview/database
    public void toAddHolidayActivity(View view) {
        Intent intent = new Intent("com.example.sinead.seethecapital.AddHolidayActivity");
        startActivity(intent);
        finish();
    }

    //This brings the user to DeleteHolidayActivity.java so they can delete a holiday from the database
    public void toDeleteHolidayActivity(View view){
        Intent intent = new Intent("com.example.sinead.seethecapital.DeleteHolidayActivity");
        startActivity(intent);
    }


    //when the main menu question mark is clicked, show the user some information about the app
    public void menuDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(MainMenu.this).create();                  //create a new alert dialog
        alertDialog.setTitle("Welcome to SeeTheCapital");                                           //set the dialog title and text
        alertDialog.setMessage("Now is the time to start keeping track of where you want to go and the money you need to get you there! After you add details about a holiday you intend to go on, click on that holiday in the main menu to get to it's holiday profile page. Happy Planning!");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }


}
