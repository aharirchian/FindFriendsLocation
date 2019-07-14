package se.mah.ab8283.p2location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class OverViewFragment extends android.support.v4.app.Fragment{

    Controller controller;
    TextView edMessage;


    public OverViewFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_over_view, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        edMessage = view.findViewById(R.id.textView);
        Button btn1 = view.findViewById(R.id.btn1);
        Button btn2 = view.findViewById(R.id.btn2);
        Button btn3 = view.findViewById(R.id.btn3);
        Button btn4 = view.findViewById(R.id.btnGoToGroups);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.pressOne();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.goToMaps();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.goToGroups();
            }
        });
    }


    public void newMessage(String mess){
        edMessage.setText(mess);
    }


    public void setController(Controller controller){
        this.controller = controller;
    }
}
