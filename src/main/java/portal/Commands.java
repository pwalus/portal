package portal;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
class Commands {

    @ShellMethod("Example method")
    public String helloWorld() {
        return "hello";
    }
}
