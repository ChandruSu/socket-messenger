import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main
{
    // program starts here
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Client or server? ");
        String type = in.nextLine().toLowerCase(Locale.ROOT).strip();

        try {
            if (type.equals("server")) {
                new Server("0.0.0.0", 3000);
            } else {
                System.out.print("Name: ");
                Client client = new Client(in.nextLine(), "0.0.0.0", 3000);
                new Thread(client).start();

                while (true) {
                    System.out.println(">");
                    client.sendMessage(in.nextLine());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to create: " + type + " connection!");
        }
    }
}
