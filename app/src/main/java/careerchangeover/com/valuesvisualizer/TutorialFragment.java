package careerchangeover.com.valuesvisualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TutorialFragment extends Fragment {

    String openTutorialPref = "openTutorial";
    SharedPreferences mPref;
    FragmentPagerAdapter pagerAdapter;
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // View Pager Adapter
        pagerAdapter = new PagerAdapter(getFragmentManager());
        pager = view.findViewById(R.id.viewPager);
        pager.setAdapter(pagerAdapter);

        view.findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // sets open tutorial flag to false on click
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean(openTutorialPref, false);
            editor.apply();
            Intent exitToMain = new Intent(getActivity(), MainActivity.class);
            startActivity(exitToMain);
            }
        });
        return view;
    }

}
