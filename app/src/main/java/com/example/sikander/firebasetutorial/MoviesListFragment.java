package com.example.sikander.firebasetutorial;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<MovieListItem> moviesList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        moviesList = new ArrayList<MovieListItem>();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "Newest";
            } else if(position == 1) {
                return "Trending";
            } else if(position == 2) {
                return "Most Popular";
            } else if(position == 3) {
                return "Highest Rated";
            }
            return "Title";
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            container.addView(view);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            if(position == 0) {
                moviesList = getArguments().getParcelableArrayList("newest_movies_list");
            } else if(position == 1) {
                moviesList = getArguments().getParcelableArrayList("trending_movies_list");
            } else if(position == 2) {
                moviesList = getArguments().getParcelableArrayList("most_popular_movies_list");
            } else if(position == 3) {
                moviesList = getArguments().getParcelableArrayList("highest_rated_movies_list");
            }
            mAdapter = new MoviesAdapter(getActivity(), moviesList);
            mRecyclerView.setAdapter(mAdapter);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
