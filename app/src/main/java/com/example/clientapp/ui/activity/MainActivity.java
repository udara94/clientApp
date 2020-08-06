package com.example.clientapp.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.clientapp.R;
import com.example.clientapp.model.dto.NotificationBody;
import com.example.clientapp.mvp.presenters.Presenter;
import com.example.clientapp.ui.fragment.HomeFragment;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.example.clientapp.utils.PrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    final String TAG = MainActivity.this.getClass().getSimpleName();
    public static MainActivity mainActivity;
    private static String MAIN_LEVEL = "MAIN_LEVEL";
    private static final int PICK_FROM_GALLERY = 101;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    int columnIndex;
    String attachmentFile;
    Uri URI = null;
    String FileName = null;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;


    public static int navItemIndex = 0;
    private String mMobile;
    private String mAction = "HOME";

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_GENERATE_CV = "generatecv";
    private static final String TAG_FAVOURITE_JOBS = "favjobs";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private SharedPreferences preferences;
    private BroadcastReceiver broadcastReceiver;
    private NotificationBody mNotificationBody;

    protected Presenter mNotificationPresenter;
    protected Presenter presenter;
    protected Presenter jobPresenter;
    private AlertDialog aDialog;
    private PrefManager prefManager;

    protected BaseBackPressedListener.OnBackPressedListener onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePresenter();
        ButterKnife.bind(this);
        mainActivity = this;

        if (findViewById(R.id.fragment_container) != null) {
            setFragment(HomeFragment.newInstance());
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarGradiant(this);
        }
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandlerFCM, new IntentFilter("com.eyepax.traveller_FCM-MESSAGE"));

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
       // performGetNotificationCountRequest();

    }



    private BroadcastReceiver mHandlerFCM = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    public void saveObjectToSharedPreferences(String preferencesKeys, String loginResponse){
        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(preferencesKeys,loginResponse).apply();
    }




    public void openDrawer(){
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(Gravity.LEFT);
        }
    }

    private void loadNavHeader() {

        // loading header background image
        Glide.with(this).load(getResources().getIdentifier("header_logo", "drawable", this.getPackageName()))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();


        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                fragmentTransaction.commitAllowingStateLoss();

                    replaceFragment(fragment);

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                mAction = "HOME";
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
//            case 1:
//                // favourite jobs fragment
//                mAction = "FAVJOBS";
//                FavouriteFragment favouriteFragment = new FavouriteFragment();
//                return favouriteFragment;
//            case 2:
//                // notifications fragment
//                mAction = "NOTIFICATION";
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                return notificationsFragment;
//            case 3:
//                // settings fragment
//                mAction = "SETTINGS";
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.tool_bar_drawable);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public void setFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, String TAG) {
        if (fragment == null) return;
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left);
        fragTransaction.add(R.id.fragment_container, fragment, TAG);
        fragTransaction.addToBackStack(TAG);
        fragTransaction.commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }



    public void initializePresenter() {

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
            super.onBackPressed();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getSupportFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getSupportFragmentManager().popBackStack();
        }
        return isPop;
    }

    protected void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        if(aDialog != null)aDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    public void setOnBackPressedListener(BaseBackPressedListener.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


}
