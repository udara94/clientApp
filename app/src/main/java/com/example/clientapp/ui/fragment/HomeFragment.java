package com.example.clientapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.common.CommonUtils;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.helpers.FirebaseHelper;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.model.dto.Notification;
import com.example.clientapp.ui.activity.MainActivity;
import com.example.clientapp.ui.adapter.ItemListAdapter;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = HomeFragment.this.getClass().getSimpleName();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static HomeFragment homeFragment;
    private static String SCORE_PREFERENCE = "SCORE_PREFERENCE";
    private static String SCORE = "SCORE";

    private ItemListAdapter itemListAdapter;
    private List<Item> mItemList = new ArrayList<>();
    private List<Notification> notificationList = new ArrayList<>();
    private List<Item> mCartList = new ArrayList<>();
    private Context mContext;

    RecyclerView mRecyclerView;
    private FirebaseHelper firebaseHelper;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mNotificationReference;
    private DatabaseReference mCartReference;

    MutableLiveData<String> listenNotificationText = new MutableLiveData<>();
    MutableLiveData<String> listenCartText = new MutableLiveData<>();

    private String notificationCount = "0";
    private String cartCount = "0";
    private boolean isGotoCart = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            homeFragment = this;
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            ((MainActivity) getActivity()).setOnBackPressedListener(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }
    @Override
    protected void initializePresenter() {

    }

    @Override
    protected void setUpUI() {

    }



    @Override
    protected void setUpToolBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.custom_action_bar_home, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView count = (TextView) mCustomView.findViewById(R.id.cart_badge);
        TextView cartCount = (TextView) mCustomView.findViewById(R.id.cart_notification);
        RelativeLayout cartLayout = (RelativeLayout) mCustomView.findViewById(R.id.cart_layout);


        mToolBar.addView(mCustomView);
        mCustomView.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNotification();
            }
        });

        mCustomView.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               gotoCart();
            }
        });
        int userType = BaseApplication.getBaseApplication().getUserType();
        cartLayout.setVisibility(View.GONE);
        if(userType != 0){
            cartLayout.setVisibility(View.GONE);
        }
        listenNotificationText.observe(getActivity(),new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                if(!changedValue.equals("0")){
                    count.setVisibility(View.VISIBLE);
                    count.setText(changedValue);
                }
            }
        });
        listenCartText.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.equals("0")){
                    cartLayout.setVisibility(View.VISIBLE);
                    cartCount.setVisibility(View.VISIBLE);
                    cartCount.setText(s);
                }
            }
        });
        title.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ROBOTO_BOLD));
        title.setText("Mini Crab");
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
    }


    @Override
    public void doBack() {

    }

    private void performGetItemsRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            getItemList();
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    //get food items from the database
    private void getItemList(){
        mDatabaseReference.addValueEventListener(valueEventListener);
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Item item = ds.getValue(Item.class);
                if(item != null){
                    mItemList.add(item);
                }
            }
            if(mItemList.size() > 0 ){
                showItemList(mItemList);
            }
            setProgressDialog(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            setProgressDialog(false);
        }
    };

    private  void initRecyclerViwe(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        itemListAdapter = new ItemListAdapter(mContext, new ArrayList<Item>());
        mRecyclerView.setAdapter(itemListAdapter);
    }
    private void showItemList(List<Item> itemList){
        //mItemList.clear();
        // mArticleList = articles;
        initRecyclerViwe();
        itemListAdapter.updateData(itemList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseHelper = new FirebaseHelper();
        mNotificationReference = FirebaseDatabase.getInstance().getReference("NotificationList");
        mCartReference = FirebaseDatabase.getInstance().getReference("cart");

        int userType = BaseApplication.getBaseApplication().getUserType();
        if(userType == 0){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("itemList");
            performGetItemsRequest();
            performGetCartRequest();
        }

        listenNotificationText.setValue("0");
        listenCartText.setValue("0");
    }

    public void gotoItemDetailScreen(Item item){
        ((MainActivity) getActivity()).addFragment(new ItemDetailsFragment().newInstance(item), ItemDetailsFragment.getTAG());
    }

    public void gotoNotification(){
        if (!BaseApplication.getBaseApplication().isLoadNotificationScreen()) {
            BaseApplication.getBaseApplication().setLoadNotificationScreen(true);
            ((MainActivity) getActivity()).addFragment(new NotificationFragment().newInstance(), NotificationFragment.getTAG());
        }
    }

    public void gotoCart(){
        isGotoCart = true;
        mCartReference.removeEventListener(cartListener);
        if (!BaseApplication.getBaseApplication().isLoadCartScreen()) {
            BaseApplication.getBaseApplication().setLoadCartScreen(true);
            ((MainActivity) getActivity()).replaceFragment(new CartFragment().newInstance(mCartList));
        }
    }

    private void performGetNotificationRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            //setProgressDialog(true);
            getNotificationList();
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    private void getNotificationList(){
        mNotificationReference.addValueEventListener(notificationListner);
    }

    ValueEventListener notificationListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int userType = BaseApplication.getBaseApplication().getUserType();
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Notification item = ds.getValue(Notification.class);
                if(item != null && item.getUserType() == userType){
                    notificationList.add(item);
                }
            }
            if(notificationList.size() > 0){
                listenNotificationText.setValue( notificationList.size()+"");
                notificationCount = notificationList.size()+"";
            }
            //setProgressDialog(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            setProgressDialog(false);
        }
    };


    private void performGetCartRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            //setProgressDialog(true);
            getCartList();
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    private void getCartList(){
        mCartReference.addValueEventListener(cartListener);
        mCartReference.keepSynced(true);
        //mCartReference.addChildEventListener(cartListener)
        //mCartReference.addListenerForSingleValueEvent(cartListener);
    }

    ValueEventListener cartListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mCartList.clear();
            cartCount = "0";
            listenCartText.setValue( "0");
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Item item = ds.getValue(Item.class);
                if(item.getPaid().equals("N")){
                    mCartList.add(item);
                }
            }
            if(mCartList.size() > 0){
                listenCartText.setValue( mCartList.size()+"");
                cartCount = mCartList.size()+"";
            }
            if(isGotoCart){
                performGetCartRequest();
                isGotoCart = false;
            }
            //setProgressDialog(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            setProgressDialog(false);
        }
    };


}
