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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 27/08/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context mContext;
    private static final int DATABASE_VERSION = 1 ;
    private static String DATABASE_NAME = "Vocabularys.db";
    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String TABLE_NAME_MEANING = "meaning";
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
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME_VOCABULARY+ "("+
                COLUMN_ID +" TEXT PRIMARY KEY, "+
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
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_MEANING);
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
            Toast.makeText(mContext,"Eror: resetAllData()",Toast.LENGTH_LONG).show();
        }
        onCreate(db);

    }

    public void createTableMeaning(){
        SQLiteDatabase db = getWritableDatabase();

        try {
            String query = "CREATE TABLE " + TABLE_NAME_MEANING+ "("+
                    COLUMN_STT +" TEXT PRIMARY KEY, "+
                    COLUMN_ID+" TEXT ,"+
                    COLUMN_EN_ANGLAIS+" TEXT, "+
                    COLUMN_EN_VIETNAMIEN+" TEXT "+
                    "); ";

            db.execSQL(query);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: createTableMeaning()",Toast.LENGTH_LONG).show();
        }
    }

    public void addWord(NouveauMot nouveauMot) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LE_MOT, nouveauMot.get_leMot());
        values.put(COLUMN_TYPE_WORD, nouveauMot.get_type_word());
        values.put(COLUMN_LV2, nouveauMot.get_lv2());
        values.put(COLUMN_EXPERT_POINT, nouveauMot.get_expert_point());

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_VOCABULARY + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToLast();

        try {
            values.put(COLUMN_ID, "W" + (getOrderFromId(c.getString(c.getColumnIndex(COLUMN_ID))) + 1));
        } catch (Exception e) {
            //trường hợp ko có phần tử nào ban đầu
            values.put(COLUMN_ID, "W" + c.getCount());
        }

        db.insert(TABLE_NAME_VOCABULARY, null, values);


        db.close();
        c.close();
    }

    public void deleteWord(String id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT *  FROM "+TABLE_NAME_VOCABULARY+" WHERE 1",null);
        c.moveToFirst();

        try {
        String deleteQuery = "DELETE FROM " + TABLE_NAME_VOCABULARY +" WHERE "+COLUMN_ID +"=\""+id+"\"";


            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: deleteWord(String id)",Toast.LENGTH_LONG).show();
        }
        //xóa nghĩa
        deleteMeaningById(id);

        db.close();
        c.close();
    }

    public void updateWord(NouveauMot nouveauMot){
        SQLiteDatabase db = getWritableDatabase();

        try {
            String updateQuery = "UPDATE "+ TABLE_NAME_VOCABULARY+ " SET " +
                    COLUMN_LE_MOT +"=\""+nouveauMot.get_leMot()+"\", "+
                    COLUMN_TYPE_WORD+"=\""+nouveauMot.get_type_word()+"\","+
                    COLUMN_LV2+"=\""+nouveauMot.get_lv2()+"\","+
                    COLUMN_EXPERT_POINT+"="+nouveauMot.get_expert_point()+" WHERE "+
                    COLUMN_ID+"=\""+nouveauMot.get_id()+"\"";

            db.execSQL(updateQuery);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: updateWord(NouveauMot nouveauMot)",Toast.LENGTH_LONG).show();
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
            String query = "SELECT * FROM "+TABLE_NAME_MEANING+" WHERE "+COLUMN_ID+" =\""+normalWordMeaning.get_id()+"\"";
            Cursor c = db.rawQuery(query,null);
            c.moveToLast();
            count = getOrderFromStt(c.getString(c.getColumnIndex(COLUMN_STT)));
            c.close();
        }catch (Exception e){
            e.getMessage();
            //khi chưa tồn tại nghĩa
        }

        values.put(COLUMN_STT,normalWordMeaning.get_id()+"M"+(count+1));
        values.put(COLUMN_ID,normalWordMeaning.get_id());
        db.insert(TABLE_NAME_MEANING,null,values);

        db.close();
    }

    public void deleteMeaningByStt(String stt){//theo stt
        SQLiteDatabase db = getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM " + TABLE_NAME_MEANING +" WHERE "+COLUMN_STT +"=\""+stt+"\"";
            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: deleteMeaning(String stt)",Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void deleteMeaningById(String id){//theo id
        SQLiteDatabase db = getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM " + TABLE_NAME_MEANING +" WHERE "+COLUMN_ID +"=\""+id+"\"";
            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: deleteMeaning(String stt)",Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void updateMeaning(NormalWordMeaning normalWordMeaning){
        SQLiteDatabase db = getWritableDatabase();

        try {
            String updateQuery = "UPDATE "+TABLE_NAME_MEANING+ " SET " +
                    COLUMN_ID +"=\""+normalWordMeaning.get_id()+"\","+
                    COLUMN_EN_ANGLAIS +"=\""+normalWordMeaning.get_enAnglais()+"\", "+
                    COLUMN_EN_VIETNAMIEN+"=\""+normalWordMeaning.get_enVietnamien()+"\" WHERE "+
                    COLUMN_STT+"=\""+normalWordMeaning.get_stt()+"\"";

            db.execSQL(updateQuery);
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: updateMeaning(NormalWordMeaning normalWordMeaning)",Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public ArrayList<NormalWordMeaning> getNormalWordMeaning(String id){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<NormalWordMeaning> result= new ArrayList<>();
        try {
            String query = "SELECT * FROM "+TABLE_NAME_MEANING+" WHERE "+COLUMN_ID+" =\""+id+"\"";
            Cursor c = db.rawQuery(query,null);
            c.moveToFirst();

            while (!c.isAfterLast()){
                NormalWordMeaning normalWordMeaning = new NormalWordMeaning();
                normalWordMeaning.set_id(c.getString(c.getColumnIndex(COLUMN_ID)));
                normalWordMeaning.set_stt(c.getString(c.getColumnIndex(COLUMN_STT)));
                normalWordMeaning.set_enAnglais(c.getString(c.getColumnIndex(COLUMN_EN_ANGLAIS)));
                normalWordMeaning.set_enVietnamien(c.getString(c.getColumnIndex(COLUMN_EN_VIETNAMIEN)));

                result.add(normalWordMeaning);
                c.moveToNext();
            }
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: getNormalWordMeaning(String id)",Toast.LENGTH_LONG).show();
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
        c.getCount();
        NouveauMot nouveauMot = new NouveauMot();
        try {
             nouveauMot = new NouveauMot(leMot,c.getString(c.getColumnIndex(COLUMN_TYPE_WORD)),
                    c.getString(c.getColumnIndex(COLUMN_LV2)));

            nouveauMot.set_id(c.getString(c.getColumnIndex(COLUMN_ID)));
            nouveauMot.set_expert_point(c.getInt(c.getColumnIndex(COLUMN_EXPERT_POINT)));
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: getNouveauMot(String leMot)",Toast.LENGTH_LONG).show();
        }

        return nouveauMot;

    }

    public ArrayList<NouveauMot> searchNouveauMot(String key,String orderBy){
        ArrayList<NouveauMot> result = new ArrayList<>();
        SQLiteDatabase db =  getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME_VOCABULARY+" WHERE "+COLUMN_LE_MOT+" LIKE '%"+key+"%' ORDER BY "+
                orderBy+" ASC";
        if (key.equals("")){
            query = "SELECT * FROM "+TABLE_NAME_VOCABULARY+" ORDER BY "+
            orderBy+" ASC";
        }
        //cursor point to a location in your resuits
        Cursor c = db.rawQuery(query,null);
        //Move to the first row in your resuits
        c.moveToFirst();

        while (!c.isAfterLast()){
            NouveauMot nouveauMot = new NouveauMot(c.getString(c.getColumnIndex(COLUMN_LE_MOT)),
                    c.getString(c.getColumnIndex(COLUMN_TYPE_WORD)),
                    c.getString(c.getColumnIndex(COLUMN_LV2)));

            nouveauMot.set_id(c.getString(c.getColumnIndex(COLUMN_ID)));
            nouveauMot.set_expert_point(c.getInt(c.getColumnIndex(COLUMN_EXPERT_POINT)));
            result.add(nouveauMot);
            c.moveToNext();
        }
        return result;
    }

    public int getOrderFromId(String id){
        try {
            StringBuilder result = new StringBuilder(id);
            result.deleteCharAt(0);
            return Integer.parseInt(result.toString());
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: getOrderFromId(String id)",Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    public int getOrderFromStt(String stt){
        try {
            StringBuilder result = new StringBuilder(stt);
            int length = result.length();
            for (int i=0;i<length;){
                if ((result.charAt(i)+"").equals("M")){
                    result.deleteCharAt(i);
                    break;
                }
                result.deleteCharAt(i);
            }
            return Integer.parseInt(result.toString());
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: getOrderFromStt(String stt)",Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    public boolean isExistItem(String tableName,String columnName,String item){
        boolean result = false;
        try {
            Cursor c = getWritableDatabase().rawQuery("SELECT * FROM "+tableName+" WHERE "+columnName+"=\""+item+"\"",null);
            if (c.getCount()>=1){
                result = true;
            }
            c.close();
        }catch (Exception e){
            e.getMessage();
            Toast.makeText(mContext,"Error: isExistItem(String tableName,String columnName,String item)"
                    ,Toast.LENGTH_LONG).show();
        }
        return result;
    }

    //lấy số phần tử trong bảng
    public int getTableCount(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+tableName,null);
        return c.getCount();
    }

    public boolean isExistTableMeaning(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_MEANING,null);
        }catch (Exception e){
            e.getMessage();
            return false;
        }
        return true;
    }
}
