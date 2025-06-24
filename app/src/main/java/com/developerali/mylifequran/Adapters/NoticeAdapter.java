package com.developerali.mylifequran.Adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.mylifequran.Models.NotificationModel;
import com.developerali.mylifequran.R;
import com.developerali.mylifequran.WebViewActivity;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

    Context context;
    ArrayList<NotificationModel> models;
    public NoticeAdapter(Context context, ArrayList<NotificationModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotificationModel ncm = models.get(position);
        holder.title.setText(ncm.getTitle());
        holder.content.setText(ncm.getContent());
        holder.date.setText(ncm.getDate());

        if (ncm.getLink().equalsIgnoreCase("null")){
            holder.link.setVisibility(View.GONE);
        }else {
            holder.link.setVisibility(View.VISIBLE);
            holder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context.getApplicationContext(), WebViewActivity.class);
                    i.putExtra("share", ncm.getLink());
                    context.startActivity(i);
                }
            });
        }

        try {
            if (ncm.getDate().equalsIgnoreCase(models.get(position - 1).getDate())){
                holder.date.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }

        holder.content.setOnClickListener(v->{
            String text = holder.title.getText().toString() + "\n\n" + holder.content.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied", text);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            clipboard.setPrimaryClip(clip);
        });


        boolean isExpended = models.get(position).isExpand();
        holder.exapndView.setVisibility(!isExpended ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, date, content;
        LinearLayout exapndView;
        ImageView delete;
        CardView link;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_description);
            date = itemView.findViewById(R.id.notice_date);
            delete = itemView.findViewById(R.id.delete_notice);
            exapndView = itemView.findViewById(R.id.expanded_view);
            link = itemView.findViewById(R.id.openWeb);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModel nk = models.get(getAdapterPosition());
                    nk.setExpand(!nk.isExpand());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
