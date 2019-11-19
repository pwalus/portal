package portal.shell;

import org.jline.reader.*;
import org.jline.terminal.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;

@Configuration
public class SpringShellConfig {

    @Autowired
    public Environment environment;

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal, ShellColorConfig shellColorConfig) {
        return new ShellHelper(terminal, shellColorConfig);
    }

    @Bean
    public InputReader inputReader(
        @Lazy LineReader lineReader,
        @Lazy Terminal terminal,
        ShellColorConfig shellColorConfig
    ) {
        return new InputReader(lineReader, new ShellHelper(terminal, shellColorConfig));
    }

    @Bean
    public ShellColorConfig shellColorConfig() {
        return new ShellColorConfig(
            environment.getProperty("shell.out.info"),
            environment.getProperty("shell.out.success"),
            environment.getProperty("shell.out.warning"),
            environment.getProperty("shell.out.error")
        );
    }

}
