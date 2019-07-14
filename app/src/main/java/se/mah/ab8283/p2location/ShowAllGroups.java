package se.mah.ab8283.p2location;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */
public class ShowAllGroups extends android.support.v4.app.Fragment{

    Controller controller;
    private List<Group> content = new ArrayList<Group>();
    private RecyclerView rvExpenses;
    private ExpenseAdapter expenseAdapter;
    TextView list;

    public ShowAllGroups() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all_groups, container, false);


        initializeComponents(view);
        return view;
    }

    private void initializeComponents(final View view) {
        Button btnAddGroup = view.findViewById(R.id.btnAddGroup);
        Button btnBack = view.findViewById(R.id.btnBackFromGroups);
        list = view.findViewById(R.id.tvShowMembers);
        final EditText edAddGroup = view.findViewById(R.id.edAddGroup);
        controller.updateGroups();

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = edAddGroup.getText().toString();
                if(!(groupName.equals(""))) {
                    controller.joinNewGroup(groupName);
                    controller.updateGroups();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setText("");
                controller.goToMain();
                edAddGroup.setText("");
            }
        });



        rvExpenses = view.findViewById(R.id.rvGroups);
        rvExpenses.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseAdapter = new ExpenseAdapter(getActivity(), content);
        expenseAdapter.setController(controller);
        rvExpenses.setAdapter(expenseAdapter);
    }

    public void setContent(ArrayList<String> groups) {
        content.clear();
        System.out.println("showGroups " + groups.size());
        for(int i = 0; i<groups.size(); i++){
            Group group = new Group(groups.get(i), 0);
            if(!(group.getName().equals("groups:["))){
                content.add(group);
                System.out.println("showGroups adding! " +group.getName());
            }

        }
    }

    public TextView showList(){
        return list;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
