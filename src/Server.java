import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private final ServerSocket socket;

    public Server(String ip, int port) throws IOException
    {
        // creates server socket
        InetAddress address = InetAddress.getByName(ip);
        this.socket = new ServerSocket(port, 100, address);

        ExecutorService pool = Executors.newFixedThreadPool(20);

        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    pool.execute(new ServerSideClient(socket.accept()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static class ServerSideClient implements Runnable
    {
        private static final List<ServerSideClient> clients = new ArrayList<>();

        private final Socket socket;

        private final BufferedReader input;
        private final PrintWriter output;

        public ServerSideClient(Socket socket) throws IOException
        {
            clients.add(this);
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String message;
                do {
                    message = input.readLine();
                    System.out.println(message);
                    for (ServerSideClient client : clients) {
                        client.output.println(message);
                    }
                } while (!socket.isClosed() && !message.equals("exit"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
