package com.example.clientapp.ui.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.model.dto.Notification;
import com.example.clientapp.utils.BaseBackPressedListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = OrderDetailsFragment.this.getClass().getSimpleName();

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static String getTAG() {
        return "OrderDetailsFragment";
    }

    public static OrderDetailsFragment newInstance(Item item) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderDetailsFragment orderDetailsFragment;

    private  Item mItem;
    private int mQuantity;
    private ImageLoader imageLoader;
    private DatabaseReference mDatabaseReference;
    private AlertDialog aDialog;

    @BindView(R.id.txt_item_name)
    TextView txtItemName;
    @BindView(R.id.txt_item_code) TextView txtItemCode;
    @BindView(R.id.txt_item_price) TextView txtItemPrice;
    //@BindView(R.id.txt_description) TextView txtItemDescription;

    @BindView(R.id.header_image) ImageView mImageView;
    @BindView(R.id.image_progress) ProgressBar imageProgress;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.btn_action) Button btnAction;

    @BindView(R.id.txt_quantity_order) EditText txtQuantity;

    @BindView(R.id.txt_total_amount) TextView txtTotal;
    @BindView(R.id.txt_table_no) EditText txtTableNo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
            this.imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_order_details, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            orderDetailsFragment = this;
            collapsingToolbarLayout.setTitle(" ");
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
        if (mItem != null) {

            txtQuantity.setText(mItem.getItemQty() +"");
            mQuantity = mItem.getItemQty();
            txtTableNo.setText(mItem.getTableNo()+ "");
            setTotalAmount(mQuantity);
            setDoneButton();
            mImageView.setImageResource(R.drawable.icon);
            if (mItem.getItemImg() != null && !mItem.getItemImg().isEmpty()) {
                ImageLoader.getInstance().displayImage(mItem.getItemImg(), mImageView, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.GONE);
                        if (mItem.getItemImg().isEmpty())
                            mImageView.setImageResource(R.drawable.icon); // clear imageview for no images
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.GONE);
                    }
                });
            } else {
                if (imageProgress != null)
                    imageProgress.setVisibility(View.GONE);
                if (mImageView != null && mItem.getItemImg() != null && mItem.getItemImg().isEmpty())
                    mImageView.setImageResource(R.drawable.icon); // clear imageview for no images
            }

            if (mItem.getItemName() != null && !mItem.getItemName().isEmpty()) {
                txtItemName.setText(mItem.getItemName());
            }

            if (mItem.getItemCode() != null && !mItem.getItemCode().isEmpty())
                txtItemCode.setText(mItem.getItemCode());


            if (mItem.getItemPrice() != null && !mItem.getItemPrice().isEmpty()) {
                txtItemPrice.setText(mItem.getItemPrice());
            }

            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userType = BaseApplication.getBaseApplication().getUserType();
                    if(userType == 1){
                        showCashierDialog();
                    }else {
                        sendToCustomer();
                    }

                }
            });
        }

    }

    private void setDoneButton(){
        int userType = BaseApplication.getBaseApplication().getUserType();
        switch (userType){
            case 1:
                btnAction.setText("Action");
                break;
            case 2:
                btnAction.setText("Finish Order");
                break;
        }
    }

    public void showCashierDialog(){
        if(aDialog != null)aDialog.dismiss();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert_cashier, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final Button addToKitchen = (Button) dialogView.findViewById(R.id.add_to_kitchen);
        final Button outOfStock = (Button) dialogView.findViewById(R.id.out_of_stock);



        outOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aDialog != null)aDialog.dismiss();
                outOfStocks();

            }
        });
        addToKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aDialog != null)aDialog.dismiss();
                sendToKitchen();
            }
        });

        aDialog = dialogBuilder.create();
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aDialog.show();
        int alertWidth = (int) getResources().getDimension(R.dimen.alert_width_size);
        int alertHeight = (int) getResources().getDimension(R.dimen.alert_height_size);

        aDialog.getWindow().setLayout(alertWidth, alertHeight);
    }

    private void sendToKitchen(){
        Notification notification = new Notification();
        notification.setUserType(2);
        notification.setCartId(mItem.getId());
        notification.setMessage("Prepare Order");
        notification.setOrderNo(mItem.getTableNo());
        notification.setTableNo(mItem.getTableNo());
        mDatabaseReference.child(mItem.getId()).setValue(notification);
        if(NotificationFragment.notificationFragment != null){
            NotificationFragment.notificationFragment.gobackAndRefresh();
        }
        getFragmentManager().popBackStack();
    }



    private void outOfStocks(){
        Notification notification = new Notification();
        notification.setUserType(0);
        notification.setCartId(mItem.getId());
        notification.setMessage("Out of Stocks");
        notification.setOrderNo(mItem.getTableNo());
        notification.setTableNo(mItem.getTableNo());
        mDatabaseReference.child(mItem.getId()).setValue(notification);
        if(NotificationFragment.notificationFragment != null){
            NotificationFragment.notificationFragment.gobackAndRefresh();
        }
        getFragmentManager().popBackStack();
    }

    private void sendToCustomer(){
        Notification notification = new Notification();
        notification.setUserType(0);
        notification.setCartId(mItem.getId());
        notification.setMessage("Robot will send your order");
        notification.setOrderNo(mItem.getTableNo());
        notification.setTableNo(mItem.getTableNo());
        mDatabaseReference.child(mItem.getId()).setValue(notification);
        if(NotificationFragment.notificationFragment != null){
            NotificationFragment.notificationFragment.gobackAndRefresh();
        }
        getFragmentManager().popBackStack();
    }

    private void setTotalAmount(int quantity){
        float itemprice = Float.parseFloat(mItem.getItemPrice());
        float totalPrice = itemprice * quantity;
        String s = String.format("%.2f", totalPrice);
        txtTotal.setText("Rs "+s+ "/=");
        mItem.setItemQty(quantity);
    }
    @Override
    protected void setUpToolBar() {

    }

    @Override
    public void doBack() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BaseApplication.getBaseApplication().setLoadOrderDetailsScreen(false);
    }
}
