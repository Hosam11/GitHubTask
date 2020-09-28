package com.example.githubtask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubtask.data.model.GitHubRepoModel;

import java.util.List;

public class GitHubAdapter extends RecyclerView.Adapter<GitHubAdapter.GitHubViewHolder> {



    List<GitHubRepoModel> gitHubRepoList ;
    Context context;

    public void setGitHubList(List<GitHubRepoModel> gitHubRepoList) {
        this.gitHubRepoList.addAll(gitHubRepoList);
//        this.gitHubRepoList = gitHubRepoList;
        Log.i(HelperClass.TAG, "GitHubAdapter >> setGitHubList: listSize() >> "
                + gitHubRepoList.size());
    }

    public GitHubAdapter(Context context, List<GitHubRepoModel> gitHubRepoList) {
        this.context = context;
        this.gitHubRepoList = gitHubRepoList;
    }

    @NonNull
    @Override
    public GitHubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row, parent, false);
        return new GitHubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GitHubViewHolder holder, int position) {

        GitHubRepoModel gitHubRepo = gitHubRepoList.get(position);
        holder.repoName.setText(gitHubRepo.getRepoName());
        holder.repoDesc.setText(gitHubRepo.getRepoDesc());
        holder.repoOwnerName.setText(gitHubRepo.getRepoOwnerName());

    }

    @Override
    public int getItemCount() {
        return gitHubRepoList.size();
    }

    class GitHubViewHolder extends RecyclerView.ViewHolder {

        TextView repoName;
        TextView repoDesc;
        TextView repoOwnerName;


        public GitHubViewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.tv_repo_name);
            repoDesc = itemView.findViewById(R.id.tv_repo_desc);
            repoOwnerName = itemView.findViewById(R.id.tv_repo_owner_name);



        }
    }
}
