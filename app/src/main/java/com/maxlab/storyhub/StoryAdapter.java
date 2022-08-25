package com.maxlab.storyhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    Context context;
    ArrayList<StoryModel> storyModels;

    public  StoryAdapter(Context context, ArrayList<StoryModel> storyModels){
        this.context = context;
        this.storyModels = storyModels;

    }
    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,null);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

        StoryModel model = storyModels.get(position);
        holder.textView.setText(model.getStoryName());
        Glide.with(context).load(model.getStoryImage())
        .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryActivity.class);
                intent.putExtra("catid",model.getStoryId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return storyModels.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.storyImage);
            textView = itemView.findViewById(R.id.storyName);
        }
    }


}
