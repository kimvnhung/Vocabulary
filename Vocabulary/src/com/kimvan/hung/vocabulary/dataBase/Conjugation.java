package com.kimvan.hung.vocabulary.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by h on 31/08/2017.
 */

public class Conjugation {
    private int _id;
    private String _leTemps,_je,_tu,_ilElle,_nous,_vous,_ilsElles;

    public Conjugation(String _leTemps, String _je, String _tu, String _ilElle, String _nous, String _vous, String _ilsElles) {
        this._leTemps = _leTemps;
        this._je = _je;
        this._tu = _tu;
        this._ilElle = _ilElle;
        this._nous = _nous;
        this._vous = _vous;
        this._ilsElles = _ilsElles;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_leTemps() {
        return _leTemps;
    }

    public void set_leTemps(String _leTemps) {
        this._leTemps = _leTemps;
    }

    public String get_je() {
        return _je;
    }

    public void set_je(String _je) {
        this._je = _je;
    }

    public String get_tu() {
        return _tu;
    }

    public void set_tu(String _tu) {
        this._tu = _tu;
    }

    public String get_ilElle() {
        return _ilElle;
    }

    public void set_ilElle(String _ilElle) {
        this._ilElle = _ilElle;
    }

    public String get_nous() {
        return _nous;
    }

    public void set_nous(String _nous) {
        this._nous = _nous;
    }

    public String get_vous() {
        return _vous;
    }

    public void set_vous(String _vous) {
        this._vous = _vous;
    }

    public String get_ilsElles() {
        return _ilsElles;
    }

    public void set_ilsElles(String _ilsElles) {
        this._ilsElles = _ilsElles;
    }
}
