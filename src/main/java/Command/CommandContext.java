package Command;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.List;

public class CommandContext implements ICommandContext {

    private final GuildMessageReceivedEvent event;

    public CommandContext(GuildMessageReceivedEvent event, String args) {
        this.event = event;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

}
