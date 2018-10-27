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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by HP on 16-05-2018.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    public List<items> itemsList;
    public Context context;
    DBHandler db;
    RadioButton b;


    public int quantity = 0, k = 0;
    public String sp = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item, price;
        public RelativeLayout viewBackground, viewForeground;

        public ImageView image;
        public Button dec;
        public Button inc, sp_des;
        //  public TextView tt1;
        public TextView tt2, reg;
        public TextView total;


        public MyViewHolder(View view) {
            super(view);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);


            item = (TextView) view.findViewById(R.id.it);
            image = (ImageView) view.findViewById(R.id.img);
            price = (TextView) view.findViewById(R.id.pr);
            dec = (Button) view.findViewById(R.id.dec);
            inc = (Button) view.findViewById(R.id.inc);
            total = (TextView) view.findViewById(R.id.total);
            tt2 = (TextView) view.findViewById(R.id.t1);
            sp_des = (Button) view.findViewById(R.id.sp_des);
            reg = (TextView) view.findViewById(R.id.reg);
            db = new DBHandler(context);
        }
    }

    public ItemsAdapter(List<items> itemsList, Context context) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        items item = itemsList.get(position);


        holder.item.setText(item.getItem());
        holder.price.setText(item.getPrice());
        holder.image.setImageResource(item.getImageId());

        holder.dec.setOnClickListener(new View.OnClickListener() {
            //   @Override
            public void onClick(View v) {
                decrement(v, holder, position);
            }
        });
        holder.inc.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                Log.e("hhsjh", "+++++++++++++++ clicked");
                increment(v, holder, position);
            }
        });
        holder.sp_des.setOnClickListener(new View.OnClickListener() {
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

                        if (checkedId == R.id.less || checkedId == R.id.med || checkedId == R.id.extra || checkedId == R.id.jain) {

                            b = (RadioButton) dialog.findViewById(checkedId);
                            sp = b.getText().toString();
                            holder.tt2.setText("0");
                            holder.total.setText("0");


                        }
                    }
                });
                sp_submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        holder.reg.setText(sp);
                    }
                });
            }
        });


    }

    public void increment(View view, MyViewHolder holder, final int position) {
        quantity = Integer.parseInt(holder.tt2.getText().toString());
        quantity = quantity + 1;
        items item = itemsList.get(position);

        //normal addition
        if (holder.reg.getText().toString().equals("Regular")) {
            if (quantity == 1) {
                if (!db.checkIfExists(item.getItem(), holder.reg.getText().toString())) {
                    TextView tv = (TextView) ((MainActivity) context).findViewById(R.id.badge_notification_1);
                    int k = Integer.parseInt(tv.getText().toString());
                    String s = String.valueOf(++k);
                    Log.e("DDDDDD", s);
                    tv.setText(s);

                    entry e = new entry(item.getItem(), item.getPrice(), 1, "Regular", item.getPrice());
                    Log.e("priceee", "++++++" + item.getPrice());
                    db.addItem(e);
                } else {
                    int update_id = db.getItem(item.getItem(), holder.reg.getText().toString());
                    int total = Integer.parseInt(item.getPrice()) * quantity;
                    entry e = new entry(update_id, item.getItem(), item.getPrice(), quantity, holder.reg.getText().toString(), String.valueOf(total));
                    Log.e("priceee", "++++++" + item.getPrice());
                    db.updateItem(e);
                }

            } else {
                int update_id = db.getItem(item.getItem(), "Regular");
                int total = Integer.parseInt(item.getPrice()) * quantity;
                entry e = new entry(update_id, item.getItem(), item.getPrice(), quantity, "Regular", String.valueOf(total));
                Log.e("priceee", "++++++" + item.getPrice());
                db.updateItem(e);
            }
        } else {
            if (quantity == 1) {
                if (!db.checkIfExists(item.getItem(), holder.reg.getText().toString())) {
                    TextView tv = (TextView) ((MainActivity) context).findViewById(R.id.badge_notification_1);
                    int k = Integer.parseInt(tv.getText().toString());
                    String s = String.valueOf(++k);
                    Log.e("DDDDDD", s);
                    tv.setText(s);

                    entry e = new entry(item.getItem(), item.getPrice(), 1, holder.reg.getText().toString(), item.getPrice());
                    Log.e("priceee", "++++++" + item.getPrice());
                    db.addItem(e);
                } else {
                    int update_id = db.getItem(item.getItem(), holder.reg.getText().toString());
                    int total = Integer.parseInt(item.getPrice()) * quantity;
                    entry e = new entry(update_id, item.getItem(), item.getPrice(), quantity, holder.reg.getText().toString(), String.valueOf(total));
                    Log.e("priceee", "++++++" + item.getPrice());
                    db.updateItem(e);
                }

               /* TextView tv = (TextView) ((MainActivity) context).findViewById(R.id.badge_notification_1);
                int k = Integer.parseInt(tv.getText().toString());
                String s = String.valueOf(++k);
                Log.e("DDDDDD", s);
                tv.setText(s);

                entry e = new entry(item.getItem(), item.getPrice(), 1, holder.reg.getText().toString(), item.getPrice());
                Log.e("priceee", "++++++" + item.getPrice());
                db.addItem(e);
*/            } else if (db.checkIfExists(item.getItem(), holder.reg.getText().toString())) {
                int qty = db.getItemQuantity(item.getItem(), holder.reg.getText().toString());
                Log.e("qty+++++", "" + qty);
                int update_id = db.getItem(item.getItem(), holder.reg.getText().toString());
                Log.e("item", "+++++++" + update_id);
                Log.e("sp", "" + sp);
                entry e1 = new entry(update_id, item.getItem(), item.getPrice(), qty + 1, holder.reg.getText().toString(), String.valueOf(Integer.parseInt(item.getPrice()) * (qty + 1)));
                Log.e("priceee", "++++++" + String.valueOf(Integer.parseInt(item.getPrice()) * (qty + 1)));
                db.updateItem(e1);
            }


        }
        List<entry> _entry = db.getAllItems();
        for (
                entry en1 : _entry)

        {
            String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
            // Writing Contacts to log
            Log.e("++++++entry added  ", log);

        }


        /*final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_custom_dialog);
        RadioGroup radiogroup = (RadioGroup) dialog.findViewById(R.id.radiogroup);
        RadioButton less = (RadioButton) dialog.findViewById(R.id.less);
        RadioButton med = (RadioButton) dialog.findViewById(R.id.med);
        RadioButton extra = (RadioButton) dialog.findViewById(R.id.extra);
        RadioButton jain = (RadioButton) dialog.findViewById(R.id.jain);
        dialog.setCancelable(false);
        dialog.show();

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d("chk", "id" + checkedId);

                if (checkedId == R.id.less || checkedId == R.id.med || checkedId == R.id.extra || checkedId == R.id.jain) {

                    {
                        b = (RadioButton) dialog.findViewById(checkedId);
                        sp = b.getText().toString();

                        items item = itemsList.get(position);
                      //  Log.e("item", "+++++++" + db.getItem(item.getItem(),sp));
                        entry ee=new entry(item.getItem(),item.getPrice(),quantity,sp,item.getPrice());

                        if(db.checkIfExists(item.getItem(),sp))
                        {
                            int qty=db.getItemQuantity(item.getItem(),sp);
                            Log.e("qty+++++",""+qty);
                            int update_id = db.getItem(item.getItem(),sp);
                            Log.e("item", "+++++++" + update_id);
                            Log.e("sp",""+sp);
                            entry e1 = new entry(update_id, item.getItem(), item.getPrice(), qty+1,sp, String.valueOf(Integer.parseInt(item.getPrice()) * (qty+1)));
                            Log.e("priceee", "++++++" + String.valueOf(Integer.parseInt(item.getPrice()) * (qty+1)));
                            db.updateItem(e1);

                        }
                        else {
                            Log.e("sp_add",""+sp);
                            entry e = new entry(item.getItem(), item.getPrice(),1, sp, item.getPrice());
                            Log.e("priceee", "++++++" + item.getPrice());
                            db.addItem(e);
                        }

                        List<entry> _entry = db.getAllItems();
                        for (
                                entry en1 : _entry)

                        {
                            String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
                            // Writing Contacts to log
                            Log.e("++++++entry added  ", log);

                        }
                        Log.e("hhsjh", "+++++++++++++++ quantity" + String.valueOf(quantity));



                        Toast.makeText(context, sp, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }

                }
            }
        });*/
        //  String sp = ((RadioButton) dialog.findViewById(radiogroup.getCheckedRadioButtonId())).getText().toString();





         /* items item = itemsList.get(position);
            entry e = new entry(item.getItem(), item.getPrice(),sp,item.getPrice());
            Log.e("priceee", "++++++" + item.getPrice());
            db.addItem(e);
            //}else {
            //   items item = itemsList.get(position);
            Log.e("item", "+++++++" + db.getItem(item.getItem()));
            int update_id = db.getItem(item.getItem());
            Log.e("item", "+++++++" + update_id);
            entry e = new entry(update_id, item.getItem(), item.getPrice(), quantity, String.valueOf(Integer.parseInt(item.getPrice()) * quantity));
            Log.e("priceee", "++++++" + String.valueOf(Integer.parseInt(item.getPrice()) * quantity));
            db.updateItem(e);

            //}
            List<entry> _entry = db.getAllItems();
            for (
                    entry en1 : _entry)

            {
                String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice();
                // Writing Contacts to log
                Log.e("++++++entry added  ", log);

            }
            Log.e("hhsjh", "+++++++++++++++ quantity" + String.valueOf(quantity));

*/


        displayQuantity(quantity, holder);

        displayTotal(quantity, holder);

    }


    public void decrement(View view, MyViewHolder holder, int position) {
        quantity = Integer.parseInt(holder.tt2.getText().toString());
        quantity = quantity - 1;
        if (quantity < 0) {
            Toast.makeText(context, "You can't order less than 0 item", Toast.LENGTH_SHORT).show();
            holder.tt2.setText("0");
        } else {
            items item = itemsList.get(position);
            int update_id = db.getItem(item.getItem(), holder.reg.getText().toString());
            entry e = new entry(update_id, item.getItem(), item.getPrice(), quantity, holder.reg.getText().toString(), String.valueOf(Integer.parseInt(item.getPrice()) * quantity));

            if (quantity == 0) {
                TextView tv = (TextView) ((MainActivity) context).findViewById(R.id.badge_notification_1);
                int k = Integer.parseInt(tv.getText().toString());
                String s = String.valueOf(--k);
                Log.e("DDDDDD", s);
                tv.setText(s);
                //  entry e1 = new entry(update_id, item.getItem(), item.getPrice(), quantity,holder.reg.getText().toString(),String.valueOf(Integer.parseInt(item.getPrice()) * quantity));
                db.deleteItem(update_id);
                Log.e("quantity", "+++++++" + quantity);
            } else {

                if (quantity >= 0)

                    db.updateItem(e);


            }
            displayQuantity(quantity, holder);
            displayTotal(quantity, holder);
            List<entry> _entry = db.getAllItems();
            for (
                    entry en1 : _entry)

            {
                String log = "Id: " + en1.getID() + " ,Item Name: " + en1.getItem() + " ,Price " + en1.getPrice() + " ,Quantity: " + en1.getQuantity() + " ,Special Description: " + en1.getSp_des() + " ,Total Price:" + en1.getTotal_price();
                // Writing Contacts to log
                Log.e("++++++entry added  ", log);

            }
        }

    }


    private void displayTotal(int quantity, MyViewHolder holder) {
        Double a = (quantity * (Double.parseDouble(holder.price.getText().toString())));
        holder.total.setText("" + a);

    }

    private void displayQuantity(int quantity, MyViewHolder holder) {
        // Log.e("hhsjh", "+++++++++++++++ quantity" + String.valueOf(quantity));

        // Double a = (quantity * (Double.parseDouble(holder.price.getText().toString())));

        //Log.e("hhsjh", "+++++++++++++++ quantity" + String.valueOf(a));


        holder.tt2.setText("" + quantity);

    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void removeItem(int position) {
        itemsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(items item, int position) {
        itemsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}


