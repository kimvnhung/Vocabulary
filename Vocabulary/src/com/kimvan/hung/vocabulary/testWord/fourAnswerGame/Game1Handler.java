package com.kimvan.hung.vocabulary.testWord.fourAnswerGame;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NormalWordMeaning;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.ArrayList;

/**
 * Created by h on 09/09/2017.
 */

public class Game1Handler {
    DataBaseHandler dbHandler;
    NouveauMot nouveauMotSelected;
    NormalWordMeaning normalWordMeaningSelected;
    Boolean anglaisouvietnamien=true;


    public Game1Handler(DataBaseHandler handler) {
        this.dbHandler = handler;
    }

    public NormalWordMeaning getNormalWordMeaningSelected() {
        return normalWordMeaningSelected;
    }

    public void setNormalWordMeaningSelected(NormalWordMeaning normalWordMeaningSelected) {
        this.normalWordMeaningSelected = normalWordMeaningSelected;
    }

    public NouveauMot getNouveauMotSelected() {
        return nouveauMotSelected;
    }

    public Boolean getAnglaisouvietnamien() {
        return anglaisouvietnamien;
    }

    public void setAnglaisouvietnamien(Boolean anglaisouvietnamien) {
        this.anglaisouvietnamien = anglaisouvietnamien;
    }

    public void setNouveauMotSelected(NouveauMot nouveauMotSelected) {
        this.nouveauMotSelected = nouveauMotSelected;
    }

