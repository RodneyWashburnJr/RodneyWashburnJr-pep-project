package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/accounts", this::createAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/user/{posted_by}", this::getAllMessagesByID);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessagebyID);
        app.put("/messages/{message_id}", this::updateMessageByID);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // Account Handlers
    private void createAccount(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account newAccount = accountService.addAccount(account);
            if (newAccount != null) {
                context.status(201).json(newAccount);
            } else {
                context.status(400).result("Account could not be created.");
            }
        } catch (Exception e) {
            context.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    // Message Handlers
    private void createMessage(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            Message newMessage = messageService.addMessage(message);
            if (newMessage != null) {
                context.status(201).json(newMessage);
            } else {
                context.status(400).result("Message could not be created.");
            }
        } catch (Exception e) {
            context.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getAllMessagesByID(Context context) {
        try {
            int postedBy = Integer.parseInt(context.pathParam("posted_by"));
            List<Message> messages = messageService.getAllMessageByID(postedBy);
            if (messages.isEmpty()) {
                context.status(404).result("No messages found for this user.");
            } else {
                context.json(messages);
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid user ID.");
        }
    }

    private void getMessageByID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getOneMessageByID(messageId);
            if (message != null) {
                context.json(message);
            } else {
                context.status(404).result("Message not found.");
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }

    private void deleteMessagebyID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            boolean isDeleted = messageService.deleteByID(messageId);
            if (isDeleted) {
                context.status(200).result("Message deleted successfully.");
            } else {
                context.status(404).result("Message not found.");
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }

    private void updateMessageByID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            String newMessageText = context.body();
            boolean isUpdated = messageService.updateByID(messageId, newMessageText);
            if (isUpdated) {
                context.status(200).result("Message updated successfully.");
            } else {
                context.status(404).result("Message not found.");
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }


}