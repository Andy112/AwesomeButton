package mad.acai.com.awesomebutton.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cepheuen.progresspageindicator.ProgressPageIndicator;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import mad.acai.com.awesomebutton.R;
import mad.acai.com.awesomebutton.layouts.NavigationDrawer;
import mad.acai.com.awesomebutton.network.VolleySingleton;
import mad.acai.com.awesomebutton.tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button button, button2;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private static final String MY_TRANSACTION = "myTransaction";
    private static final String MY_BOOKING = "myBooking";
    private static final String MY_BPR = "myBPR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawer drawerFragment = (NavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar, "MainActivity");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubActivity.class));
            }
        });

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tab_text_view);
        /*mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });*/
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        ProgressPageIndicator pagerIndicator = (ProgressPageIndicator) findViewById(R.id.pageIndicator);

        mTabs.setViewPager(mPager);
       /* button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubActivity.class));
            }
        });*/
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.button_action_selector);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView).setBackgroundDrawable(R.drawable.fab_selector).build();

        ImageView iconMyTransaction = new ImageView(this);
        iconMyTransaction.setImageResource(R.drawable.ic_account_balance_white_48dp);

        ImageView iconMyBooking = new ImageView(this);
        iconMyBooking.setImageResource(R.drawable.ic_account_box_white_48dp);

        ImageView iconMyBPR = new ImageView(this);
        iconMyBPR.setImageResource(R.drawable.ic_backup_white_48dp);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_sub_selector));

        SubActionButton myTransBtn = itemBuilder.setContentView(iconMyTransaction).build();
        SubActionButton myBookBtn = itemBuilder.setContentView(iconMyBooking).build();
        SubActionButton myBPRBtn = itemBuilder.setContentView(iconMyBPR).build();

        myTransBtn.setTag(MY_TRANSACTION);
        myBookBtn.setTag(MY_BOOKING);
        myBPRBtn.setTag(MY_BPR);

        myTransBtn.setOnClickListener(this);
        myBookBtn.setOnClickListener(this);
        myBPRBtn.setOnClickListener(this);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(myTransBtn)
                .addSubActionView(myBookBtn)
                .addSubActionView(myBPRBtn)
                .attachTo(actionButton)
                .build();

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                imageView.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhR);
                animation.start();
            }
            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                imageView.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhR);
                animation.start();
            }
        });

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, TabLibrary.class));
        }
        if (id == R.id.vector) {
            startActivity(new Intent(this, VectorTestActivity.class));
        }
        else if(id == R.id.exit){

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(MY_TRANSACTION)) {

        }
        if (v.getTag().equals(MY_BOOKING)) {
            startActivity(new Intent(this, TabLibrary.class));
        }
        if (v.getTag().equals(MY_BPR)) {

        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        int icons[] = {R.drawable.ic_account_box_white_48dp, R.drawable.ic_account_balance_white_48dp, R.drawable.ic_backup_white_48dp};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,36,36);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class MyFragment extends Fragment {

        private TextView textView;
        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_tab, container, false);

            textView = (TextView)layout.findViewById(R.id.position);
            Bundle bundle = getArguments();
            if (bundle != null) {
                textView.setText("The page selected is " + bundle.getInt("position"));
                textView.setText("");
            }
            RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://acaiii.com/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // Toast.makeText(getActivity(), "RESPONSE " + response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "ERROR " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
            return layout;
        }
    }
}
