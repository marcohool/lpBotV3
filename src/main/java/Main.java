import Config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class Main {

    private Main() throws LoginException {

        JDA jda = JDABuilder.createDefault(Config.getToken("TOKEN"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Listeners())
                .setActivity(Activity.watching("PFC Lokomotiv Plovdiv"))
                .build();

    }

    public static void main(String[] args) throws LoginException {
        new Main();
    }

}
