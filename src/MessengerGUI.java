import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class MessengerGUI 
{
    public ListView<String> messageList;
    public TextField nameInput;
    public TextField ipInput;
    public TextField portInput;
    public ChoiceBox<String> clientType;
    public TextField messageInput;
    public Button sendButton;
    public Button connectButton;

    /**
     * Sets up messenger app GUI.
     */
    public void initialize() 
    {
        clientType.getItems().addAll("Client", "Server");
        clientType.setValue("Client");

        IOManager.setDisplayProcess(m -> Platform.runLater(() -> messageList.getItems().add(m)));
        messageInput.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                sendButton.fire();
            }
        });
    }

    public void connect()
    {
        connectButton.setDisable(true);

        // retrieves ip address
        String ip = ipInput.getText();

        // retrieves port
        int port;
        try {
            port = Integer.parseInt(portInput.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port provided!");
            return;
        }

        try {
            if (clientType.getSelectionModel().getSelectedItem().equals("Server")) {
                Server server = new Server(ip, port);
                sendButton.setOnAction(e -> {
                    server.sendMessage(messageInput.getText());
                    messageInput.setText("");
                });
            } else {
                Client client = new Client(nameInput.getText(), ip, port);
                sendButton.setOnAction(e -> {
                    client.sendMessage(messageInput.getText());
                    messageInput.setText("");
                });
            }
            sendButton.setDisable(false);
        } catch (IOException e) {
            System.out.println("Failed to create messenger client!");
        }
    }
}
