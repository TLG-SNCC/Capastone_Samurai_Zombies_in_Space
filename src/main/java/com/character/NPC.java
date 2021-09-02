package com.character;

import java.util.ArrayList;
import java.util.Map;

public class NPC {

    private static Map<String, Map<String, ArrayList<String>>> characters = Cast.CAST.getCast();
    private ArrayList<String> dialogue;
    private String description = "Can't really tell what you're seeing. Are you losing it?";
    private static int dialogueNum = 0;

    public NPC(String character) {
        if (characters.containsKey(character)) {
            dialogue = characters.get(character).get("dialogue");
            System.out.println(characters.get(character));
            description = characters.get(character).get("description").get(0);
        } else {
            dialogue = new ArrayList<>();
            dialogue.add("Are you talking to yourself? Could you be infected??");
            dialogue.add("Seriously, who are you talking to?");
            dialogue.add("There is no response. Is anyone even here?");
        }

    }

    public static boolean checkCast(String potentialNPC) {
        return characters.containsKey(potentialNPC);
    }

    public String getDescription() {
        return description;
    }

    public String getDialogue() {
        String speech;
        speech = dialogue.get(getDialogueNum() % dialogue.size());
        incrementDialogueNum();
        return speech + "\n";
    }

    private void incrementDialogueNum() {
        dialogueNum += 1;
    }

    private int getDialogueNum() {
        return dialogueNum;
    }
}