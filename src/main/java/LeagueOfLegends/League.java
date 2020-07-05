package LeagueOfLegends;
import Config.Config;
import Command.CommandContext;
import Command.CommandInterface;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
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

        context.getChannel().sendFile(new File("assets/league events/final.png"), "final.png").queue();

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

        BufferedImage backgroundImg = ImageIO.read(new File("assets/league events/lokobot template.png"));
        BufferedImage summonerIcon = ImageIO.read(new URL(summoner.getProfileIcon().getImage().getURL()));
        BufferedImage tierIcon;
        String tierImageURL;
        String tierTextString;
        int noOfWins;
        int noOfLosses;
        Color colourOfLeague;

        if (ranked) {

            String tierName = le.getTier().toString();

            noOfWins = le.getWins();
            noOfLosses = le.getLosses();
            tierTextString = le.getTier()+" "+le.getDivision()+" "+le.getLeaguePoints()+" LP";

            switch (tierName.toLowerCase()) {
                case "iron":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/0/03/Season_2019_-_Iron_1.png/revision/latest?cb=20181229234926";
                    colourOfLeague = new Color(93, 86, 86);
                    break;
                case "bronze":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/f/f4/Season_2019_-_Bronze_1.png/revision/latest?cb=20181229234910";
                    colourOfLeague = new Color(136, 67, 43);
                    break;
                case "silver":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/7/70/Season_2019_-_Silver_1.png/revision/latest?cb=20181229234936";
                    colourOfLeague = new Color(118, 146, 155);
                    break;
                case "gold":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/9/96/Season_2019_-_Gold_1.png/revision/latest?cb=20181229234920";
                    colourOfLeague = new Color(216, 166, 67);
                    break;
                case "platinum":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/7/74/Season_2019_-_Platinum_1.png/revision/latest?cb=20181229234932";
                    colourOfLeague = new Color(69, 224, 156);
                    break;
                case "diamond":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/9/91/Season_2019_-_Diamond_1.png/revision/latest?cb=20181229234917";
                    colourOfLeague = new Color(66, 80, 132);
                    break;
                case "master":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/1/11/Season_2019_-_Master_1.png/revision/latest?cb=20181229234929";
                    colourOfLeague = new Color(115, 72, 146);
                    break;
                case "grandmaster":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/7/76/Season_2019_-_Grandmaster_1.png/revision/latest?cb=20181229234923";
                    colourOfLeague = new Color(248, 83, 83);
                    break;
                case "challenger":
                    tierImageURL = "https://vignette.wikia.nocookie.net/leagueoflegends/images/5/5f/Season_2019_-_Challenger_1.png/revision/latest?cb=20181229234913";
                    colourOfLeague = new Color(245, 167, 89);
                    break;
                default:
                    tierImageURL = null;
                    colourOfLeague = null;
            }

            assert tierImageURL != null;
            tierIcon = ImageIO.read(new URL(tierImageURL));

        } else {
            tierIcon = ImageIO.read(new File("assets/league events/unranked.png"));
            noOfWins = 0;
            noOfLosses = 0;
            colourOfLeague = Color.WHITE;
            tierTextString = "UNRANKED";
        }



        Image iconScale = summonerIcon.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        Graphics g = backgroundImg.getGraphics();

        g.drawImage(iconScale, 16, 16, null);
        g.drawImage(tierIcon, 595, 366, null);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout summonerName = new TextLayout(summoner.getName().toUpperCase(), new Font("Impact", Font.BOLD, 50), frc);
        Shape nameOutline = summonerName.getOutline(null);

        g2d.translate(250,137);
        g2d.setColor(Color.WHITE);
        g2d.fill(nameOutline);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.draw(nameOutline);

        // Level text
        TextLayout level = new TextLayout("LEVEL: "+summoner.getLevel(), new Font("Impact", Font.BOLD, 27), frc);
        Shape levelOutline = level.getOutline(null);
        g2d.translate(2, 35);
        g2d.setColor(Color.WHITE);
        g2d.fill(levelOutline);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        g2d.draw(levelOutline);

        // Wins text
        TextLayout wins = new TextLayout("WINS: "+noOfWins, new Font("Impact", Font.BOLD, 30), frc);
        Shape winsOutline = wins.getOutline(null);
        if (noOfWins >= 100) {
            g2d.translate(-206, 90);
        } else {
            if (noOfWins >= 10) {
                g2d.translate(-196, 90);
            } else {
                g2d.translate(-187, 90);
            }
        }
        g2d.setColor(Color.WHITE);
        g2d.fill(winsOutline);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        g2d.draw(winsOutline);

        // Losses text
        TextLayout losses = new TextLayout("LOSSES: "+noOfLosses, new Font("Impact", Font.BOLD, 30), frc);
        Shape lossesOutline = losses.getOutline(null);
        g2d.translate(-12, 36);
        g2d.setColor(Color.WHITE);
        g2d.fill(lossesOutline);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        g2d.draw(lossesOutline);

        // Tier text
        TextLayout tierText = new TextLayout(tierTextString, new Font("Impact", Font.BOLD, 45), frc);
        Shape tierOutline = tierText.getOutline(null);
        if (tierTextString.toLowerCase().startsWith("challenger") || tierTextString.toLowerCase().startsWith("grandmaster")) {
            g2d.translate(135, 248);
        } else {
            g2d.translate(215, 248);
        }
        g2d.setColor(colourOfLeague);
        g2d.fill(tierOutline);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.draw(tierOutline);
        g2d.dispose();

        File outFile = new File("assets/league events/final.png");
        ImageIO.write(backgroundImg, "png", outFile);

    }

    private String setRegion(String[] rawSplit) {

        try {
            switch (rawSplit[2].toLowerCase()){
                case "na":
                    Orianna.setDefaultRegion(Region.NORTH_AMERICA);
                    return appendUsername(rawSplit, 3);
                case "eune":
                    Orianna.setDefaultRegion(Region.EUROPE_NORTH_EAST);
                    return appendUsername(rawSplit, 3);
                default:
                    Orianna.setDefaultRegion(Region.EUROPE_WEST);
                    return appendUsername(rawSplit, 2);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private String appendUsername(String[] rawSplit, int usernameBegginingIndex){

        String username = "";

        for (int i = usernameBegginingIndex; i < rawSplit.length; i++){
            username = username.concat(rawSplit[i]);
        }

        return username;

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("league");
    }
}
