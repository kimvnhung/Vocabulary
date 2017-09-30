package com.kimvan.hung.vocabulary.dataBase;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 27/08/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1 ;
    private static String DATABASE_NAME = "Vocabularys.db";
    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LE_MOT = "_leMot";
    public static final String COLUMN_TYPE_WORD = "_typeWord";
    public static final String COLUMN_LV2= "_lv2";
    public static final String COLUMN_EXPERT_POINT = "_expertPoint";

    public static final String COLUMN_STT = "_stt";
    public static final String COLUMN_EN_ANGLAIS = "_enAnglais";
    public static final String COLUMN_EN_VIETNAMIEN = "_enVietnamien";
    public static final String COLUMN_LE_TEMPS = "_leTemps";
    public static final String COLUMN_JE = "_je";
    public static final String COLUMN_TU = "_tu";
    public static final String COLUMN_IL_ELLE = "_ilElle";
    public static final String COLUMN_NOUS = "_nous";
    public static final String COLUMN_VOUS = "_vous";
    public static final String COLUMN_ILS_ELLES = "_ilsElles";


    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
        this.DATABASE_NAME=name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME_VOCABULARY+ "("+
                COLUMN_ID +" INTERGER PRIMARY KEY, "+
                COLUMN_LE_MOT+" TEXT, "+
                COLUMN_TYPE_WORD+" TEXT, "+
                COLUMN_LV2+" TEXT,"+
                COLUMN_EXPERT_POINT+" INTEGER "+
                "); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VOCABULARY);
        onCreate(db);
    }

    public void resetAllData(){
        SQLiteDatabase db =getWritableDatabase();

        try {

            // query to obtain the names of all tables in your database
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();

            // iterate over the result set, adding every table name to a list
            while (c.moveToNext()) {
                tables.add(c.getString(0));
            }

            // call DROP TABLE on every table name
            for (String table : tables) {
                String dropQuery = "DROP TABLE IF EXISTS " + table;
                db.execSQL(dropQuery);
            }
        }catch (Exception e){
            e.getMessage();
        }
        onCreate(db);
        /*String query = "SELECT * FROM "+TABLE_NAME_VOCABULARY +" WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        String motpara,typepara;
        while (!c.isAfterLast()){
            motpara = c.getString(c.getColumnIndex(COLUMN_LE_MOT));
            typepara = c.getString(c.getColumnIndex(COLUMN_TYPE_WORD));
            try {
                db.execSQL("DELETE FROM "+TABLE_NAME_VOCABULARY+" WHERE "+COLUMN_LE_MOT +"=\""+motpara+"\"");
                deleteTable(motpara);
                if (typepara.equals("le verbe")){
                    deleteTable(motpara+"lv2");
                }
            }catch (Exception e){
                e.getMessage();
            }
            c.moveToNext();
        }
        db.close();*/
    }

    public void createAnotherTable(String nameTable,String typeWord){
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL("DROP TABLE IF EXISTS " + nameTable);
            String query = "CREATE TABLE " + nameTable+ "("+
                    COLUMN_STT +" INTERGER PRIMARY KEY, "+
                    COLUMN_EN_ANGLAIS+" TEXT, "+
                    COLUMN_EN_VIETNAMIEN+" TEXT "+
                    "); ";

            db.execSQL(query);
        }catch (Exception e){
            e.getMessage();
        }

        if (typeWord!=null){
            if (typeWord.equals("le verbe")){
                String query2 = "CREATE TABLE " + nameTable+"lv2"+ "("+
                        COLUMN_ID +" INTERGER PRIMARY KEY, "+
                        COLUMN_LE_TEMPS+" TEXT, "+
                        COLUMN_JE+" TEXT, "+
                        COLUMN_TU+" TEXT, "+
                        COLUMN_IL_ELLE+" TEXT, "+
                        COLUMN_NOUS+" TEXT, "+
                        COLUMN_VOUS+" TEXT, "+
                        COLUMN_ILS_ELLES+" TEXT "+
                        "); ";
                try {
                    db.execSQL(query2);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }
    }

    public void addWord(NouveauMot nouveauMot){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LE_MOT,nouveauMot.get_leMot());
        values.put(COLUMN_TYPE_WORD,nouveauMot.get_type_word());
        values.put(COLUMN_LV2,nouveauMot.get_lv2());
        values.put(COLUMN_EXPERT_POINT,nouveauMot.get_expert_point());

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_VOCABULARY+ " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        values.put(COLUMN_ID,c.getCount());

        db.insert(TABLE_NAME_VOCABULARY,null,values);
        db.close();
    }

    public void deleteWord(int id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT *  FROM "+TABLE_NAME_VOCABULARY+" WHERE 1",null);
        c.moveToFirst();

        String motPara = c.getString(c.getColumnIndex(COLUMN_LE_MOT));
        String typePara = c.getString(c.getColumnIndex(COLUMN_TYPE_WORD));
        try {
        String deleteQuery = "DELETE FROM " + TABLE_NAME_VOCABULARY +" WHERE "+COLUMN_ID +"="+id;


            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.getMessage();
        }

        deleteTable(motPara);
        if (typePara.equals("le verbe")){
            deleteTable(motPara+"lv2");
        }
        resetIdWord(id);
        db.close();
    }

    public void updateWord(NouveauMot nouveauMot){
        SQLiteDatabase db = getWritableDatabase();

        try {
            String updateQuery = "UPDATE "+ TABLE_NAME_VOCABULARY+ " SET " +
                    COLUMN_LE_MOT +"=\""+nouveauMot.get_leMot()+"\", "+
                    COLUMN_TYPE_WORD+"=\""+nouveauMot.get_type_word()+"\","+
                    COLUMN_LV2+"=\""+nouveauMot.get_lv2()+"\","+
                    COLUMN_EXPERT_POINT+"="+nouveauMot.get_expert_point()+" WHERE "+
                    COLUMN_ID+"="+nouveauMot.get_id();

            db.execSQL(updateQuery);
        }catch (Exception e){
            e.getMessage();
        }
        db.close();
    }

    public void addMeaning(NormalWordMeaning normalWordMeaning){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EN_ANGLAIS,normalWordMeaning.get_enAnglais());
        values.put(COLUMN_EN_VIETNAMIEN,normalWordMeaning.get_enVietnamien());

        int count =-1;
        try {
            String query = "SELECT * FROM "+normalWordMeaning.getNameWord()+" WHERE 1 ";
            Cursor c = db.rawQuery(query,null);
            count = c.getCount();
        }catch (Exception e){
            e.getMessage();
        }


        if (count>= 0){
            values.put(COLUMN_STT,count);
            db.insert(normalWordMeaning.getNameWord(),null,values);
        }



        db.close();
    }

    public void deleteMeaning(String nameTable,int stt){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM " + nameTable +"WHERE "+COLUMN_STT +"="+stt;
            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.getMessage();
        }
        db.close();
    }

    public void deleteTable(String nameTable){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "DROP TABLE IF EXISTS "+nameTable;
            db.execSQL(query);
        }catch (Exception e){
            e.getMessage();
        }
        db.close();
    }

    public void updateMeaning(NormalWordMeaning normalWordMeaning){
        SQLiteDatabase db = getWritableDatabase();

        try {
            String updateQuery = "UPDATE "+normalWordMeaning.getNameWord()+ " SET " +
                    COLUMN_EN_ANGLAIS +"="+normalWordMeaning.get_enAnglais()+", "+
                    COLUMN_EN_VIETNAMIEN+"="+normalWordMeaning.get_enVietnamien()+" WHERE "+
                    COLUMN_STT+"="+normalWordMeaning.get_stt();

            db.execSQL(updateQuery);
        }catch (Exception e){
            e.getMessage();
        }
        db.close();
    }

    public void addConjugation(VerbWordMeaning verbWordMeaning){
        SQLiteDatabase db =  getWritableDatabase();

        for (int i =0;i<verbWordMeaning.getLesTemps().size();i++){
            ContentValues values = new ContentValues();
            Conjugation conjugation = verbWordMeaning.getLesTemps().get(i);


            values.put(COLUMN_ID,conjugation.get_id());
            values.put(COLUMN_LE_TEMPS,conjugation.get_leTemps());
            values.put(COLUMN_JE,conjugation.get_je());
            values.put(COLUMN_TU,conjugation.get_tu());
            values.put(COLUMN_IL_ELLE,conjugation.get_ilElle());
            values.put(COLUMN_NOUS,conjugation.get_nous());
            values.put(COLUMN_VOUS,conjugation.get_vous());
            values.put(COLUMN_ILS_ELLES,conjugation.get_ilsElles());

            db.insert(verbWordMeaning.getNameWord()+"lv2",null,values);
        }

        db.close();
    }

    public void updateConjugation(String nameTable,Conjugation conjugation){
        String query = "UPDATE "+nameTable +" SET "+
                COLUMN_LE_TEMPS +"=\""+conjugation.get_leTemps()+"\","+
                COLUMN_JE +"=\""+conjugation.get_je()+"\","+
                COLUMN_TU +"=\""+conjugation.get_tu()+"\","+
                COLUMN_IL_ELLE +"=\""+conjugation.get_ilElle()+"\","+
                COLUMN_NOUS +"=\""+conjugation.get_nous()+"\","+
                COLUMN_VOUS +"=\""+conjugation.get_vous()+"\","+
                COLUMN_ILS_ELLES +"=\""+conjugation.get_ilsElles()+"\""+
                " WHERE "+COLUMN_ID +"="+conjugation.get_id();

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public ArrayList<NormalWordMeaning> getNormalWordMeaning(String mot){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<NormalWordMeaning> result= new ArrayList<>();
        try {
            String query = "SELECT * FROM "+mot+" WHERE 1";
            Cursor c = db.rawQuery(query,null);
            c.moveToFirst();

            while (!c.isAfterLast()){
                NormalWordMeaning normalWordMeaning = new NormalWordMeaning();
                normalWordMeaning.set_stt(c.getInt(c.getColumnIndex(COLUMN_STT)));
                normalWordMeaning.set_enAnglais(c.getString(c.getColumnIndex(COLUMN_EN_ANGLAIS)));
                normalWordMeaning.set_enVietnamien(c.getString(c.getColumnIndex(COLUMN_EN_VIETNAMIEN)));
                normalWordMeaning.setNameWord(mot);

                result.add(normalWordMeaning);
                c.moveToNext();
            }
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    public NouveauMot getNouveauMot(String leMot){
        SQLiteDatabase db =  getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME_VOCABULARY+" WHERE "+COLUMN_LE_MOT+"=\""+leMot+"\"";

        //cursor point to a location in your resuits
        Cursor c = db.rawQuery(query,null);
        //Move to the first row in your resuits
        c.moveToFirst();

        NouveauMot nouveauMot = new NouveauMot(leMot,c.getString(c.getColumnIndex(COLUMN_TYPE_WORD)),
               c.getString(c.getColumnIndex(COLUMN_LV2)));

        nouveauMot.set_id(c.getInt(c.getColumnIndex(COLUMN_ID)));
        nouveauMot.set_expert_point(c.getInt(c.getColumnIndex(COLUMN_EXPERT_POINT)));

        return nouveauMot;

    }

    public ArrayList<NouveauMot> searchNouveauMot(String key,String orderBy){
        ArrayList<NouveauMot> result = new ArrayList<>();
        SQLiteDatabase db =  getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME_VOCABULARY+" WHERE "+COLUMN_LE_MOT+" LIKE '%"+key+"%' ORDER BY "+
                orderBy+" DESC";
        if (key.equals("")){
            query = "SELECT * FROM "+TABLE_NAME_VOCABULARY+" ORDER BY "+
            orderBy+" DESC";
        }
        //cursor point to a location in your resuits
        Cursor c = db.rawQuery(query,null);
        //Move to the first row in your resuits
        c.moveToFirst();

        while (!c.isAfterLast()){
            NouveauMot nouveauMot = new NouveauMot(c.getString(c.getColumnIndex(COLUMN_LE_MOT)),
                    c.getString(c.getColumnIndex(COLUMN_TYPE_WORD)),
                    c.getString(c.getColumnIndex(COLUMN_LV2)));

            nouveauMot.set_id(c.getInt(c.getColumnIndex(COLUMN_ID)));
            nouveauMot.set_expert_point(c.getInt(c.getColumnIndex(COLUMN_EXPERT_POINT)));
            result.add(nouveauMot);
            c.moveToNext();
        }
        return result;
    }

    public void resetIdWord(int idCur){
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_VOCABULARY+" WHERE "+COLUMN_ID+" >="+idCur,null);
            int paraCur =c.getCount();
            int count=0;
            c.moveToFirst();

            while (count<paraCur){
                String update = "UPDATE "+TABLE_NAME_VOCABULARY+" SET "+
                        COLUMN_ID +"="+(idCur+count++)+
                        " WHERE "+COLUMN_LE_MOT+"=\""+c.getString(c.getColumnIndex(COLUMN_LE_MOT))+"\"";
                db.execSQL(update);
                c.moveToNext();
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    public String fixName(String in){

        StringBuilder out = new StringBuilder(in);

        return out.toString();
    }
}
