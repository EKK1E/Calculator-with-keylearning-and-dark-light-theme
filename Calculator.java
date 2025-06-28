import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator {
    static boolean isDarkTheme = true;
    static Color darkBg = new Color(33, 33, 33);
    static Color lightBg = new Color(240, 240, 240);
    static JButton[] allButtons;
    static JButton themeButton;
    static JTextField displayField;
    static JPanel buttonPanel;
    static JFrame mainFrame;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        mainFrame = frame;
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextField display = new JTextField("0");
        displayField = display;
        display.setFont(new Font("Courier New", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        themeButton = new JButton("☀️");
        themeButton.setFocusable(false);
        themeButton.addActionListener(e -> {
            isDarkTheme = !isDarkTheme;
            applyTheme();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(display, BorderLayout.CENTER);
        topPanel.add(themeButton, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonPanel = panel;
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "←"
        };

        JButton[] btns = new JButton[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            String text = buttons[i];
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
            btn.setFocusPainted(false);
            panel.add(btn);
            btns[i] = btn;

            btn.addActionListener(e -> {
                String cmd = e.getActionCommand();
                handleInput(cmd, display);
            });
        }
        allButtons = btns;

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String cmd = String.valueOf(c);
                if ("0123456789.+-*/".contains(cmd)) {
                    handleInput(cmd, display);
                } else if (c == '\n') {
                    handleInput("=", display);
                } else if (c == '\b') {
                    handleInput("←", display);
                } else if (Character.toUpperCase(c) == 'C') {
                    handleInput("C", display);
                }
            }
        });

        frame.setFocusable(true);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        applyTheme();
    }

    public static void applyTheme() {
        Color bg = isDarkTheme ? darkBg : lightBg;
        Color fg = isDarkTheme ? Color.GREEN : Color.BLACK;
        Color btnBg = isDarkTheme ? new Color(55, 55, 55) : new Color(220, 220, 220);
        Color btnFg = isDarkTheme ? Color.WHITE : Color.BLACK;
        Color borderColor = isDarkTheme ? Color.GRAY : Color.LIGHT_GRAY;

        mainFrame.getContentPane().setBackground(bg);
        displayField.setBackground(isDarkTheme ? Color.BLACK : Color.WHITE);
        displayField.setForeground(fg);
        buttonPanel.setBackground(bg);

        for (JButton btn : allButtons) {
            btn.setBackground(btnBg);
            btn.setForeground(btnFg);
            btn.setBorder(BorderFactory.createLineBorder(borderColor));
        }

        themeButton.setText(isDarkTheme ? "☀️" : "🌙");
    }

    public static void handleInput(String cmd, JTextField display) {
        if (cmd.matches("[0-9\\.]")) {
            if (display.getText().equals("0")) display.setText(cmd);
            else display.setText(display.getText() + cmd);
        } else if (cmd.equals("C")) {
            display.setText("0");
        } else if (cmd.equals("←")) {
            String current = display.getText();
            if (current.length() > 1)
                display.setText(current.substring(0, current.length() - 1));
            else
                display.setText("0");
        } else if (cmd.equals("=")) {
            try {
                String result = String.valueOf(eval(display.getText()));
                display.setText(result);
            } catch (Exception ex) {
                display.setText("Blad");
            }
        } else {
            display.setText(display.getText() + " " + cmd + " ");
        }
    }

    public static double eval(String expr) {
        String[] tokens = expr.split(" ");
        double result = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String op = tokens[i];
            double next = Double.parseDouble(tokens[i + 1]);
            switch (op) {
                case "+": result += next; break;
                case "-": result -= next; break;
                case "*": result *= next; break;
                case "/": result /= next; break;
            }
        }
        return result;
    }
}
