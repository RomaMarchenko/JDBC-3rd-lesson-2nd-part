package lesson3HW.part2;


import java.lang.management.GarbageCollectorMXBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ceffzvpakwhb.us-east-2.rds.amazonaws.com:1521:ORCL";

    private static final String USER = "main";
    private static final String PASS = "main2001";
    private Random random = new Random();

    private final String SAVE_TEST_SPEED_INSTANCE = "INSERT INTO TEST_SPEED VALUES(?, ?, ?)";
    private final String DELETE_TEST_SPEED_BY_ID = "DELETE FROM TEST_SPEED WHERE ID = ?";
    private final String GET_ID_LIST = "SELECT ID FROM TEST_SPEED";
    private final String DELETE_1000_TEST_SPEED_INSTANCES = "DELETE FROM TEST_SPEED WHERE ROWNUM <= 1000";
    private final String SELECT_TEST_SPEED_BY_ID = "SELECT * FROM TEST_SPEED WHERE ID = ?";
    private final String SELECT_1000_TEST_SPEED_INSTANCES = "SELECT * FROM TEST_SPEED WHERE ROWNUM <= 1000";

    public long testSavePerformance() {
        long start = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_TEST_SPEED_INSTANCE);
            for (int count = 0; count < 1000; count++) {
                TestSpeed testSpeed = createRandomTestSpeedInstance();
                preparedStatement.setLong(1, testSpeed.getId());
                preparedStatement.setString(2, testSpeed.getSomeString());
                preparedStatement.setLong(3, testSpeed.getSomeNumber());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;

        //1st performance took 403982 ms
        //2nd performance took 160082 ms
        //3nd performance took 165281 ms
    }

    public long testDeleteByIdPerformance() throws SQLException {
        long start = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEST_SPEED_BY_ID);

            ArrayList<Long> testSpeedId = (ArrayList<Long>) getIdList();

            for (int count = 0; count < testSpeedId.size(); count++) {
                preparedStatement.setLong(1, testSpeedId.get(count));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;

        //performance took 256503 ms
    }

    public long testDeletePerformance() {
        long start = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
           Statement statement = connection.createStatement();
           statement.executeUpdate(DELETE_1000_TEST_SPEED_INSTANCES);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;

        //performance took 3223 ms
    }

    public long testSelectByIdPerformance() {
        long start = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEST_SPEED_BY_ID);

            ArrayList<Long> testSpeedId = (ArrayList<Long>) getIdList();

            for (int count = 0; count < testSpeedId.size(); count++) {
                preparedStatement.setLong(1, testSpeedId.get(count));

                preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;

        //performance took 169482 ms
    }

    public long testSelectPerformance() {
        long start = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeQuery(SELECT_1000_TEST_SPEED_INSTANCES);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;

        //performance took 3663 ms
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private TestSpeed createRandomTestSpeedInstance() {
        TestSpeed testSpeed = new TestSpeed(Math.abs(random.nextLong()), createRandomString(), random.nextLong());
        return testSpeed;
    }

    private String createRandomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < random.nextInt(20000) + 1; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private List<Long> getIdList() {
        List<Long> testSpeedId = new ArrayList<>();
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(GET_ID_LIST);
            while(resultSet.next()) {
                testSpeedId.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return testSpeedId;
    }
}
