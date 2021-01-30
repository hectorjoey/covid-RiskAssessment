package hector.developers.covid19riskassessment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.Supervisor;


public class SupervisorAdapter extends RecyclerView.Adapter<SupervisorAdapter.ViewHolder> {
    List<Supervisor> supervisorList;
    Context context;

    public SupervisorAdapter(List<Supervisor> supervisorList, Context context) {
        this.supervisorList = supervisorList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Supervisor supervisor = supervisorList.get(position);
        holder.tvSupFullname.setText(supervisor.getFullname());
        holder.tvSupDesignation.setText(supervisor.getDesignation());
        holder.tvSupEmail.setText(supervisor.getEmail());
        holder.tvSupState.setText(supervisor.getState());

        holder.supCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
    }

    @Override
    public int getItemCount() {
        return supervisorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSupFullname;
        private TextView tvSupDesignation;
        private TextView tvSupEmail;
        private TextView tvSupState;
        CardView supCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSupFullname = itemView.findViewById(R.id.tvSupervisorFullname);
            tvSupDesignation = itemView.findViewById(R.id.tvSupervisorDesignation);
            tvSupEmail = itemView.findViewById(R.id.tvSupEmail);
            tvSupState = itemView.findViewById(R.id.tvSupState);
            supCardView = itemView.findViewById(R.id.supCardView);
        }
    }
}