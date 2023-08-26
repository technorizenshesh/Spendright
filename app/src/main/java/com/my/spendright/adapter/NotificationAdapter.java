package com.my.spendright.adapter;

import android.content.Context;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.spendright.Model.NotificationModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ItemNotificationsBinding;
import com.my.spendright.utils.MySpannable;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    ArrayList<NotificationModel.Result>arrayList;
    boolean chk =false;

    public NotificationAdapter(Context context, ArrayList<NotificationModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notifications,parent,false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       if(arrayList.get(position).getNotificationType().equalsIgnoreCase("SENDT_BY_ADMIN"))   {
           holder.binding.tvTitle.setText(arrayList.get(position).getMessage());
          // holder.binding.tvTitle1.setText(arrayList.get(position).getMessage());

       }
           else {
              // holder.binding.tvTitle.setText("₦"+arrayList.get(position).getTransactionAmount() + " for " + arrayList.get(position).getDescription());
              holder.binding.tvTitle.setText(arrayList.get(position).getDescription());

       }
      holder.binding.txtTime.setText(arrayList.get(position).getDateTime());
/*
      if(arrayList.get(position).isChk()==false) {
          holder.binding.tvTitle.setVisibility(View.VISIBLE);
          holder.binding.tvTitle1.setVisibility(View.GONE);

      }
      else{
          holder.binding.tvTitle.setVisibility(View.GONE);
          holder.binding.tvTitle1.setVisibility(View.VISIBLE);      }*/


      /*  if(!arrayList.get(position).getEmoji().equalsIgnoreCase("")){
            holder.binding.tvImg.setVisibility(View.VISIBLE);
            holder.binding.img1.setVisibility(View.GONE);
            holder.binding.tvImg.setText(Preference.decodeEmoji(arrayList.get(position).getEmoji()));
            // genericViewHolder.img1.setImageBitmap(Preference.drawTextToBitmap(mContext,R.id.img1,model.getEmoji()));
        }
        else {
            holder.binding.tvImg.setVisibility(View.GONE);
            holder.binding.img1.setVisibility(View.VISIBLE);
        }*/

      /*  holder.binding.txtPrice.setOnClickListener(view -> {
           for (int i =0;i<arrayList.size();i++){
               arrayList.get(i).setChk(false);
           }
           arrayList.get(position).setChk(true);
           notifyDataSetChanged();
        });*/
//      Log.e("line count===",holder.binding.tvTitle.getLayout().getLineCount()+"");
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.logo_one)
                        .error(R.mipmap.logo_one).into(holder.binding.img1);
        makeTextViewResizable(holder,3,"Read more",true);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationsBinding binding;
        public MyViewHolder(@NonNull ItemNotificationsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public static void makeTextViewResizable(final MyViewHolder holder, final int maxLine, final String expandText, final boolean viewMore) {

        if (holder.binding.tvTitle.getTag() == null) {
            holder.binding.tvTitle.setTag(holder.binding.tvTitle.getText());
        }
        ViewTreeObserver vto = holder.binding.tvTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text ="";
                int lineEndIndex =0;
                ViewTreeObserver obs = holder.binding.tvTitle.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if (maxLine == 0) {
                    lineEndIndex = holder.binding.tvTitle.getLayout().getLineEnd(0);
                    text = holder.binding.tvTitle.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && holder.binding.tvTitle.getLineCount() >= maxLine) {
                    lineEndIndex = holder.binding.tvTitle.getLayout().getLineEnd(maxLine - 1);
                    text = holder.binding.tvTitle.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                }
                else if ( holder.binding.tvTitle.getLineCount()==1) {
                    lineEndIndex = holder.binding.tvTitle.getLayout().getLineEnd(holder.binding.tvTitle.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle.getText().subSequence(0, lineEndIndex)+"";
                }

                else if ( holder.binding.tvTitle.getLineCount()==2) {
                    lineEndIndex = holder.binding.tvTitle.getLayout().getLineEnd(holder.binding.tvTitle.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle.getText().subSequence(0, lineEndIndex)+"";
                }

                else if (holder.binding.tvTitle.getLineCount()>=3) {
                    lineEndIndex = holder.binding.tvTitle.getLayout().getLineEnd(holder.binding.tvTitle.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                }
                Log.e("line size====",holder.binding.tvTitle.getLineCount()+"");
                /*else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount());
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }*/
                holder.binding.tvTitle.setText(text);
                holder.binding.tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
                holder.binding.tvTitle.setText(
                        addClickablePartTextViewResizable(holder.binding.tvTitle.getText().toString(), holder, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final String strSpanned, final MyViewHolder holder,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned;
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(true) {

                @Override
                public void onClick(View widget) {
                    holder.binding.tvTitle.setLayoutParams(holder.binding.tvTitle.getLayoutParams());
                    holder.binding.tvTitle.setText(holder.binding.tvTitle.getTag().toString(), TextView.BufferType.SPANNABLE);
                    holder.binding.tvTitle.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(holder, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(holder, 3, "Read more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

}
