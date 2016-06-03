package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Gives the user a breakdown of what they have saved and what they still need to save before going on holidays
public class HolidaySavingStatus extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declare class variables so they can be used by many methods in this class
    TextView sleepsOutput, sleepText, accountOutput, needOutput, balanceOutput, dayOutput, weekOutput, congratsText;
    private String sleeps="";                                               //holds number of sleeps until departure
    private double numWeeks=0.0;                                            //holds number of weeks until departure
    private String account="";                                              //holds bank account total
    private String need="";                                                 //holds total of Shopping + Spending Lists
    private String balance="";                                              //holds the amount that still needs to be saved
    private String day="";                                                  //holds how much to save per day before going away
    private String week="";                                                 //holds how much to save per week before going away
    private String travel ="";                                              //holds the travel price

    private String today="";                                                //holds the current date
    private String departure="";                                            //holds the departure date of the selected holiday
    private String holidayName = "";                                        //holds the name of the chosen destination (sent by intent extra)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_saving_status);

        Bundle holidayData = getIntent().getExtras();                                   //receive the extra information passed through intent
        holidayName = holidayData.getString("holidayName");                             //save extra information in class variable

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //set the navigation side drawer
        navigationView.setNavigationItemSelectedListener(this);


        //get custom background photo
        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.savingstatus);     //reference the xml file
        Holiday info = new Holiday (this);                                              //make an instance of the database
        info.open();                                                                    //open the database
        int theme = info.getThemeID(holidayName);                                       //get the theme ID for this holiday
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




        //get the saving status outputs
        sleepsOutput = (TextView)findViewById(R.id.sleepsOutput);
        sleepText = (TextView)findViewById(R.id.sleepDefault);
        accountOutput=(TextView)findViewById(R.id.accountOutput);
        needOutput=(TextView)findViewById(R.id.needOutput);
        balanceOutput=(TextView)findViewById(R.id.balanceOutput);
        dayOutput=(TextView)findViewById(R.id.dayOutput);
        weekOutput=(TextView)findViewById(R.id.weekOutput);
        congratsText=(TextView)findViewById(R.id.congratsText);


        setDates();                                                         //get the current + departure date
        getDateDiff();                                                      //calculates how many days between the two dates
        sleepsOutput.setText(sleeps);                                       //output the amount of sleeps left
        sleepText.setText("more sleeps until " + holidayName);              //output String to say what holiday it is for

        account = info.getAmount();                                         //get the amount in the bank account
        accountOutput.setText("€" + account);                               //output the amount in the bank account

        need = info.getTotalHolidayAmount(holidayName);                     //get the total of Shopping + Spending Lists + travel price
        needOutput.setText("€" + need);                                     //output the total
        info.close();                                                       //close the database



        getBalanceOutput();                                                 //get the difference between what the user has in the bank account and what they need to save
        if(Integer.parseInt(balance)<=0){                                   //if the user has reached their savings target
            int leftover = Math.abs(Integer.parseInt(balance));             //get the absolute of the 'leftover' money
            congratsText.setText("You saved €" + leftover + " over your target");   //output
            balanceOutput.setText("Congrats!");
            dayOutput.setText("N/A");
            weekOutput.setText("N/A");
        }else{                                                              //otherwise, show the user how much they still need to save
            congratsText.setText("Amount still to save:");
            balanceOutput.setText("€" + balance);

            double bal = Double.parseDouble(balance);
            double days = Double.parseDouble(sleeps);
            day = String.format("%.2f",(bal / days));                       //format day + week savings to 2 decimal places
            week = String.format("%.2f",(bal/numWeeks));

            dayOutput.setText("€" + day);
            weekOutput.setText("€" + week);
        }

    }





    //get difference of bank account-total spending
    public void getBalanceOutput(){
        int accountNum = Integer.parseInt(account);                     //convert account into int
        int needNum=0;
        if(need.equals("N/A")){                                         //if the user has reached their target
            needNum=0;                                                  //leave the needed amount at 0
        }else{                                                          //otherwise convert the total of the Shopping + Spending Lists into an int
            needNum= Integer.parseInt(need);
        }
        balance="" +(needNum - accountNum);                             //calculate the difference
    }



    //get current date and departure date
    public void setDates(){
        Calendar c1 = Calendar.getInstance();                           //make an instance of the Calendar class
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");       //set what format you would like the date in (day-month-year)
        today = df.format(c1.getTime());                                //get the current date (today)

        Holiday info = new Holiday(HolidaySavingStatus.this);           //make an instance of the database
        info.open();                                                    //open the database
        departure = info.getDepatureDate(holidayName);                  //get the departure date of the selected holiday
        info.close();                                                   //close the database
    }




    //get how many sleeps left
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
            numWeeks = days/7;                                          //get the difference in weeks (save as class variable)

            int noSleeps = (int)days;                                   //cast the double into an int (get rid of decimal place)
            sleeps = ""+noSleeps;                                       //cast the int into a String and save in class variable

        } catch (ParseException e) {                                    //incase there is an error
            e.printStackTrace();
        }
    }


    //when the saving status question mark is clicked, show the user some information about what to do
    public void statusDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(HolidaySavingStatus.this).create();        //create a new alert dialog
        alertDialog.setTitle("Your Saving Status");                                                       //set the dialog title and text
        alertDialog.setMessage("This is a breakdown of what you have in your bank a/c, how much you need in total and how much you still need to save to get to your target. It also helps you calculate how much you should save per day or per week (approx).");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }






    //Navigation drawer code

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
            finish();                                                                               //finish HolidaySavingStatus.java
        } else if (id == R.id.spending_money) {                                                     //^^the above comments apply to the rest of the 'else if' statements
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySpendingMoney");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.bank_account) {                                                       //do nothing if its own page is clicked

        } else if (id == R.id.change_theme) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayChangeTheme");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.holiday_profile) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayProfilePage");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
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
