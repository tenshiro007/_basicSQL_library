package com.example.sqllibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookAdapter.DeleteBook {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        adapter=new BookAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        new GetAllBook().execute();
    }

    @Override
    public void onDeleteBookResult(int bookid) {
        // TODO: 7/28/2021 delete book by id
        new DeleteBookById().execute(bookid);
    }

    private class GetAllBook extends AsyncTask<Void,Void, ArrayList<Book>>{

        private DatabaseHelper databaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseHelper=new DatabaseHelper(MainActivity.this);
        }

        @Override
        protected ArrayList<Book> doInBackground(Void... voids) {
            try{
                SQLiteDatabase db=databaseHelper.getReadableDatabase();
                Cursor cursor=db.rawQuery("SELECT * FROM books",null);
                if(null !=cursor){
                    if(cursor.moveToFirst()){
                        ArrayList<Book>book=new ArrayList<>();
                        for(int i=0;i<cursor.getCount();i++){
                            int id = cursor.getInt(cursor.getColumnIndex("id"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String author=cursor.getString(cursor.getColumnIndex("author"));
                            String imageUrl=cursor.getString(cursor.getColumnIndex("imageUrl"));
                            String desc=cursor.getString(cursor.getColumnIndex("description"));
                         book.add(new Book(id,name,author,imageUrl,desc));

                         cursor.moveToNext();
                        }
                        Log.d(TAG, "doInBackground: books"+book.get(0).toString());
                        cursor.close();
                        db.close();
                        return book;
                    }else{
                        cursor.close();
                        db.close();
                    }
                }else{
                    db.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            super.onPostExecute(books);
            if(null !=books){
                adapter.setBooks(books);
            }else{
                adapter.setBooks(new ArrayList<>());
            }
        }
    }
    private class DeleteBookById extends AsyncTask<Integer,Void,Boolean>{

        private DatabaseHelper databaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseHelper=new DatabaseHelper(MainActivity.this);
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            try{
                SQLiteDatabase db=databaseHelper.getWritableDatabase();
                int deletedRow=db.delete("books","id=?",new String[]{String.valueOf(integers[0])});
                db.close();
                if(deletedRow>0){
                    Log.d(TAG, "doInBackground: Delete Successfully");
                    return true;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(MainActivity.this, "Book Deleted Successfully", Toast.LENGTH_SHORT).show();
                new GetAllBook().execute();
            }
        }
    }
    
}