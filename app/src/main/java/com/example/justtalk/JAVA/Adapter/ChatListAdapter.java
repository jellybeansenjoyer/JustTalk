package com.example.justtalk.JAVA.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.justtalk.JAVA.models.User;
import com.example.justtalk.R;
import com.example.justtalk.databinding.ModelChatBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<String> oldList = Collections.emptyList();
    @NonNull
    @NotNull
    @Override
    public ChatListAdapter.ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return ChatViewHolder.getInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatListAdapter.ChatViewHolder holder, int position) {
        holder.bind(oldList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldList.size();
    }

    static public class ChatViewHolder extends RecyclerView.ViewHolder{
        public ModelChatBinding mBinding;
        public ViewGroup mParent;
        ChatViewHolder(View view,ViewGroup parent){
            super(view);
            mParent = parent;
            mBinding = DataBindingUtil.bind(view);
        }
        public void bind(String id){
            mBinding.nameUser.setText(id);
//            User user;
            User user = null;
            Glide.with(mParent.getContext()).load(user).into(mBinding.dpUser);
            mBinding.nameUser.setText(user.getName());

        }
        public static ChatViewHolder getInstance(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat,parent,false);
            return new ChatViewHolder(view,parent);
        }
    }

    public void submitList(List<String> newList){
        DiffUtil.Callback cb = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        };
        DiffUtil.DiffResult calc = DiffUtil.calculateDiff(cb);
        oldList = newList;
        calc.dispatchUpdatesTo(this);
    }

}
