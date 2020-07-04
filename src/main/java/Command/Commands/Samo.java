package Command.Commands;

import Command.CommandContext;
import Command.CommandInterface;

import java.util.Collections;
import java.util.List;

public class Samo implements CommandInterface {

    @Override
    public void handle(CommandContext context) {
        context.getChannel().sendMessage("LOKO").queue();
    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("samo");
    }
}
