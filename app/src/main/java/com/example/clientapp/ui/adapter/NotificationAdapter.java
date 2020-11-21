package com.example.clientapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;
import com.example.clientapp.model.dto.Item;
import com.example.clientapp.model.dto.Notification;
import com.example.clientapp.ui.fragment.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Notification> notifocationList = new ArrayList<>();

    public NotificationAdapter(Context mContext, List<Notification> notifocationList) {
        this.mContext = mContext;
        this.notifocationList = notifocationList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification, parent, false);
        ItemRowHolder itemRowHolder = new ItemRowHolder(inflate);
        return itemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemRowHolder){
            final ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
            final Notification item = notifocationList.get(position);

                if(item.getOrderNo() != null && !item.getOrderNo().isEmpty()){
                    itemRowHolder.txtOrderNo.setText(item.getOrderNo());
                }

                if(item.getTableNo() != null && !item.getTableNo().isEmpty()){
                    itemRowHolder.txtTableNo.setText(item.getTableNo());
                }

                if(item.getMessage() != null && !item.getMessage().isEmpty()){
                    itemRowHolder.txtMessage.setText(item.getMessage());
                    if(item.getMessage().equals("Out of Stocks")){
                        itemRowHolder.btnCancelOrder.setVisibility(View.VISIBLE);
                        itemRowHolder.btnCancelOrder.setText("Cancel Order");
                        itemRowHolder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(NotificationFragment.notificationFragment != null){
                                    NotificationFragment.notificationFragment.cancelOrder(item);
                                }
                            }
                        });
                    }
                    if(item.getMessage().equals("Robot will send your order")){
                        itemRowHolder.btnCancelOrder.setVisibility(View.VISIBLE);
                        itemRowHolder.btnCancelOrder.setText("Collect Order");
                        itemRowHolder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(NotificationFragment.notificationFragment != null){
                                    NotificationFragment.notificationFragment.cancelOrder(item);
                                }
                            }
                        });
                    }
                }

                itemRowHolder.parentContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int userType = BaseApplication.getBaseApplication().getUserType();
                        if(userType == 1 || userType == 2){
                            if(NotificationFragment.notificationFragment != null){
                                NotificationFragment.notificationFragment.getCartItem(item);
                            }
                        }
                    }
                });

        }
    }

    @Override
    public int getItemCount() {
        return (null != notifocationList ? notifocationList.size() : 0);
    }

    public void updateData(List<Notification> messageList, int flag) {
        if(flag == 0){
            for (int i = 0; i < messageList.size(); i++) {
                notifocationList.add(messageList.get(i));
                notifyItemInserted(getItemCount());
            }
        }else {
            notifocationList.clear();
            notifyDataSetChanged();
        }

    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.txt_source) TextView sourceName;
        @BindView(R.id.txt_order_number) TextView txtOrderNo;
        @BindView(R.id.txt_table_number) TextView txtTableNo;
        @BindView(R.id.txt_message) TextView txtMessage;
        @BindView(R.id.parent_container) RelativeLayout parentContainer;
        @BindView(R.id.btn_cancel_order) Button btnCancelOrder;


        public ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
