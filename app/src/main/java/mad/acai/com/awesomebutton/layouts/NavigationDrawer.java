package mad.acai.com.awesomebutton.layouts;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mad.acai.com.awesomebutton.R;
import mad.acai.com.awesomebutton.adapters.InfoAdapter;
import mad.acai.com.awesomebutton.objects.Information;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment implements InfoAdapter.ClickListener {

    private RecyclerView recyclerView;
    public static String PREF_FILE_NAME = "testpref";
    public static String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private InfoAdapter adapter;
    private View containerView;
    private static String sClassName = null;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPrefences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.drawer_list);

        adapter = new InfoAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "OnClick " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "OnLongClick " + position, Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
        }

        public static List<Information> getData() {
            List<Information> data = new ArrayList<>();

            switch ("MainActivity") {
            case "MainActivity":
                int icons[] = {R.drawable.ic_menu_camera, R.drawable.ic_menu_manage, R.drawable.ic_menu_send, R.drawable.ic_menu_share};
                String[] titles = {"Camera", "Manage", "Send", "Share"};

        /*for (int i = 0; i<100; i++) {
            Information current = new Information();
            current.numId = icons[i%icons.length];
            current.title = titles[i%titles.length];
            data.add(current);
        }*/
                for (int i = 0; i < icons.length && i < titles.length; i++) {
                    Information current = new Information();
                    current.numId = icons[i];
                    current.title = titles[i];
                    data.add(current);
                }
                break;
            default:
                int iconss[] = {R.drawable.ic_menu_camera, R.drawable.ic_menu_manage};
                String[] titless = {"Camera", "Manage"};

                for (int i = 0; i < iconss.length && i < titless.length; i++) {
                    Information current = new Information();
                    current.numId = iconss[i];
                    current.title = titless[i];
                    data.add(current);
                }
                break;
        }
            return data;
    }
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, String className) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        sClassName = className;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPrefrences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPrefrences(Context context, String prefrenceName, String prefrenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefrenceName, prefrenceValue);
        editor.apply();
    }

    public String readFromPrefences(Context context, String prefrenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getString(prefrenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        //startActivity(new Intent(getActivity(), SubActivity.class));
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

}
