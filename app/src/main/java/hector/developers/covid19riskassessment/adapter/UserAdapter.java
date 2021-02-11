package hector.developers.covid19riskassessment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.activities.DetailsActivity;
import hector.developers.covid19riskassessment.activities.HealthDetailsActivity;
import hector.developers.covid19riskassessment.model.Users;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<Users> userList;

    public UserAdapter(ArrayList<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = userList.get(position);
        holder.tvFirstName.setText(users.getFirstname());
        holder.tvEmail.setText(users.getEmail());
//        holder.tvDesignation.setText(users.getDesignation());
        holder.tvUserType.setText(users.getUserType());

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("key", users);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFirstName;
        private final TextView tvEmail;
//        private TextView tvDesignation;
        private final TextView tvUserType;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            tvFirstName = itemView.findViewById(R.id.tvFirstname);
            tvEmail = itemView.findViewById(R.id.tvEmail);
//            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvUserType = itemView.findViewById(R.id.tvUserType);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}