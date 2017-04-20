#include<iostream>
using namespace std;

void merge(int *arr, int l, int mid, int h){
	int leftSize = mid-l+1;
	int rightSize = h-mid;
	int *leftArray = new int[leftSize];
	int *rightArray = new int[rightSize];
	//copying data into left and right arrays
	for(int i=l;i<=mid;i++)
		leftArray[i-l] = arr[i];
	for(int i=mid+1;i<=h;i++)
		rightArray[i-(mid+1)] = arr[i];
	//merging arrays
	int k=l;
	int i=0,j=0;
	while(i<leftSize && j<rightSize){
		if(leftArray[i]<rightArray[j]){
			arr[k] = leftArray[i];
			i++;
		}else{
			arr[k] = rightArray[j];
			j++;
		}
		k++;
	}
	//copy remaining elements from left array into original array
	while(i<leftSize){
		arr[k] = leftArray[i];
		i++;
		k++;
	}
	//copy remaining elements from right array into original array
	while(j<rightSize){
		arr[k] = rightArray[j];
		j++;
		k++;
	}
}

void mergeSort(int *arr, int l, int h){
	if(l<h){
		int mid = (l+h)/2;
		mergeSort(arr,l,mid);
		mergeSort(arr,mid+1,h);
		merge(arr,l,mid,h);
	}
}

void mergeSortIterative(int *arr, int n){
	int l,h,mid;
	for(int curSize=1;curSize<=n-1;curSize++){
		for(l=0;l<n-1;l=l+2*curSize){
			mid = l+curSize-1;
			h = min(l+2*curSize-1,n-1);
			cout<<l<<" "<<mid<<" "<<h<<endl;
			merge(arr,l,mid,h);
		}
	}
}

int main(){
	int arr[] = {1,7,3,21,4,64,35};
	int n = sizeof(arr)/sizeof(int);
	for(int i=0;i<n;i++)
		cout<<arr[i]<<" ";
	//mergeSort(arr,0,n-1);
	mergeSortIterative(arr,n);
	cout<<"\nArray after sorting:-";
	for(int i=0;i<n;i++)
		cout<<arr[i]<<" ";
	cout<<endl;
}