package com.example.clientapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.common.CommonUtils;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.ui.activity.MainActivity;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = PaymentFragment.this.getClass().getSimpleName();

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static String getTAG() {
        return "ItemDetailsFragment";
    }

    public static PaymentFragment newInstance(Item item) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentFragment paymentFragment;
    private  Item mItem;

    @BindView(R.id.btn_proceed) Button btnProceed;
    @BindView(R.id.radio_cash_payment) RadioButton radioCash;
    @BindView(R.id.radio_casheless_payment) RadioButton radioCashless;

    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_payment, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            paymentFragment = this;
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

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("cart");
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doProceedAction();
            }
        });
    }

    private void doProceedAction(){
        if(radioCash.isChecked()){
            addToCart();
            getFragmentManager().popBackStack();
            getFragmentManager().popBackStack();
        }else {
            if (!BaseApplication.getBaseApplication().isLoadCashlessScreen()) {
                BaseApplication.getBaseApplication().setLoadCashlessScreen(true);
                gotoCashlessFragment();
            }
        }
    }

    //add to items to the db
    private  void addToCart(){
        String id = mDatabaseReference.push().getKey();
        mItem.setId(id);
        mDatabaseReference.child(id).setValue(mItem);
        Toast.makeText(getActivity(), "Item added to the cart", Toast.LENGTH_LONG).show();
    }

    private void  gotoCashlessFragment(){
        ((MainActivity) getActivity()).addFragment(new CashlessFragment().newInstance(mItem), CashlessFragment.getTAG());
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
        title.setText("Payment Details");
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
        paymentFragment = null;
        BaseApplication.getBaseApplication().setLoadPaymentDetailsScreen(false);
    }
}
