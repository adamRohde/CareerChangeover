package careerchangeover.com.valuesvisualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyDbHandler dbHandler;
    SharedPreferences mPref;
    String seeTutorialPref = "openTutorial";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        // default open tutorial flag set to true
        boolean goToTutorial = mPref.getBoolean(seeTutorialPref,true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // opens tutorial is flag is set to true
        if (goToTutorial) {
            Intent openTutorial = new Intent(MainActivity.this,TutorialActivity.class);
            startActivity(openTutorial);
        }

        // Sets button on click listeners
//        findViewById(R.id.myValues).setOnClickListener(this);
//        findViewById(R.id.employerValues).setOnClickListener(this);

        BottomNavigationView bottomNavigationMenu = findViewById(R.id.bottom_navigation);

        bottomNavigationMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.help_menu:
                                getSupportFragmentManager().beginTransaction().replace(R.id.main_menu_layout, new TutorialFragment()).commit();
//                                Intent openTutorial = new Intent(MainActivity.this,TutorialActivity.class);
//                                startActivity(openTutorial);
                                break;
                            case R.id.profile_menu:
                                return true;
                            case R.id.result_menu:
                                Intent toResults = new Intent(MainActivity.this,ResultsActivity.class);
                                startActivity(toResults);
                                break;
                        }
                        return false;
                    }
                });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_menu_layout, new HomeFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main activity menu
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        switch (item.getItemId()) {
            case R.id.viewTutorial:
                Intent goToTutorial = new Intent(MainActivity.this,TutorialActivity.class);
                goToTutorial.putExtra("allow",true);
                startActivity(goToTutorial);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myValues:
                // Switches to personal survey page with extra
                Intent toSelfEval = new Intent(MainActivity.this,SurveyActivity.class);
                toSelfEval.putExtra("column_name", dbHandler.COLUMN_SELF_EVAL);
                startActivity(toSelfEval);
                break;
            case R.id.employerValues:
                // Switches to employer survey page with extra
                Intent toEmployerEval = new Intent(MainActivity.this,SurveyActivity.class);
                toEmployerEval.putExtra("column_name", dbHandler.COLUMN_EMPLOYER_EVAL);
                startActivity(toEmployerEval);
                break;
        }
    }
}
