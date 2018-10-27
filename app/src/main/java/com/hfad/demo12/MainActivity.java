package com.hfad.demo12;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private List<items> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    public TextView badge_notification1;
    RelativeLayout notificationCount1;
    DBHandler db;
    public Context context;
    Button cart_button;

    /* @Override
     public void onClick(String value){
 // value this data you receive when increment() / decrement() called
     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        itemList.clear();
        db = new DBHandler(this);
       // db.deleteTable();
        cart_button = (Button) findViewById(R.id.cart_button);

        notificationCount1 = (RelativeLayout) findViewById(R.id.badge_layout1);
        badge_notification1 = (TextView) findViewById(R.id.badge_notification_1);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ItemsAdapter(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, MainActivity.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareMovieData();

          badge_notification1.setText(String.valueOf(db.getItemsCount()));
        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Cart_Items.class);
                startActivity(i);
            }
        });


    }


    private void prepareMovieData() {
        items item = new items(R.drawable.jl, "Jaljeera Water", "60");
        itemList.add(item);

        item = new items(R.drawable.soda, "Jaljeera Soda", "80");
        itemList.add(item);

        item = new items(R.drawable.veg, "Veg. Jal Frezi", "205");
        itemList.add(item);

        item = new items(R.drawable.paneer, "Paneer JalFrezi", "220");
        itemList.add(item);


        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ItemsAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = itemList.get(viewHolder.getAdapterPosition()).getItem();

            // backup of removed item for undo purpose
            final items deletedItem = itemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            mAdapter.removeItem(viewHolder.getAdapterPosition());

            //mAdapter.notifyDataSetChanged();


            //items item = itemsList.get(position);
            //entry en=db.getEntry(deletedItem.getItem());
            //entry e = new entry(en.getID(), deletedItem.getItem(), deletedItem.getPrice(), en.getQuantity(), String.valueOf(Integer.parseInt(deletedItem.getPrice()) * (en.getQuantity())));
            db.deleteItemUsingItemName(deletedItem.getItem());

            int kk = Integer.parseInt(badge_notification1.getText().toString());
            if (kk != 0) {
                Log.e("kk", "+++++++++" + kk);
                String s = String.valueOf(--kk);
                Log.e("DDDDDD", s);
                badge_notification1.setText(s);
            }
            List<entry> cartList = db.getAllItems();
            for (entry en1 : cartList) {
                String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice();
                // Writing Contacts to log
                Log.e("++++++data  ", log);

            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + "", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


}
