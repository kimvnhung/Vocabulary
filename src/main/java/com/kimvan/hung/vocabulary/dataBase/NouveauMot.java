package com.kimvan.hung.vocabulary.dataBase;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by h on 22/08/2017.
 */

public class NouveauMot implements Parcelable{

    private String _id;
    private String _leMot;
    private String _type_word;
    private int _expert_point;
    private String _lv2;


    public NouveauMot() {
    }

    public NouveauMot(String _leMot, String _type_word, String _lv2) {
        this._leMot = _leMot;
        this._type_word = _type_word;
        this._lv2 = _lv2;
        this._expert_point = 100;
    }

    public NouveauMot(String _id, String _leMot, String _type_word,String _lv2 , int _expert_point) {
        this._id = _id;
        this._leMot = _leMot;
        this._type_word = _type_word;
        this._expert_point = _expert_point;
        this._lv2 = _lv2;
    }

    public int get_expert_point() {
        return _expert_point;
    }

    public void set_expert_point(int _expert_point) {
        this._expert_point = _expert_point;
    }

    public String get_lv2() {
        return _lv2;
    }

    public void set_lv2(String _lv2) {
        this._lv2 = _lv2;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_leMot() {
        return _leMot;
    }

    public void set_leMot(String _leMot) {
        this._leMot = _leMot;
    }

    public String get_type_word() {
        return _type_word;
    }

    public void set_type_word(String _type_word) {
        this._type_word = _type_word;
    }

    public void setColorExpert(View view){
        int point = get_expert_point();
       try {
           GradientDrawable gd = (GradientDrawable)view.getBackground().getCurrent();
           gd.setColor(Color.parseColor(getColorExpert(point)));
       }catch (Exception e){
            TextView para = (TextView)view;
           para.setTextColor(Color.parseColor(getColorExpert(point)));
       }
    }

    public String getColorExpert(int point){
        String[] listColor={
                "#79f480",
                "#2dc937",
                "#99c140",
                "#e7b416",
                "#db7b2b",
                "#cc3232",
                "#8b1a1a"
        };

        if (point==0){
            return listColor[0];
        }else if (point<=20){
            return listColor[1];
        }else if (point<=40){
            return listColor[2];
        }else if (point<=60){
            return listColor[3];
        }else if (point<=80){
            return listColor[4];
        }else if (point<=100){
            return listColor[5];
        }
        return listColor[6];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NouveauMot createFromParcel(Parcel in) {
            return new NouveauMot(in);
        }

        public NouveauMot[] newArray(int size) {
            return new NouveauMot[size];
        }
    };

    public NouveauMot(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        set_id(data[0]);
        set_leMot(data[1]);
        set_type_word(data[2]);
        set_lv2(data[3]);
        set_expert_point(Integer.parseInt(data[4]));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(get_id()),get_leMot(),
                get_type_word(),get_lv2(),String.valueOf(get_expert_point())});
    }
}
