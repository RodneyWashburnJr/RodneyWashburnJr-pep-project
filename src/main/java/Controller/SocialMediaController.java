package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        app.post("/register", this::createAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{id}/messages", this::getAllMessagesByUser);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessagebyID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.post("/login", this::loginUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccount(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            if (account.getUsername() == null || account.getUsername().trim().isEmpty() || account.getPassword() == null || account.getPassword().length() < 4) {
            context.status(400).result("");
            return;
        }
            Account newAccount = accountService.addAccount(account);
            if (newAccount != null) {
                context.status(200).json(newAccount);
            } else {
                context.status(400).result("");
            }
        } catch (Exception e) {
            context.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    private void createMessage(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
                context.status(400);
                return;
            }
            Message newMessage = messageService.addMessage(message);
            if (newMessage != null) {
                context.status(200).json(newMessage);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
            context.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getAllMessagesByUser(Context context) {
        try {
            int accountID = Integer.parseInt(context.pathParam("id"));
            List<Message> messages = messageService.getAllMessageByUser(accountID);
            context.status(200).json(messages);
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
                context.status(200).result("");
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }

    private void deleteMessagebyID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessage = messageService.getOneMessageByID(messageId);
            if (deletedMessage != null) {
                boolean isDeleted = messageService.deleteByID(messageId);
                if (isDeleted) {
                    context.status(200).json(deletedMessage); // Return the deleted message
                    return;
                }
            }
            context.status(200); 
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }

    private void updateMessageByID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            String requestBody = context.body();
            String newMessageText = "";
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestBody);
                newMessageText = jsonNode.get("message_text").asText();
            } catch (IOException e) {
                context.status(400).result("Invalid request body.");
                return;
            }
            
            if (newMessageText == null || newMessageText.trim().isEmpty()) {
                context.status(400).result("");
                return;
            }
            if(newMessageText.length() > 255){
                context.status(400).result("");
                return;
            }
    
            boolean isUpdated = messageService.updateByID(messageId, newMessageText);
            if (isUpdated) {
                Message updatedMessage = messageService.getOneMessageByID(messageId);
                context.status(200).json(updatedMessage);
            } else {
                context.status(400);
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID.");
        }
    }
    private void loginUser(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Account loginRequest = objectMapper.readValue(context.body(), Account.class);
    
            // Validate user credentials
            Account authenticatedUser = accountService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
    
            if (authenticatedUser != null) {
                context.status(200).json(authenticatedUser);
            } else {
                context.status(401).result("");  // Unauthorized
            }
        } catch (IOException e) {
            context.status(400).result("Invalid request format.");
        }
    }

}