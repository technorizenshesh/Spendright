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
           holder.binding.tvTitle1.setText(arrayList.get(position).getMessage());

       }
           else {
              // holder.binding.tvTitle.setText("₦"+arrayList.get(position).getTransactionAmount() + " for " + arrayList.get(position).getDescription());
              holder.binding.tvTitle1.setText(arrayList.get(position).getDescription());

       }

           if(arrayList.get(position).getTitle()!=null){
               holder.binding.tvTitle.setVisibility(View.VISIBLE);
               holder.binding.tvTitle.setText(arrayList.get(position).getTitle());

           }
           else {
               holder.binding.tvTitle.setVisibility(View.GONE);

           }


           if(arrayList.get(position).getDateTime()!=null) {
               holder.binding.txtTime.setText(Preference.convertDate222(arrayList.get(position).getDateTime()));
              Log.e("convert date===",Preference.convertDate222(arrayList.get(position).getDateTime()));

           }
           else  {
               holder.binding.txtTime.setText(Preference.getCurrentDate22());
               Log.e("system date===",Preference.getCurrentDate22());

           }

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
     // if(holder.binding.tvTitle1.getLineCount()>1)  makeTextViewResizable(holder,3,"Read more",true);

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

        if (holder.binding.tvTitle1.getTag() == null) {
            holder.binding.tvTitle1.setTag(holder.binding.tvTitle1.getText());
        }
        ViewTreeObserver vto = holder.binding.tvTitle1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text ="";
                int lineEndIndex =0;
                ViewTreeObserver obs = holder.binding.tvTitle1.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if (maxLine == 0) {
                    lineEndIndex = holder.binding.tvTitle1.getLayout().getLineEnd(0);
                    text = holder.binding.tvTitle1.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && holder.binding.tvTitle1.getLineCount() >= maxLine) {
                    lineEndIndex = holder.binding.tvTitle1.getLayout().getLineEnd(maxLine - 1);
                    text = holder.binding.tvTitle1.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                }
                else if ( holder.binding.tvTitle1.getLineCount()==1) {
                    lineEndIndex = holder.binding.tvTitle1.getLayout().getLineEnd(holder.binding.tvTitle1.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle1.getText().subSequence(0, lineEndIndex)+"";
                }

                else if ( holder.binding.tvTitle1.getLineCount()==2) {
                    lineEndIndex = holder.binding.tvTitle1.getLayout().getLineEnd(holder.binding.tvTitle1.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle1.getText().subSequence(0, lineEndIndex)+"";
                }

                else if (holder.binding.tvTitle1.getLineCount()>=3) {
                    lineEndIndex = holder.binding.tvTitle1.getLayout().getLineEnd(holder.binding.tvTitle1.getLayout().getLineCount() - 1);
                    text = holder.binding.tvTitle1.getText().subSequence(0, lineEndIndex - expandText.length() + 9) + " " + expandText;
                }
                Log.e("line size====",holder.binding.tvTitle1.getLineCount()+"");
                /*else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount());
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }*/
                holder.binding.tvTitle1.setText(text);
                holder.binding.tvTitle1.setMovementMethod(LinkMovementMethod.getInstance());
                holder.binding.tvTitle1.setText(
                        addClickablePartTextViewResizable(holder.binding.tvTitle1.getText().toString(), holder, lineEndIndex, expandText,
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
                    holder.binding.tvTitle1.setLayoutParams(holder.binding.tvTitle1.getLayoutParams());
                    holder.binding.tvTitle1.setText(holder.binding.tvTitle1.getTag().toString(), TextView.BufferType.SPANNABLE);
                    holder.binding.tvTitle1.invalidate();
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
