package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;


//Giving the user the chance to change the theme/background of the holiday profile
public class HolidayChangeTheme extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declare class variables so they can be used by many methods in this class
    private String holidayName = "";                        //holds the name of the chosen destination (sent by intent extra)
    private SeekBar themePicker = null;                     //seekbar used to choose a different background photo
    private ImageView themeImage;                           //shows a preview of the background image
    private int progressChanged =0;                         //holds the position of the seekbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_change_theme);

        Bundle holidayData = getIntent().getExtras();                                       //receive the extra information passed through intent
        holidayName = holidayData.getString("holidayName");                                 //save extra information in class variable

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);       //set the navigation side drawer
        navigationView.setNavigationItemSelectedListener(this);



        //get custom background photo
        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.changetheme);          //reference the xml file
        Holiday info = new Holiday (this);                                                  //make an instance of the database
        info.open();                                                                        //open the database
        int theme = info.getThemeID(holidayName);                                           //get the theme ID for this holiday
        info.close();                                                                       //close the database
        switch(theme){                                                                      //use switch case to set one of the five backgrounds
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




        //Theme Picker code
        themePicker = (SeekBar)findViewById(R.id.seekTheme);                                    //reference the seekbar + image view in xml file
        themeImage = (ImageView)findViewById(R.id.themeImage);
        themePicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {    //when the slider is moved (/progress has changed)
                progressChanged = progress;                                                     //update the class variable to the actual position of slider

                switch(progressChanged){                                                        //use switch case to display a preview of background images
                    case 0:
                        themeImage.setBackgroundResource(R.mipmap.beach_main);
                        break;
                    case 1:
                        themeImage.setBackgroundResource(R.mipmap.snow_main);
                        break;
                    case 2:
                        themeImage.setBackgroundResource(R.mipmap.city_main);
                        break;
                    case 3:
                        themeImage.setBackgroundResource(R.mipmap.country_main);
                        break;
                    case 4:
                        themeImage.setBackgroundResource(R.mipmap.main_bg);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {                                  //when the user stops moving the slider (/position has been chosen)
                switch(progressChanged){                                                        //use switch case to make a Toast appear with the chosen theme name
                    case 0:
                        Toast.makeText(getApplicationContext(), "Sun Holiday", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Snow Holiday", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "City Break", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Countryside", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }


    //when the save theme button is clicked, the theme ID in the holiday database is updated
    public void saveTheme(View view){
        Holiday info = new Holiday (this);                                                  //make new instance of database
        info.open();                                                                        //open the database
        info.updateTheme(progressChanged, holidayName);                                     //call method to update the theme id
        info.close();                                                                       //close the database
        Intent intent = new Intent("com.example.sinead.seethecapital.HolidayProfilePage");  //go back to the HolidayProfilePage
        intent.putExtra("holidayName", holidayName);                                        //attach holidayName as an extra on the intent
        startActivity(intent);
        finish();                                                                           //finish HolidayChangeTheme activity
    }



    //when the change theme question mark is clicked, show the user some information about what to do
    public void themeDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(HolidayChangeTheme.this).create();        //create a new alert dialog
        alertDialog.setTitle("Change the Theme");                                                       //set the dialog title and text
        alertDialog.setMessage("Here you can change the background of your holiday profile. Drag the slider right and left, and select which holiday theme you think best suits your destination.");
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
            finish();                                                                               //finish HolidayChangeTheme.java
        } else if (id == R.id.spending_money) {                                                     //^^the above comments apply to the rest of the 'else if' statements
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySpendingMoney");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.bank_account) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySavingStatus");
            intent.putExtra("holidayName", holidayName);
            startActivity(intent);
            finish();
        } else if (id == R.id.change_theme) {                                                       //do nothing if its own page is clicked

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
