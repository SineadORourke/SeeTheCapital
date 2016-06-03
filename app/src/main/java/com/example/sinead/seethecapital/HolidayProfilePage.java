package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//The main holiday profile page (when a user clicks on a holiday in the main menu), and here you can go to many other activities using the navigation drawer
public class HolidayProfilePage extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    //Declare class variables so they can be used by many methods in this class
    private String holidayName = "";                    //holds the name of the chosen destination (sent by intent extra)
    private TextView holidayText, holidaycountdown;

    private String sleeps="";                           //holds the amount of sleeps left before the holiday
    private String today="";                            //holds the current date the app is being used
    private String departure="";                        //holds the departure date of the selected holiday


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_profile_page);

        Bundle holidayData = getIntent().getExtras();                       //receive the extra information passed through intent
        holidayName = holidayData.getString("holidayName");                 //save extra information in class variable

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //sets the navigation side drawer
        navigationView.setNavigationItemSelectedListener(this);



        holidaycountdown = (TextView)findViewById(R.id.holidaycountdown);
        holidayText = (TextView)findViewById(R.id.holidayNameText);

        setDates();                                                         //gets the current date and the departure date
        getDateDiff();                                                      //calculates how many days are between the two dates
        holidaycountdown.setText(sleeps);                                   //Shows the amount of sleeps left before the holiday
        holidayText.setText(holidayName);                                   //Shows the name of the holiday that the user selected


        //get custom background photo
        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.holidayprofile);   //reference the xml file
        Holiday info = new Holiday (this);                                              //make an instance of the database
        info.open();                                                                    //open the database
        int theme = info.getThemeID(holidayName);                                       //get the theme ID for this holiday
        info.close();                                                                   //close the database
        switch(theme){                                                                  //use switch case to set one of the five backgrounds
            case 0:
                relLayout.setBackgroundResource(R.mipmap.beach_main);
                break;
            case 1:
                relLayout.setBackgroundResource(R.mipmap.snow_main);
                break;
            case 2:
                relLayout.setBackgroundResource(R.mipmap.city_main);
                break;
            case 3:
                relLayout.setBackgroundResource(R.mipmap.country_main);
                break;
            case 4:
                relLayout.setBackgroundResource(R.mipmap.main_bg);
                break;
        }
    }



    //get current date and departure date
    public void setDates(){
        Calendar c1 = Calendar.getInstance();                           //make an instance of the Calendar class
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");       //set what format you would like the date in (day-month-year)
        today = df.format(c1.getTime());                                //get the current date (today)

        Holiday info = new Holiday(HolidayProfilePage.this);            //make an instance of the database
        info.open();                                                    //open the database
        departure = info.getDepatureDate(holidayName);                  //get the departure date of the selected holiday
        info.close();                                                   //close the database
    }


    //get how many sleeps left til the departure date
    public void getDateDiff(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");       //set what format you would like the date in (day-month-year)
        try{
            Date todayD = df.parse(today);                              //parse the today String into a Date data type
            Date deptD = df.parse(departure);                           //parse the departure String into a Date data type
            double diff = deptD.getTime() - todayD.getTime();           //get the difference between the two (in miliseconds)
            double seconds = diff/1000;                                 //get the difference in seconds
            double minutes = seconds/60;                                //get the difference in minutes
            double hours = minutes/60;                                  //get the difference in hours
            double days = hours/24;                                     //get the difference in days

            int noSleeps = (int)days;                                   //cast the double into an int (get rid of decimal place)
            sleeps = ""+noSleeps;                                       //cast the int into a String and save in class variable

        } catch (ParseException e) {                                    //incase there is an error
            e.printStackTrace();
        }
    }


    //when the holiday profile question mark is clicked, show the user some information about what to do
    public void profileDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(HolidayProfilePage.this).create();        //create a new alert dialog
        alertDialog.setTitle("Your Holiday Profile");                                                       //set the dialog title and text
        alertDialog.setMessage("This is a specific profile for your selected holiday. Swipe the screen from left to right to open the menu. Have fun exploring the other activities.");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }






    //Navigation Drawer code

    //auto-generated by Android Studio
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //auto-generated by Android Studio
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.holiday_profile_page, menu);
        return true;
    }

    //auto-generated by Android Studio
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //auto-generated by Android Studio
    @SuppressWarnings("StatementWithEmptyBody")

    //This controls what happens when an item in the Navigation Drawer is clicked
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.shopping_list) {                                                             //if shopping list is clicked
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayShoppingList");     //go to HolidayShoppingList.java
            intent.putExtra("holidayName", holidayName);                                            //send extra information with the intent
            startActivity(intent);

        } else if (id == R.id.spending_money) {                                                     //^^the above comments apply to the rest of the 'else if' statements
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySpendingMoney");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);

        } else if (id == R.id.bank_account) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySavingStatus");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.change_theme) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayChangeTheme");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.holiday_profile) {                                                    //do nothing if its own page is clicked

        } else if (id == R.id.main_menu) {                                                          //this is the only case that does not need to send extra information with the intent
            Intent intent = new Intent("com.example.sinead.seethecapital.MainMenu");
            startActivity(intent);
            finish();
        } else if (id == R.id.holiday_location) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayLocation");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);                                                    //close the drawer
        return true;                                                                                //return that the action is completed
    }

}
