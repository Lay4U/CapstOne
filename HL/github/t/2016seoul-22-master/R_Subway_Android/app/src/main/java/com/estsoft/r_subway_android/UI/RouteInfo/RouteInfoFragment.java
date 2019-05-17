package com.estsoft.r_subway_android.UI.RouteInfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;


public class RouteInfoFragment extends Fragment {
    private static final String TAG = "RouteInfoFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static RouteNew[]  route;
    FragmentActivity mActivity;
    RecyclerView mRecyclerView;
    RouteRecyclerViewAdapter adapter;


    public RouteInfoFragment() {
        // Required empty public constructor
    }

    public void reInflateRouteConInfo() {
//        Log.d(TAG, "reInflateRouteConInfo: " + mPage);
        adapter.reInflateRouteCon();
    }

    public static RouteInfoFragment newInstance(int page, RouteNew[] route1) {
        RouteInfoFragment fragment = new RouteInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        route = route1;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        setRetainInstance(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_route_info, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.route_recycler_view);
        adapter = new RouteRecyclerViewAdapter(mActivity, route, mPage);

        return rootView;
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.SetOnItemClickListener(new RouteRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
                // do something with position
            }
        });
    }
}
