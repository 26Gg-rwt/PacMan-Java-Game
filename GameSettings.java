import java.awt.Color;

public class GameSettings {

    String playerName;
    String playerColorName;
    String difficulty;

    public GameSettings(String name, String color, String level) {

        playerName = name;
        playerColorName = color;
        difficulty = level;
    }

    public Color getPlayerColor() {

        if (playerColorName.equals("Yellow")) {
            return Color.YELLOW;
        }

        if (playerColorName.equals("Green")) {
            return Color.GREEN;
        }

        if (playerColorName.equals("Purple")) {
            return new Color(160, 80, 220);
        }

        if (playerColorName.equals("White")) {
            return Color.WHITE;
        }

        if (playerColorName.equals("Cyan")) {
            return Color.CYAN;
        }

        return Color.YELLOW;
    }

    public static boolean isAllowedPlayerColor(String color) {

        return color.equals("Yellow")
                || color.equals("Green")
                || color.equals("Purple")
                || color.equals("White")
                || color.equals("Cyan");
    }
}