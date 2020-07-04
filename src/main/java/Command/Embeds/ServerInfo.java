package Command.Embeds;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerInfo implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        Guild guild = context.getGuild();
        List<Member> staffMembers = new ArrayList<>();

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(guild.getRoleById(658711693379108864L)); // Admin
        roles.add(guild.getRoleById(666058483052838934L)); // Head mod
        roles.add(guild.getRoleById(547453187863478302L)); // Mod
        roles.add(guild.getRoleById(602179269917147137L)); // Trial mod

        for (Role role : roles) {
            for (Member member : guild.getMembersWithRoles(role)) {
                if (!staffMembers.contains(member)) {
                    staffMembers.add(member);
                }
            }
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(guild.getName(), guild.getVanityUrl(), guild.getIconUrl())
                .setDescription("Server information").addField(":crown: Owner", guild.getOwner().getEffectiveName(), true)
                .addField(":earth_africa: Region", guild.getRegion().toString(), true)
                .addField(":date: Date Created", "" + guild.getTimeCreated().toString().substring(0, 10), true)
                .addField(":speech_balloon: Text Channels", "" + guild.getTextChannels().size(), true)
                .addField(":speaker: Voice Channels", "" + guild.getVoiceChannels().size(), true)
                .addField(":bust_in_silhouette: Members", "" + guild.getMemberCount(), true)
                .addField(":man_superhero: Staff Members", "" + staffMembers.size(), true)
                .addField(":sunglasses: Real Boys", "" + guild.getMembersWithRoles(guild.getRoleById(567767433075753000L)).size(), true)
                .addField(":rotating_light: Enforcement", "" + guild.getMembersWithRoles(guild.getRoleById(576123562977394689L)).size(), true)
                .setImage("https://media.discordapp.net/attachments/543763819508269056/543769158106742794/IMG_0646ma6iniiii-1024x7681.png?width=903&height=677")
                .setColor(new Color(54, 57, 63));
        context.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public List<String> getCalls() {
        return Arrays.asList("server info", "serverinfo", "si");
    }
}
