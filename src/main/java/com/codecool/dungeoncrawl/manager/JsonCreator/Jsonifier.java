package com.codecool.dungeoncrawl.manager.JsonCreator;

import com.codecool.dungeoncrawl.manager.DAOs.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;


public class Jsonifier {

    private String file;

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

    private void saveEnemies(){}
}

