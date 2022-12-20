import java.sql.*;
import java.sql.Connection;

public class Database {
    static String url = "jdbc:mysql://localhost:3306/schema";
    static String username = "root";
    static String password = "1q2w3e4RR";

    public static void connectToDatabase() {

        int day = dayParse();
        String month = monthParse();
        int year = yearParse();
        String time = timeParse();

        try {

            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO `score` (date_day, date_month, date_year, time_of_day, score_num) VALUE (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, day);
            preparedStatement.setString(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setString(4, time);
            preparedStatement.setInt(5, GamePanel.score);

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                System.out.println("A new row has been added successfully to the table.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateHighScore() {
        int highscore = 0;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT MAX(score_num) FROM `score`";
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                highscore = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highscore;
    }


    public static int dayParse() {
        return Integer.parseInt(GamePanel.dateTime.split(" ")[0]);
    }

    public static String monthParse() {
        return GamePanel.dateTime.split(" ")[1];
    }

    public static int yearParse() {
        return Integer.parseInt(GamePanel.dateTime.split(" ")[2]);
    }

    public static String timeParse() {
        return GamePanel.dateTime.split(" ")[3];
    }
}
