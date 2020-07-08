package LeagueOfLegends;

import Command.CommandContext;
import Command.CommandInterface;
import Config.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SetLeagueCommand implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        String message = context.getMessage().getContentRaw();

        if (!(context.getAuthor().getIdLong() == Constants.owner)) {
            context.getChannel().sendMessage("fuck off").queue();
            return;
        }

        try {
            FileWriter writer = new FileWriter("src/main/resources/assets/league command status.txt", false);
            if (message.toLowerCase().endsWith(" enabled") || message.toLowerCase().endsWith(" on")) {
                writer.write("enabled");
                context.getChannel().sendMessage("command is ENABLED").queue();
            } else if (message.toLowerCase().endsWith(" disabled") || message.toLowerCase().endsWith(" off")) {
                writer.write("disabled");
                context.getChannel().sendMessage("command is DISABLED").queue();
            } else {
                context.getChannel().sendMessage("the fuck you want me to do ?").queue();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("set league command");
    }
}
