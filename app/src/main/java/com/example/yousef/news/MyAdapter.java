package com.example.yousef.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Story> news;
    private Context context;

     MyAdapter(Context context, List<Story> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView sectionName;
        TextView date;
        TextView author;
        Story story;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            sectionName = itemView.findViewById(R.id.txt_sectionName);
            date = itemView.findViewById(R.id.txt_date);
            author = itemView.findViewById(R.id.txt_author);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(story.getWebUrl()));
            context.startActivity(intent);
        }

        void bind(Story story) {
            title.setText(story.getWebTitle());
            sectionName.setText(story.getSectionName());
            date.setText(story.getFormattedDate());
            author.setText(story.getAuthor());
            this.story = story;
        }
    }
}
