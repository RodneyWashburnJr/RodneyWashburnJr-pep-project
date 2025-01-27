package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
public class MessageDAO {
public Message newMessage(Message message){
    Connection conn = ConnectionUtil.getConnection();
    try{
        String createMessage = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUE (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(createMessage, Statement.RETURN_GENERATED_KEYS);
        long currentTimeMillis = System.currentTimeMillis();
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, currentTimeMillis);

        ps.executeUpdate();
        ResultSet messages = ps.getGeneratedKeys();
        if(messages.next()){
            int generatedID = messages.getInt(1);
            message.setMessage_id(generatedID);
        }
        message.setTime_posted_epoch(currentTimeMillis);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a z");
        System.out.println("Message created at: " + dateTime.format(formatter));
        return message;


    } catch (SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}
public List<Message> getAllMessages(){
    Connection conn = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try{
        String retreiveAll = "SELECT * FROM Message";
        PreparedStatement ps = conn.prepareStatement(retreiveAll);
        ResultSet AllMessages = ps.executeQuery();

    } catch (SQLException e){
        System.out.println(e.getMessage());
    }
}
}