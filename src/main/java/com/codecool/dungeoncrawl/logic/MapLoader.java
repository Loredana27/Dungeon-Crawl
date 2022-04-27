package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.actors.enemies.*;
import com.codecool.dungeoncrawl.logic.actors.items.*;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(InputStream mapFile) {
        InputStream is = mapFile;
        assert is != null;
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case '+':
                            cell.setType(CellType.TREE);
                            break;
                        case '*':
                            cell.setType(CellType.TREE);
                            Bear bear = new Bear(cell);
                            map.addAI(bear);
                            break;
                        case '$':
                            cell.setType(CellType.TREE);
                            HealPotion healPotion = new HealPotion(cell);
                            map.addItem(healPotion.getTileName());
                            break;
                        case 'h':
                            cell.setType(CellType.HOUSE);
                            break;
                        case 'f':
                            cell.setType(CellType.GRASS);
                            Farmer farmer = new Farmer(cell);
                            map.addAI(farmer);
                            break;
                        case '-':
                            cell.setType(CellType.GRASS);
                            break;
                        case '!':
                            cell.setType(CellType.FLOOR);
                            Soldier soldier = new Soldier(cell);
                            map.addAI(soldier);
                            break;
                        case 'a':
                            cell.setType(CellType.FLOOR);
                            Treasure treasury = new Treasure(cell);
                            map.addItem(treasury.getTileName());
                            break;
                        case 'A':
                            cell.setType(CellType.FLOOR);
                            TreasureKey treasureKey = new TreasureKey(cell);
                            map.addItem(treasureKey.getTileName());
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            Skeleton skeleton = new Skeleton(cell);
                            map.addAI(skeleton);
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            Bat bat = new Bat(cell);
                            map.addAI(bat);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case '1':
                            cell.setType(CellType.FLOOR);
                            Sword sword = new Sword(cell);
                            map.addItem(sword.getTileName());
                            break;
                        case 'd':
                            cell.setType(CellType.FLOOR);
                            Door door = new Door(cell);
                            map.setDoor(door);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            Key key = new Key(cell);
                            map.addItem(key.getTileName());
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
