package hr.mucnjakf.rmi;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import hr.mucnjakf.jndi.InitialDirContextCloseable;

import javax.naming.Context;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Hashtable;

public class ChatClient {

    private ChatService chatService;

    public static final String RMI_URL = "rmi://localhost:1099";
    public static final String RMI_SERVER_NAME = "server";

    public ChatClient() {
        fetchChatServer();
    }

    private void fetchChatServer() {
        final Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, RegistryContextFactory.class.getName());
        props.put(Context.PROVIDER_URL, RMI_URL);

        try (InitialDirContextCloseable context = new InitialDirContextCloseable(props)) {
            chatService = (ChatService) context.lookup(RMI_SERVER_NAME);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            chatService.sendMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
