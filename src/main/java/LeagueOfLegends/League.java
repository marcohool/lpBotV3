package LeagueOfLegends;
import Config.Config;
import Command.CommandContext;
import Command.CommandInterface;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.merakianalytics.orianna.types.dto.league.LeagueEntries;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class League implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        Orianna.setRiotAPIKey(Config.getAPIKey("APIKEY"));

        String raw = context.getMessage().getContentRaw();
        String[] rawSplit = raw.split("\\s+");
        String summonerName = setRegion(rawSplit);

        // If user gives summoner name
        if (summonerName == null){
            context.getChannel().sendMessage("stop trying to break the bot retard").queue();
            return;
        }

        Summoner summoner = Orianna.summonerNamed(summonerName).get();

        // If summoner exists
        if (summoner.getLevel() == 0){
            context.getChannel().sendMessage("give me a summoner that exists or u get banned").queue();
            return;
        }

        imageSetup(summoner);

    }

    private void imageSetup(Summoner summoner) {

        try {

            LeagueEntry le = summoner.getLeaguePosition(Queue.RANKED_SOLO);

            // If summoner is ranked
            if (le != null) {
                try {
                    drawImage(le, summoner, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Summoner is unranked
            else {
                try {
                    drawImage(null, summoner, false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void drawImage(LeagueEntry le, Summoner summoner, Boolean ranked) throws IOException {

        BufferedImage img = ImageIO.read(new File("assets/league events/lokobot template.png"));
        BufferedImage icon = ImageIO.read(new URL(summoner.getProfileIcon().getImage().getURL()));
        //BufferedImage tier = null;

        try {
            String tier = le.getTier().toString();
        } catch (NullPointerException e) {
            System.out.println(summoner.getName()+ " unranked");
            // Fix summoner name, only getting 3rd index but name could have space in it
        }


        }

    private String setRegion(String rawSplit[]) {

        try {
            switch (rawSplit[2].toLowerCase()){
                case "na":
                    Orianna.setDefaultRegion(Region.NORTH_AMERICA);
                    return rawSplit[3];
                case "eune":
                    Orianna.setDefaultRegion(Region.EUROPE_NORTH_EAST);
                    return rawSplit[3];
                default:
                    Orianna.setDefaultRegion(Region.EUROPE_WEST);
                    return rawSplit[2];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("league");
    }
}
