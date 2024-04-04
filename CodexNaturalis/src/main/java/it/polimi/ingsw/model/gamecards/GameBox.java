package it.polimi.ingsw.model.gamecards;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameBox {
    private final Set<ResourceCard> resourceCardSet = new HashSet<>();
    private final Set<GoldCard> goldCardSet = new HashSet<>();
    private final Set<StarterCard> starterCardSet = new HashSet<>();
    private final Set<Goal> goalSet = new HashSet<>();

    public Set<ResourceCard> getResourceCardSet() {
        return new HashSet<>(resourceCardSet);
    }

    public Set<GoldCard> getGoldCardSet() {
        return new HashSet<>(goldCardSet);
    }

    public Set<StarterCard> getStarterCardSet() {
        return new HashSet<>(starterCardSet);
    }

    public Set<Goal> getGoalSet() {
        return new HashSet<>(goalSet);
    }

    public GameBox(List<String> jsonFiles) {
        // WIP

    }
    private class ResourceAdapter extends TypeAdapter<Resource> {
        @Override
        public void write(JsonWriter jsonWriter, Resource resource) throws IOException {
        }

        @Override
        public Resource read(JsonReader jsonReader) throws IOException {
            String val = jsonReader.nextString();
            return stringToResource(val);
        }

        private Resource stringToResource(String val) throws IOException {
            Resource r;
            if (val.equals("a")) {
                return Reign.ANIMAL;
            }
            if (val.equals("b")) {
                return Reign.BUG;
            }
            if (val.equals("pl")) {
                return Reign.PLANTS;
            }
            if (val.equals("m")) {
                return Reign.MUSHROOM;
            }
            if (val.equals("s")) {
                return Tool.SCROLL;
            }
            if (val.equals("p")) {
                return Tool.PHIAL;
            }
            if (val.equals("f")) {
                return Tool.FEATHER;
            }
            if (val.equals("e"))
                return Reign.EMPTY;
            if (val.equals("null"))
                return null;
            throw new IOException();
        }
    }
    public static void main(String[] arg) throws FileNotFoundException {

        List<String> jsons= new ArrayList<>();
        File f=new File("C:\\Users\\glagu\\Desktop\\università\\PROVA FINALE (INGEGNERIA DEL SOFTWARE)\\json\\ResourceCard_template.json");
        Scanner s= new Scanner(f);

        StringBuilder json= new StringBuilder();
        while(s.hasNext()){
            json.append(s.nextLine());
        }

        jsons.add(json.toString());
        f=new File("C:\\Users\\glagu\\Desktop\\università\\PROVA FINALE (INGEGNERIA DEL SOFTWARE)\\json\\GoldCard_template.json");
        s= new Scanner(f);
        json= new StringBuilder();
        while(s.hasNext()){
            json.append(s.nextLine());
        }
        jsons.add(json.toString());
        f=new File("C:\\Users\\glagu\\Desktop\\università\\PROVA FINALE (INGEGNERIA DEL SOFTWARE)\\json\\GoldCardTool_template.json");
        s= new Scanner(f);
        json= new StringBuilder();
        while(s.hasNext()){
            json.append(s.nextLine());
        }
        jsons.add(json.toString());
        f=new File("C:\\Users\\glagu\\Desktop\\università\\PROVA FINALE (INGEGNERIA DEL SOFTWARE)\\json\\GoldCardAngles_template.json");
        s= new Scanner(f);
        json= new StringBuilder();
        while(s.hasNext()){
            json.append(s.nextLine());
        }
        jsons.add(json.toString());
        f=new File("C:\\Users\\glagu\\Desktop\\università\\PROVA FINALE (INGEGNERIA DEL SOFTWARE)\\json\\StarterCard_template.json");
        s= new Scanner(f);
        json= new StringBuilder();
        while(s.hasNext()){
            json.append(s.nextLine());
        }
        jsons.add(json.toString());


        GameBox gb=new GameBox(jsons);
        /*
        for(GoldCard card: gb.getGoldCardSet()){
            System.out.println(card.getCardResources());
            System.out.println(card.getPoints());
            System.out.println(card.getReign());
            System.out.println(card.IsFront());
            System.out.println(card.getRequirements());
            System.out.println(card.getTool());
          }
        */
        for(StarterCard card: gb.getStarterCardSet()){
            System.out.println(card.getCardResources());
            System.out.println(card.IsFront());
            System.out.println(card.getResourceBack("NW"));
            System.out.println(card.getCentralResource());
        }

    }
}
