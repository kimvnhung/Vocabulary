package com.kimvan.hung.vocabulary.testWord.fourAnswerGame;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;

import java.util.Date;

/**
 * Created by h on 25/01/2018.
 */

public class ScoreHandler {
    static final String SCORE_TABLE_NAME = "_4answer_game_score";
    static final String COLUMN_ID = "id";
    static final String COLUMN_BEST_SCORE = "_best_score";

    DataBaseHandler dbHandler;

    public ScoreHandler(DataBaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    //Tạo bảng điểm nếu chưa tồn tại
    public void createTableScore(){
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            String create = "CREATE TABLE IF NOT EXISTS "+SCORE_TABLE_NAME+"( "+
                    COLUMN_ID +" INTEGER  PRIMARY KEY, " +
                    COLUMN_BEST_SCORE +" INTEGER " +
                    ")";
            db.execSQL(create);
            Cursor c =db.rawQuery("SELECT * FROM "+SCORE_TABLE_NAME,null);

            c.moveToFirst();
            if (c.getCount()==0){
                db.execSQL("INSERT INTO "+SCORE_TABLE_NAME+
                        " VALUES (1,0)");
            }
            db.close();
        }catch (Exception e){
            e.getMessage();
        }
    }
    //Cập nhật điểm
    public void updateScore(int best){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String update = "UPDATE "+SCORE_TABLE_NAME+" SET "+
                COLUMN_BEST_SCORE+" = "+best+" WHERE "+COLUMN_ID+" = 1";
        db.execSQL(update);
        db.close();
    }
    //Lấy bestScore
    public int getBestScore(){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+SCORE_TABLE_NAME,null);
        c.moveToFirst();
        int result = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE));
        c.close();
        return result;
    }

    //Phần code cho learning Graph
    //
    //
    //
    //
    //
    //
    //
    // / //////

    public static final String DATE_TABLE_NAME = "datetable";
    public static final String MONTH_TABLE_NAME = "monthtable";
    public static final String YEAR_TABLE_NAME = "yeartable";

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID_MONTH = "idmonth";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_NUMBER_QUESTION = "numquestion";
    public static final String COLUMN_BEST_SCORE_IN_STH = "bestscoreinsth";

    public static final long ONE_DAY = 86400000;


    public void createDateTable() {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            String create = "CREATE TABLE "+DATE_TABLE_NAME+"(" +
                    COLUMN_DATE+"  LONG PRIMARY KEY, " +
                    COLUMN_NUMBER_QUESTION + " INTEGER, " +
                    COLUMN_BEST_SCORE_IN_STH + " INTEGER " +
                    "); ";
            db.execSQL(create);
        }catch (Exception e){
            e.getMessage();
        }

    }

    //tạo bảng tháng (id,tháng,năm,giá trị 1,giá trị 2)
    public void createMonthTable(){
        SQLiteDatabase database = dbHandler.getWritableDatabase();
        try {
            String create1 = "CREATE TABLE " +MONTH_TABLE_NAME+" ("+
                    COLUMN_ID_MONTH + " LONG PRIMARY KEY, "+
                    COLUMN_MONTH + " INTEGER, " +
                    COLUMN_YEAR + " INTEGER, " +
                    COLUMN_NUMBER_QUESTION + " INTEGER, " +
                    COLUMN_BEST_SCORE_IN_STH + " INTEGER " +
                    ");";
            database.execSQL(create1);
        }catch (Exception e){
            //do nothing
        }
    }
    //tạo bảng năm (năm,value1,value2)
    public void createYearTable(){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        try {
            String create = "CREATE TABLE " + YEAR_TABLE_NAME+" (" +
                    COLUMN_YEAR + " INTEGER PRIMARY KEY, " +
                    COLUMN_NUMBER_QUESTION + " INTEGER, " +
                    COLUMN_BEST_SCORE_IN_STH + " INTEGER " +
                    ");";

            db.execSQL(create);
        }catch (Exception e){
            //do nothing
        }
    }

    //thêm thông tin trong bảng ngày
    public void insertDataOnDate(long dateTime,int numQuestion,int bestScoreInSth){
        SQLiteDatabase dp = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE,dateTime);
        values.put(COLUMN_NUMBER_QUESTION,numQuestion);
        values.put(COLUMN_BEST_SCORE_IN_STH,bestScoreInSth);

        dp.insert(DATE_TABLE_NAME,null,values);
        //cập nhật bảng tháng năm
        sumUpLastMonthToMonthTable();
    }


    //tổng hợp tháng hiện tại để thêm/cập nhật thông tin trong bảng tháng
    public void sumUpLastMonthToMonthTable(){
        try {
            SQLiteDatabase dp = dbHandler.getWritableDatabase();
            int curMonth = new Date().getMonth();
            int curYear = new Date().getYear();
            long firstDayInCurMonth = new Date(curYear,curMonth,1).getTime();
            Cursor c = dp.rawQuery("SELECT * FROM "+DATE_TABLE_NAME + " WHERE "+COLUMN_DATE+" >= "+
                    firstDayInCurMonth,null);
            c.moveToFirst();
            int sumInMonth = 0;
            int bestInMonth = 0;

            //tổng hợp tháng gần nhất
            while(!c.isAfterLast()){
                int value1 = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
                int value2 = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
                sumInMonth += value1;
                bestInMonth = bestInMonth>=value2?bestInMonth:value2;
                c.moveToNext();
            }
            try {
                String check = "SELECT * FROM "+MONTH_TABLE_NAME + " WHERE "+
                        COLUMN_MONTH + " = "+curMonth;
                c = dp.rawQuery(check,null);

                if (c.getCount() == 0) {
                    insertDataOnMonth(curMonth,curYear,sumInMonth,bestInMonth);
                }else {
                    updateDataOnMonth(curMonth,curYear,sumInMonth,bestInMonth);
                }
            }catch (Exception e){

            }
            c.close();

            sumUpLastYearToYearTable();
        }catch (Exception e){
            e.getMessage();
        }
    }

    //thêm/cập nhật thông tin năm hiện tại
    private void sumUpLastYearToYearTable() {
        try {
            int curYear = new Date().getYear();
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM "+MONTH_TABLE_NAME + " WHERE "+
                    COLUMN_ID_MONTH + " >= " + getTime(curYear,0,1),null);
            int sumUptoYearTable = 0;
            int bestInYear = 0;
            c.moveToFirst();
            while (!c.isAfterLast()){
                int value1 = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
                int value2 = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
                sumUptoYearTable += value1;
                bestInYear = bestInYear>=value2?bestInYear:value2;
                c.moveToNext();
            }

            try {
                String check = "SELECT * FROM "+YEAR_TABLE_NAME + " WHERE "+
                        COLUMN_YEAR + " = "+curYear;
                c = db.rawQuery(check,null);
                if (c.getCount() == 0) {
                    insertDataOnYear(curYear,sumUptoYearTable,bestInYear);
                }else {
                    updateDataOnYear(curYear,sumUptoYearTable,bestInYear);
                }
                c.close();
            }catch (Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //cập nhật thông tin bảng năm
    private void updateDataOnYear(int curYear, int sumUptoYearTable, int bestInYear) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String update = "UPDATE "+YEAR_TABLE_NAME+
                " SET "+COLUMN_NUMBER_QUESTION+" = "+(sumUptoYearTable+getNumQuestionPlayed(YEAR_TABLE_NAME, getTimeOnThisYearBegining()))+
                ", "+COLUMN_BEST_SCORE_IN_STH +" = "+bestInYear+" WHERE " +
                COLUMN_YEAR + " = "+curYear;
        db.execSQL(update);
    }

    //thêm thông tin vào bảng năm
    private void insertDataOnYear(int curYear, int sumUptoYearTable, int bestInYear) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_YEAR,curYear);
        values.put(COLUMN_NUMBER_QUESTION,sumUptoYearTable);
        values.put(COLUMN_BEST_SCORE_IN_STH,bestInYear);

        db.insert(YEAR_TABLE_NAME,null,values);
    }

    //lấy mã thời gian từ ngày tháng năm
    private long getTime(int Year, int Month, int date) {
        return new Date(Year,Month,date).getTime();
    }

    //cập nhật thông tin vào bảng tháng
    private void updateDataOnMonth(int curMonth, int curYear, int sumInMonth, int bestInMonth) {
        String update = "UPDATE "+MONTH_TABLE_NAME+" " +
                " SET "+COLUMN_NUMBER_QUESTION + " = "+(sumInMonth+getNumQuestionPlayed(MONTH_TABLE_NAME, getTimeOnThisMonthBegining()))+
                ", "+ COLUMN_BEST_SCORE_IN_STH +" = "+
                bestInMonth+" WHERE " +COLUMN_ID_MONTH+" ="+getTime(curYear,curMonth,1);
        SQLiteDatabase dp = dbHandler.getWritableDatabase();
        dp.execSQL(update);
    }

    //thêm thông tin vào bảng tháng
    private void insertDataOnMonth(int curMonth, int curYear, int sumInMonth, int bestInMonth) {
        SQLiteDatabase dp = dbHandler.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put(COLUMN_ID_MONTH,getTime(curYear,curMonth,1));
        values.put(COLUMN_MONTH,curMonth);
        values.put(COLUMN_YEAR,curYear);
        values.put(COLUMN_NUMBER_QUESTION,sumInMonth);
        values.put(COLUMN_BEST_SCORE_IN_STH,bestInMonth);

        dp.insert(MONTH_TABLE_NAME,null,values);
    }

    //lấy ngày cuối cùng của tháng
    public long getLastDayInMonth(int month, int year) {
        long dateTime = new Date(year,month,27).getTime();
        while (getMonth(dateTime)==month){
            dateTime+=ONE_DAY;
        }
        return dateTime-ONE_DAY;
    }

    //đưa ra năm từ mã ngày tháng năm
    public int getYear(long dateTime){
        Date result = new Date(dateTime);
        return result.getYear();
    }

    //đưa ra tháng từ mã ngày tháng năm
    public int getMonth(long dateTime){
        Date result = new Date(dateTime);
        return result.getMonth();
    }

    //đưa ra ngày từ mà ngày tháng năm
    public int getDate(long dateTime){
        return new Date(dateTime).getDate();
    }

    //lấy các giá trị trong tháng
    public int[][] getValuesOfDates(int month,int year,int numValue){
        int[][] result = new int[2][numValue];
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM "+DATE_TABLE_NAME+" WHERE " +
                    COLUMN_DATE + " >= "+ getTime(year,month,1)+" AND "+COLUMN_DATE +" " +
                    "<= "+(getLastDayInMonth(month,year)+ONE_DAY),null);
            c.moveToFirst();
            int index = 0;
            while(!c.isAfterLast()){
                if (getDate(c.getLong(c.getColumnIndex(COLUMN_DATE)))==index+1){
                    result[0][index] = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
                    result[1][index] = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
                    c.moveToNext();
                }else {
                    result[0][index] = 0;
                    result[1][index] = 0;
                }
                index++;
            }
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    //lấy giá trị trong năm
    public int[][] getValuesOfMonths(int year) {
        int[][] result = new int[2][12];
        try {
            SQLiteDatabase dp = dbHandler.getWritableDatabase();
            Cursor c = dp.rawQuery("SELECT * FROM "+MONTH_TABLE_NAME+" WHERE "+COLUMN_YEAR+" = "+year,null);
            c.moveToFirst();

            while (!c.isAfterLast()){
                int month = c.getInt(c.getColumnIndex(COLUMN_MONTH));
                result[0][month] = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
                result[1][month] = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
                c.moveToNext();
            }
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    // trả về số năm đã qua
    public int getListYearSize() {
        int result =0;
        try {
            Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+YEAR_TABLE_NAME,null);
            result = c.getCount();
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    //lấy giá trị qua các năm
    public int[][] getValuesOfYears() {
        int[][] result = new int[2][getListYearSize()];
        Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+YEAR_TABLE_NAME,null);
        c.moveToFirst();
        int index=0;
        while (!c.isAfterLast()){
            result[0][index] = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
            result[1][index] = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
            c.moveToNext();
            index++;
        }
        return result;
    }

    //kiểm tra xem ngày hôm nay đã chơi chưa
    public boolean isPlayed(){
        boolean result = false;
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM "+DATE_TABLE_NAME+" WHERE "+
                    COLUMN_DATE+" = "+ getTimeOnTodayBegining(),null);
            c.moveToFirst();
            if (c.getCount()!=0){
                result = true;
            }
        }catch (Exception e){
            e.getMessage();

        }
        return result;
    }

    //thêm/cập nhật thông tin điểm trong ngày
    public void insertValueInDate(int numQuestion,int bestScoreInSth){

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        if (MainActivity.isPlayed){
            String update = "UPDATE "+DATE_TABLE_NAME+" " +
                    " SET "+COLUMN_NUMBER_QUESTION + " = "+(numQuestion+getNumQuestionPlayed(DATE_TABLE_NAME, getTimeOnTodayBegining()))+
                    ", "+ COLUMN_BEST_SCORE_IN_STH +" = "+
                    bestScoreInSth+" WHERE " +COLUMN_DATE+" ="+ getTimeOnTodayBegining();

            db.execSQL(update);
            //cập nhật lại bảng tháng,năm
            sumUpLastMonthToMonthTable();
        }else {
            // thêm thông tin và tự động cập nhật bảng tháng năm
            insertDataOnDate(getTimeOnTodayBegining(),numQuestion,bestScoreInSth);
        }
    }

    //lấy bestScoreInDay
    public int getBestScoreInSth(String inTableName,long time){
        Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+inTableName+" WHERE "+
                COLUMN_DATE+" = "+time,null);
        c.moveToFirst();
        int result = c.getInt(c.getColumnIndex(COLUMN_BEST_SCORE_IN_STH));
        c.close();
        return result;
    }

    //lấy số câu hỏi đã chơi
    public int getNumQuestionPlayed(String inTableName,long time){
        try {
            Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+inTableName+" WHERE "+
                    COLUMN_DATE +" = "+time,null);
            c.moveToFirst();
            int result = c.getInt(c.getColumnIndex(COLUMN_NUMBER_QUESTION));
            c.close();
            return result;
        }catch (Exception e){
            e.getMessage();
            return 0;
        }
    }

    //lấy mã thời gian ngày đầu năm nay
    public long getTimeOnThisYearBegining(){
        return getTime(new Date().getYear(),0,1);
    }

    //lấy mã thời gian ngày đầu tháng này
    public long getTimeOnThisMonthBegining(){
        return getTime(new Date().getYear(),new Date().getMonth(),1);
    }

    //lấy mã thời gian lúc bắt đầu ngày
    public long getTimeOnTodayBegining(){
        return getTime(new Date().getYear(),new Date().getMonth(),new Date().getDate());
    }

    public DataBaseHandler getDbHandler() {
        return dbHandler;
    }
}
