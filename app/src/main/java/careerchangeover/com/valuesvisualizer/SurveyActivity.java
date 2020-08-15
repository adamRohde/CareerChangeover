package careerchangeover.com.valuesvisualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity implements View.OnClickListener{

    MyDbHandler myDbHandler;
    ItemAdapter itemAdapter;
    SharedPreferences mPref;
    String disclaimerAcceptedPref = "disclaimerAccepted";
    TextView surveyStatement;
    ListView listView;
    Value[] valuesArray;
    String column;
    AlertDialog mAlertDialog;

    public static final int REQUEST_FIRST_INTENT = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        // contains extra about which survey is being taken
        column = getIntent().getStringExtra("column_name");

        // Finds which survey question to ask
        setSurveyStatement();

        // Initializes db handler class
        myDbHandler = new MyDbHandler(this);
        valuesArray = myDbHandler.getValuesArray();

        // List View Adapter
        itemAdapter = new ItemAdapter(this,valuesArray,column);
        listView = findViewById(R.id.surveyListView);
        listView.setAdapter(itemAdapter);

        findViewById(R.id.saveButton).setOnClickListener(this);

    }

    public void setSurveyStatement() {
        // Displays correct survey question
        surveyStatement = findViewById(R.id.surveyStatementTextView);

        if (column.startsWith("personal")) {
            surveyStatement.setText(getResources().getString(R.string.personal_survey_statement));
            surveyStatement.setAllCaps(true);
        } else if (column.startsWith("employer")) {
            surveyStatement.setText(getResources().getString(R.string.employer_survey_statement));
            surveyStatement.setAllCaps(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                // Db updated with rank entries
                valuesArray = itemAdapter.getValuesArray();
                myDbHandler.updateRank(valuesArray,column);

                // Default disclaimer accepted flag set to false
                mPref = PreferenceManager.getDefaultSharedPreferences(this);
                boolean doNotShowAgain = mPref.getBoolean(disclaimerAcceptedPref,false);

//                // Decides whether or not to view disclaimer
//                if (column.startsWith("employer") || doNotShowAgain) {
//                    Intent save = new Intent(SurveyActivity.this,MainActivity.class);
//                    startActivity(save);
//                } else {
//                    Intent disclaimerPopUp = new Intent(SurveyActivity.this,DisclaimerPopUp.class);
//                    startActivity(disclaimerPopUp);
//                }

                // Calculates results if survey is completed
                if (checkSurveyComplete()) {
                    myDbHandler.calculateScore(valuesArray,column);
                    Intent mainActivityIntent = new Intent(SurveyActivity.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                }else
                {
                    setReminder();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if this is true, this means your first intent finished. So you can start your second intent
        if (requestCode == REQUEST_FIRST_INTENT) {
            Intent mainActivityIntent = new Intent(SurveyActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    private void setReminder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
        View view = LayoutInflater.from(SurveyActivity.this).inflate(R.layout.set_reminder_layout, null);
        view.findViewById(R.id.set_reminder_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.hide();
                Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, "Complete Values Survey");
                startActivityForResult(insertCalendarIntent, REQUEST_FIRST_INTENT);

            }
        });
        view.findViewById(R.id.set_reminder_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.hide();
                Intent mainActivityIntent = new Intent(SurveyActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
        builder.setView(view);

        mAlertDialog = builder.create();
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.show();
    }

    private boolean checkSurveyComplete() {
        // Checks if survey is complete
        for (Value value: valuesArray) {
            int rank = value.getRank(column);
            if (rank == -1) return false;
        }
        return true;
    }
}
