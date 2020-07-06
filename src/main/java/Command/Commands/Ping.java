package Command.Commands;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.JDA;
import java.util.Collections;
import java.util.List;

public class Ping implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        JDA jda = context.getJDA();
        jda.getRestPing().queue((ping) -> context.getChannel().sendMessageFormat("%sms", ping).queue());

    }

    @Override
    public List<String> getCalls() {

        return Collections.singletonList("ping");
    }
}
