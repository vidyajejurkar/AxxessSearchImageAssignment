package com.vidya.axxesssearchimageassignment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.vidya.axxesssearchimageassignment.Fragments.SearchByImageNameFragment;
import com.vidya.axxesssearchimageassignment.R;
import com.vidya.axxesssearchimageassignment.adapter.SearchByImageAdapter;
import com.vidya.axxesssearchimageassignment.interfaces.IDataCallback;
import com.vidya.axxesssearchimageassignment.interfaces.ISearch;
import com.vidya.axxesssearchimageassignment.interfaces.RNService;
import com.vidya.axxesssearchimageassignment.model.Datum;
import com.vidya.axxesssearchimageassignment.remote.ApiUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SearchImageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton ivBackArrowSearchActivity;
    SearchView et_searchFriend;
    ImageView iv_searchTournament;

    ArrayList<ISearch> iSearch = new ArrayList<>();
    private RecyclerView searchByTournamentNameRecyclerView;
    private SearchByImageAdapter searchActivityRetrofitAdapter;
    private ArrayList<Datum> listdata;
    private ArrayList<Datum> tempData;
    private   String[] tournaments;
    private ArrayList<Datum> tournamentList;
    private String newText;
    ArrayList<String> listData = null;
    IDataCallback iDataCallback = null;
    private MenuItem searchMenuItem;
    SearchView searchView = null;
    LinearLayout llInviteFriendSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        makeActionOverflowMenuShown();
        viewPager = (ViewPager) findViewById(R.id.viewpagerSearch);
        setupViewPager(viewPager);
        listdata = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.tabsSearch);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_hint_type_message);



        //search
        searchByTournamentNameRecyclerView = (RecyclerView) findViewById(R.id.searchByPeopleNameRecyclerView);

       // et_searchFriend = (SearchView) findViewById(R.id.et_searchFriend);
        ivBackArrowSearchActivity = (ImageButton) findViewById(R.id.ivBackArrowSearchActivity);
        final Intent intent = getIntent();
        if (intent.hasExtra("TabNumber")) {
            String tab = intent.getExtras().getString("TabNumber");
            Log.e("TabNumberFriendList",tab);
            switchToTab(tab);

        }
        // Attach the page change listener inside the activity
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getApplicationContext(),
                        "Selected page position: " + position,
                        Toast.LENGTH_SHORT).show();
                if (searchView != null && !searchView.isIconified()) {
                    //searchView.onActionViewExpanded();
                    searchView.setIconified(true);
                    searchView.setIconified(true);

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchByImageNameFragment(), "Images");
        viewPager.setAdapter(adapter);
    }

//test

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String query) {
                PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
                assert pagerAdapter != null;
                for(int i = 0; i < pagerAdapter.getCount(); i++) {

                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                    if(viewPagerFragment != null && viewPagerFragment.isAdded()) {

                        if (viewPagerFragment instanceof SearchByImageNameFragment){
                            SearchByImageNameFragment searchPeopleFragment = (SearchByImageNameFragment) viewPagerFragment;
                            if (searchPeopleFragment != null){
                                searchPeopleFragment.beginSearch(query); // Calling the method beginSearch of ChatFragment
                            }
                        }
                        }

                }

                return false;
            }



            public boolean onQueryTextSubmit(String query) {
                // Here u can get the value "query" which is entered in the
                PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
                assert pagerAdapter != null;
                for(int i = 0; i < pagerAdapter.getCount(); i++) {

                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                    if(viewPagerFragment != null && viewPagerFragment.isAdded()) {

                        if (viewPagerFragment instanceof SearchByImageNameFragment){
                            SearchByImageNameFragment searchPeopleFragment = (SearchByImageNameFragment) viewPagerFragment;
                            if (searchPeopleFragment != null){
                                Toast.makeText(getApplicationContext(),"In Search by people ",Toast.LENGTH_LONG).show();
                                searchPeopleFragment.beginSearch(query); // Calling the method beginSearch of ChatFragment
                            }
                        }
                    }
                }
                return false;

            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
           /// super(manager);
            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return new SearchByImageNameFragment(); //ChildFragment1 at position 0

            }
            return null; //does not happen
        }

        @Override
        public int getCount() {
           // return mFragmentList.size();
            return 1;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            notifyDataSetChanged();
            return POSITION_NONE;
        }
    }

    //open tournament tab if called by tournament activity
    public void switchToTab(String tab){
        if(tab.equals("0")){
            viewPager.setCurrentItem(0);
        }
    }
}


