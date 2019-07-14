package se.mah.ab8283.p2location;


import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class Controller {

    MainActivity mainActivity;
    ConnectToServer connectToServer;

    FragmentShowMaps fragmentShowMaps;
    String mess = "";
    private ArrayList<String> groupsImIn = new ArrayList<String>();
    ShowAllGroups showAllGroups;
    OverViewFragment overviewFragment;


    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
       iniSOverViewFragment();
        iniShowGroups();
        mainActivity.setFragment(overviewFragment,"OverviewFragment" );
     //   mainActivity.setFragment(showAllGroups,"showAllGroups" );
        inishowMaps();
        pressConnect();
    }


    private void inishowMaps() {
        fragmentShowMaps = (FragmentShowMaps)mainActivity.getFragment("fragmentShowMaps");
        if(fragmentShowMaps ==null) {
            fragmentShowMaps = new FragmentShowMaps();
        }
        fragmentShowMaps.setController(this);
        fragmentShowMaps.setMain(mainActivity);
    }


    private void iniShowGroups() {
        showAllGroups = (ShowAllGroups)mainActivity.getFragment("showAllGroups");
        if(showAllGroups ==null) {
            showAllGroups = new ShowAllGroups();
        }
        showAllGroups.setController(this);
    }

    public void iniSOverViewFragment(){
        overviewFragment = (OverViewFragment)mainActivity.getFragment("OverviewFragment");
        if(overviewFragment==null) {
            overviewFragment = new OverViewFragment();
        }
        overviewFragment.setController(this);
    }


    public void goToGroups(){
        connectToServer.changeJson(1);
        updateGroups();
     //   mainActivity.setFragment(showAllGroups,"showAllGroups" );
    }

    public void goToMain(){
        mainActivity.setFragment(overviewFragment, "OverviewFragment");
    }


    public void message(String mess){
        overviewFragment.newMessage(mess);
    }

    public void pressConnect(){
        connectToServer = new ConnectToServer(this);
    }


    public void pressOne(){
        connectToServer.changeJson(1);
    }

    public void goToMaps(){
         fragmentShowMaps.setContent(groupsImIn);
         mainActivity.setFragment(fragmentShowMaps,"fragmentShowMaps" );
         System.out.println("å  see whats there " + groupsImIn);
    }

    public void groupList(ArrayList<String> groups){
        System.out.println(" groups -- " + groups);
        showAllGroups.setContent(groups);
        mainActivity.setFragment(showAllGroups,"showAllGroups" );

    }

    public void updateGroups(){
        connectToServer.changeJson(1);
    }

    public void joinNewGroup(String groupName) {
        connectToServer.joinGroup("Amin", groupName);
        groupsImIn.add(groupName);
        System.out.println("  Add to " + groupName);
    }

    public void leaveGroup(String groupName) {
        connectToServer.leaveGroup("Amin", groupName);
        int index = groupsImIn.indexOf(groupName);
        groupsImIn.remove(index);
        fragmentShowMaps.setContent(groupsImIn);
        System.out.println("  Leaving " + groupName);
    }

    public void showMembers(String groupName) {
        connectToServer.showGroupMembers(groupName);

    }

    public void sendListOfMembersToShow(String mess) {

        if(mess.equals("members:[")){
            mess = "No members";
        }
        else{
            mess = "Members\n" + mess;
        }
        final String message = mess;

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView test = showAllGroups.showList();
                test.setText(message);
            }
        });
    }

    public void showThisGroupOnMap(String groupName){
        ArrayList<String> theOnesIneedNow = new ArrayList<String>();
        ArrayList<String> membersLocation = connectToServer.getLocations();
        for(int i = 0; i<membersLocation.size(); i++){
            if(membersLocation.get(i).contains(groupName)){
                theOnesIneedNow.add(membersLocation.get(i));
            }
        }

        fragmentShowMaps.showMembersLocation(theOnesIneedNow);
    }


    public String getLocation(){
        return fragmentShowMaps.getLocation();
    }
}
