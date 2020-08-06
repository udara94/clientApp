package com.example.clientapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientapp.R;
import com.example.clientapp.common.CommonUtils;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.model.dto.Item;
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
    private Context mContext;

    RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;


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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("itemList");
        performGetItemsRequest();
    }



    @Override
    protected void setUpToolBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.custom_action_bar_home, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        mToolBar.addView(mCustomView);
        mCustomView.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mCustomView.findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).openDrawer();
            }
        });
        title.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ROBOTO_BOLD));
        title.setText("Ministry of Crab");
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

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
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
        });
    }

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

    public void gotoItemDetailScreen(Item item){
        ((MainActivity) getActivity()).addFragment(new ItemDetailsFragment().newInstance(item), ItemDetailsFragment.getTAG());
    }
}
