package com.character;

import java.util.ArrayList;
import java.util.Map;

public class NPC {

    static Map<String, Map<String, ArrayList<String>>> characters = Cast.CAST.getCast();
    ArrayList<String> dialogue;
    static int dialogueNum = 0;

    public NPC(String character) {
        if (characters.containsKey(character)) {
            dialogue = characters.get(character).get("dialogue");
        } else {
            dialogue = new ArrayList<>();
            dialogue.add("Are you talking to yourself? Could you be infected??");
            dialogue.add("Seriously, who are you talking to?");
            dialogue.add("There is no response. Is anyone even here?");
        }

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