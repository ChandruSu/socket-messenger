
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable
{
    private final Socket socket;

    private final BufferedReader input;
    private final PrintWriter output;

    private final String name;
    private boolean running;

    public Client(String name, String ip, int port) throws IOException
    {
        this.name = name;

        // creates client socket
        InetAddress address = InetAddress.getByName(ip);
        this.socket = new Socket(address, port);

        // creates input and output streams
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run()
    {
        running = true;
        output.printf("User %s has joined server!\n", socket.getLocalAddress());

        try {
            String message;

            do {
                message = input.readLine();
                System.out.println(message);
            } while (!socket.isClosed() && !message.equals("exit"));
        } catch (IOException e) {
            System.err.println("Failed to read message!");
        } finally {
            // closes client socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket!");
            }
            running = false;
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
