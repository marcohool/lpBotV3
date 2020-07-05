package Command.Commands;

import Command.CommandContext;
import Command.CommandInterface;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Photos implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        context.getChannel().sendTyping().queue();
        String image = getRandomImage();
        File file = new File("assets/photo files/" + image);
        context.getChannel().sendFile(file, image).queue();

    }

    private static String getRandomImage() {

        File folder = new File("assets/photo files");
        File[] listOfFiles = folder.listFiles();

        Random rand = new Random();
        int randNumb = rand.nextInt(listOfFiles.length);

        return listOfFiles[randNumb].getName();

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("photos");
    }
}
