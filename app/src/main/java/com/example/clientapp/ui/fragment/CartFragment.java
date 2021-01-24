package com.example.clientapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.common.CommonUtils;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.ui.activity.MainActivity;
import com.example.clientapp.ui.adapter.CartAdapter;
import com.example.clientapp.ui.adapter.ItemListAdapter;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = CartFragment.this.getClass().getSimpleName();
    public static String getTAG() {
        return "CartFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static CartFragment newInstance(List<Item> item) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    public static CartFragment cartFragment;
    private List<Item> mItemList = new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    RecyclerView mRecyclerView;
    private CartAdapter cartAdapter;
    private Context mContext;

    @BindView(R.id.btn_pay_now) Button btnPayNow;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemList = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_cart, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            cartFragment = this;
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            ((MainActivity) getActivity()).setOnBackPressedListener(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    protected void setUpUI() {
        if(mItemList != null && mItemList.size() > 0){
            showItemList(mItemList);
            btnPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       gotoPaymentDetailsScreen(mItemList);
                }
            }
            );
        }

    }
    public void gotoPaymentDetailsScreen(List<Item> itemList){
        ((MainActivity) getActivity()).addFragment(new PaymentFragment().newInstance(itemList), PaymentFragment.getTAG());
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

    private void showItemList(List<Item> itemList){
        //mItemList.clear();
        // mArticleList = articles;
        initRecyclerViwe();
        cartAdapter.updateData(null, 1);
        cartAdapter.updateData(itemList, 0);

    }

    private  void initRecyclerViwe(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        cartAdapter = new CartAdapter(mContext, new ArrayList<Item>());
        mRecyclerView.setAdapter(cartAdapter);
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        mToolBar.addView(mCustomView);
        mCustomView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(new HomeFragment().newInstance(), HomeFragment.getTAG());
            }
        });
        title.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ROBOTO_BOLD));
        title.setText("Cart");
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cartFragment = null;
        BaseApplication.getBaseApplication().setLoadCartScreen(false);
    }

    @Override
    public void doBack() {

    }
}
