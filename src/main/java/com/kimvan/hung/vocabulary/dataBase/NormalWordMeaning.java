package com.kimvan.hung.vocabulary.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by h on 28/08/2017.
 */

public class NormalWordMeaning implements Parcelable{

    private String _id;
    private String _stt;
    private String _enAnglais,_enVietnamien;

    public NormalWordMeaning( String _enAnglais, String _enVietnamien) {

        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }



    public NormalWordMeaning(String _stt, String _enAnglais, String _enVietnamien) {
        this._stt = _stt;
        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }

    public NormalWordMeaning(String _id, String _stt, String _enAnglais, String _enVietnamien) {
        this._id = _id;
        this._stt = _stt;
        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }

    public NormalWordMeaning(){

    }

    public NormalWordMeaning(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_stt() {
        return _stt;
    }

    public void set_stt(String _stt) {
        this._stt = _stt;
    }

    public String get_enAnglais() {
        return _enAnglais;
    }

    public void set_enAnglais(String _enAnglais) {
        this._enAnglais = _enAnglais;
    }

    public String get_enVietnamien() {
        return _enVietnamien;
    }

    public void set_enVietnamien(String _enVietnamien) {
        this._enVietnamien = _enVietnamien;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(get_stt()),
                get_enAnglais(),get_enVietnamien()});
    }

    public NormalWordMeaning(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        set_stt(data[0]);
        set_enAnglais(data[1]);
        set_enVietnamien(data[2]);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NormalWordMeaning createFromParcel(Parcel in) {
            return new NormalWordMeaning(in);
        }

        public NormalWordMeaning[] newArray(int size) {
            return new NormalWordMeaning[size];
        }
    };
}
