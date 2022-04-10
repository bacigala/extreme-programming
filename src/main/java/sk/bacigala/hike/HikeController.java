package sk.bacigala.hike;

import org.springframework.web.bind.annotation.*;
import sk.bacigala.hikeplanner.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/hike")
public class HikeController {

    @CrossOrigin
    @PostMapping("/search")
    public ArrayList<Hike> get(@RequestBody Map<String, String> payload) {
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

}
