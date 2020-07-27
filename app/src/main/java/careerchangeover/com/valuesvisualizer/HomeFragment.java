package careerchangeover.com.valuesvisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements View.OnClickListener {

    MyDbHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Sets button on click listeners
        view.findViewById(R.id.myValues).setOnClickListener(this);
        view.findViewById(R.id.employerValues).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myValues:
                // Switches to personal survey page with extra
                Intent toSelfEval = new Intent(getActivity(), SurveyActivity.class);
                toSelfEval.putExtra("column_name", dbHandler.COLUMN_SELF_EVAL);
                startActivity(toSelfEval);
                break;
            case R.id.employerValues:
                // Switches to employer survey page with extra
                Intent toEmployerEval = new Intent(getActivity(), SurveyActivity.class);
                toEmployerEval.putExtra("column_name", dbHandler.COLUMN_EMPLOYER_EVAL);
                startActivity(toEmployerEval);
                break;
        }
    }
}
