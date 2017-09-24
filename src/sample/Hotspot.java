package sample;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Marvin on 9/20/2017.
 */
public class Hotspot {

    public static String startNetwork(){

        String input = "";
        String output;
        try{
            //Runtime.getRuntime().exec("Runtime.getRuntime().exec("runas /profile /user:Administrator \"cmd.exe /c Powrprof.dll,SetSuspendState\"");");
            Process p = Runtime.getRuntime().exec("netsh wlan start hostednetwork");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            Scanner in = new Scanner(stdInput);
            while (in.hasNextLine()){
                input += in.nextLine();
            }
            if(input.trim().equalsIgnoreCase("The hosted network started.")){
                output = "started";
            }else{
                output = "Error: \n "+input;
            }

        }catch(IOException ex){
            output = ex.getMessage().toString();
        }
        return output;
    }

    public static String stopNetwork(){
        String input = "";
        String output;
        try{
            Process p = Runtime.getRuntime().exec("netsh wlan stop hostednetwork");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNextLine()){
                input += in.nextLine();
            }

            if(input.trim().equalsIgnoreCase("The hosted network stopped.")){
                output = "stopped";
            }else{
                output = "Error: \n "+input;
            }

        }catch(IOException ex){
            output = ex.getMessage().toString();
        }
        return output;
    }

    public static String setSSID(String SSID){
        String input = "";
        String output;
        try{
            Process p = Runtime.getRuntime().exec("netsh wlan set hostednetwork ssid = "+SSID);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            Scanner in = new Scanner(stdInput);
            while (in.hasNextLine()){
                input += in.nextLine();
            }
            if(input.trim().equalsIgnoreCase("The SSID of the hosted network has been successfully changed.")){
                output = "success";
            }else{
                output = "Error: \n "+input;
            }

        }catch(IOException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = "null";
        }
        return output;

    }

    public static String getSSID(){

        String output = "";
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan show hostednetwork");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNext()) {
                String temp = in.next();
                if (temp.equalsIgnoreCase("SSID")) {
                    in.next();
                    in.next();
                    output = in.next();
                }
            }
        }catch(IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = null;
        }
        output = output.replace("\"","");
        return output;
    }

    public  static String setPassword(String passkey){
        String input = "";
        String output;
        try{
            Process p = Runtime.getRuntime().exec("netsh wlan set hostednetwork key = \""+passkey+"\"");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            Scanner in = new Scanner(stdInput);
            while (in.hasNextLine()){
                input += in.nextLine();
            }
            if(input.trim().equalsIgnoreCase("The user key passphrase of the hosted network has been successfully changed.")){

                output = "success";

            }else{
                output = "Error: \n "+input;
            }

        }catch(IOException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = "null";
        }
        return output;
    }

    public static String getPassword(){
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan show hostednetwork security");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNext()) {
                String temp = in.next();
                if (temp.equals("User")) {
                    in.next();
                    in.next();
                    in.next();
                    output = in.next();
                    break;
                }
            }
        }catch(IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = "null";
        }

        return output;    }

    public static String getSecurity(){
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan show hostednetwork");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNext()) {
                String temp = in.next();
                if (temp.equalsIgnoreCase("Authentication")) {
                    in.next();
                    output = in.next();
                }
            }
        }catch(IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = "null";
        }

        return output;
    }

    public static String getNoOfDevices(){
        String output = "0";
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan show hostednetwork");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNext()) {
                String temp = in.next();
                if (temp.equals("Number")) {
                    in.next();
                    in.next();
                    in.next();
                    output = in.next();
                }
            }
        }catch(IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage().toString());
            output = "null";
        }


        return output;
    }

    public static boolean isActive(){
        boolean isActive = false;
        String status = "";
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan show hostednetwork");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Scanner in = new Scanner(stdInput);

            while (in.hasNext()) {
                String temp = in.next();
                if (temp.equals("Status")) {
                    in.next();
                    status = in.next();
                }
            }
            isActive = status.equals("Started");
        }catch(IOException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage().toString());
            isActive = false;
        }


        return isActive;

    }


    }






