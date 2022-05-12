package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.enemies.*;
import com.codecool.dungeoncrawl.logic.actors.items.*;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActorTest {
    GameMap gameMap = new GameMap(3,3, CellType.FLOOR);
    Player player = new Player(gameMap.getCell(1, 1));

    @Test
    @Order(1)
    void move_up_playerMoved(){
        player.move(0,-1);
        assertEquals(gameMap.getCell(1,0), player.getCell());
    }

    @Test@Order(2)
    void move_down_playerMoved(){
        player.move(0,1);
        assertEquals(gameMap.getCell(1,2), player.getCell());
    }

    @Test@Order(3)
    void move_left_playerMoved(){
        player.move(-1, 0);
        assertEquals(gameMap.getCell(0,1),player.getCell());
    }

    @Test@Order(4)
    void  move_right_playerMoved(){
        player.move(1,0);
        assertEquals(gameMap.getCell(2,1), player.getCell());
    }

    @Test@Order(5)
    void move_up_normalPlayerNotMoved(){
        gameMap.getCell(1,0).setType(CellType.WALL);
        player.move(0,-1);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(6)
    void move_down_normalPlayerNotMoved(){
        gameMap.getCell(1,2).setType(CellType.WALL);
        player.move(0,1);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(7)
    void move_left_normalPlayerNotMoved(){
        gameMap.getCell(0,1).setType(CellType.WALL);
        player.move(-1, 0);
        assertEquals(gameMap.getCell(1,1),player.getCell());
    }

    @Test@Order(8)
    void  move_right_normalPlayerNotMoved(){
        gameMap.getCell(2,1).setType(CellType.WALL);
        player.move(1,0);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }
    @Test@Order(9)
    void move_up_devPlayerMoved(){
        gameMap.getCell(1,0).setType(CellType.WALL);
        player.setName("dev");
        player.move(0,-1);
        assertEquals(gameMap.getCell(1,0), player.getCell());
    }

    @Test@Order(10)
    void move_down_devPlayerMoved(){
        gameMap.getCell(1,2).setType(CellType.WALL);
        player.setName("dev");
        player.move(0,1);
        assertEquals(gameMap.getCell(1,2), player.getCell());
    }

    @Test@Order(11)
    void move_left_devPlayerMoved(){
        gameMap.getCell(0,1).setType(CellType.WALL);
        player.setName("dev");
        player.move(-1, 0);
        assertEquals(gameMap.getCell(0,1),player.getCell());
    }

    @Test@Order(12)
    void  move_right_devPlayerMoved(){
        gameMap.getCell(2,1).setType(CellType.WALL);
        player.setName("dev");
        player.move(1,0);
        assertEquals(gameMap.getCell(2,1), player.getCell());
    }

    @Test@Order(13)
    void move_heal_playerMoved(){
        HealPotion healPotion = new HealPotion(gameMap.getCell(1,0));
        player.move(0,-1);
        assertEquals(healPotion.getTileName(), gameMap.getCell(1,0).getTempItem());
        assertEquals(gameMap.getCell(1,0), player.getCell());

    }

    @Test@Order(14)
    void move_healPickUp_playerHealthIncreased(){
        player.getCell().setTempItem(new HealPotion(player.getCell()));
        int health = player.health;
        player.pickupItem();
        assertTrue(health< player.health);

    }

    @Test@Order(15)
    void move_sword_playerMoved(){
        Sword sword = new Sword(gameMap.getCell(1,0));
        player.move(0,-1);
        assertEquals(sword.getTileName(), gameMap.getCell(1,0).getTempItem());
        assertEquals(gameMap.getCell(1,0), player.getCell());
    }

    @Test@Order(16)
    void move_swordPickUp_playerAttackIncreased(){
        player.getCell().setTempItem(new Sword(player.getCell()));
        int attack = player.attack;
        player.pickupItem();
        assertTrue(attack< player.attack);
        assertTrue(player.getItems().containsKey("sword"));
    }

    @Test@Order(17)
    void move_treasureKeyPickUp_playerAttackIncreased(){
        player.getCell().setTempItem(new TreasureKey(gameMap.getCell(1,0)));
        player.move(0,-1);
        assertEquals(gameMap.getCell(1,0), player.getCell());
        player.pickupItem();
        assertTrue(player.getItems().containsKey("treasure key"));
    }

    @Test@Order(18)
    void move_treasure_playerCanMoveJustWithAKey(){
        player.getCell().setTempItem(new Treasure(gameMap.getCell(1,0)));
        player.move(0,-1);
        System.out.println(player.getX());
        assertEquals(gameMap.getCell(1,1), player.getCell());
        player.getCell().setTempItem(new TreasureKey(player.getCell()));
        player.pickupItem();
        player.move(0,-1);
        assertEquals(gameMap.getCell(1,0), player.getCell());
        assertEquals("treasure", player.getCell().getTempItem());
    }


    @Test@Order(19)
    void move_treasurePickUp_playerAttackAndHealthIncreased(){
        player.getCell().setTempItem(new TreasureKey(player.getCell()));
        player.pickupItem();
        player.getCell().setTempItem(new Treasure(player.getCell()));
        int attack = player.attack;
        int health = player.health;
        player.pickupItem();
        assertTrue(attack< player.attack);
        assertTrue(health< player.health);
        assertTrue(player.getItems().containsKey("bigsword"));
        assertFalse(player.getItems().containsKey("treasure key"));
    }

    @Test@Order(20)
    void move_keyPickUp_playerItemsContainsKey(){
        player.getCell().setTempItem(new Key(player.getCell()));
        player.pickupItem();
        assertTrue(player.getItems().containsKey("key"));
    }


    @Test@Order(21)
    void addItem_item_addOnlyOneItem(){
        assertFalse(player.getItems().containsKey("sword"));
        player.addItem("sword");
        assertEquals(1, player.getItems().get("sword"));
        player.addItem("sword");
        assertEquals(2, player.getItems().get("sword"));
    }

    @Test@Order(22)
    void removeItem_item_removeOnlyOneItem(){
        player.addItem("sword");
        player.addItem("sword");
        assertEquals(2, player.getItems().get("sword"));
        player.removeItem("sword");
        assertEquals(1, player.getItems().get("sword"));
        player.removeItem("sword");
        assertFalse(player.getItems().containsKey("sword"));
    }

    @Test@Order(23)
    void move_skeleton_playerNotMovedAndHPDecreased(){
        Skeleton skeleton = new Skeleton(gameMap.getCell(0,0));
        int health = player.health;
        player.move(-1,-1);
        assertTrue(health> player.health);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(24)
    void move_soldier_playerNotMovedAndHPDecreased(){
        Soldier soldier = new Soldier(gameMap.getCell(0,0));
        int health = player.health;
        player.move(-1,-1);
        assertTrue(health> player.health);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(25)
    void move_bat_playerNotMovedAndHPDecreased(){
        Bat bat = new Bat(gameMap.getCell(0,0));
        int health = player.health;
        player.move(-1,-1);
        assertTrue(health> player.health);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(26)
    void move_bear_playerNotMovedAndHPDecreased(){
        Bear bear = new Bear(gameMap.getCell(0,0));
        int health = player.health;
        player.move(-1,-1);
        assertTrue(health> player.health);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }

    @Test@Order(27)
    void move_farmer_playerNotMovedAndHPDoesNotChange(){
        Farmer farmer = new Farmer(gameMap.getCell(0,0));
        int health = player.health;
        player.move(-1,-1);
        assertEquals(health, player.health);
        assertEquals(gameMap.getCell(1,1), player.getCell());
    }
}