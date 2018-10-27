package com.hfad.demo12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Cart_Items extends AppCompatActivity {
    private List<entry> cartList = new ArrayList<>();
    private RecyclerView recyclerView;
    public TextView amount;
    private Button order_button;
    private CartAdapter mAdapter;
    RadioGroup radioGroup;
    RadioButton pay_now, cod;
    private String mode_of_payment;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__items);
//        mode_of_payment = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        db = new DBHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        cod = (RadioButton) findViewById(R.id.cod);
        pay_now = (RadioButton) findViewById(R.id.pay_now);
        order_button = (Button) findViewById(R.id.order_button);
        mAdapter = new CartAdapter(this, cartList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        cartList = db.getAllItems();
        mAdapter.refreshData(cartList);

        amount = (TextView) findViewById(R.id.amount);


        int amt = 0;
        for (entry en1 : cartList) {
            amt += Integer.parseInt(en1.getTotal_price());
        }
        amount.setText(String.valueOf(amt));

        Log.e("CartList size:", "++++++++++" + cartList.size());
       /* for (entry en1 : cartList) {
            String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice();
            // Writing Contacts to log
            Log.e("++++++data  ", log);
            // Toast.makeText(this, "Check" + log, Toast.LENGTH_SHORT).show();
        }*/

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i=new Intent(Cart_Items.this,MainActivity.class);
        startActivity(i);
    }
}
