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
        Message existingMessage = messageDAO.getMessageByID(message.getMessage_id());
    if (existingMessage != null) {
        return null;
    }
        return messageDAO.newMessage(message);
    }
    public List<Message> getAllMessageByID(int posted_by) {
        return messageDAO.messagesByUser(posted_by);
    }
    public Message getOneMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }
    public boolean deleteByID(int message_id){
        return messageDAO.deleteMessagebyID(message_id);
    }
    public boolean updateByID(int message_id, String newMessageText){
        return messageDAO.updateMessageByID(message_id, newMessageText);
    }
}
