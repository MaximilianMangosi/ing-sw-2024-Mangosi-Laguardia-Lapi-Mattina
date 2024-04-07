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

/**
 * GameBox contains a Set for each type of card of the game: ResourceCard,GoldCard,StarterCard,Goal
 * and a Gson object used for adding cards to sets using jsons files
 */
public class GameBox {
    private final Gson gson;
    private final Set<ResourceCard> resourceCardSet = new HashSet<>();
    private final Set<GoldCard> goldCardSet = new HashSet<>();
    private final Set<StarterCard> starterCardSet = new HashSet<>();
    private final Set<Goal> goalSet = new HashSet<>();

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
     * @param jsons a list of String representing a ResourceCard in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addToResourceCardSet(List<String> jsons){
        for(String json :jsons){
            resourceCardSet.add(gson.fromJson(json,ResourceCard.class));
        }
    }
    /**
     * Add a list of GoldCard to goldCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a GoldCard in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addToGoldCardSet(List<String> jsons){
        for(String json :jsons){
            goldCardSet.add(gson.fromJson(json,GoldCard.class));
        }
    }
    /**
     * Add a list of StarterCard to starterCardSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a StarterCard in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addToStarterCardSet(List<String> jsons){
        for(String json :jsons){
            starterCardSet.add(gson.fromJson(json,StarterCard.class));
        }
    }
    /**
     * Add a list of Goal to goalSet, parsing a list of json
     * @author Giuseppe Laguardia
     * @param jsons a list of String representing a Goal in json format
     * @throws JsonSyntaxException if the string is badly formatted
     */
    public void addToGoalSet(List<String> jsons){
        for(String json :jsons){
            goalSet.add(gson.fromJson(json,Goal.class));
        }
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
