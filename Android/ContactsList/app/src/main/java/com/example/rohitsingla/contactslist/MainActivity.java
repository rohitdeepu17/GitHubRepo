package com.example.rohitsingla.contactslist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    ListView lvCallList;
    ProgressDialog pd;
    ArrayList<String> aa = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCallList = (ListView) findViewById(R.id.list);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(MainActivity.this,
                        "Loading..", "Please Wait", true, false);
            }// End of onPreExecute method

            @Override
            protected Void doInBackground(Void... params) {
                getContacts();

                return null;
            }// End of doInBackground method

            @Override
            protected void onPostExecute(Void result) {
                pd.dismiss();
                CustomAdapter cus = new CustomAdapter(MainActivity.this);
                // ArrayAdapter<String>   arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,aa);
                lvCallList.setAdapter(cus);

                //setting item click listener to make a call to the use
                lvCallList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //For dialer : no permission required for this
                        /*Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0123456789"));
                        startActivity(intent);*/
                        //To directly make a call without going to dialer : permission required : CALL_PHONE
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        String phone = parent.getItemAtPosition(position).toString();
                        Log.d(TAG,phone);
                        intent.setData(Uri.parse("tel:"+phone));
                        startActivity(intent);
                    }
                });
            }//End of onPostExecute method
        }.execute((Void[]) null);
    }

    private void getContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                aa.add(name);
                //if this contact has any phone number
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    //add only first phone number
                    boolean flagTaken = false;
                    while (pCur.moveToNext() && !flagTaken) {
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        num.add(phoneNumber);
                        flagTaken = true;
                    }
                    pCur.close();
                }else{
                    //add default contact number
                    num.add("99999");
                }
            }
        }
    }

    public class CustomAdapter extends BaseAdapter{

        private Context mContext;

        CustomAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return aa.size();
        }

        //It is upto the user whatever he wants to return
        @Override
        public Object getItem(int position) {
            return num.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            final int pos = position;

            if (convertView == null)
            {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row, null);
                holder.textviewName = (TextView) convertView.findViewById(R.id.textView1);
                holder.textviewNumber = (TextView) convertView.findViewById(R.id.textView2);

                convertView.setTag(holder);
            }//End of if condition
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }//End of else

            holder.textviewName.setId(position);
            holder.textviewNumber.setId(position);


            holder.textviewName.setText(aa.get(position));
            holder.textviewNumber.setText(num.get(position));

            holder.id = position;

            return convertView;
        }
    }

    static class ViewHolder
    {
        TextView textviewName;
        TextView textviewNumber;
        int id;
    }

}
