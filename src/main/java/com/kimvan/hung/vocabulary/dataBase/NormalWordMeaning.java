package com.kimvan.hung.vocabulary.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by h on 28/08/2017.
 */

public class NormalWordMeaning implements Parcelable{

    private int _stt;
    private String nameWord;
    private String _enAnglais,_enVietnamien;

    public NormalWordMeaning(String nameWord, String _enAnglais, String _enVietnamien) {
        this.nameWord = nameWord;
        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }



    public NormalWordMeaning(int _stt, String nameWord, String _enAnglais, String _enVietnamien) {
        this._stt = _stt;
        this.nameWord = nameWord;
        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }

    public NormalWordMeaning(){

    }

    public NormalWordMeaning(String _enAnglais, String _enVietnamien) {
        this._enAnglais = _enAnglais;
        this._enVietnamien = _enVietnamien;
    }

    public String getNameWord() {
        return nameWord;
    }

    public void setNameWord(String nameWord) {
        this.nameWord = nameWord;
    }

    public int get_stt() {
        return _stt;
    }

    public void set_stt(int _stt) {
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
        dest.writeStringArray(new String[]{String.valueOf(get_stt()),getNameWord(),
                get_enAnglais(),get_enVietnamien()});
    }

    public NormalWordMeaning(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        set_stt(Integer.parseInt(data[0]));
        setNameWord(data[1]);
        set_enAnglais(data[2]);
        set_enVietnamien(data[3]);
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
