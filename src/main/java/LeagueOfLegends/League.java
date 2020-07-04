package LeagueOfLegends;
import Config.Config;
import Command.CommandContext;
import Command.CommandInterface;
import com.merakianalytics.orianna.Orianna;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class League implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        Orianna.setRiotAPIKey(Config.getAPIKey("APIKEY"));

        String raw = context.getMessage().getContentRaw();

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("league");
    }
}
