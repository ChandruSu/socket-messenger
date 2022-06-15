import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application
{
    // program starts here
    public static void main(String[] args)
    {
        if (Arrays.asList(args).contains("--no-gui")) {
            runConsoleMessenger();
        } else {
            launch(args);
        }
    }

    private static void runConsoleMessenger() {
        IOManager.setMode(IOManager.IOMode.CONSOLE);

        Scanner input = new Scanner(System.in);

        System.out.print("Run as server [1] or client [2]? ");
        String runType = input.nextLine();

        System.out.print("IP: ");
        String ip = input.nextLine();

        System.out.print("Port: ");
        int port;

        try {
            port = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port provided!");
            return;
        }

        try {
            if (runType.equals("1")) {
                new Server(ip, port);
            } else if (runType.equals("2")) {
                System.out.print("Username: ");
                String username = input.nextLine();

                Client client = new Client(username, ip, port);
                new Thread(() -> client.writeLoop(input)).start();
            } else {
                System.out.printf("Invalid messenger setting '%s' specified!\n", runType);
            }
        } catch (IOException e) {
            System.out.println("Failed to create messenger client!");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        IOManager.setMode(IOManager.IOMode.GUI);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("messenger.fxml")));
        stage.setScene(new Scene(root));
        stage.setTitle("Socket Messenger");
        stage.setResizable(true);
        stage.show();
    }
}
