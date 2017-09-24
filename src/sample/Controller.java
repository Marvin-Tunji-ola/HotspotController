package sample;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.Hotspot;

import javax.swing.*;


public class Controller implements Initializable {
    boolean isActive = false;


    @FXML
    public ToggleButton hotspotSwitch;

    @FXML
    public Label ssid, type, security, status, connected, errorLabel, successLabel;

    @FXML
    public TextArea console;

    @FXML public Hyperlink developer;

    @FXML
    public TextField editSSID;

    @FXML
    public PasswordField editPasskey;

    @FXML
    public CheckBox showPasskey;

    @FXML
    public VBox settingsForm;
    @FXML
    public Button saveSettings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hotspotSwitch.setDisable(true);
        setVariables();
        new Thread(()->{
            String connected = Hotspot.getNoOfDevices();
            while (true){
                if(!connected.equalsIgnoreCase(Hotspot.getNoOfDevices())){

                    Platform.runLater(()->{
                        this.connected.setText(Hotspot.getNoOfDevices());
                    });
                }
            }
        }).start();

        developer.setOnAction((e)->{
            try {
                Desktop.getDesktop().browse(new URI("https://about.me/marvin-tunji-ola"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });
        hotspotSwitch.setOnAction((e)->{
            if(!isActive) {
                hotspotSwitch.setText("Starting Hotspot.....");
                startNetwork();
            }else if(isActive){
                hotspotSwitch.setText("Stopping Hotspot.....");
                stopNetwork();
            }
        });
        showPasskey.setOnMouseClicked((e)->{
            if(showPasskey.isSelected()){
                //show Password
                String passkey = editPasskey.getText().toString();
                settingsForm.getChildren().remove(4);
                TextField editPasskey = new TextField();
                editPasskey.setText(passkey);
                settingsForm.getChildren().add(4,editPasskey);


            }else{
                TextField editPasskey = (TextField) settingsForm.getChildren().get(4);
                String passkey = editPasskey.getText().toString();
                settingsForm.getChildren().remove(4);
                this.editPasskey.setText(passkey);
                settingsForm.getChildren().add(4,this.editPasskey);


            }
            //JOptionPane.showMessageDialog(null, showPasskey.isSelected()+"Show Password Checked");
        });

        saveSettings.setOnMouseClicked((e)->{
            String SSID = editSSID.getText().trim();
            String passkey = editPasskey.getText().trim();
            if(passkey.length() > 8) {
                errorLabel.setText("");
                //String passResult, ssidResult;

                new Thread(()->{
                    String passResult = Hotspot.setPassword(passkey);
                    String ssidResult =Hotspot.setSSID(SSID);
                    Platform.runLater(()->{
                        if(passResult.equalsIgnoreCase("success") || ssidResult.equalsIgnoreCase("success")){
                            setVariables();
                            successLabel.setText("Successfully Changed");
                            new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                    Platform.runLater(()->{
                                        successLabel.setText("");
                                    });

                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }

                            }).start();
                        }
                    });
                    Hotspot.startNetwork();
                }).start();


            }else{
                errorLabel.setText("Passkey must be atleast 8 Characters");
            }

        });


    }
    
    public void startNetwork(){
        new Thread(()->{
            String result = Hotspot.startNetwork();
            if(result.equalsIgnoreCase("started")){
                Platform.runLater(() -> {
                    //Update UI here
                    isActive = true;
                    setVariables();
                    status.setText("Active");
                    hotspotSwitch.setText("Turn Off Hotspot");
                    hotspotSwitch.setStyle("-fx-background-color:#d31e1e;");
                    console.setText("Hosted Network Started");
                });
            }else{
                console.setText(result);
            }
        }).start();




    }

    public void stopNetwork(){
        new Thread(()->{
        String result = Hotspot.stopNetwork();
        if(result.equalsIgnoreCase("stopped")){
            Platform.runLater(() -> {
            isActive = false;
            setVariables();
            status.setText("InActive");
            hotspotSwitch.setText("Turn On Hotspot");
            hotspotSwitch.setStyle("-fx-background-color:#0fc944;");
            console.setText("Hosted Network Stopped");
            });
        }else{
            console.setText(result);
        }
        }).start();
    }

    public void setVariables(){

        new Thread( ()->
        {
            String ssidString = Hotspot.getSSID(), noOfDev = Hotspot.getNoOfDevices(),
            securityType = Hotspot.getSecurity(), password = Hotspot.getPassword();
            isActive = Hotspot.isActive();
            Platform.runLater(()->{
                ssid.setText(ssidString);
                security.setText(securityType);
                connected.setText(noOfDev);
                editSSID.setText(ssidString);
                editPasskey.setText(password);
                hotspotSwitch.setDisable(false);
                if(isActive){
                    status.setText("Active");
                    hotspotSwitch.setText("Turn Off Hotspot");
                    hotspotSwitch.setStyle("-fx-background-color:#d31e1e;");
                    console.setText("Hosted Network Started");
                }
            });
        }).start();

    }

}