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
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            System.out.println("Message text is empty or null.");  
            return null;
        }
        if (message.getMessage_text().length() > 255) {
            System.out.println("Message text exceeds 255 characters.");  
            return null;
        }
        Message newMessage = messageDAO.newMessage(message);
        if (newMessage == null) {
            System.out.println("Message creation failed in DAO layer.");  
        }
        return newMessage;
        
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
                return false;
            }
            return messageDAO.updateMessageByID(message_id, newMessageText);
        } catch (Exception e) {
            System.err.println("Error updating message: " + e.getMessage());
            return false;
        }
    }
    
}
