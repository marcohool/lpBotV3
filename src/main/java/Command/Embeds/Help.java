package Command.Embeds;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Help implements CommandInterface {
    @Override
    public void handle(CommandContext context) {
        EmbedBuilder builder = new EmbedBuilder().setAuthor("LOKOMOTIV PLOVDIV ULTRA", context.getGuild().getVanityUrl(),
                "https://cdn.discordapp.com/attachments/284431549389078528/707619019716427786/996538_10151717866423328_916723410_n.png")
                .setThumbnail("https://media.discordapp.net/attachments/646789403024293922/707543657984557066/200px-PFC_Lokomotiv_Plovdiv_crest.png")
                .setDescription("All bot comands are stated below")
                .addField("<:lokomitivplovdiv:617474948805427222> Ronix:tm:","`lp server info`, `lp staff`", false)
                .addField("<:loko:707933283891282021> PFC Lokomotiv Plovdiv 1926 ", "`lp samo`, `lp photos`", false)
                .addField("<:league:707659747662233680> League of Legends",
                        "`lp league [summoner name]`, `lp league na [summoner name]`, `lp league eune [summoner name]`",
                        false)
                .addField(":musical_note: Music", "`lp join`, `lp leave`", false)
                .addField(":shield: Admin", "`lp set league command enabled`, `lp set league command disabled`", false)
                .addField(":wrench: Other", "`lp pfp`, `lp pfp @[user]`", false)
                .setColor(new Color(54, 57, 63));
        context.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("help");
    }
}