    //Note: All point below is expert point
    public int getMaxId(){
        int id = 0;
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE 1 ORDER BY "+
            dbHandler.COLUMN_ID+" DESC",null);
            c.moveToFirst();
            id =c.getInt(c.getColumnIndex(dbHandler.COLUMN_ID));
            c.close();
        }catch (Exception e){
            e.getMessage();
        }
        return id;
    }



    //lấy điểm expert cao nhất và thấp nhất
    public int[] getMaxMinPoint(){ //result[0] là max, result[1] min
        int[] result = new int[2];
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE 1 ORDER BY "+
                    dbHandler.COLUMN_EXPERT_POINT+" DESC",null);
            c.moveToFirst();

            result[0] = c.getInt(c.getColumnIndex(dbHandler.COLUMN_EXPERT_POINT));

            c.moveToLast();
            result[1] = c.getInt(c.getColumnIndex(dbHandler.COLUMN_EXPERT_POINT));

            c.close();
        }catch (Exception e){
            e.getMessage();
        }

        return result;
    }

    public ArrayList<NouveauMot> getLeMot(Cursor c){
        ArrayList<NouveauMot> result = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()){
            result.add(new NouveauMot(
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_ID)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_LE_MOT)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_TYPE_WORD)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_LV2)),
                    c.getInt(c.getColumnIndex(dbHandler.COLUMN_EXPERT_POINT))));
            c.moveToNext();
        }

        return result;
    }
    //lấy từ gần vs điểm expert ngẫu nhiên
    public NouveauMot getMotNearRandomPoint(){
        NouveauMot result = new NouveauMot();
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            int point = getRandomPoint();
            Cursor c = db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY +" WHERE "+
                dbHandler.COLUMN_EXPERT_POINT+" IN (" +
                    "SELECT v1." + dbHandler.COLUMN_EXPERT_POINT+" FROM "+dbHandler.TABLE_NAME_VOCABULARY+" AS V1, "+
                    dbHandler.TABLE_NAME_VOCABULARY+" AS V2 WHERE v1." + dbHandler.COLUMN_EXPERT_POINT+"= v2." +
                    dbHandler.COLUMN_EXPERT_POINT+" ORDER BY abs(v2." + dbHandler.COLUMN_EXPERT_POINT+" - "+point+ ") ASC, v1." +
                    dbHandler.COLUMN_EXPERT_POINT+" DESC LIMIT 1 )",null);
            /*Cursor c = db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE "+
                    dbHandler.COLUMN_EXPERT_POINT +" >= "+point+" ORDER BY "+dbHandler.COLUMN_EXPERT_POINT +" DESC",null);
            ArrayList<NouveauMot> para = getLeMot(c);
            if (c.getCount()==0){
                c = db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE "+
                        dbHandler.COLUMN_EXPERT_POINT +" <= "+point+" ORDER BY "+dbHandler.COLUMN_EXPERT_POINT +" ASC",null);
            }*/
            c.moveToFirst();
            result = new NouveauMot(
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_ID)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_LE_MOT)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_TYPE_WORD)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_LV2)),
                    c.getInt(c.getColumnIndex(dbHandler.COLUMN_EXPERT_POINT)));

            c.close();
        }catch (Exception e){
            e.getMessage();
        }

        return result;
    }

    //lấy nghĩa ngẫu nhiên từ 1 đã chọn
    public NormalWordMeaning getRandomMeaning(String id){
        NormalWordMeaning result = new NormalWordMeaning();
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM " +dbHandler.TABLE_NAME_MEANING+" WHERE "+
                    dbHandler.COLUMN_ID+"=\""+id+"\"",null);
            int size =c.getCount();
            c.moveToFirst();
            size =(int)( size*Math.random());
            while (size-->0){
                c.moveToNext();
            }
            result = new NormalWordMeaning(
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_ID)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_STT)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_EN_ANGLAIS)),
                    c.getString(c.getColumnIndex(dbHandler.COLUMN_EN_VIETNAMIEN))
            );
            c.close();
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    public String getMeaning(NormalWordMeaning selected){
        if (anglaisouvietnamien){
            return selected.get_enVietnamien();
        }else {
            return selected.get_enAnglais();
        }
    }
    //lấy bộ đáp án
    //Should call at first
    public String[] getAnswer(){
        String[] result = new String[4];
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        setNouveauMotSelected(getMotNearRandomPoint());

        setNormalWordMeaningSelected(getRandomMeaning(getNouveauMotSelected().get_id()));
        result[0]= getMeaning(getNormalWordMeaningSelected());
        int count =3;
        try {
            Cursor c = db.rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE 1",null);

            while (count>0){
                int size = c.getCount();
                size = (int)(size*Math.random());
                c.moveToFirst();
                while (size-->0){
                    c.moveToNext();
                }
                String checkDoubleAnswer = c.getString(c.getColumnIndex(dbHandler.COLUMN_LE_MOT));
                if (checkDoubleAnswer.equals(getNouveauMotSelected().get_leMot())){

                }else {
                    result[4-count] = getMeaning(getRandomMeaning(c.getString(c.getColumnIndex(dbHandler.COLUMN_ID))));
                    //check if is double answer
                    for (int i=0;i<4-count;i++){
                        if (result[i].equals(result[4-count])){
                            count++;
                            break;
                        }
                    }
                    count--;
                }
            }
            c.close();
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    //lấy số từ có điểm = max hiện có trong database
    public int getCountMaxPoint(){
        int[] maxMin = getMaxMinPoint();
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT "+dbHandler.COLUMN_EXPERT_POINT+" FROM "+
            dbHandler.TABLE_NAME_VOCABULARY+" WHERE "+dbHandler.COLUMN_EXPERT_POINT+" = "+maxMin[0],null);
        return c.getCount();
    }


    //lấy giá trị điểm expert bất kỳ
    public int getRandomPoint() {
        double random = Math.random();
        int result = 0;
        int[] maxMinPoint = getMaxMinPoint();
        int N=dbHandler.getTableCount(dbHandler.TABLE_NAME_VOCABULARY);
        int n=getCountMaxPoint();
        double kPoint = 1-Math.pow((maxMinPoint[0]-maxMinPoint[1])/(double)(21*maxMinPoint[0]/20-19*maxMinPoint[1]/20),2);
        if(n/N>kPoint){
            maxMinPoint[0]= (int) ((Math.sqrt(N)/Math.sqrt(N-n))*(maxMinPoint[0]-maxMinPoint[1])+maxMinPoint[1]+0.5);
        }else {
            maxMinPoint[0]+=(int) ((maxMinPoint[0]-maxMinPoint[1])*0.05+0.5);
        }
        /*if (random<=0.7){
            result = (int)((maxMinPoint[1]-maxMinPoint[0])*random/1.4 + maxMinPoint[0]);
        }else {
            result = (int)((maxMinPoint[1]-maxMinPoint[0])*random/0.6+maxMinPoint[0]/0.6-2*maxMinPoint[1]/3);
        }*/
        result=(int)((maxMinPoint[0]-maxMinPoint[1])*Math.sqrt(random)+maxMinPoint[1]);
        return result;
    }


}
