package it.polimi.ingsw.model.gamecards;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.GoldCardAngles;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.*;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;

import java.io.IOException;
import java.util.*;

/**
 * GameBox contains a Set for each type of card of the game: ResourceCard,GoldCard,StarterCard,Goal
 * and a Gson object used for adding cards to sets using jsons files
 */
public class GameBox {
    private final Gson gson;
    private final Set<ResourceCard> resourceCardSet = new LinkedHashSet<>();
    private final Set<GoldCard> goldCardSet = new LinkedHashSet<>();
    private final Set<StarterCard> starterCardSet = new LinkedHashSet<>();
    private final Set<Goal> goalSet = new LinkedHashSet<>();

    /**
     * resourceCardSet getter
     * @author GiuseppeLaguardia
     * @return a copy of resourceCardSet
     */
    public Set<ResourceCard> getResourceCardSet() {
        return new HashSet<>(resourceCardSet);
    }
    /**
     * goldCardSet getter
     * @author GiuseppeLaguardia
     * @return a copy of goldCardSet
     */
    public Set<GoldCard> getGoldCardSet() {
        return new HashSet<>(goldCardSet);
    }
    /**
     * starterCardSet getter
     * @author GiuseppeLaguardia
     * @return a copy of starterCardSet
     */
    public Set<StarterCard> getStarterCardSet() {
        return new HashSet<>(starterCardSet);
    }
    /**
     * GoalSet getter
     * @author GiuseppeLaguardia
     * @return a copy of goalSet
     */
    public Set<Goal> getGoalSet() {
        return new HashSet<>(goalSet);
    }

    /**
     * GameBox constructor
     * @author Giuseppe Laguardia
     */
    public GameBox() {
        GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Resource.class,new ResourceAdapter());
        gson=gsonBuilder.create();
    }

    /**
     * Add a list of ResourceCard to resourceCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a ResourceCard in json format, if the string is badly formatted the program exit
     */
    public void addToResourceCardSet(List<String> jsons){
        for(String json :jsons){
            try{
                resourceCardSet.add(gson.fromJson(json,ResourceCard.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file ResourceCard_%d.json",jsons.indexOf(json)+1);
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of GoldCard to goldCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a GoldCard in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addGoldCards(List<String> jsons){
        for(String json :jsons){
            try {
                goldCardSet.add(gson.fromJson(json,GoldCard.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file GoldCard_%d.json",jsons.indexOf(json)+1);
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of GoldCardAngle to goldCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a GoldCardAngle in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addGoldCardAngles(List<String> jsons){
        for(String json :jsons){
            try {
                goldCardSet.add(gson.fromJson(json,GoldCard.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file GoldCardAngles_%d.json",jsons.indexOf(json)+1);
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of GoldCardTool to goldCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a GoldCardTool in json format,if the string is badly formatted the program exit
     */
    public void addGoldCardTools(List<String> jsons){
        for(String json :jsons){
            try {
                goldCardSet.add(gson.fromJson(json,GoldCard.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file GoldCardTool_%d.json",jsons.indexOf(json)+1);
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of StarterCard to starterCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a StarterCard in json format,if the string is badly formatted the program exit
     */
    public void addToStarterCardSet(List<String> jsons){
        for(String json :jsons){
            try {
                starterCardSet.add(gson.fromJson(json,StarterCard.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file StarterCard_%d",jsons.indexOf(json));
                System.exit(0);
            }

        }

    }
    /**
     * Add a list of StairGoal to goalSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a StairGoal in json format,if the string is badly formatted the program exit
     */
    public void addStairGoals(List<String> jsons){
        for(String json :jsons){
            try {
                goalSet.add(gson.fromJson(json, StairGoal.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file StairGoal_%d",jsons.indexOf(json));
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of LGoal to goalSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a LGoal in json format,if the string is badly formatted the program exit
     */
    public void addLGoals(List<String> jsons){
        for(String json :jsons){
            try {
                goalSet.add(gson.fromJson(json, LGoal.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file LGoal_%d",jsons.indexOf(json));
                System.exit(0);
            }
        }
    }
    /**
     * Add a list of IdenticalGoal to goalSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a IdenticalGoal in json format,if the string is badly formatted the program exit
     */
    public void addIdenticalGoals(List<String> jsons){
        for(String json :jsons){
            try {
                goalSet.add(gson.fromJson(json, IdenticalGoal.class));
            }catch (JsonSyntaxException e){
                System.out.printf("Parsing error in file IdenticalGoal_%d",jsons.indexOf(json));
                System.exit(0);
            }
        }
    }
    /**
     * Add a DistinctGoal to goalSet
     * @author Giuseppe Laguardia
     */
    public void addDistinctGoals(){
        goalSet.add(new DistinctGoal(3));
    }

    private class ResourceAdapter extends TypeAdapter<Resource> {
        @Override
        public void write(JsonWriter jsonWriter, Resource resource) {
        }

        @Override
        public Resource read(JsonReader jsonReader) throws IOException {
            String val = jsonReader.nextString();
            return stringToResource(val);
        }

        private Resource stringToResource(String val) throws IOException {
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
}
