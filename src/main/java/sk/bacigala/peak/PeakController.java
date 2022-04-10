package sk.bacigala.peak;

import org.springframework.web.bind.annotation.*;
import sk.bacigala.hikeplanner.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/peak")
public class PeakController {

    @CrossOrigin
    @PostMapping("/search")
    public ArrayList<Peak> get(@RequestBody Map<String, String> payload) {
        ArrayList<Peak> result = new ArrayList<>();
        try {
            Connection connection = Database.connect();

            // construct prepared statement
            StringBuilder statementBuilder = new StringBuilder();
            statementBuilder.append("SELECT * FROM peak WHERE TRUE");
            if (payload.containsKey("id"))
                statementBuilder.append(" AND id = ?");
            if (payload.containsKey("nameSearch"))
                statementBuilder.append(" AND LOWER(name) LIKE LOWER(?)");
            if (payload.containsKey("minHeight"))
                statementBuilder.append(" AND height > ?");
            if (payload.containsKey("maxHeight"))
                statementBuilder.append(" AND height < ?");
            if (payload.containsKey("minLatitude"))
                statementBuilder.append(" AND latitude > ?");
            if (payload.containsKey("maxLatitude"))
                statementBuilder.append(" AND latitude < ?");
            if (payload.containsKey("minLongitude"))
                statementBuilder.append(" AND longitude > ?");
            if (payload.containsKey("maxLongitude"))
                statementBuilder.append(" AND longitude < ?");

            PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());

            int parameterCount = 0;
            if (payload.containsKey("id"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("id")));
            if (payload.containsKey("nameSearch"))
                preparedStatement.setString(++parameterCount, '%' + payload.get("nameSearch") + '%');
            if (payload.containsKey("minHeight"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("minHeight")));
            if (payload.containsKey("maxHeight"))
                preparedStatement.setLong(++parameterCount, Integer.parseInt(payload.get("maxHeight")));
            if (payload.containsKey("minLatitude"))
                preparedStatement.setString(++parameterCount, payload.get("minLatitude"));
            if (payload.containsKey("maxLatitude"))
                preparedStatement.setString(++parameterCount, payload.get("maxLatitude"));
            if (payload.containsKey("minLongitude"))
                preparedStatement.setString(++parameterCount, payload.get("minLongitude"));
            if (payload.containsKey("maxLongitude"))
                preparedStatement.setString(++parameterCount, payload.get("maxLongitude"));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Peak(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("height"),
                        resultSet.getString("latitude"),
                        resultSet.getString("longitude")
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
    @PostMapping("/create")
    public String create(@RequestBody Map<String, String> peakInfo) {
        // check incoming peakInfo
        if (!peakInfo.containsKey("name"))
            return "FAIL";
        if (!peakInfo.containsKey("height"))
            return "FAIL";
        if (!peakInfo.containsKey("latitude"))
            return "FAIL";
        if (!peakInfo.containsKey("longitude"))
            return "FAIL";

        int key;

        try {
            Connection connection = Database.connect();

            String statement = "INSERT INTO peak (name, height, latitude, longitude) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            int parameterCount = 0;
            preparedStatement.setString(++parameterCount, peakInfo.get("name"));
            preparedStatement.setLong(++parameterCount, Integer.parseInt(peakInfo.get("height")));
            preparedStatement.setString(++parameterCount, peakInfo.get("latitude"));
            preparedStatement.setString(++parameterCount, peakInfo.get("longitude"));

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

}
