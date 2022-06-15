
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private final Socket socket;

    private final BufferedReader input;
    private final PrintWriter output;

    private final String name;
    private volatile boolean running;

    public Client(String name, String ip, int port) throws IOException
    {
        this.name = name;

        // creates client socket
        InetAddress address = InetAddress.getByName(ip);
        this.socket = new Socket(address, port);

        // creates input and output streams
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.running = true;
        new Thread(this::readLoop).start();
    }

    public void readLoop()
    {
        output.printf("User %s has joined server!\n", socket.getLocalAddress());

        try {
            String message;

            do {
                message = input.readLine();
                IOManager.displayMessage(message);
            } while (!socket.isClosed() && running);
        } catch (IOException e) {
            IOManager.displayMessage("Failed to read message!");
        } finally {
            // closes client socket
            try {
                socket.close();
            } catch (IOException e) {
                IOManager.displayMessage("Failed to close socket!");
            }
            running = false;
        }
    }

    public void writeLoop(Scanner userInput)
    {
        while (isRunning()) {
            String message = userInput.nextLine();

            if (message.equals("exit")) {
                running = false;
            } else {
                sendMessage(message);
            }
        }
    }

    public void sendMessage(String message)
    {
        output.printf("[%s] %s\n", name, message);
    }

    public boolean isRunning()
    {
        return running;
    }
}
