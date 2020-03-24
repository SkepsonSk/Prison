package pl.trollcraft.chat;

import org.bukkit.entity.Player;
import pl.trollcraft.util.ChatUtil;

public class ChatProcessor {

    public enum Response {
        DISABLED("&cChat jest wylaczony."),
        SAME_MSG("&cProsze nie powtarzac wiadomosci."),
        CHAT_OFF("&cTwoj chat jest wylaczony."),
        LOCKED("&cMozesz pisac co 2 sekundy."),
        OK("OK");

        private String message;

        Response(String message){
            this.message = ChatUtil.fixColor(message);
        }

        public String getMessage() { return message; }
    }

    // -------- -------- -------- --------

    private static boolean chatEnabled = true;

    // -------- -------- -------- --------

    public static boolean isChatEnabled() { return chatEnabled; }
    public static void switchChat() { ChatProcessor.chatEnabled = !chatEnabled; }

    // -------- -------- -------- --------

    public static String deflood(String message) { return message.replaceAll("(...+?)\\1+", "$1"); }

    public static Response process(Player player, String message) {
        ChatProfile chatProfile = ChatProfile.get(player);

        if (player.hasPermission("prison.admin")) return Response.OK;

        if (!chatEnabled) return Response.DISABLED;
        if (!chatProfile.hasChatEnabled()) return Response.CHAT_OFF;
        if (chatProfile.changeLastMessage(message)) return Response.SAME_MSG;
        if (!chatProfile.canWrite()) return Response.LOCKED;

        chatProfile.lockMessaging(2);
        return Response.OK;
    }

}
