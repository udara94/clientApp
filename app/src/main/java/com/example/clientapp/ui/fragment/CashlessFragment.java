package com.example.clientapp.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.common.CommonUtils;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashlessFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = CashlessFragment.this.getClass().getSimpleName();

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static String getTAG() {
        return "CashlessFragment";
    }

    public static CashlessFragment newInstance(Item item) {
        CashlessFragment fragment = new CashlessFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    public static CashlessFragment cashlessFragment;
    private Item mItem;
    private DatabaseReference mDatabaseReference;


    @BindView(R.id.btn_proceed) Button btnProceed;
    @BindView(R.id.imageView) ImageView imageView;

    String mId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_cashless, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            cashlessFragment = this;
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
                addToCart();
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
            }
        });
        generateBarcode();
    }

    //add to items to the db
    private  void addToCart(){
       // String id = mDatabaseReference.push().getKey();
        if(mId != null){
            String id = mId;
            mItem.setId(id);
            System.out.println("==============>>id"+ id);
            mDatabaseReference.child(id).setValue(mItem);
            Toast.makeText(getActivity(), "Item added to the cart", Toast.LENGTH_LONG).show();
        }

    }

    private void generateBarcode(){
        String id = mDatabaseReference.push().getKey();
        mId = id;
        System.out.println("==============>>id"+ mId);
        String text = id; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
        title.setText("Cashless Payment");
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
        cashlessFragment = null;
        BaseApplication.getBaseApplication().setLoadCashlessScreen(false);
    }

}
