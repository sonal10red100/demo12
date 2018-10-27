package com.hfad.demo12;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
DBHandler db;
    private List<entry> cartList;
    private  TextView special;
    private Context context;
    private int qty;
    private  String pr;
    String sp,prev_sp;

    private LayoutInflater inflater;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name,quantity,tot_price,SP,org_pr,reg;
        public ImageView image;
        public Button minus,plus;

        public MyViewHolder(View view) {
            super(view);
            db = new DBHandler(context);
            item_name=(TextView)view.findViewById(R.id.item_name);
            quantity=(TextView)view.findViewById(R.id.quantity);
            tot_price=(TextView)view.findViewById(R.id.tot_price);
            image = (ImageView) view.findViewById(R.id.image);
            SP=(TextView)view.findViewById(R.id.SP);
            special=(TextView)view.findViewById(R.id.special);
            minus=(Button)view.findViewById(R.id.minus);
            plus=(Button)view.findViewById(R.id.plus);
            org_pr=(TextView)view.findViewById(R.id.org_pr);
           // reg=(TextView)view.findViewById(R.id.reg);

        }
    }

    public CartAdapter( Context context,List<entry> cartList) {
        this.cartList=cartList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {
        entry en= cartList.get(position);
        holder.item_name.setText(en.getItem());
        holder.quantity.setText(String.valueOf(en.getQuantity()));
        holder.tot_price.setText(en.getTotal_price());
        holder.SP.setText(en.getSp_des());
        holder.org_pr.setText(en.getPrice());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inc(v,holder,position);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dec(v,holder,position);
            }
        });
        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_custom_dialog);
                RadioGroup radiogroup = (RadioGroup) dialog.findViewById(R.id.radiogroup);
                RadioButton less = (RadioButton) dialog.findViewById(R.id.less);
                RadioButton med = (RadioButton) dialog.findViewById(R.id.med);
                RadioButton extra = (RadioButton) dialog.findViewById(R.id.extra);
                RadioButton jain = (RadioButton) dialog.findViewById(R.id.jain);
                final Button sp_submit_button = (Button) dialog.findViewById(R.id.sp_submit_button);
                //   dialog.setCancelable(false);
                dialog.show();

                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Log.d("chk", "id" + checkedId);

                        if (checkedId == R.id.less || checkedId == R.id.med || checkedId == R.id.extra || checkedId == R.id.jain)
                        {

                           RadioButton b = (RadioButton) dialog.findViewById(checkedId);
                            sp = b.getText().toString();


                        }
                    }
                });
                sp_submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        prev_sp= holder.SP.getText().toString();
                        holder.SP.setText(sp);
                        if(db.checkIfExists(holder.item_name.getText().toString(),sp))
                        {
                            int update_id = db.getItem(holder.item_name.getText().toString(),sp);
                            entry e=db.getItemFromId(update_id);
                            int q=Integer.parseInt(holder.quantity.getText().toString());
                            String TOTAL=holder.tot_price.getText().toString();
                            //db.deleteItem(new entry(holder.item_name.getText().toString(),holder.org_pr.getText().toString(),Integer.parseInt(holder.quantity.getText().toString()),sp,holder.tot_price.getText().toString()));
                               int id=db.getItem(holder.item_name.getText().toString(),prev_sp);
                               entry e1=db.getItemFromId(id);
                               db.deleteItem(id);
                               int tt=Integer.parseInt(e.getTotal_price())+Integer.parseInt(e1.getTotal_price());
                               entry ee=new entry(update_id,e.getItem(),e.getPrice(),e.getQuantity()+e1.getQuantity(),sp,String.valueOf(tt));
                               db.updateItem(ee);

                            cartList = db.getAllItems();
                            notifyDataSetChanged();

                               List<entry> _entry = db.getAllItems();
                            for (entry en1 : _entry)
                            {
                                String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
                                // Writing Contacts to log
                                Log.e("++++++entry added  ", log);
                            }

                        }
                    }
                });
            }

            });

    }




    private void dec(View v, MyViewHolder holder, int position) {
        qty = Integer.parseInt(holder.quantity.getText().toString());
        pr=holder.tot_price.getText().toString();
        qty = qty - 1;
       // Log.e("item,sp","++++"+holder.item_name.getText().toString()+" "+holder.SP.getText().toString());
        int update_id = db.getItem(holder.item_name.getText().toString(),holder.SP.getText().toString());

        if(qty==0)
        {

            db.deleteItem(update_id);
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,getItemCount());
        }
        else
        {
           // int update_id = db.getItem(holder.item_name.getText().toString(),holder.SP.getText().toString());
            Log.e("item", "+++++++" + update_id);
            int tot=Integer.parseInt(holder.tot_price.getText().toString())-Integer.parseInt(holder.org_pr.getText().toString());
            entry e1 = new entry(update_id,holder.item_name.getText().toString(),holder.org_pr.getText().toString(),qty,holder.SP.getText().toString(),String.valueOf(tot));
            // Log.e("priceee", "++++++" + String.valueOf(Integer.parseInt(item.getPrice()) * (qty+1)));
            db.updateItem(e1);

            holder.quantity.setText(String.valueOf(qty));
            holder.tot_price.setText(String.valueOf(tot));


        }
        List<entry> _entry = db.getAllItems();
        for (entry en1 : _entry)
        {
            String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
            // Writing Contacts to log
            Log.e("++++++entry added  ", log);
        }


    }

    private void inc(View v, MyViewHolder holder, int position) {
        qty = Integer.parseInt(holder.quantity.getText().toString());
        pr=holder.tot_price.getText().toString();
      //  qty = qty + 1;

            int qty=db.getItemQuantity(holder.item_name.getText().toString(),holder.SP.getText().toString());
            Log.e("qty+++++",""+qty);
            int update_id = db.getItem(holder.item_name.getText().toString(),holder.SP.getText().toString());
            Log.e("item", "+++++++" + update_id);
            int tot=Integer.parseInt(holder.tot_price.getText().toString())+Integer.parseInt(holder.org_pr.getText().toString());
            entry e1 = new entry(update_id,holder.item_name.getText().toString(),holder.org_pr.getText().toString(),qty+1,holder.SP.getText().toString(),String.valueOf(tot));
           // Log.e("priceee", "++++++" + String.valueOf(Integer.parseInt(item.getPrice()) * (qty+1)));
            db.updateItem(e1);

            holder.quantity.setText(String.valueOf(qty+1));
            holder.tot_price.setText(String.valueOf(tot));

           // Cart_Items ci=new Cart_Items();
            //ci.amount.setText(db.getCartTotal());

       /* int amt = 0;
        for (entry en1 : cartList) {
            amt += Integer.parseInt(en1.getTotal_price());
        }
        Cart_Items ci=new Cart_Items();
        ci.amount.setText(String.valueOf(amt));
*/
        List<entry> _entry = db.getAllItems();
        for (entry en1 : _entry)
        {
            String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
            // Writing Contacts to log
            Log.e("++++++entry added  ", log);
        }

       // TextView foregroundView = ((ItemsAdapter.MyViewHolder)RecyclerView.ViewHolder).tt2;


    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void refreshData(List<entry> cartList){
        this.cartList = cartList;
        notifyDataSetChanged();
    }

}
