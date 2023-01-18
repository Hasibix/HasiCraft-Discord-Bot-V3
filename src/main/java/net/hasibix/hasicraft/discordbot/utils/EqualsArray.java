package net.hasibix.hasicraft.discordbot.utils;

public class EqualsArray {
    public static Boolean Equals(Object obj, Object[] array) {
        for (Object i : array) {
            if(obj == i) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}
