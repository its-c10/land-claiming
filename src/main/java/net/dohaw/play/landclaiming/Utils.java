package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static List<File> getFilesInConfig(final File folder){
        List<File> files = new ArrayList<>();
        if(folder.isDirectory()){
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    files.add(fileEntry);
                }
            }
        }else{
            return null;
        }
        return files;
    }

    public static String replacePlaceholders(String placeholder, String str, String value){
        return str.replace(placeholder, value);
    }

    public static String getPartBeforeButton(String msg, final String BUTTON){
        List<String> arr = Arrays.asList(msg.split(" "));
        List<String> firstPartTemp = arr.subList(0, getIndexOfButton(msg, BUTTON));
        String firstPart = String.join(" ", firstPartTemp);
        return firstPart;
    }

    public static String getRestAfterButton(String msg, final String BUTTON){
        List<String> arr = Arrays.asList(msg.split(" "));
        /*
            If the button isn't the last thing in the sentence
         */
        if(getIndexOfButton(msg, BUTTON) != arr.size() - 1){
            List<String> restOfSentenceTemp = arr.subList(getIndexOfButton(msg, BUTTON) + 1, arr.size());
            String restOfSentence = String.join(" ", restOfSentenceTemp);
            return restOfSentence;
        }else{
            return null;
        }

    }

    public static int getIndexOfButton(String msg, final String BUTTON){
        List<String> arr = Arrays.asList(msg.split(" "));
        int indexButton = 0;
        for(String s : arr){
            if(s.equalsIgnoreCase(BUTTON)){
                indexButton = arr.indexOf(s);
            }
        }
        return indexButton;
    }

    public static TextComponent createButtonMsg(ChatFactory chatFactory, String msg, final String BUTTON, String buttonCommand, String buttonHoverText){

        List<String> arr = Arrays.asList(msg.split(" "));
        int indexButton = Utils.getIndexOfButton(msg, BUTTON);
        String button = arr.get(indexButton);
        String firstPart = Utils.getPartBeforeButton(msg, BUTTON);
        String restOfSentence = Utils.getRestAfterButton(msg, BUTTON);

        net.md_5.bungee.api.chat.TextComponent tcMsg1 = new net.md_5.bungee.api.chat.TextComponent(chatFactory.colorString(firstPart) + " ");
        net.md_5.bungee.api.chat.TextComponent tcButton = new net.md_5.bungee.api.chat.TextComponent(chatFactory.colorString(button) + " ");
        net.md_5.bungee.api.chat.TextComponent tcMsg2 = new net.md_5.bungee.api.chat.TextComponent(chatFactory.colorString(restOfSentence));

        if(buttonHoverText != null){
            tcButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(buttonHoverText).create()));
        }

        if(buttonCommand != null){
            tcButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, buttonCommand));
        }

        tcMsg1.addExtra(tcButton);
        tcMsg1.addExtra(tcMsg2);

        return tcMsg1;
    }

    public static TextComponent createButtonMsg(ChatFactory chatFactory, String msg, final String BUTTON, final String BUTTON2, String button1Command, String button2Command, String button1HoverText, String button2HoverText){

        String firstPart = Utils.getPartBeforeButton(msg, BUTTON);
        String partAfterFirstButton;

        List<String> arr = Arrays.asList(msg.split(" "));
        List<String> sentenceBeforeSecondButton = arr.subList(getIndexOfButton(msg, BUTTON) + 1, getIndexOfButton(msg, BUTTON2));

        partAfterFirstButton = String.join(" ", sentenceBeforeSecondButton);

        int indexButton = arr.indexOf(BUTTON);
        int indexButton2 = arr.indexOf(BUTTON2);
        String partAfterSecondButton = Utils.getRestAfterButton(msg, BUTTON2);

        String button = arr.get(indexButton);
        String button2 = arr.get(indexButton2);

        TextComponent tcMsg1 = new TextComponent(chatFactory.colorString(firstPart) + " ");
        TextComponent tcButton1 = new TextComponent(chatFactory.colorString(button) + " ");
        TextComponent tcBetweenMsg = new TextComponent(chatFactory.colorString(partAfterFirstButton) + " ");
        TextComponent tcButton2 = new TextComponent(chatFactory.colorString(button2));
        TextComponent tcLastMsg;

        if(button1Command != null) {
            tcButton1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, button1Command));
        }

        if(button2Command != null) {
            tcButton2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, button2Command));
        }

        if(button1HoverText != null){
            tcButton1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(button1HoverText).create()));
        }

        if(button2HoverText != null){
            tcButton2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(button2HoverText).create()));
        }

        tcMsg1.addExtra(tcButton1);
        tcMsg1.addExtra(tcBetweenMsg);
        tcMsg1.addExtra(tcButton2);

        if(partAfterSecondButton != null){
            tcLastMsg = new TextComponent(chatFactory.colorString(" " + partAfterSecondButton));
            tcMsg1.addExtra(tcLastMsg);
        }

        return tcMsg1;

    }

    public static boolean isAdminDescription(RegionDescription desc){
        return desc == RegionDescription.ADMIN_MARKET || desc == RegionDescription.SPAWN || desc == RegionDescription.PVP_ARENA || desc == RegionDescription.JAIL || desc == RegionDescription.TUTORIAL;
    }

}
