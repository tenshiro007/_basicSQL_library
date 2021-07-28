package com.example.sqllibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name,author,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        image=findViewById(R.id.image);
        name=findViewById(R.id.txtName);
        author=findViewById(R.id.txtAuthor);
        desc=findViewById(R.id.txtDesc);

        Intent intent=getIntent();
        if(intent !=null){

        int id=intent.getIntExtra("book_id", 0);
            if (id != 0) {
                // TODO: 7/28/2021  Get the book by id
                new GetBookById().execute(id);
            }
        }

    }
    private class GetBookById extends AsyncTask<Integer,Void,Book>{

        private DatabaseHelper databaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseHelper=new DatabaseHelper(BookActivity.this);
        }

        @Override
        protected Book doInBackground(Integer... integers) {
            try{
                SQLiteDatabase db= databaseHelper.getReadableDatabase();
                Cursor cursor=db.rawQuery("SELECT * FROM books WHERE id=?",new String[]{String.valueOf(integers[0])});
                if(null !=cursor){
                    if(cursor.moveToFirst()){

                            int id = cursor.getInt(cursor.getColumnIndex("id"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String author=cursor.getString(cursor.getColumnIndex("author"));
                            String imageUrl=cursor.getString(cursor.getColumnIndex("imageUrl"));
                            String desc=cursor.getString(cursor.getColumnIndex("description"));
                            Book book=new Book(id,name,author,imageUrl,desc);

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
        protected void onPostExecute(Book book) {
            super.onPostExecute(book);
            if(book !=null){
                name.setText(book.getName());
                author.setText(book.getAuthor());
                Glide.with(BookActivity.this).asBitmap().load(book.getImageUrl())
                        .into(image);
                desc.setText(book.getDescription());
            }
        }
    }
}