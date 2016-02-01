package com.example.rohitsingla.sortvizualization;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rohitsingla on 30/01/16.
 */
public class BucketSorter implements Sorter {
    private static final String TAG = BucketSorter.class.getName();
    Context mContext;

    int n;
    ArrayList<Integer> mArrayList;

    //keeping fixed number of buckets;
    int numOfBuckets = 5;
    ArrayList<Integer>[] buckets;

    //variable for iteration on number of buckets while sorting
    int i=0;

    Handler mHandler = new Handler();


    BucketSorter(Context context, ArrayList<Integer> arrayList, int num){
        mContext = context;
        mArrayList = arrayList;
        n = num;


        buckets = new ArrayList[numOfBuckets];
        for(int i=0;i<numOfBuckets;i++)
            buckets[i] = new ArrayList<Integer>();

        //ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>(numOfBuckets);
        for(int i=0;i<n;i++){
            ((ArrayList<Integer>) buckets[mArrayList.get(i) % 5]).add(mArrayList.get(i));
        }
    }
    @Override
    public void sort() {
        Log.d(TAG, "Inside sort function");
        final Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if(i<numOfBuckets){
                    StringBuilder elementsInBuckets = new StringBuilder();
                    //sort this bucket
                    Collections.sort(buckets[i]);

                    //append all elements of all buckets to elementsInBuckets
                    for(int j=0;j<numOfBuckets;j++){
                        elementsInBuckets.append("Bucket No. "+j+" contains ");
                        for(int k=0;k<buckets[j].size();k++){
                            elementsInBuckets.append(buckets[j].get(k) + " ");
                        }
                        elementsInBuckets.append("\n");
                    }

                    Log.d(TAG, "The elements in bucket after sorting Bucket "+(i+1)+" are : \n"+elementsInBuckets.toString());
                    SortVizActivity.textViewArray.setText("The elements in bucket after sorting Bucket "+(i+1)+" are : \n"+elementsInBuckets.toString());
                    mHandler.postDelayed(this, 2000);
                }else{
                    //merging buckets after sorting individual buckets
                    int[] bucketIterators = new int[numOfBuckets];
                    for(int i=0;i<numOfBuckets;i++)
                        bucketIterators[i] = 0;
                    StringBuilder finalSortedArray = new StringBuilder();
                    for(int i=0;i<n;i++){
                        int bucketIndex = getMinBucketIndex(buckets, numOfBuckets, bucketIterators);
                        mArrayList.set(i, buckets[bucketIndex].get(bucketIterators[bucketIndex]));
                        finalSortedArray.append(mArrayList.get(i)+" ");
                        bucketIterators[bucketIndex]++;
                    }
                    Log.d(TAG, "Final sorted array = "+finalSortedArray);
                    SortVizActivity.textViewArray.setText("Final sorted array = "+finalSortedArray);
                }
                i++;

            }
        };
        mHandler.post(mRunnable);
    }

    int getMinBucketIndex(ArrayList<Integer>[] buckets, int numberOfBuckets, int[] bucketIterators){
        int minIndex=-1;
        int minVal = 100000;        //very large value
        for(int i=0;i<numberOfBuckets;i++){
            if(bucketIterators[i]<buckets[i].size() && minVal>buckets[i].get(bucketIterators[i]))
            {
                minVal = buckets[i].get(bucketIterators[i]);
                minIndex = i;
            }
        }
        return minIndex;
    }
}
