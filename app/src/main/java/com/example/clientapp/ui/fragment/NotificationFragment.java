package com.example.clientapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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
import com.example.clientapp.ui.adapter.NotificationAdapter;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = NotificationFragment.this.getClass().getSimpleName();
    public static String getTAG() {
        return "NotificationFragment";
    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    public static NotificationFragment notificationFragment;


    private NotificationAdapter notificationAdapter;
    private List<Notification> mItemList = new ArrayList<>();
    private Context mContext;
    private Item mItem;
    RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mCartReference;


    @BindView(R.id.refresh_layout) NestedScrollView nestedScrollView;
    @BindView(R.id.empty_feed) RelativeLayout emptyFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_notification, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            notificationFragment = this;
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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("NotificationList");
        mCartReference = FirebaseDatabase.getInstance().getReference("cart");
        initRecyclerViwe();
        performGetItemsRequest();
    }

    public void gobackAndRefresh(){
        performGetItemsRequest();
        mDatabaseReference.removeEventListener(notificationListner);
        //getFragmentManager().popBackStack();
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

    private void getItemList(){
        resetRecyclerView();
        mDatabaseReference.addValueEventListener(notificationListner);
    }

    private void resetRecyclerView(){
        mItemList.clear();
        if(notificationAdapter != null){
            notificationAdapter.updateData(null, 1);
            toggleView(true);
        }
    }


    private void showItemList(List<Notification> itemList){
        notificationAdapter.updateData(null, 1);
        itemList = filterNotification(itemList);
        if(itemList != null && itemList.size()> 0){
            toggleView(false);
            notificationAdapter.updateData(itemList, 0);
        }else {
            toggleView(true);
        }
    }

    private List<Notification> filterNotification(List<Notification> itemList){
        List<Notification> fileredList = new ArrayList<>();
        int userType = BaseApplication.getBaseApplication().getUserType();
        for (Notification item: itemList) {
            if(item.getUserType() == userType){
                fileredList.add(item);
            }
        }
        return  fileredList;

    }

    private void toggleView(boolean isEmpty){
        if(isEmpty){
            nestedScrollView.setVisibility(View.GONE);
            emptyFeed.setVisibility(View.VISIBLE);
        }else {
            nestedScrollView.setVisibility(View.VISIBLE);
            emptyFeed.setVisibility(View.GONE);
        }
    }
    private  void initRecyclerViwe(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        notificationAdapter = new NotificationAdapter(mContext, new ArrayList<Notification>());
        mRecyclerView.setAdapter(notificationAdapter);
    }


    @Override
    protected void setUpToolBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        mToolBar.addView(mCustomView);

        mCustomView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getFragmentManager().popBackStack();
            }
        });
        title.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ROBOTO_BOLD));
        title.setText("Notification");
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void doBack() {
        getFragmentManager().popBackStack();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BaseApplication.getBaseApplication().setLoadNotificationScreen(false);
        mDatabaseReference.removeEventListener(notificationListner);
        mCartReference.removeEventListener(cartItemListner);
        notificationFragment = null;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    public void getCartItem(Notification notification){
        mCartReference.child(notification.getCartId()).addValueEventListener(cartItemListner);
    }

    public void cancelOrder(Notification notification){
        mDatabaseReference.removeEventListener(notificationListner);
        mCartReference.removeEventListener(cartItemListner);
        mCartReference.child(notification.getCartId()).removeValue();
        mDatabaseReference.child(notification.getCartId()).removeValue();
        resetRecyclerView();
        performGetItemsRequest();
    }

    private void gotoOrderDetails(Item item){
        mCartReference.removeEventListener(cartItemListner);
        if (!BaseApplication.getBaseApplication().isLoadOrderDetailsScreen()) {
            BaseApplication.getBaseApplication().setLoadOrderDetailsScreen(true);
            ((MainActivity) getActivity()).addFragment(new OrderDetailsFragment().newInstance(item), OrderDetailsFragment.getTAG());
        }
    }

    ValueEventListener notificationListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Notification item = ds.getValue(Notification.class);
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

    ValueEventListener cartItemListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mItem = dataSnapshot.getValue(Item.class);
            if(mItem != null)
                gotoOrderDetails(mItem);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            setProgressDialog(false);
        }
    };

}
