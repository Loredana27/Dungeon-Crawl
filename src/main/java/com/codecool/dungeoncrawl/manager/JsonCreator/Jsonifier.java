package com.codecool.dungeoncrawl.manager.JsonCreator;

import com.codecool.dungeoncrawl.manager.DAOs.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Jsonifier {

    private final String file;

    public Jsonifier(String fileName) throws IOException {
        this.file = fileName;
    }

    public void saveGame(JsonGameDAO gameDAO){
        System.out.println(gameDAO);
        String path = ".\\saves\\" + file + ".json";
        try(PrintWriter out = new PrintWriter(new FileWriter(path))){
            String jsonString = gameDAO.toJson();
            System.out.println(jsonString);
            out.write(jsonString);
        } catch (IOException e) {
            try {
                Files.createDirectory(Path.of(".\\saves"));
                saveGame(gameDAO);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public GameDAO loadGame(){
        JSONParser jsonParser = new JSONParser();
        String path = ".\\saves\\" + file + ".json";
        try(FileReader in = new FileReader(path)){
            HashMap object = (HashMap) jsonParser.parse(in);
            System.out.println(object);
//            Date saveDate = new Date((Long) object.get("saveDate"));
            String name = (String) object.get("name");
            System.out.println("name:  " + name);
            int actualMap =  Integer.parseInt((String) object.get("actualMap"));
            List<HashMap> availableItems = (List<HashMap>) object.get("availableItems");
            System.out.println("availableItems:  " + availableItems);
            List<HashMap> items = (List<HashMap>) object.get("items");
            System.out.println("items:  " + items);
            List<HashMap> enemies = (List<HashMap>) object.get("enemies");
            System.out.println("enemies:  " + enemies);

            ArrayList<AvailableItemDAO> availableItemDAOS = new ArrayList<>();
            availableItems.forEach(e -> {
                System.out.println(e);
                        availableItemDAOS.add(
                                new AvailableItemDAO(
                                        (String) e.get("type"),
                                        Integer.parseInt(String.valueOf(e.get("posX"))),
                                        Integer.parseInt(String.valueOf(e.get("posY")))
                                )
                        );
                    }
            );

            ArrayList<ItemDAO> itemDAOS = new ArrayList<>();
            items.forEach(e ->
                itemDAOS.add(new ItemDAO((String) e.get("type")))
            );

            ArrayList<EnemyDAO> enemyDAOS = new ArrayList<>();
            enemies.forEach(e->
                    enemyDAOS.add(
                            new EnemyDAO(
                            (String) e.get("type"),
                            Integer.parseInt(String.valueOf(e.get("posX"))),
                            Integer.parseInt(String.valueOf(e.get("posY")))
                            )
                    )
            );

            HashMap player = (HashMap) object.get("player");
            PlayerDAO playerDAO = new PlayerDAO(
                    (String) player.get("name"),
                    Integer.parseInt(String.valueOf(player.get("posX"))),
                    Integer.parseInt(String.valueOf(player.get("posY")))
            );
            return new GameDAO(name,actualMap,playerDAO,enemyDAOS,itemDAOS,availableItemDAOS);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

