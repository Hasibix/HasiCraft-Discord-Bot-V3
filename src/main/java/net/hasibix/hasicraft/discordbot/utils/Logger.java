package net.hasibix.hasicraft.discordbot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nullable;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Logger {
    @Nullable static String logsPath;
    Class<?> clazz;
    public static class Color {

        public static final String RESET = "\033[0m";

        public static final String BLACK = "\033[0;30m";
        public static final String RED = "\033[0;31m";
        public static final String GREEN = "\033[0;32m";
        public static final String YELLOW = "\033[0;33m";
        public static final String BLUE = "\033[0;34m";
        public static final String PURPLE = "\033[0;35m";
        public static final String CYAN = "\033[0;36m";
        public static final String WHITE = "\033[0;37m";

        public static final String BLACK_BOLD = "\033[1;30m";
        public static final String RED_BOLD = "\033[1;31m";
        public static final String GREEN_BOLD = "\033[1;32m";
        public static final String YELLOW_BOLD = "\033[1;33m";
        public static final String BLUE_BOLD = "\033[1;34m";
        public static final String PURPLE_BOLD = "\033[1;35m";
        public static final String CYAN_BOLD = "\033[1;36m";
        public static final String WHITE_BOLD = "\033[1;37m";

        public static final String BLACK_UNDERLINED = "\033[4;30m";
        public static final String RED_UNDERLINED = "\033[4;31m";
        public static final String GREEN_UNDERLINED = "\033[4;32m";
        public static final String YELLOW_UNDERLINED = "\033[4;33m";
        public static final String BLUE_UNDERLINED = "\033[4;34m";
        public static final String PURPLE_UNDERLINED = "\033[4;35m";
        public static final String CYAN_UNDERLINED = "\033[4;36m";
        public static final String WHITE_UNDERLINED = "\033[4;37m";

        public static final String BLACK_BACKGROUND = "\033[40m";
        public static final String RED_BACKGROUND = "\033[41m";
        public static final String GREEN_BACKGROUND = "\033[42m";
        public static final String YELLOW_BACKGROUND = "\033[43m";
        public static final String BLUE_BACKGROUND = "\033[44m";
        public static final String PURPLE_BACKGROUND = "\033[45m";
        public static final String CYAN_BACKGROUND = "\033[46m";
        public static final String WHITE_BACKGROUND = "\033[47m";

        public static final String BLACK_BRIGHT = "\033[0;90m";
        public static final String RED_BRIGHT = "\033[0;91m";
        public static final String GREEN_BRIGHT = "\033[0;92m";
        public static final String YELLOW_BRIGHT = "\033[0;93m";
        public static final String BLUE_BRIGHT = "\033[0;94m";
        public static final String PURPLE_BRIGHT = "\033[0;95m";
        public static final String CYAN_BRIGHT = "\033[0;96m";
        public static final String WHITE_BRIGHT = "\033[0;97m";

        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";
     
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m";
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";

    }

    private enum Type {
        Info,
        Debug,
        Warn,
        Error,
        Trace,
        Fatal
    }

    private String getTypeString(boolean isColor, Type type) {
        switch(type) {
            case Info:
                if(isColor) {
                    return Color.GREEN + "INFO";
                } else {
                    return "INFO";
                }
            case Debug:
                if(isColor) {
                    return Color.BLUE + "DEBUG";
                } else {
                    return "DEBUG";
                }
            case Warn:
                if(isColor) {
                    return Color.YELLOW + "WARN";
                } else {
                    return "WARN";
                }
            case Error:
                if(isColor) {
                    return Color.RED + "ERROR";
                } else {
                    return "ERROR";
                }
            case Trace:
                if(isColor) {
                    return Color.CYAN + "TRACE";
                } else {
                    return "TRACE";
                }
            case Fatal:
                if(isColor) {
                    return Color.PURPLE + "FATAL";
                } else {
                    return "FATAL";
                }
        }
        return null;
    }

    private Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static Logger of(Class<?> clazz) {
        return new Logger(clazz);
    }

    public static void setPath(String path) {
        if(path != null) {
            logsPath = path;
        } else {
            logsPath = null;
        }
    }

    void writeLog(String text, Type type) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String line = String.format("[%s] [%s] [%s]: %s", Color.CYAN + dtf.format(now) + Color.RESET, getTypeString(true, type) + Color.RESET, this.clazz.getSimpleName(), text);
        String lineNoColor = String.format("[%s] [%s] [%s]: %s", dtf.format(now), getTypeString(false, type), this.clazz.getName(), text);
        System.out.println(line);

        if(logsPath != null) {
            DateTimeFormatter fdtf = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
            try {
                Files.createDirectories(Paths.get(logsPath));
                Files.writeString(
                    Paths.get(logsPath + fdtf.format(now) + ".log"),
                    lineNoColor + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
                );

            } catch (IOException e) {
                String er = String.format("[%s] [%s] [%s]: %s", Color.CYAN + dtf.format(now) + Color.RESET, Color.RED + "ERR" + Color.RESET, Logger.class.getName(), Color.RESET + "An exception occurred in logger.\n" + ExceptionUtils.getStackTrace(e));
                System.err.println(er);
            }
        }
    }

    public void info(String text) {
        writeLog(text,  Type.Info);
    }

    public void debug(String text) {
        writeLog(text, Type.Debug);
    }

    public void warn(String text) {
        writeLog(text, Type.Warn);
    }
    
    public void error(String text) {
        writeLog(text, Type.Error);
    }

    public void trace(Exception e) {
        writeLog(ExceptionUtils.getStackTrace(e), Type.Trace);
    }

    public void fatal(String text) {
        writeLog(text, Type.Fatal);
    }

}
