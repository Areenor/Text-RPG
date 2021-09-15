package Services;

import org.beryx.textio.StringInputReader;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class Terminal {
    private static Terminal INSTANCE;
    private TextIO _textIO;
    private StringInputReader _inputReader;
    private TextTerminal _terminal;

    private Terminal() {
        _textIO = TextIoFactory.getTextIO();
        _inputReader = _textIO.newStringInputReader();
        _terminal = _textIO.getTextTerminal();
    }

    private static Terminal GetInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Terminal();
        }

        return INSTANCE;
    }


    public static String Read() {
        Terminal instance = GetInstance();
        return instance._inputReader.read();
    }

    public static String Read(String message) {
        if (message != null) {
            Terminal instance = GetInstance();
            return instance._inputReader.read(message);
        }
        return  null;
    }

    public static void Print(String message) {
        if (message != null) {
            Terminal instance = GetInstance();
            instance._terminal.print(message);
        }
    }

    public static void PrintLine(String message) {
        if (message != null) {
            Terminal instance = GetInstance();
            instance._terminal.println(message);
        }
    }
    public static void CloseTerminal() {
        Terminal instance = GetInstance();
        instance._textIO.dispose();
    }
}
