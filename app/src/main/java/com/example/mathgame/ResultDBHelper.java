package com.example.mathgame;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResultDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydb2.db";
    private static String CREATE_TABLES;
    private static String[] TABLE_NAMES;

    ResultDBHelper(Context context, String[] TABLE_NAMES) {
        super(context, DATABASE_NAME, null, 1);
        ResultDBHelper.TABLE_NAMES = TABLE_NAMES;
        StringBuilder text = new StringBuilder();
        for (String i: TABLE_NAMES)
        text.append("create table ").append(i).append("(_id integer primary key autoincrement UNIQUE, ").append("time double, mistakes integer, Q integer);\n");
        CREATE_TABLES = text.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (int i = 0; i < CREATE_TABLES.split("\n").length; i++) {
            database.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAMES[i]);
            database.execSQL(CREATE_TABLES.split("\n")[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    void addResult(double time, int mistakes, int Q, SQLiteDatabase database, String table_name){
        ContentValues mMap = new ContentValues();
        mMap.put("time", time);
        mMap.put("mistakes", mistakes);
        mMap.put("Q", Q);
        database.insert(table_name, null, mMap);
    }

    Cursor readResults(SQLiteDatabase database, String table_name){
        String[] projections = {"_id", "time", "mistakes", "Q"};
        return database.query(table_name, projections, null, null,
                null, null, null);
    }

    double getStatistics(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(("select (avg(Q/time)) as avg from " + table_name), null);
        c.moveToFirst();
        double ans = c.getDouble(c.getColumnIndex("avg"));
        c.close();
        return ans;
    }
}
