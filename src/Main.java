import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { //we use invoke later to make our GUI thread-safe.
            new GUI();
        });
    }
}
