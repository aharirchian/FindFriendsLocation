package se.mah.ab8283.p2location;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */
public class ConnectToServer {

    ArrayList<String> listMembers = new ArrayList<String>();
    ArrayList<String> listId = new ArrayList<String>();
    ArrayList<String> groups = new ArrayList<String>();
    ArrayList<String> membersLocation = new ArrayList<String>();
    Controller controller;

    //private String inetAddress = "172.18.36.81";
    //private int port = 7117;
private String inetAddress = "195.178.227.53";
    private int port = 8443;

    InputStream is;
    DataInputStream dis;
    JSONObject json;
    boolean first = true;
    int test = 1;
    boolean showgroups = false;
    boolean showMembers = false;
    boolean register = false;

    private HashMap memberInfo = new HashMap();
    String name = "";
    boolean realperson = false;

    InetSocketAddress inetSocketAddress;
    InetAddress addr;
    Socket socket;
   // InputStream is;
    OutputStream os ;
    DataOutputStream dos ;
    //DataInputStream dis;


    public ConnectToServer(Controller controller) {
        this.controller = controller;
        new Thread(new Runnable() {
            public void run() {
        try {
            inetSocketAddress = new InetSocketAddress(inetAddress, port);
            addr = inetSocketAddress.getAddress();
            socket = new Socket(addr, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            dis = new DataInputStream(is);
        } catch (IOException e) { e.printStackTrace();}
        }}).start();

    }

    public void leaveGroup(final String name, final String groupName){
        String id = "";
        if(memberInfo.containsKey(name)){
            id = memberInfo.get(name).toString();
        }
        final String searchId = id;
        new Thread(new Runnable() {
            public void run() {
                try {
                    json = new JSONObject();
                    json.put("type", "unregister");
              //      json.put("group", groupName);
                    json.put("id", searchId);
                    connect();
                }
                catch (JSONException e) { e.printStackTrace(); }
            }
        }).start();
    }


    public void joinGroup(final String name, final String groupName){
        this.name = name;
        new Thread(new Runnable() {
            public void run() {
                try {
                    register = true;
                    json = new JSONObject();
                    json.put("type", "register");
                    json.put("group", groupName);
                    json.put("member", name);
                    connect();
                   // giveLocation(name);
                }
                catch (JSONException e) { e.printStackTrace(); }
            }
        }).start();
        realperson = true;
    }

    public void giveLocation(final String name){
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println(" finding id for name " + name);
                    String id = "";
                    if(memberInfo.containsKey(name)){
                        id = memberInfo.get(name).toString();
                    }
                    final String searchId = id;

                    String[]location = controller.getLocation().split(",");

                    while(true) {
                        json = new JSONObject();
                        json.put("type", "location");
                        json.put("id", searchId);
                        json.put("longitude", ""+location[0]);
                        json.put("latitude", ""+location[1]);
                        connect();
                        Thread.sleep(6000);
                    }
                }
                catch (JSONException e) { e.printStackTrace(); } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showGroupMembers(final String groupName) {
        try {
            json = new JSONObject();
            json.put("type", "members");
            json.put("group", groupName);
            showMembers = true;
            connect();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeJson(int i) {
        try {
            if (i == 1) {
                json = new JSONObject();
                showgroups = true;
                json.put("type", "groups");
                connect();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (first) {
                        first = false;
                        listenOnServer();
                    }

                    String message = json.toString();
                    System.out.println(" sending  - " + message);
                    dos.writeUTF(message);
                    dos.flush();
                    // dos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void listenOnServer() {
        new Thread(new Runnable() {
            boolean running = false;

            public void run() {
                try {
                    running = true;
                    while (running) {
                        if (dis.available() > 0) {
                            String message = dis.readUTF();
                            System.out.println(" message from server - " + message);
                            if (!(message.contains("exception"))) {

                                if (message.contains("locations")) {
                                    message = locations(message);
                                }

                                if (showgroups == true) {
                                    showgroups = false;
                                    message = getGroups(message);
                                    controller.message(message);
                                    controller.groupList(groups);
                                }

                                if (showMembers == true) {
                                    showMembers = false;
                                    message = showMembers(message);
                                    controller.message(message);
                                    controller.sendListOfMembersToShow(message);
                                }

                                if (register == true) {
                                    register = false;
                                    message = addingPerson(message);
                                    controller.message(message);
                                    if (realperson)
                                        giveLocation(name);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String locations(String message) {

        //{"type":"locations","group":"P2","location":[{"member":"AA","longitude":"12.994727707590863","latitude":"55.594700781978915"},{"member":"Amin","longitude":"NaN","latitude":"NaN"}]}

        String[] test = message.split(",");
        String group = test[1].replace("\"group\":\"", "");
        group.replace("\"", "");
        String member = test[2].replace("\"location\":[{\"member\":\"", "");
        member.replace("\"", "");

        System.out.println("Test - " + member + " next - " + group);

        try {
            JSONObject jsonObj = new JSONObject(message.toString());

            JSONArray grupper = jsonObj.getJSONArray("location");

            System.out.println("Groups " + grupper.length());
            for(int i = 0; i< grupper.length(); i++) {
                System.out.println("in group  " + grupper.get(i).toString());
                JSONObject obj = new JSONObject( grupper.get(i).toString());
                String memberName = obj.get("member").toString();
                String longitude = obj.get("longitude").toString();
                String latitude = obj.get("latitude").toString();

                System.out.println(" person  " + memberName + "   Longitude  " + longitude + " Latitude  " + latitude);

                membersLocation.add(group+","+memberName+","+longitude+","+latitude);


                System.out.println(" Members Location  " + membersLocation);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        };
        return "test";
    }


    public String addingPerson(String message) {

        String[] test = message.split(",");
        String res = "";
        if (test.length > 2) {
            System.out.println("test - " + test[2] + test[3] + " next -" + test[4]);
            String group = test[2].replace("\"id\":\"" , "");

            String person = test[3];
            String id = test[4].replace("\"}", "");

            System.out.println(" group - " + group);
            System.out.println(" person - " + person);
            System.out.println(" id - " + id);
            res = "adding person " + person + "\n with id: " + id;

            id = group+","+person+","+id;
            System.out.println("ö id - " + id);
            memberInfo.put(person, id);

            listMembers.add(person);
            listId.add(id);
        }
        return res;
    }


    public String getGroups(String message) {
        String[] test = message.split(",");
        System.out.println(" test length - " + test.length);
        if (test.length > 1) {
            for (int i = 1; i < test.length; i++) {
                if (i == 1) {
                    String temp = test[i].replace("\"groups\":[{\"group\":", "");
                    temp = temp.replace("\"", "");
                    temp = temp.replace("}", "");
                    temp = temp.replace("]", "");
                    test[i] = temp;
                } else {
                    String temp = test[i].replace("{\"group\":\"", "");
                    temp = temp.replace("\"}]}", "");
                    temp = temp.replace("\"}", "");
                    test[i] = temp;

                }
                System.out.println(" test[" + i + "] " + test[i]);
            }
        }
        String res = "";
        groups.clear();
        for (int i = 1; i < test.length; i++) {
            groups.add(test[i]);
            res += test[i] + "\n";
        }
        return res;

    }

    public String showMembers(String message) {
        String test[] = message.split(",");
        message.replace("{", "");
        if (test.length > 1) {
            for (int i = 2; i < test.length; i++) {
                if (i == 2) {
                    String temp = test[i].replace("\"group\":\"", "");
                    temp = temp.replace("\"", "");
                    temp = temp.replace("members:[{", "");
                    temp = temp.replace("}", "");
                    temp = temp.replace("member:", "");
                    temp = temp.replace("]", "");
                    temp = temp.replace("\"}", "");
                    test[i] = temp;
                }
                if (i == 3) {
                    String temp = test[i].replace("{\"member\":\"", "");
                    temp = temp.replace("\"}", "");
                    temp = temp.replace("]}", "");
                    test[i] = temp;
                } else {
                    String temp = test[i].replace("{\"member\":\"", "");
                    temp = temp.replace("\"}]}", "");
                    temp = temp.replace("\"}", "");
                    test[i] = temp;
                }
                System.out.println(" test[" + i + "] " + test[i]);
            }
        }

        String res = "";
        for (int i = 2; i < test.length; i++) {
            res += test[i] + "\n";
        }
        return res;
    }


    public ArrayList<String> getLocations(){
        return membersLocation;
    }

}
