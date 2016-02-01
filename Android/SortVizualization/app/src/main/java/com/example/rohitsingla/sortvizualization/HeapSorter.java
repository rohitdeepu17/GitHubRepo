package com.example.rohitsingla.sortvizualization;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rohitsingla on 30/01/16.
 */
public class HeapSorter implements Sorter {
    private static final String TAG = HeapSorter.class.getName();
    static Context mContext;

    int n;
    ArrayList<Integer> mArrayList;
    Handler mHandler;

    int j;

    HeapSorter(Context context, ArrayList<Integer> arrayList, int num){
        mContext = context;
        mArrayList = arrayList;
        n = num;
        mHandler = new Handler();
        j = n-1;
    }

    //This N to change during heap sort.
    private static int N;

    /* Function to build a heap */
    public void heapify()
    {
        N = n-1;
        for (int i = N/2; i >= 0; i--)
            maxheap(i);
    }
    /* Function to swap largest element in heap */
    public void maxheap(int i)
    {
        int left = 2*i ;
        int right = 2*i + 1;
        int max = i;
        if (left <= N && mArrayList.get(left) > mArrayList.get(i))
            max = left;
        if (right <= N && mArrayList.get(right) > mArrayList.get(max))
            max = right;

        if (max != i)
        {
            swap(i, max);
            maxheap(max);
        }
    }
    /* Function to swap two numbers in an array */
    public void swap(int i, int j)
    {
        int temp = mArrayList.get(i);
        mArrayList.set(i, mArrayList.get(j));
        mArrayList.set(j, temp);
    }


    @Override
    public void sort() {
        Log.d(TAG,"Inside sort function");
        heapify();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if(j>0){
                    swap(0, j);
                    N = N-1;
                    maxheap(0);

                    final StringBuilder arrayOfIntegers = new StringBuilder();
                    Log.d(TAG, "The value of n = "+n);
                    for(int i=0;i<n;i++)
                        arrayOfIntegers.append(mArrayList.get(i)+" ");

                    Log.d(TAG, "Array of Integers = "+arrayOfIntegers);
                    SortVizActivity.textViewArray.setText("Array after iteration"+(n-j)+" = "+arrayOfIntegers.toString());
                    arrayOfIntegers.delete(0, n-1);

                    mHandler.postDelayed(this, 3000);
                }else{
                    final StringBuilder arrayOfIntegers = new StringBuilder();
                    Log.d(TAG, "The value of n = "+n);
                    for(int i=0;i<n;i++)
                        arrayOfIntegers.append(mArrayList.get(i)+" ");

                    Log.d(TAG, "Final sorted array = "+arrayOfIntegers);
                    SortVizActivity.textViewArray.setText("Final sorted array = "+arrayOfIntegers.toString());
                    arrayOfIntegers.delete(0, n-1);
                }
                Log.d(TAG, "The value of j = "+j);
                j--;
            }
        };
        mHandler.post(mRunnable);
    }
}
