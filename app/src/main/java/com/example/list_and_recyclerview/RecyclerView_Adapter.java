package com.example.list_and_recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_Adapter extends  RecyclerView.Adapter{
    private static final int DEFAULT_MAX_ROWS = 100;
    private final LayoutInflater li;
    private final Context ctx;
    private final int maxRows;

    //one arg constructor uses DEFAULT_MAX_ROWS
    public RecyclerView_Adapter(Context ctx) {
        this(ctx,DEFAULT_MAX_ROWS);
     }

     //two arg constructor in case user wants to define their own maxrows
    public RecyclerView_Adapter(Context ctx, int maxRows) {
        this.ctx = ctx;
        li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.maxRows=maxRows;
    }
    private class GetNumber extends AsyncTask<Void, Void, Integer> {
        //ref to a viewholder, this could change if
        //RowViewHolder myVH is recycled and reused!!!!!!!!!
        private RowViewHolder myVh;
        //since myVH may be recycled and reused
        //we have to verify that the result we are returning
        //is still what the viewholder wants
        private int original_number;

        public GetNumber(RowViewHolder myVh) {
            //hold on to a reference to this viewholder
            //note that its contents (specifically iv) may change
            //iff the viewholder is recycled
            this.myVh = myVh;
            //make a copy to compare later, once we have the image
            this.original_number = myVh.numb;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            //just sleep for a bit to simulate long running downloaded
            //but could just as easily make a network call
            try {
                Thread.sleep(100); //sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return original_number*original_number;
        }
        @Override
        protected void onPostExecute(Integer param) {
            //got a result, if the following are NOT equal
            // then the view has been recycled and is being used by another
            // number DO NOT MODIFY
            if (this.myVh.numb == this.original_number){
                //still valid
                //set the result on the main thread
                myVh.iv.setImageResource(R.drawable.ok);
                myVh.tvInfo.setText(Integer.toString(this.myVh.numb) + " squared =");
                myVh.tvResult.setText(Integer.toString(param));
             }
            else{
                //NOTE This is only here to show you when
                //a thread returns to a recycled ViewHolder
                //You should not highlight the fact that you wasted
                //work, so comment out the following 3 lines for a real app
                myVh.iv.setImageResource(R.drawable.notneeded);
                myVh.tvInfo.setText("DANG! work wasted");
                myVh.tvResult.setText("");
             }
        }

    }
    class RowViewHolder extends RecyclerView.ViewHolder {
        private static final int UNINITIALIZED = -1;
        int numb = UNINITIALIZED;
        TextView tvInfo;
        TextView tvResult;
        ImageView iv;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = (TextView)itemView.findViewById(R.id.tvInfo);
            tvResult = (TextView)itemView.findViewById(R.id.tvResult);
            iv=(ImageView)itemView.findViewById(R.id.imageView1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //call this when we need to create a brand new PagerViewHolder
        View view = li.inflate(R.layout.row_layout2, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //passing in an existing instance, reuse the internal resources
        //pass our data to our ViewHolder.
        RowViewHolder viewHolder = (RowViewHolder) holder;
        viewHolder.numb= position;

        //initialize the UI
        viewHolder.iv.setImageResource(R.drawable.unknown);
        viewHolder.tvInfo.setText("Hold on a sec...");
        viewHolder.tvResult.setText("");


        //launch a thread to 'retreive' the image
        GetNumber myTask = new GetNumber(viewHolder);
        myTask.execute();

//        viewHolder.iv.setImageResource(R.drawable.ok);
//        viewHolder.tvInfo.setText(Integer.toString(position));
//        viewHolder.tvResult.setText(Integer.toString(position*position));
    }

    @Override
    public int getItemCount() {
        return this.maxRows;
    }
}
