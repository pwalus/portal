package portal.shell;

import org.jline.reader.*;
import org.jline.terminal.*;
import org.springframework.context.annotation.*;

@Configuration
public class SpringShellConfig {

    private ShellHelper shellHelper;

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
        return shellHelper = new ShellHelper(terminal);
    }

    @Bean
    public InputReader inputReader(@Lazy LineReader lineReader) {
        return new InputReader(lineReader, this.shellHelper);
    }

}
