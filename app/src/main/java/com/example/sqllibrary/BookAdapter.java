package com.example.sqllibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    public interface DeleteBook{
        void onDeleteBookResult(int bookid);
    }
    private DeleteBook deleteBook;
    private Context context;
    private ArrayList<Book>books=new ArrayList<>();

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  BookAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(books.get(position).getName());
        Glide.with(context).asBitmap().load(books.get(position).getImageUrl())
                .into(holder.imageView);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,BookActivity.class);
                intent.putExtra("book_id", books.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Are your sure to delete "+books.get(position).getName()+" ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 7/28/2021 create navigate to main activity
//                        cast to parent activity
                        try{
                            deleteBook=(DeleteBook)context;
                            deleteBook.onDeleteBookResult(books.get(position).getId());

                        }catch (ClassCastException e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imageView;
        private MaterialCardView parent;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            imageView=itemView.findViewById(R.id.image);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
