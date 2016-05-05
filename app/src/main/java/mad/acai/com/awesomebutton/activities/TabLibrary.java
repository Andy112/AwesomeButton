package mad.acai.com.awesomebutton.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import mad.acai.com.awesomebutton.R;
import mad.acai.com.awesomebutton.extras.SortListener;
import mad.acai.com.awesomebutton.fragments.BoxOfficeFragment;
import mad.acai.com.awesomebutton.fragments.SearchFragment;
import mad.acai.com.awesomebutton.fragments.UpComingFragment;
import mad.acai.com.awesomebutton.layouts.NavigationDrawer;
import mad.acai.com.awesomebutton.services.MyService;
import mad.acai.com.awesomebutton.tabs.MaterialTab;
import mad.acai.com.awesomebutton.tabs.MaterialTabHost;
import mad.acai.com.awesomebutton.tabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class TabLibrary extends AppCompatActivity implements MaterialTabListener, View.OnClickListener {

    private static final int JOB_ID = 100;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private JobScheduler mJobScheduler;
    private static final int MOVIE_SEARCH_RESULTS = 0, MOVIE_HITS = 1, MOVIE_UPCOMING = 2;

    private static final String BOOKING_ID = "myTransaction";
    private static final String BOOKING_STATUS = "myBooking";
    private static final String BOOKING_FARE = "myBPR";
    private ImageView iconMyBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Bookings");
        mJobScheduler = JobScheduler.getInstance(this);
        //constructJob();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationDrawer drawerFragement = (NavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragement.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, "TabLibrary");

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(adapter.getPageTitle(i)).setTabListener(this));
            //tabHost.addTab(tabHost.newTab().setIcon(adapter.getIcon(i)).setTabListener(this));
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        buildFAB();
    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));

        builder.setPeriodic(300000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);

        mJobScheduler.schedule(builder.build());
    }

    private void buildFAB() {

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.button_action_selector);

        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(imageView).setBackgroundDrawable(R.drawable.fab_selector).build();

        ImageView iconMyTransaction = new ImageView(this);
        iconMyTransaction.setImageResource(R.drawable.ic_account_balance_white_48dp);

        iconMyBooking = new ImageView(this);
        iconMyBooking.setImageResource(R.drawable.ic_account_box_white_48dp);

        ImageView iconMyBPR = new ImageView(this);
        iconMyBPR.setImageResource(R.drawable.ic_backup_white_48dp);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_sub_selector));

        SubActionButton bookId = itemBuilder.setContentView(iconMyTransaction).build();
        SubActionButton bookStatus = itemBuilder.setContentView(iconMyBooking).build();
        SubActionButton bookFare = itemBuilder.setContentView(iconMyBPR).build();

        bookId.setTag(BOOKING_ID);
        bookStatus.setTag(BOOKING_STATUS);
        bookFare.setTag(BOOKING_FARE);

        bookId.setOnClickListener(this);
        bookStatus.setOnClickListener(this);
        bookFare.setOnClickListener(this);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(bookId)
                .addSubActionView(bookStatus)
                .addSubActionView(bookFare)
                .attachTo(actionButton)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Hey, you just hit " + item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));
        }
        if (id == R.id.tab_with_lib) {
        } else if (id == R.id.exit) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public void onClick(View v) {

        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment instanceof SortListener) {
            if (v.getTag().equals(BOOKING_ID)) {
                ((SortListener) fragment).onSortById();
            }
            if (v.getTag().equals(BOOKING_STATUS)) {
                ((SortListener) fragment).onSortByBookingStatus();
            }
            if (v.getTag().equals(BOOKING_FARE)) {
                ((SortListener) fragment).onSortByFare();
            }

        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.ic_account_box_white_48dp, R.drawable.ic_account_balance_white_48dp, R.drawable.ic_backup_white_48dp};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case MOVIE_SEARCH_RESULTS:
                    fragment = SearchFragment.newInstance("", "");
                    break;
                case MOVIE_HITS:
                    fragment = BoxOfficeFragment.newInstance(iconMyBooking, "", "");
                    break;
                case MOVIE_UPCOMING:
                    fragment = UpComingFragment.newInstance("", "");
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
