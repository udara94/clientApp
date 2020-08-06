package com.example.clientapp.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.clientapp.model.dto.ItemDB;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "udaradb";
    private static final int DATABASE_VERSION = 3;

    private Dao<ItemDB,Long> todoDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, ItemDB.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            if(checkTableExits(database, "items"))
                TableUtils.dropTable(connectionSource, ItemDB.class, false);
            onCreate(database, connectionSource);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private boolean checkTableExits(SQLiteDatabase database, String tableName){
        Cursor c = null;
        boolean tableExists = false;
        try {
            c = database.query(tableName, null,null,null,null,null,null,null);
            tableExists = true;
        }catch (Exception e){

        }
        return tableExists;
    }

    public Dao<ItemDB, Long> getDao() throws  SQLException{
        if(todoDao == null){
            todoDao = getDao(ItemDB.class);
        }
        return  todoDao;
    }

    @Override
    public void close() {
        todoDao = null;
        super.close();
    }

    public void clearTable() throws SQLException{
        TableUtils.clearTable(getConnectionSource(), ItemDB.class);
    }
}
