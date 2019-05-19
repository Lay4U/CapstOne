package jeimsko.com.subway.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jeimsko.com.subway.R;

/**
 * Created by jeimsko on 2016. 9. 9..
 */

public class ThirdPageFragment extends Fragment{


    public static  ThirdPageFragment newInstance(){

        ThirdPageFragment tf = new ThirdPageFragment();

        return tf;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = (View) inflater.inflate(R.layout.thirdpage, container, false);

        return rootView;
    }

}
