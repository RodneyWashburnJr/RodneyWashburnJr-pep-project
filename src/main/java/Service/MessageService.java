package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;
public class MessageService {
    public MessageDAO messageDAO;


    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    public Message addMessage(Message message) {
        try {
            // Add validation if needed (e.g., message text must not be empty)
            if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be null or empty.");
            }
            return messageDAO.newMessage(message);
        } catch (Exception e) {
            System.err.println("Error adding message: " + e.getMessage());
            return null;
        }

    }
    public List<Message> getAllMessageByID(int posted_by) {
        return messageDAO.messagesByUser(posted_by);
    }
    public Message getOneMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }
    public boolean deleteByID(int message_id){
        try {
            return messageDAO.deleteMessagebyID(message_id);
        } catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
            return false;
        }
    }
    public boolean updateByID(int message_id, String newMessageText){
        try {
            if (newMessageText == null || newMessageText.isEmpty()) {
                throw new IllegalArgumentException("New message text cannot be null or empty.");
            }
            return messageDAO.updateMessageByID(message_id, newMessageText);
        } catch (Exception e) {
            System.err.println("Error updating message: " + e.getMessage());
            return false;
        }
    }
    
}
