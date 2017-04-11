package com.bykth.confdroid.confdroid_application;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;


/**
 * Created by Glantz on 2017-04-10.
 */
public class appParser {

private String DB_Pathway;
private String DB_Name;



    public appParser(String db_Name,String db_Pathway){
        this.DB_Name=db_Name;
        this.DB_Pathway=db_Pathway;

    }


    public void alterSQLite(String SQLQuerry) throws IOException {
        Runtime.getRuntime().exec("su sqlite3 -csv "+this.DB_Pathway+this.DB_Name+"\""+SQLQuerry+"\";");
    }


}
