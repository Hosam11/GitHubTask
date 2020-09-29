package com.example.githubtask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubtask.data.model.GitHubRepoModel;

import java.util.List;

public class GitHubAdapter extends RecyclerView.Adapter<GitHubAdapter.GitHubViewHolder> {


    List<GitHubRepoModel> gitHubRepoList;
    Context context;

    GitHubRepoModel gitHubRepo;
    GitHubRepoModel.OwnerModel owner;

    public GitHubAdapter(Context context, List<GitHubRepoModel> gitHubRepoList) {
        this.context = context;
        this.gitHubRepoList = gitHubRepoList;
    }

    public void setGitHubList(List<GitHubRepoModel> gitHubRepoList) {
        this.gitHubRepoList.addAll(gitHubRepoList);
        notifyDataSetChanged();
        Log.i(HelperClass.TAG, "## GitHubAdapter ## -- setGitHubList() -- this.listSize() >> "
                + this.gitHubRepoList.size());
    }

    @NonNull
    @Override
    public GitHubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row, parent, false);
        return new GitHubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GitHubViewHolder holder, int position) {
        gitHubRepo = gitHubRepoList.get(position);
        owner = gitHubRepo.getOwnerModel();
        holder.repoName.setText(gitHubRepo.getRepoName());
        holder.repoDesc.setText(gitHubRepo.getRepoDesc());
        holder.repoOwnerName.setText(owner.getLogin());
        // default is light green
        if (gitHubRepo.isFork()) {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        holder.linearLayout.setOnLongClickListener(view -> {
            showAlert();
            return true;
        });
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Where to Go? ");
        builder.setMessage("choose which url you want to open..");

        builder.setNegativeButton("Owner", (dialogInterface, i) -> {
            Log.i(HelperClass.TAG, "## GitHubAdapter ## ShowAlert() -- " +
                    "owner url >> " + owner.getOwnerURL());
            goToUrl(owner.getOwnerURL());

        });
        builder.setPositiveButton("Repo", (dialogInterface, i) -> {
            Log.i(HelperClass.TAG, "ShowAlert: " +
                    " -- repo url >> " + gitHubRepo.getRepoURL());
            goToUrl(gitHubRepo.getRepoURL());

        });
        builder.show();
    }

    /**
     * open a browser with url based on user selection
     *
     * @param url
     */
    private void goToUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return gitHubRepoList.size();
    }

    class GitHubViewHolder extends RecyclerView.ViewHolder {

        TextView repoName;
        TextView repoDesc;
        TextView repoOwnerName;
        LinearLayout linearLayout;

        public GitHubViewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.tv_repo_name);
            repoDesc = itemView.findViewById(R.id.tv_repo_desc);
            repoOwnerName = itemView.findViewById(R.id.tv_repo_owner_name);
            linearLayout = itemView.findViewById(R.id.linear_layout_row);
        }
    }
}
