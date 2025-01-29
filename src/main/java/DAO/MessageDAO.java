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
        String checkUser = "SELECT * FROM Account WHERE account_id = ?";
        PreparedStatement userPs = conn.prepareStatement(checkUser);
        userPs.setInt(1, message.getPosted_by());
        ResultSet userResult = userPs.executeQuery();
        if (!userResult.next()) {  // If no user found, return null
            System.out.println("User ID " + message.getPosted_by() + " does not exist.");  // Debugging log
            return null;
        }

        String createMessage = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(createMessage, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());

        int affectedRows = ps.executeUpdate();

        if (affectedRows > 0){
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setMessage_id(generatedKeys.getInt(1));  // Set the generated ID
            }
            return message;
        }


    } catch (SQLException e){
        e.printStackTrace();
    }
    return null;
}
public List<Message> getAllMessages(){
    Connection conn = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try{
        String retreiveAll = "SELECT * FROM Message";
        PreparedStatement ps = conn.prepareStatement(retreiveAll);
        ResultSet allMessages = ps.executeQuery();
        while(allMessages.next()){
            Message message = new Message(allMessages.getInt("message_id"), allMessages.getInt("posted_by"), allMessages.getString("message_text"), allMessages.getLong("time_posted_epoch"));
            messages.add(message);
        }
    } catch (SQLException e){
        e.printStackTrace();
    }
    return messages;
}
public Message getMessageByID(int message_id){
    Connection conn = ConnectionUtil.getConnection();
    try {
        String messageById = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(messageById);
        ps.setInt(1, message_id);
        ResultSet message = ps.executeQuery();
        while(message.next()){
            Message oneMessage = new Message(message.getInt("message_id"), message.getInt("posted_by"), message.getString("message_text"), message.getLong("time_posted_epoch"));
            return oneMessage;
        }

    } catch (SQLException e){
        e.printStackTrace();
    }
    return null;
} 
public boolean deleteMessagebyID(int message_id){
    Connection conn = ConnectionUtil.getConnection();
    try {
        String deleteMessage = "DELETE FROM Message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(deleteMessage);
        ps.setInt(1, message_id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }catch (SQLException e){
        e.printStackTrace();
    }
    return false;
}
public boolean updateMessageByID(int message_id, String newMessageText){
    Connection conn = ConnectionUtil.getConnection();
    try {
        String editMessage = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(editMessage);
        ps.setString(1, newMessageText);
        ps.setInt(2, message_id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e){
        e.printStackTrace();
    }
    return false;
}
public List<Message> messagesByUser(int posted_by){
    Connection conn = ConnectionUtil.getConnection();
    List<Message> userMessages = new ArrayList<>();
    try{
        String retreiveAll = "SELECT * FROM Message WHERE posted_by = ?";
        PreparedStatement ps = conn.prepareStatement(retreiveAll);
        ps.setInt(1, posted_by);
        ResultSet allUserMessages = ps.executeQuery();
        while(allUserMessages.next()){
            Message message = new Message(allUserMessages.getInt("message_id"), allUserMessages.getInt("posted_by"), allUserMessages.getString("message_text"), allUserMessages.getLong("time_posted_epoch"));
            userMessages.add(message);
        }
    } catch (SQLException e){
        e.printStackTrace();
    }
    return userMessages;
}
}