package com.chef.master.bakingapp.room.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Petya Marinova on 7/11/2018.
 */
public class IngredientDBHelper  extends SQLiteOpenHelper {

    public IngredientDBHelper(Context context) {

        super(context, IngredientContract.DB_NAME, null, IngredientContract.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {

        String sqlQuery = "CREATE TABLE " + IngredientContract.TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientContract.Columns.QUANTITY + " TEXT, " + IngredientContract.Columns.MEASURE + " TEXT, " +
                IngredientContract.Columns.INGREDIENT + ")";

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + IngredientContract.TABLE);
        onCreate(sqlDB);
    }
}