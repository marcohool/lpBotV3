package Command.Embeds;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Staff implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        Guild guild = context.getGuild();

        HashMap<Long, List<Member>> staffMembers = getStaffMembersHashMap(guild);

        StringBuilder owner = new StringBuilder();
        StringBuilder admins = new StringBuilder();
        StringBuilder headMods = new StringBuilder();
        StringBuilder mods = new StringBuilder();
        StringBuilder trialMods = new StringBuilder();


        for (Map.Entry<Long, List<Member>> entry : staffMembers.entrySet()) {
            List<Member> memberList = entry.getValue();
            for (Member member : memberList) {
                switch (entry.getKey().toString()) {
                    case "644664825460555801":
                        owner.append("\n").append(member.getEffectiveName()).append(getOnlineStatusEmoji(member));
                        break;
                    case "658711693379108864":
                        admins.append("\n").append(member.getEffectiveName()).append(getOnlineStatusEmoji(member));
                        break;
                    case "666058483052838934":
                        headMods.append("\n").append(member.getEffectiveName()).append(getOnlineStatusEmoji(member));
                        break;
                    case "547453187863478302":
                        mods.append("\n").append(member.getEffectiveName()).append(getOnlineStatusEmoji(member));
                        break;
                    case "602179269917147137":
                        trialMods.append("\n").append(member.getEffectiveName()).append(getOnlineStatusEmoji(member));
                        break;
                }
            }
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(guild.getName() + " Staff List", guild.getVanityUrl(), guild.getIconUrl())
                .setDescription("Staff list")
                .addField("Owner", owner.toString(), false)
                .addField("Administrators", admins.toString(), false)
                .addField("Head Moderators", headMods.toString(), false).addField("Moderators", mods.toString(), false)
                .addField("Trial Moderators", trialMods.toString(), false)
                .setThumbnail("https://cdn.discordapp.com/attachments/646789403024293922/707543657984557066/200px-PFC_Lokomotiv_Plovdiv_crest.png")
                .setImage("https://media.discordapp.net/attachments/543763819508269056/546077497410715649/FB_IMG_1550132979006.jpg?width=882&height=678")
                .setColor(new Color(54, 57, 63));
        context.getChannel().sendMessage(builder.build()).queue();

    }

    private static Object getOnlineStatusEmoji(Member member) {

        switch (member.getOnlineStatus().toString()) {
            case "ONLINE":
                return " :green_circle:";
            case "OFFLINE":
                return " :black_circle:";
            case "IDLE":
                return " :orange_circle:";
            case "DO_NOT_DISTURB":
                return " :red_circle:";
        }
        return "";
    }

    private HashMap<Long, List<Member>> getStaffMembersHashMap(Guild guild) {

        HashMap<Long, List<Member>> staffMembers = new HashMap<>();
        List<Long> staffRoles = Arrays.asList(644664825460555801L, 658711693379108864L, 666058483052838934L, 547453187863478302L, 602179269917147137L);
        List<Member> membersLogged = new ArrayList<>();

        for (Long roles : staffRoles) { // Loops each staff role
            List<Member> membersWithStaffRole = new ArrayList<>();
            for (Member member : guild.getMembersWithRoles(guild.getRoleById(roles))) { // Loops members who have the staff role
                if (!membersLogged.contains(member)) {
                    membersWithStaffRole.add(member);
                    membersLogged.add(member);
                }
            }
            staffMembers.put(roles, membersWithStaffRole);
        }

        return staffMembers;

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("staff");
    }
}

