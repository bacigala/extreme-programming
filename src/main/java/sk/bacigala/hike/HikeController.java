package sk.bacigala.hike;

import org.springframework.web.bind.annotation.*;
import sk.bacigala.hikeplanner.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/hike")
public class HikeController {

    @CrossOrigin
    @PostMapping("/search")
    public ArrayList<Hike> search(@RequestBody Map<String, String> payload) {
        ArrayList<Hike> result = new ArrayList<>();
        try {
            Connection connection = Database.connect();

            // construct prepared statement
            StringBuilder statementBuilder = new StringBuilder();
            statementBuilder.append("SELECT * FROM hike WHERE TRUE");
            if (payload.containsKey("id"))
                statementBuilder.append(" AND id = ?");
            if (payload.containsKey("authorId"))
                statementBuilder.append(" AND author_id = ?");
            if (payload.containsKey("nameSearch"))
                statementBuilder.append(" AND LOWER(name) LIKE LOWER(?)");
            if (payload.containsKey("minDate"))
                statementBuilder.append(" AND date >= ?");
            if (payload.containsKey("maxDate"))
                statementBuilder.append(" AND date <= ?");
            if (payload.containsKey("peakId"))
                statementBuilder.append(" AND peak_id = ?");
            if (payload.containsKey("minDifficulty"))
                statementBuilder.append(" AND difficulty >= ?");
            if (payload.containsKey("maxDifficulty"))
                statementBuilder.append(" AND difficulty <= ?");

            PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());

            int parameterCount = 0;
            if (payload.containsKey("id"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("id")));
            if (payload.containsKey("authorId"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("author_id")));
            if (payload.containsKey("nameSearch"))
                preparedStatement.setString(++parameterCount, '%' + payload.get("nameSearch") + '%');
            if (payload.containsKey("minDate"))
                preparedStatement.setDate(++parameterCount, Date.valueOf(payload.get("minDate")));
            if (payload.containsKey("maxDate"))
                preparedStatement.setDate(++parameterCount, Date.valueOf(payload.get("maxDate")));
            if (payload.containsKey("peakId"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("peak_id")));
            if (payload.containsKey("minDifficulty"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("minDifficulty")));
            if (payload.containsKey("maxDifficulty"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("maxDifficulty")));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Hike(
                        resultSet.getLong("id"),
                        resultSet.getLong("peak_id"),
                        resultSet.getLong("difficulty"),
                        resultSet.getLong("author_id"),
                        resultSet.getString("name"),
                        resultSet.getDate("date")
                ));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @CrossOrigin
    @PostMapping("/modify")
    public String modify(@RequestBody Map<String, String> payload) {
        if (!payload.containsKey("id"))
            return "FAIL - no id specified";

        try {
            Connection connection = Database.connect();
            StringBuilder statementBuilder = new StringBuilder();
            statementBuilder.append("UPDATE hike SET ");
            if (payload.containsKey("name"))
                statementBuilder.append(" name = ?,");
            if (payload.containsKey("date"))
                statementBuilder.append(" date = ?,");
            if (payload.containsKey("peak_id"))
                statementBuilder.append(" peak_id  = ?,");
            if (payload.containsKey("difficulty"))
                statementBuilder.append(" difficulty = ?,");
            if (payload.containsKey("author_id"))
                statementBuilder.append(" author_id = ?,");
            statementBuilder.deleteCharAt(statementBuilder.length()-1);
            statementBuilder.append(" WHERE id = ?");

            PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());

            int parameterCount = 0;
            if (payload.containsKey("name"))
                preparedStatement.setString(++parameterCount, payload.get("name"));
            if (payload.containsKey("date"))
                preparedStatement.setDate(++parameterCount, Date.valueOf(payload.get("date")));
            if (payload.containsKey("peak_id"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("peak_id")));
            if (payload.containsKey("difficulty"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("difficulty")));
            if (payload.containsKey("author_id"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("author_id")));
            preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("id")));

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }

        return "OK";
    }

    @CrossOrigin
    @PostMapping("/create")
    public String create(@RequestBody Map<String, String> hikeInfo) {
        // check incoming hikeInfo
        if (!hikeInfo.containsKey("name"))
            return "FAIL";
        if (!hikeInfo.containsKey("date"))
            return "FAIL";
        if (!hikeInfo.containsKey("peak_id"))
            return "FAIL";
        if (!hikeInfo.containsKey("difficulty"))
            return "FAIL";
        if (!hikeInfo.containsKey("author_id"))
            return "FAIL";

        int key;

        try {
            Connection connection = Database.connect();

            String statement = "INSERT INTO hike (name, date, peak_id, difficulty, author_id) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            int parameterCount = 0;
            preparedStatement.setString(++parameterCount, hikeInfo.get("name"));
            preparedStatement.setDate(++parameterCount, Date.valueOf(hikeInfo.get("date")));
            preparedStatement.setLong(++parameterCount, Integer.parseInt(hikeInfo.get("peak_id")));
            preparedStatement.setLong(++parameterCount, Integer.parseInt(hikeInfo.get("difficulty")));
            preparedStatement.setLong(++parameterCount, Integer.parseInt(hikeInfo.get("author_id")));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            key = resultSet.getInt(1);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }

        return Integer.toString(key);
    }

    @CrossOrigin
    @PostMapping("/delete")
    public String delete(@RequestBody Map<String, String> hikeInfo) {
        if (!hikeInfo.containsKey("id"))
            return "FAIL";

        try {
            Connection connection = Database.connect();
            String statement = "DELETE FROM hike WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setLong(1, Integer.parseInt(hikeInfo.get("id")));
            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows > 0 ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

}
