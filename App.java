import javax.swing.*;
import java.awt.*;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Pac-Man Game");

            frame.setSize(400, 320);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();

            panel.setLayout(new GridLayout(5, 2, 10, 10));
            panel.setBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            );

            JLabel title = new JLabel("PAC-MAN GAME");
            JLabel nameLabel = new JLabel("Enter Name:");
            JLabel colorLabel = new JLabel("Choose Colour:");
            JLabel difficultyLabel = new JLabel("Difficulty:");

            JTextField nameField = new JTextField();

            String[] colors = {
                    "Yellow",
                    "Green",
                    "Purple",
                    "White",
                    "Cyan"
            };

            JComboBox<String> colorBox =
                    new JComboBox<>(colors);

            String[] difficulties = {
                    "Easy",
                    "Medium",
                    "Hard"
            };

            JComboBox<String> difficultyBox =
                    new JComboBox<>(difficulties);

            JButton startButton = new JButton("Start Game");

            panel.add(title);
            panel.add(new JLabel(""));

            panel.add(nameLabel);
            panel.add(nameField);

            panel.add(colorLabel);
            panel.add(colorBox);

            panel.add(difficultyLabel);
            panel.add(difficultyBox);

            panel.add(new JLabel(""));
            panel.add(startButton);

            frame.add(panel);

            startButton.addActionListener(e -> {

                String name = nameField.getText().trim();

                String color =
                        (String) colorBox.getSelectedItem();

                String difficulty =
                        (String) difficultyBox.getSelectedItem();

                if (name.isEmpty()) {
                    name = "Player";
                }

                if (!GameSettings.isAllowedPlayerColor(color)) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Please select a valid Pac-Man colour."
                    );

                    return;
                }

                GameSettings settings =
                        new GameSettings(name, color, difficulty);

                frame.dispose();

                new PacMan(settings);
            });

            frame.setVisible(true);
        });
    }
}