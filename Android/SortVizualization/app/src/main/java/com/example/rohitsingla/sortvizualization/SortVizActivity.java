package com.example.rohitsingla.sortvizualization;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class SortVizActivity extends Activity implements SortObserver{
    private static final String TAG = SortVizActivity.class.getName();
    EditText editTextN;
    Button buttonHeapSort, buttonBucketSort, buttonGenerateArray;
    public static TextView textViewArray;
    private int n = 0;
    ArrayList<Integer> arrayToSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_viz);
        editTextN = (EditText)findViewById(R.id.edit_text_number_of_elements);
        buttonHeapSort = (Button)findViewById(R.id.button_heap_sort);
        buttonBucketSort = (Button)findViewById(R.id.button_bucket_sort);
        buttonGenerateArray = (Button)findViewById(R.id.button_generate_array);
        textViewArray = (TextView)findViewById(R.id.text_view_array);

        buttonGenerateArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editTextN.getText().toString();
                if(number == null || number.isEmpty()){
                    Toast.makeText(SortVizActivity.this, "Please enter some number", Toast.LENGTH_LONG).show();
                }else{
                    n = Integer.parseInt(number);
                    arrayToSort = generateRandomArray(n);
                    StringBuilder arrayString = new StringBuilder();
                    for(int i=0;i<n;i++)
                        arrayString.append(arrayToSort.get(i) + " ");
                    Log.d(TAG, "Initial Array = "+arrayString);
                    textViewArray.setText(arrayString);
                }
            }
        });

        buttonHeapSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editTextN.getText().toString();
                if(number == null || number.isEmpty()){
                    Toast.makeText(SortVizActivity.this, "Please enter some number", Toast.LENGTH_LONG).show();
                }else {
                    Sorter mSorter = new HeapSorter(SortVizActivity.this, arrayToSort, n);
                    mSorter.sort();
                }
            }
        });

        buttonBucketSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editTextN.getText().toString();
                if(number == null || number.isEmpty()){
                    Toast.makeText(SortVizActivity.this, "Please enter some number", Toast.LENGTH_LONG).show();
                }else {
                    Sorter mSorter = new BucketSorter(SortVizActivity.this, arrayToSort, n);
                    mSorter.sort();
                }
            }
        });

    }

    /**
     * This function generates a random array of integers 1 to n
     * @param n number of elements in the array
     * @return shuffled array of integers from 1 to n
     */
    ArrayList<Integer> generateRandomArray(int n){
        ArrayList<Integer> arrayList = new ArrayList<Integer>(n);
        //inserting numbers from 1 to n into the array list
        for(int i=1;i<=n;i++)
            arrayList.add(i);
        Collections.shuffle(arrayList);
        return arrayList;
    }

    /**
     * This array updates the text view after each iteration of any sorting algorithm
     */
    @Override
    public void onArrayUpdate() {

    }
}
