import Command.CommandContext;
import Command.CommandInterface;
import Config.Constants;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import java.util.List;
import java.util.Map;

public class Listeners extends ListenerAdapter {

    CommandManager commands = new CommandManager();

    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        if (event.getMember().getId().equals("644623474433851395")){
            if (event.isGuildMuted()){
                AuditLogPaginationAction auditLogs = event.getGuild().retrieveAuditLogs();
                auditLogs.type(ActionType.MEMBER_UPDATE);
                auditLogs.limit(1);
                auditLogs.queue( (entries) ->
                {
                    AuditLogEntry entry = entries.get(0);
                    User muter = entry.getUser();
                    if (!muter.getId().equals("644623474433851395")) { // If i haven't muted myself
                        muter.openPrivateChannel().flatMap(channel -> channel.sendMessage("nice try \nhttps://discord.gg/U5pQXG8")).queue();
                        event.getGuild().kick(muter.getId()).queue();
                        event.getMember().mute(false).queue();

                    }
                });
            }
        }
    }
    

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        User user = event.getAuthor();

        if (user.isBot()) {
            return;
        }

        String prefix = Constants.prefix;
        String raw = event.getMessage().getContentRaw().toLowerCase();

        checkBannedWordsAndEmojis(raw, event);

        if (raw.equalsIgnoreCase(prefix + " shutdown") && user.getIdLong() == Constants.owner) {
            event.getChannel().sendMessage("cya").queue();
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix) && !raw.equalsIgnoreCase(prefix)) {

            String args = raw.substring(3);

            CommandContext ctx = new CommandContext(event, args);

            for (Map.Entry<CommandInterface, List<String>> entry : commands.getCommands().entrySet()) { // For command in command list
                for (String call : entry.getValue()) { // For call in command call list
                    if ((args).startsWith(call+" ") || args.equalsIgnoreCase(call))  {
                        entry.getKey().handle(ctx);
                        break;
                    }
                }
            }
        }
    }

    private void checkBannedWordsAndEmojis(String message, GuildMessageReceivedEvent event) {


        String[] splitMessage = message.split("\\s+");

        for (String wordInMessage : splitMessage) {
            for (String word : Constants.bannedWords) {
                if (wordInMessage.equalsIgnoreCase(word)) {
                    event.getChannel().sendMessage("stop saying " + word).queue();
                }
            }
            for (String emoji : Constants.bannedEmotes) {
                if (wordInMessage.equalsIgnoreCase(emoji)) {
                    event.getChannel().sendMessage("dont say that emote").queue();
                }
            }
        }
    }

}

