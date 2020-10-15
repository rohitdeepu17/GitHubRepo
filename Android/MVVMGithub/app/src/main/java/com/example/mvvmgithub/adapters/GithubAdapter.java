package com.example.mvvmgithub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmgithub.R;
import com.example.mvvmgithub.models.GithubModel;
import com.example.mvvmgithub.utils.Utils;

import java.util.List;

public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.ViewHolder> {

    private List<GithubModel> mGithubModels;

    public GithubAdapter(List<GithubModel> mGithubModels) {
        this.mGithubModels = mGithubModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the layout
        View githubView = inflater.inflate(R.layout.item_github,parent,false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(githubView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        GithubModel gitItem = mGithubModels.get(position);

        // Set item views based on your views and data model
        TextView id,name,description,license,permission;
        id = holder.id;
        name = holder.name;
        description = holder.description;
        license = holder.license;
        permission = holder.permission;
        id.setText(""+gitItem.getmOpenIssuesCount());
        name.setText(gitItem.getmName());
        description.setText(gitItem.getmDescription());
        license.setText(gitItem.getmLicenseModel().getmName());
        permission.setText(Utils.booleanToString(gitItem.getmPermissionsModel().ismAdmin()));
    }

    @Override
    public int getItemCount() {
        return mGithubModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id,name,description,license,permission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.git_id);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            license = (TextView) itemView.findViewById(R.id.license);
            permission = (TextView) itemView.findViewById(R.id.permisssion);


        }
    }
}
