package com.mob3000.cinematrum.sqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.User;

import java.util.ArrayList;


public class DataAcessor {

    private DataAcessor() {
    }

    public static User getSingleUser(Context ctx, int userId) {
        User user = new User();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " where " + DatabaseHelper.COLUMN_USER_userID + "=?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {

                User tmpUser = new User();
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_name);
                int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
                int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephon);
                int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);
                tmpUser.setUser_id(cursor.getInt((idIndex)));
                tmpUser.setUserType(cursor.getString(userTypeIndex));
                tmpUser.setName(cursor.getString(nameIndex));
                tmpUser.setPassword(cursor.getString(passwordIndex));
                tmpUser.setTelephone(cursor.getString(telephoneIndex));
                user = tmpUser;

            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return user;
        }
    }


    public static ArrayList<User> getAllUser(Context ctx) {
        ArrayList<User> allUsers = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " ;";
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    User tmpUser = new User();
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userID);
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_name);
                    int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
                    int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephon);
                    int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);
                    tmpUser.setUser_id(cursor.getInt((idIndex)));
                    tmpUser.setUserType(cursor.getString(userTypeIndex));
                    tmpUser.setName(cursor.getString(nameIndex));
                    tmpUser.setPassword(cursor.getString(passwordIndex));
                    tmpUser.setTelephone(cursor.getString(telephoneIndex));
                    allUsers.add(tmpUser);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return allUsers;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return allUsers;
        }
    }
}
