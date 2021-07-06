package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String DB_Name ="myapplication";
    private static final String DB_CATEGORIES = "Categories";
    private static final String DB_TASKS_LIST = "Tasks_List";
    private static final String DB_TASKS_INVENTORY = "Tasks_Inventory";
    private static final String DB_TASKS_IMAGES = "Tasks_Images";
    private static final String DB_TASKS_MEETING= "Tasks_Meeting";
    ContentValues cv;
    SQLiteDatabase myDB=getReadableDatabase();


    public DataBaseHelper(Context context){
        super(context, DB_Name,null,1);
        cv = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_CATEGORIES + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, CATG_NAME TEXT UNIQUE,CATG_DESC TEXT,CATG_TYPE INT);");
        db.execSQL("CREATE TABLE " + DB_TASKS_LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT UNIQUE,CATGNO INTEGER);"); // List / Meeting
        db.execSQL("CREATE TABLE " + DB_TASKS_INVENTORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT UNIQUE,QUANTITY TEXT, CATGNO INTEGER);"); // Counter/Inventory
        db.execSQL("CREATE TABLE " + DB_TASKS_IMAGES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE BLOB,CATGNO INTEGER);");
        db.execSQL("CREATE TABLE " + DB_TASKS_MEETING + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK_NAME TEXT UNIQUE, DATE TEXT, TIME TEXT, CATGNO INTEGER)");

        Log.i("Database","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_MEETING);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TASKS_MEETING);
        onCreate(db);
    }

    public boolean insertCategory(String name,String desc,int type)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CATG_NAME", name);
        cv.put("CATG_DESC", desc);
        cv.put("CATG_TYPE",type);
        long result = myDB.insert(DB_CATEGORIES,null,cv);
        return result != -1;
    }
    public void editCategory(String original_name, String new_name,String new_descp)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CATG_NAME", new_name);
        cv.put("CATG_DESC", new_descp);
        myDB.update(DB_CATEGORIES,cv, "CATG_NAME = ? ", new String[]{original_name});
    }
    public void deleteCategory(String name)
    {
        myDB = getWritableDatabase();
        myDB.delete(DB_CATEGORIES,"CATG_NAME = ?  ",new String[]{name});
    }
    public Cursor viewCategories()
    {
        myDB = getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from " + DB_CATEGORIES,null);
        return cursor;
    }

    public int[] getInfo(String catg)
    {
        Cursor cursor = myDB.rawQuery("select * from " + DB_CATEGORIES + " where CATG_NAME = ?",new String[]{catg});
        cursor.moveToFirst();
        int[] info = new int[2];
        info[0] = cursor.getInt(0);
        info[1] = cursor.getInt(3);
        return info;
    }




    // FUNCTIONS FOR LIST



    public boolean insertTask_List(String name,int Catg_ID)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TASK", name);
        cv.put("CATGNO", Catg_ID);
        long result = myDB.insert(DB_TASKS_LIST,null,cv);
        return result != -1 ;
    }
    public Cursor viewTasks_List(int Catg_ID)
    {
        myDB = getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from " + DB_TASKS_LIST + " where CATGNO =" + Catg_ID,null);
        return cursor;
    }
    public void deleteTasks_List(int Catg_ID,String name)
    {
        myDB = getWritableDatabase();
        myDB.delete(DB_TASKS_LIST,"TASK = ? AND CATGNO = ?  ",new String[]{name,String.valueOf(Catg_ID)});
    }
    public void editTasks_List(int Catg_ID,String original_name, String new_name)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TASK", new_name);
        myDB.update(DB_TASKS_LIST,cv, "TASK = ? AND CATGNO = ? ", new String[]{original_name,String.valueOf(Catg_ID)});
    }




    // FUNCTIONS FOR INVENTORY



    public boolean insertTask_Inventory(String name,int Catg_ID,String quantity)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TASK", name);
        cv.put("QUANTITY", quantity);
        cv.put("CATGNO", Catg_ID);
        long result = myDB.insert(DB_TASKS_INVENTORY,null,cv);
        return result != -1 ;
    }
    public Cursor viewTasks_Inventory(int Catg_ID)
    {
        myDB = getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from " + DB_TASKS_INVENTORY + " where CATGNO =" + Catg_ID,null);
        return cursor;
    }
    public boolean deleteTasks_Inventory(int Catg_ID,String name)
    {
        myDB = getWritableDatabase();
        int result = myDB.delete(DB_TASKS_INVENTORY,"TASK = ? AND CATGNO = ? ",new String[]{name,String.valueOf(Catg_ID)});
        return  result!=0;
    }
    public void editTasks_Inventory(int Catg_ID,String original_name,String original_quantity,String new_name,String new_quantity)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(new_quantity == null)
            cv.put("TASK", new_name);
        else if(new_name == null)
            cv.put("QUANTITY",new_quantity);
        else
        {
            cv.put("TASK", new_name);
            cv.put("QUANTITY",new_quantity);
        }
        myDB.update(DB_TASKS_INVENTORY,cv, "TASK = ? AND CATGNO = ?", new String[]{original_name,String.valueOf(Catg_ID)});
    }


    //FUNCTIONS FOR IMAGES
    public void insertTask_Image(int Catg_no, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put("IMAGE",image);
        cv.put("CATGNO",Catg_no);
        database.insert( DB_TASKS_IMAGES, null, cv );
    }
    public Cursor viewTasks_Images(int Catg_ID)
    {
        myDB = getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from " +  DB_TASKS_IMAGES + " where CATGNO =" + Catg_ID,null);
        return cursor;
    }
    /*public boolean deleteTasks_Images(int Catg_ID, byte[] image)
    {
        myDB = getWritableDatabase();
        int result = myDB.delete(DB_TASKS_IMAGES,"IMAGE = ? AND CATGNO = ? ",new String[]{String.valueOf(image),String.valueOf(Catg_ID)});
        return  result!=0;
    }*/
    public boolean deleteTasks_Images(int Catg_ID, byte[] image)
    {
        int change_id = 0;
        Cursor cursor = viewTasks_Images(Catg_ID);
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            byte[] array = cursor.getBlob(1);
            if(Arrays.equals(image,array))
                change_id = id;
        }
        myDB = getWritableDatabase();
        int result = myDB.delete(DB_TASKS_IMAGES,"ID = ?",new String[]{String.valueOf(change_id)});
        return  result!=0;
    }
    public Cursor viewTasks(int Catg_ID){
        myDB = getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from " + DB_TASKS_MEETING + " where CATGNO =" + Catg_ID,null);
        return cursor;
    }
    public Cursor viewTaskDetails(String taskName,int CATG_ID){
        myDB=this.getReadableDatabase();
        Cursor cursor=myDB.rawQuery("select * from "+DB_TASKS_MEETING+" where TASK_NAME = ? AND CATGNO=?", new String[]{taskName,String.valueOf(CATG_ID)});
        return cursor;
    }
    public void updateTask(String ptn,String ntn,String nd,String nt,int Catg_ID){
        myDB=this.getWritableDatabase();
        ContentValues contentvalues=new ContentValues();
        contentvalues.put("TASK_NAME",ntn);
        contentvalues.put("DATE",nd);
        contentvalues.put("TIME",nt);
        myDB.update(DB_TASKS_MEETING,contentvalues, "TASK_NAME = ? AND CATGNO = ?", new String[]{ptn,String.valueOf(Catg_ID)});
    }
    public void delete_task(String string,int Catg_ID){
        myDB=this.getWritableDatabase();
        myDB.delete(DB_TASKS_MEETING,"TASK_NAME =?",new String[]{string});
    }
    public void insertTask(String Task,String Date,String Time,int Catg_ID)
    {
        myDB = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TASK_NAME", Task);
        cv.put("DATE", Date);
        cv.put("TIME", Time);
        cv.put("CATGNO", Catg_ID);
        myDB.insert(DB_TASKS_MEETING,null,cv);
    }
}