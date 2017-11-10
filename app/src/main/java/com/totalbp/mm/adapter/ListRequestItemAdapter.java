package com.totalbp.mm.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.totalbp.mm.R;
import com.totalbp.mm.model.ListItemRequestOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezra.R on 10/10/2017.
 */

public class ListRequestItemAdapter extends RecyclerView.Adapter<ListRequestItemAdapter.MyViewHolder>{
    private Context mContext;
    private List<ListItemRequestOut> itemRequest;
    private MessageAdapterListener listener;
    private SparseBooleanArray selectedItems;
    private static int currentSelectedIndex = -1;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView tvItemName, tvTower, tvFloor, tvZona, tvMoCode, tvStatusRequest, tvCreatedDate;
        public RelativeLayout messageContainer;


        public MyViewHolder(View view) {
            super(view);

            tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            tvTower = (TextView) view.findViewById(R.id.tvTower);
            tvFloor = (TextView) view.findViewById(R.id.tvFloor);
            tvZona = (TextView) view.findViewById(R.id.tvZona);
            tvMoCode = (TextView) view.findViewById(R.id.tvMoCode);
            tvStatusRequest = (TextView) view.findViewById(R.id.tvStatusRequest);
            tvCreatedDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            messageContainer = (RelativeLayout) view.findViewById(R.id.message_container);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public ListRequestItemAdapter(Context mContext, List<ListItemRequestOut> itemrequest, MessageAdapterListener listener) {
        this.mContext = mContext;
        this.itemRequest = itemrequest;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item_request_out_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ListItemRequestOut rowItem = itemRequest.get(position);

        holder.tvItemName.setText(rowItem.getDeskripsi());
        holder.tvTower.setText(rowItem.getNama_Tower());
        holder.tvFloor.setText(rowItem.getNama_Lantai());
        holder.tvZona.setText(rowItem.getNama_Zona());
        holder.tvMoCode.setText(rowItem.getKode_MO());
        if(rowItem.getStatusRequest().equals(""))
        {   holder.tvStatusRequest.setBackgroundResource(R.drawable.btn_yellowtrans);
            holder.tvStatusRequest.setText("NEW REQUEST");
        }
        else if(rowItem.getStatusRequest().equals("Waiting for Approval"))
        {
            holder.tvStatusRequest.setBackgroundResource(R.drawable.btn_orangetrans);
            holder.tvStatusRequest.setText("WAITING FOR APPROVAL");
        }
        else if(rowItem.getStatusRequest().equals("APPROVED"))
        {
            holder.tvStatusRequest.setBackgroundResource(R.drawable.btn_bluetrans);
            holder.tvStatusRequest.setText("APPROVED");
        }
        else if(rowItem.getStatusRequest().equals("REJECTED"))
        {
            holder.tvStatusRequest.setBackgroundResource(R.drawable.btn_redtrans);
            holder.tvStatusRequest.setText("REJECTED");
        }
        else if(rowItem.getStatusRequest().equals("ON PROGRESS"))
        {
            holder.tvStatusRequest.setBackgroundResource(R.drawable.btn_orangetrans);
            holder.tvStatusRequest.setText("ON PROGRESS");
        }



        holder.tvCreatedDate.setText(rowItem.getTanggal_Dibuat());

        //applyFile(holder, rowItem);
        // handle icon animation
        //applyIconAnimation(holder, position);

        // display profile image
        //applyProfilePicture(holder, rowItem);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {


        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });
         /*
        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
        */
    }

    private void applyFile(MyViewHolder holder, ListItemRequestOut message) {
        /*
        if (!message.getFileUploadUrl().equals("")) {
            holder.ivFile.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_insert_drive_file_black_24dp));
            holder.ivFile.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        } else {
            holder.ivFile.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_attach_file_black_24dp));
            //holder.ivFile.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        }
        */
    }

    private void applyImportant(MyViewHolder holder, ListItemRequestOut message) {
        /*
        if (message.isImm()) {
            holder.ivStar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
            holder.ivStar.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        } else {
            holder.ivStar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
            //holder.ivStar.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        }
        */
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        /*
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
        */
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    //@Override
    //public long getItemId(int position) {
//        return messages.get(position).getId();
//    }



    @Override
    public int getItemCount() {
        return itemRequest.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        itemRequest.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface MessageAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }

}
