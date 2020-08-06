package com.example.clientapp.model.db;

import android.content.Context;

import com.example.clientapp.model.dto.ItemDB;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class DatabaseManager {
    private final String TAG = DatabaseManager.this.getClass().getSimpleName();

    private final Context mContext;
    private static DatabaseManager INSTANCE;
    private DatabaseHelper databaseHelper;

    private Dao<ItemDB, Long> itemDao;
    private static String PERSON_NAME = "personName";
    private static String PERSON_AGE = "personAge";
    private final int QTY_MAX_VALUE = 99999;

    public DatabaseManager(Context mContext) {
        this.mContext = mContext;
        databaseHelper = OpenHelperManager.getHelper(mContext,DatabaseHelper.class);

        try {
            itemDao = databaseHelper.getDao();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance(Context context){
        if(INSTANCE == null) INSTANCE = new DatabaseManager(context);
        return  INSTANCE;
    }

    public void releaseDB(){
        if (databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
            INSTANCE = null;
        }
    }

    public  int clearAllData(){
        try {
            if(databaseHelper == null)return -1;
            databaseHelper.clearTable();
            return 0;
        }catch (SQLException e){
            e.printStackTrace();
            return  -1;
        }
    }

    public List<ItemDB> getAllItems(){
        try {
            if(itemDao == null)return null;
            return itemDao.queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public long getAllItemsCounts() {
        try {
            if (itemDao == null) return -1;
            return itemDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

//    public int insertItem(ItemDB itemDB, boolean updateTotalQuantity) {
//        try {
//            UpdateBuilder updateBuilder = itemDao.updateBuilder();
//            String selectedCodeOvr  = itemDB.getName()!= null ? "jkjjj" : itemDB.getName();
//            itemDB.setName(selectedCodeOvr);
//
//            if (itemDao == null) return -1;
//            if (isItemExisting(selectedCodeOvr)) {
//                double selectedQuantity = 0;
//                double totalQuantity = 0;
//                double currentQuantity = Double.parseDouble(itemDB.getItmQuantity() != null ? itemDB.getItmQuantity() : "0" );
//                if(!updateTotalQuantity){
//                    selectedQuantity = Double.parseDouble(getItemSelectedQuantity(selectedCodeOvr));
//                    totalQuantity = selectedQuantity + currentQuantity;
//                } else {
//                    totalQuantity = currentQuantity;
//                }
//                if (totalQuantity > QTY_MAX_VALUE) return -2;
//                updateBuilder.updateColumnValue(ITEM_QUANTITY,( Math.round(totalQuantity)) + "").where().eq(SELECTED_CODE_OVER,itemDB.getSelectedCodeOvr());
//                updateBuilder.update();
//            } else {
//                itemDao.create(itemDB);
//            }
//            return 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

//    public String getItemSelectedQuantity(String selectedCodeOvr) {
//        QueryBuilder queryBuilder = itemDao.queryBuilder();
//        List<ItemDB> itemDBList = new ArrayList<>();
//        try {
//            queryBuilder.selectColumns(ITEM_QUANTITY).where().eq(SELECTED_CODE_OVER,selectedCodeOvr);
//            itemDBList = queryBuilder.query();
//            String quantity = itemDBList.get(0).getItmQuantity();
//            return quantity != null ? quantity : "0";
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return "0";
//        }
//    }

//    public boolean isItemExisting (String selectedCodeOvr){
//        QueryBuilder queryBuilder = itemDao.queryBuilder();
//        boolean flag = false;
//        try {
//            if (queryBuilder.where().eq(SELECTED_CODE_OVER,selectedCodeOvr).countOf() > 0) {
//                flag = true;
//            } else {
//                flag = false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }

//    public int insertItem(ArrayList<ItemDB> itemDBs) {
//        try {
//            if (itemDao == null) return -1;
//            for (ItemDB itemDB : itemDBs) {
//                itemDao.create(itemDB);
//            }
//            return 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

//    public int deleteItem(String selectedCodeOver) {
//        try {
//            if (itemDao == null) return -1;
//            DeleteBuilder deleteBuilder = itemDao.deleteBuilder();
//            if(selectedCodeOver != null || !selectedCodeOver.isEmpty()) deleteBuilder.where().eq(SELECTED_CODE_OVER,selectedCodeOver);
//            deleteBuilder.delete();
//            return 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }


}
