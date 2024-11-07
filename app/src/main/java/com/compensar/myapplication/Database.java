package com.compensar.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private  Context context;
    private   static  final String Database_Name="shoop.db";
    private  static  final int Database_Version=1;

    private  static  final String Table_Name="products";
    private  static  final String Column_Id="id";
    private  static  final String Column_Name_product="name";
    private  static  final String Column_Pricing="price";



    public Database(@Nullable Context context) {
        super(context, Database_Name, null, Database_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE "+Table_Name +
                " ( "+Column_Id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Column_Name_product+" TEXT,"
                +Column_Pricing+" INTEGER );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        onCreate(db);
    }

    public void deleteProduct(String row_id){
        SQLiteDatabase db=  getWritableDatabase();
       long result= db.delete(Table_Name, "id=?", new String[]{row_id});
       if (result==-1){
           Toast.makeText(context, "Failed to delete data", Toast.LENGTH_SHORT);
       }
       else{
           Toast.makeText(context, "delete product successful", Toast.LENGTH_SHORT);
       }
    }

    public long addProduct(String nameProduct, int price  ){
    SQLiteDatabase db=  getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(Column_Name_product, nameProduct);
         cv.put(Column_Pricing, price);
         long result= db.insert(Table_Name, null, cv );
         if (result == -1){
             Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
         }else{
             Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
         }
        return result;
    }
     Cursor readAllProducts (){
        String query="SELECT * FROM "+ Table_Name;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=null;
        if (db !=null){
           cursor= db.rawQuery(query, null);
        }
         return cursor;
     }
     void updateProduct (String row_id, String newNameProduct, String newPriceProduct){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(Column_Name_product, newNameProduct );
        cv.put(Column_Pricing, newPriceProduct);
        long result=db.update(Table_Name, cv, "id=?", new String[]{row_id});
        if (result==-1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
        }
     }
}
