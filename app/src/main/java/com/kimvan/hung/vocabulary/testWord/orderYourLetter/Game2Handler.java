package com.kimvan.hung.vocabulary.testWord.orderYourLetter;

import android.database.sqlite.SQLiteDatabase;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.testWord.fourAnswerGame.Game1Handler;

/**
 * Created by h on 15/07/2018.
 */

public class Game2Handler extends Game1Handler {

    public Game2Handler(DataBaseHandler handler) {
        super(handler);
    }

    public String getContentQuestion(){
        return super.getAnswer()[0];
    }


}
