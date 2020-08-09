package com.vidya.axxesssearchimageassignment.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.vidya.axxesssearchimageassignment.R;
import com.vidya.axxesssearchimageassignment.adapter.SearchByImageAdapter;
import com.vidya.axxesssearchimageassignment.interfaces.IFragmentListener;
import com.vidya.axxesssearchimageassignment.interfaces.RNService;
import com.vidya.axxesssearchimageassignment.model.Datum;
import com.vidya.axxesssearchimageassignment.model.Example;
import com.vidya.axxesssearchimageassignment.model.Image;
import com.vidya.axxesssearchimageassignment.remote.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchByImageNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchByImageNameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchByImageNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchByPeopleNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchByImageNameFragment newInstance(String param1, String param2) {
        SearchByImageNameFragment fragment = new SearchByImageNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private RecyclerView searchByImageNameRecyclerView;
    private SearchByImageAdapter searchByImageAdapter;
    SearchByImageAdapter.SearchByImageAdapterItemClickListener searchByImageAdapterItemClickListener;
    private ArrayList<Datum> listdata;
    private String mSearchTerm = null;
    ImageView profile_image;
    TextView imageName;
    private RNService rnService;
    private static final String ARG_SEARCHTERM = "search_term";
    private IFragmentListener mIFragmentListener = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_image, container, false);
        searchByImageNameRecyclerView = (RecyclerView) view.findViewById(R.id.searchByPeopleNameRecyclerView);
        imageName = (TextView) view.findViewById(R.id.imageName);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);


        listdata = new ArrayList<>();
        rnService = ApiUtils.getRNService();
        getTournamentListOnSearch();
        return view;
    }

    private void getTournamentListOnSearch() {
      Call<Example> call = rnService.getData("Client-ID 137cda6b5008a7c");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                if (response.isSuccessful()) {

                    List<Datum> data = response.body().getData();
                    ArrayList<String> images = new ArrayList<>();

                    for(Datum datum: data) {
                        if(datum.getImages() != null) {
                            if(datum.getImages().size() > 0) {
                                Image img = datum.getImages().get(0);
                                images.add(img.getLink());
                            }
                        }
                    }

                    Log.d("Fragment", "posts loaded from API" + data);
                    searchByImageAdapter = new SearchByImageAdapter(getContext(), data, searchByImageAdapterItemClickListener);
                    searchByImageNameRecyclerView.setAdapter(searchByImageAdapter);
                    searchByImageNameRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
                    searchByImageAdapter.notifyDataSetChanged();


                } else {

                    int statusCode = response.code();

                    Log.d("SearchByImageFragment", "error code -" + statusCode);
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d("MainActivity", "error loading from API");

            }
        });
    }
    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }
    public void beginSearch(String query) {
        Log.e("QuerySearchByPeopleName", query);
        searchByImageAdapter.getFilter().filter(query);
    }


}
