package com.ruse.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StringCleaner {

    private static final String[] BLOCKED_WORDS = new String[]{".com", ".net", ".org", "<img", "@cr", "<img=",
            ":tradereq:", ":duelreq:", "<col=", "<shad=", "java.", ".java", ".json", ".xml", ".txt", ".bat", ".exe", ".dll", ".bin",
            ".dat", ".zip", ".rar", ".7z", ".tar", ".gz", ".gzip", ".tar.gz", ".tar.gzip", ".gz"
    };

    private static final String[] SECURITY_WORDS = new String[]{"rootkit", "sql", "player.get", "inject", "injected",
            "script", "break;", "while(true)", "while (true)", "while(false)", "while (false)", "while (true",
            "for(", "for (", "character", "getRuntime", "while (false", "=true", "= true", "=false", "= false",
            "getclass", "getclasses", "forname", "java.lang", "java.util", "java.io", "java.net", "java.awt",
            "java.applet", "sleep(", ".jar", "join(", "java.lang", "java.util", "java.io", "java.net", "java.awt",
            "java.", "client.java", "client.class", "force.", ".class", "getinputstream", "getoutputstream",
            "void.", ".void", ":void", "java.", ".java", ".json", ".xml", ".txt", ".bat", ".exe", ".dll", ".bin",
            ".dat", ".zip", ".rar", ".7z", ".tar", ".gz", ".gzip", ".tar.gz", ".tar.gzip", ".gz", "integer",
            "byte", "boolean", "array", "svchost", "cmd", "cmd.exe",
            "cmd.exe /c", "cmd.exe /k", "cmd.exe /s", "cmd.exe /v", "cmd.exe /a", "cmd.exe /b", "cmd.exe /e",
            "cmd.exe /f", "cmd.exe /g", "cmd.exe /i", "cmd.exe /j", "cmd.exe /l", "cmd.exe /m", "cmd.exe /n",
            "cmd.exe /", "cmd.exe /p", "cmd.exe /q", "cmd.exe /r", "cmd.exe /t", "cmd.exe /u", "cmd.exe /w",
            "cmd.exe /x", "cmd.exe /y", "cmd.exe /z", "cmd.exe /0", "cmd.exe /1", "cmd.exe /2", "cmd.exe /3",
            "cmd.exe /4", "cmd.exe /5", "cmd.exe /6", "cmd.exe /7", "cmd.exe /8", "cmd.exe /9", "cmd.exe /!",
            "cmd.exe /@", "cmd.exe /#", "cmd.exe /$", "cmd.exe /%", "cmd.exe /^", "cmd.exe /&", "cmd.exe /*",
            "cmd.exe /(", "cmd.exe /)", "cmd.exe /-", "cmd.exe /_", "cmd.exe /+", "cmd.exe /=", "cmd.exe /?",
            "cmd.exe /~", "cmd.exe /`", "cmd.exe /|", "cmd.exe /{", "cmd.exe /}", "cmd.exe /[", "cmd.exe /]",
            "cmd.exe /;", "cmd.exe /'", "cmd.exe /\"", "cmd.exe /<", "cmd.exe />", "cmd.exe /,", "cmd.exe /.",
            "cmd.exe /:", "cmd.exe /\\", "cmd.exe /|", "cmd.exe /^", "cmd.exe /&", "cmd.exe /*", "cmd.exe /(",
            "installutil", "installutil.exe", "installutil.exe /u", "installutil.exe /uninstall", "installutil.exe /?",
            "regini", "regini.exe", "regini.exe /?", "regini.exe /a", "regini.exe /u", "regini.exe /update",
            "/*/", "/**/", "*/**/", "/* */", "/*  */", "/*   */", "/*    */", "/*     */", "getDeclared", "getDeclaredFields",
            "getDeclaredMethods", "getDeclaredConstructors", "getDeclaredField", "getDeclaredMethod",
            "getDeclaredConstructor", "getDeclaredClasses", "getDeclaredClass", "getProtectionDomain",
            "getSigners", "getAnnotation", "getAnnotations", "getDeclaredAnnotations", "getDeclaredAnnotation",
            "getEnclosingClass", "getEnclosingConstructor", "getEnclosingMethod", "getEnumConstants",
            "getGenericInterfaces", "getGenericSuperclass", "getInterfaces", "getMethods", "getFields",
            "getConstructors", "getField", "getMethod", "getConstructor", ".exe"

    };

    public static String[] bypassWords = {
            "double", "don boss", "shit", "ddr", "divine", "chary", "selling", "ring", "char", "bubble", "bath", "joint", "ugh", "welp",
           "boss"
    };

    public static @NotNull String cleanString(@NotNull String s){
        String[] words = s.split(" ");
        for(int i = 0; i < words.length; i++){
            for(String wor : bypassWords){
                if (words[i].equals(wor)) {
                    break;
                }
            }
            for(String word : BLOCKED_WORDS){
                if (words[i].contains(word)) {
                    words[i] = "ae!ae";
                    break;
                }
            }
            for(String word : SECURITY_WORDS){
                if (words[i].contains(word)) {
                    words[i] = "ae!ae";
                    break;
                }
            }
        }
        return String.join(" ", words);
    }

    public static @NotNull String cleanInput(String s){
        if(containsBlockedWord(s))
            System.out.println("Blocked word detected: " + s);
        String ss = cleanString(s);
        if(censored(ss))
            System.out.println("Censored word detected: " + s);
        return ss;
    }

    public static boolean containsBlockedWord(@NotNull String s){
        String[] chunk = s.split(" ");
        for(String sss : chunk) {
            for(String wor : bypassWords){
                if (sss.equals(wor)) {
                    break;
                }
            }
            if(Arrays.stream(BLOCKED_WORDS).anyMatch(sss::equalsIgnoreCase)) {

                return true;
            }
        }
        return Arrays.stream(BLOCKED_WORDS).anyMatch(s::equalsIgnoreCase);
    }

    public static boolean securityBreach(@NotNull String s){
        String[] chunk = s.split(" ");
        for(String sss : chunk) {
            for(String wor : bypassWords){
                if (sss.equals(wor)) {
                    break;
                }
            }
            if(Arrays.stream(SECURITY_WORDS).anyMatch(sss::equalsIgnoreCase))
                return true;
            for(String word : SECURITY_WORDS){
                if (sss.contains(word)) {
                    return true;
                }
            }
        }
        for(String word : SECURITY_WORDS){
            if (s.contains(word)) {
                return true;
            }
        }
        return Arrays.stream(SECURITY_WORDS).anyMatch(s::equalsIgnoreCase);
    }

    public static boolean securityBreach(@NotNull String @NotNull [] s){
        for(String sss : s) {
            for(String wor : bypassWords){
                if (sss.equals(wor)) {
                    break;
                }
            }
            if(Arrays.stream(SECURITY_WORDS).anyMatch(sss::equalsIgnoreCase))
                return true;
        }
        return false;
    }

    @Contract(pure = true)
    public static boolean censored(@NotNull String s){
        return s.contains("Ae!Ae") || s.contains("ae!Ae") || s.contains("ae!ae") || s.contains("Ae!ae");
    }

    @Contract(pure = true)
    public static boolean censored(@NotNull String @NotNull [] s){
        for(String ss : s)
            if(censored(ss))
                return true;
        return false;
    }
}
