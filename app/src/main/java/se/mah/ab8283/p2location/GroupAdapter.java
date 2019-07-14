package se.mah.ab8283.p2location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.Holder> {
    private LayoutInflater inflater;
    private List<Group> content;
    Controller controller;

    public GroupAdapter(Context context) {
        this(context,new ArrayList<Group>());
    }

    public GroupAdapter(Context context, List<Group> content) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content = content;
    }


    public void setContent(List<Group> content) {
        this.content = content;
        super.notifyDataSetChanged();
    }

    public void setController(Controller controller) {
        this.controller=controller;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.maprow,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

      //  holder.imgCat.setImageIcon(content.get(position).getExpenseId());
        holder.tvGroupName.setText(content.get(position).getName());
     //   holder.btnJoin.setBottom(content.get(position).g);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvGroupName;
        private Button btnShowThis;

        public Holder(View itemView) {
            super(itemView);

            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            btnShowThis = itemView.findViewById(R.id.btnShowGrp);


            btnShowThis.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                  controller.showThisGroupOnMap(content.get(getPosition()).getName());
                }


            });

        }
    }



}
