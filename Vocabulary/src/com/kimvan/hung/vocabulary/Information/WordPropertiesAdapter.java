package com.kimvan.hung.vocabulary.Information;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by h on 05/09/2017.
 */

public class WordPropertiesAdapter extends RecyclerView.Adapter<WordPropertiesAdapter.MyViewHolder> {

    private List<NouveauMot> nouveauMotList;
    private int curPosition;

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    LinearLayout.LayoutParams normalLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView leMot,typeWord,lv2,expertPoint,shape;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(leMot.getText().toString());
            menu.add(0,0,0,"Delete");
            menu.add(0,1,0,"Edit");
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            this.leMot = (TextView)itemView.findViewById(R.id.le_mot_item_list_view_txt);
            this.typeWord = (TextView)itemView.findViewById(R.id.type_word_item_list_view);
            this.lv2 = (TextView)itemView.findViewById(R.id.lv2_item_list_view);
            this.expertPoint = (TextView)itemView.findViewById(R.id.expert_point_txt);
            this.shape = (TextView)itemView.findViewById(R.id.shape_expert_point);

            itemView.setOnCreateContextMenuListener(this);
        }
    }

    public WordPropertiesAdapter(List<NouveauMot> nouveauMotList) {
        this.nouveauMotList = nouveauMotList;
    }

    @Override
    public WordPropertiesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordPropertiesAdapter.MyViewHolder holder, int position) {
        NouveauMot nouveauMot = nouveauMotList.get(position);

        holder.leMot.setText(nouveauMot.get_leMot());
        holder.typeWord.setText(nouveauMot.get_type_word());
        holder.lv2.setText(nouveauMot.get_lv2());
        holder.expertPoint.setText(String.valueOf(nouveauMot.get_expert_point()));

        nouveauMot.setColorExpert(holder.shape);

        holder.itemView.setLayoutParams(normalLayoutParams);
    }

    @Override
    public int getItemCount() {
        return nouveauMotList.size();
    }


}
