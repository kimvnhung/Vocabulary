package com.kimvan.hung.vocabulary.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by h on 28/08/2017.
 */

public class VerbWordMeaning extends NormalWordMeaning {

    ArrayList<Conjugation> lesTemps;


    public VerbWordMeaning(String nameWord, String _enAnglais, String _enVietnamien) {
        super(nameWord, _enAnglais, _enVietnamien);
    }

    public VerbWordMeaning() {
    }

    public VerbWordMeaning(String _enAnglais, String _enVietnamien) {
        super(_enAnglais, _enVietnamien);
    }

    public Conjugation setNewConjugation(String leTemps, String je, String tu,String ilElle,String nous,
                                         String vous,String ilsElles){
        Conjugation result = new Conjugation(leTemps,je,tu,ilElle,nous,vous,ilsElles);
        return result;
    }

    public ArrayList<Conjugation> getLesTemps() {
        return lesTemps;
    }

    public void setLesTemps(ArrayList<Conjugation> lesTemps) {
        this.lesTemps = lesTemps;
    }


}
