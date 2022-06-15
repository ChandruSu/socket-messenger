public class IOManager
{
    public enum IOMode
    {
        CONSOLE, GUI
    }

    public interface StringProcessor {
        void process(String string);
    }

    private static IOMode ioMode;
    private static StringProcessor displayProcess;

    public static void setMode(IOMode mode)
    {
        ioMode = mode;
    }

    public static void displayMessage(String message)
    {
        if (ioMode == IOMode.CONSOLE) {
            System.out.println(message);
        } else {
            displayProcess.process(message);
        }
    }

    public static void setDisplayProcess(StringProcessor displayProcess)
    {
        IOManager.displayProcess = displayProcess;
    }
}
