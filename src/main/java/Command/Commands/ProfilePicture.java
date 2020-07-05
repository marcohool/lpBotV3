package Command.Commands;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;

public class ProfilePicture implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        String message = context.getMessage().getContentRaw();

        try {
            if (message.endsWith("pfp")) {
                context.getChannel().sendMessage(context.getAuthor().getAvatarUrl()).queue();
            } else {
                message = message.replaceAll("[^\\d.]", "");
                try {
                    User user = context.getGuild().retrieveMemberById(message).complete().getUser();
                    context.getChannel().sendMessage(user.getAvatarUrl()).queue();
                } catch (Exception error) {
                    context.getChannel().sendMessage("give me a real user with a profile picture dumbass").queue();
                }
            }
        } catch (Exception e) {
            context.getChannel().sendMessage("you dont have a profile picture bastard").queue();
        }

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("pfp");
    }
}
