package com.kimvan.hung.vocabulary.testWord;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kimvan.hung.vocabulary.R;

import java.util.List;

/**
 * Created by h on 09/09/2017.
 */

public class ListGameAdapter extends RecyclerView.Adapter<ListGameAdapter.MyViewHolder> {
    private List<GameProperties> gameList;
    private Context mContext;
    LinearLayout.LayoutParams normalParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameGame,description;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.nameGame = (TextView)itemView.findViewById(R.id.name_of_the_game_item);
            this.description = (TextView)itemView.findViewById(R.id.game_description_item);

            itemView.setOnFocusChangeListener(
                    new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                // run scale animation and make it bigger
                                Animation anim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in);
                                itemView.startAnimation(anim);
                                anim.setFillAfter(true);
                            } else {
                                // run scale animation and make it smaller
                                Animation anim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_out);
                                itemView.startAnimation(anim);
                                anim.setFillAfter(true);
                            }
                        }
                    }
            );
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public ListGameAdapter(List<GameProperties> gameList) {
        this.gameList = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setmContext(parent.getContext());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_game,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GameProperties gameProperties = gameList.get(position);
        holder.nameGame.setText(gameProperties.getName_of_the_game());
        holder.description.setText(gameProperties.getDescription());

        holder.itemView.setLayoutParams(normalParams);

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
