package Command;

import java.util.Collections;
import java.util.List;

public interface CommandInterface {

    void handle(CommandContext context);

    default List<String> getCalls() {
        return Collections.emptyList();

    }
}
