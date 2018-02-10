package com.example.gyan.workshop.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gyan.workshop.Models.User;
import com.example.gyan.workshop.Models.Workshop;

import java.util.ArrayList;

/**
 * Created by Gyan on 1/24/2018.
 */

//Class to Handle Database

public class WorkshopDatabase extends SQLiteOpenHelper {

    //static name deceleration for table and database
    private static int DB_version = 1;
    private static String DB_NAME = "WorkshopDatabase";
    private static String WORKSHOPS_TABLE_NAME = "WorkShops";
    private static String USERS_TABLE_NAME = "Users";
    private static String REGISTERED_COURSES = "RegCourses";
    public ArrayList<Workshop> workshops = new ArrayList<Workshop>();

    //Constructor
    public WorkshopDatabase(Context context) {
        super(context, WorkshopDatabase.DB_NAME, null, WorkshopDatabase.DB_version);
    }

    /*
    * Three Tables are created
    * 1. Users: to store different users.
    * 2. WorkShops: To store all workshops.
    * 3. RegCourses: To store course registered by
    *    all users.*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "                    name TEXT NOT NULL DEFAULT 'Guest',\n" +
                "                    password TEXT NOT NULL DEFAULT 'abcd',\n" +
                "                    email TEXT NOT NULL DEFAULT 'guestemail@gmail.com');");

        db.execSQL("CREATE TABLE WorkShops(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,\n" +
                "                    name TEXT NOT NULL DEFAULT 'Machine Learning',\n" +
                "                    descripction TEXT NOT NULL DEFAULT 'This is about...');");
        db.execSQL("CREATE TABLE RegCourses(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "                        user_id INT,\n" +
                "                        course_id INT,\n" +
                "                        FOREIGN KEY (user_id) REFERENCES users(id),\n" +
                "                        FOREIGN KEY (course_id) REFERENCES course(id));");
    }

    //Function to create dummy WorkShop entries
    public void createDummyWorkshops() {
        addWorkShops("Machine Learning", "This course is about Machine learning");
        addWorkShops("Artificial Intelligence", "This course is about Artificial Intelligence");
        addWorkShops("Tensor Flow", "This course is about Tensor Flow");
        addWorkShops("Deep Learning", "This course is about Deep Learning");
        addWorkShops("Data Analysis", "This course is about Data Analysis");
    }

    //Function to add new workshop
    public void addWorkShops(String name, String description) {
        Log.i("This", "Working4");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("descripction", description);
        db.insert(WorkshopDatabase.WORKSHOPS_TABLE_NAME, null, contentValues);
        db.close();
    }

    //Function to add new User
    public int addUser(String name, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("email", email);
        return (int) db.insert(WorkshopDatabase.USERS_TABLE_NAME, null, contentValues);
    }

    //Function to enroll User with id(user_id) to Course with id(course_id)
    public void enrollInWorkshop(int user_id, int course_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("course_id", course_id);
        db.insert(WorkshopDatabase.REGISTERED_COURSES, null, contentValues);
    }

    //Function to check if Email is already taken while signup.
    public boolean checkIfEmailExists(String email) {
        int regestration_id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + WorkshopDatabase.USERS_TABLE_NAME + " WHERE email = '" + email + "';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                regestration_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return false;
        }
        cursor.close();
        return regestration_id != -1;
    }

    /*
    *   Login User with email and password
    *   returns null if password email mismatch.
    *   return User object if combination Exists.
    *   */
    public User loginWithEmail(String email, String password) {
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + WorkshopDatabase.USERS_TABLE_NAME + " WHERE email = '" + email + "' AND password = '" + password + "' LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                user.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                user.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                user.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return user;
    }

    //Function to query all the workshops from database
    //returns arraylist of Workshop model.
    public ArrayList<Workshop> queryWorkshops() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + WORKSHOPS_TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("descripction"));
                    workshops.add(new Workshop(id, name, description));
                } while (cursor.moveToNext());
                cursor.close();
                return workshops;
            } else {
                cursor.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Uses Table joins of User WorkShops and RegCourses to retrieve
     * courses enrolled by user of id(user_id).
     * returns arraylist of Workshop or empty list if
     * no course registered.*/
    public ArrayList<Workshop> getCurrentWorkShop(String user_id) {
        ArrayList<Workshop> workshops1 = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT c.id, c.name, c.descripction FROM RegCourses r \n" +
                    "  JOIN WorkShops c ON r.course_id = c.id\n" +
                    "  JOIN Users u ON r.user_id = u.id WHERE u.id = " + user_id + ";";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("descripction"));
                    workshops1.add(new Workshop(id, name, description));
                } while (cursor.moveToNext());
                cursor.close();
                return workshops1;
            } else {
                cursor.close();
                return workshops1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return workshops1;
        }
    }

    //Function to check if user with id(user_id) is enrolled in course with id (course_id)
    public boolean checkIfEnrolled(String user_id, String course_id) {
        int regestration_id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + WorkshopDatabase.REGISTERED_COURSES + " WHERE user_id = " + user_id + " AND course_id = " + course_id + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                regestration_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return false;
        }
        cursor.close();
        return regestration_id != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
