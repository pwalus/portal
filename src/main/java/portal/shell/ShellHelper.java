package portal.shell;

import org.jline.terminal.*;
import org.jline.utils.*;

public class ShellHelper {

    private final Terminal terminal;

    private final ShellColorConfig shellColorConfig;

    public ShellHelper(Terminal terminal, ShellColorConfig shellColorConfig) {
        this.terminal = terminal;
        this.shellColorConfig = shellColorConfig;
    }

    public String getColored(String message, PromptColor color) {
        return (new AttributedStringBuilder())
            .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
            .toAnsi();
    }

    public String getInfoMessage(String message) {
        return getColored(message, PromptColor.valueOf(shellColorConfig.info));
    }

    public String getSuccessMessage(String message) {
        return getColored(message, PromptColor.valueOf(shellColorConfig.success));
    }

    public String getWarningMessage(String message) {
        return getColored(message, PromptColor.valueOf(shellColorConfig.warning));
    }

    public String getErrorMessage(String message) {
        return getColored(message, PromptColor.valueOf(shellColorConfig.error));
    }

    /**
     * Print message to the console in the default color.
     *
     * @param message message to print
     */
    public void print(String message) {
        print(message, null);
    }

    /**
     * Print message to the console in the success color.
     *
     * @param message message to print
     */
    public void printSuccess(String message) {
        print(message, PromptColor.valueOf(shellColorConfig.success));
    }

    /**
     * Print message to the console in the info color.
     *
     * @param message message to print
     */
    public void printInfo(String message) {
        print(message, PromptColor.valueOf(shellColorConfig.info));
    }

    /**
     * Print message to the console in the warning color.
     *
     * @param message message to print
     */
    public void printWarning(String message) {
        print(message, PromptColor.valueOf(shellColorConfig.warning));
    }

    /**
     * Print message to the console in the error color.
     *
     * @param message message to print
     */
    public void printError(String message) {
        print(message, PromptColor.valueOf(shellColorConfig.error));
    }

    /**
     * Generic Print to the console method.
     *
     * @param message message to print
     * @param color (optional) prompt color
     */
    public void print(String message, PromptColor color) {
        String toPrint = message;
        if (color != null) {
            toPrint = getColored(message, color);
        }
        terminal.writer().println(toPrint);
        terminal.flush();
    }
}
