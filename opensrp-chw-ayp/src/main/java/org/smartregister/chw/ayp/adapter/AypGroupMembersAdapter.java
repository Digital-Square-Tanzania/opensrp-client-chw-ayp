package org.smartregister.chw.ayp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;

import java.util.ArrayList;
import java.util.List;

public class AypGroupMembersAdapter extends RecyclerView.Adapter<AypGroupMembersAdapter.VH> {

    public interface Listener {
        void onMemberClicked(MemberObject member);
    }

    private final Listener listener;
    private final List<MemberObject> items = new ArrayList<>();

    public AypGroupMembersAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<MemberObject> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ayp_group_member, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MemberObject m = items.get(position);
        holder.name.setText(String.format("%s %s", m.getFullName(), ""));
        holder.sub.setText(m.getAddress());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMemberClicked(m);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView sub;
        ImageView chevron;

        public VH(@NotNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            name = itemView.findViewById(R.id.tv_name);
            sub = itemView.findViewById(R.id.tv_sub);
            chevron = itemView.findViewById(R.id.iv_chevron);
        }
    }
}

