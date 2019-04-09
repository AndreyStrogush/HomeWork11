import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FruitShopTest {
    private static final String PATH = "firstFile.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String SHOP = "shop.json";
    private static final Logger LOGGER = Logger.getLogger(FruitShopTest.class);

    private List<Fruit> fruits;
    private FruitShop shop;

    @Before
    public void init() {
        shop = new FruitShop();
        fruits = new ArrayList<>();
        Fruit apple = new Fruit(TypeOfFruits.APPLE, 10, LocalDate.of(2019, 01, 04), 20);
        Fruit banana = new Fruit(TypeOfFruits.BANANA, 30, LocalDate.of(2019, 04, 07), 30);
        Fruit avocado = new Fruit(TypeOfFruits.AVOCADO, 20, LocalDate.of(2019, 03, 30), 25);
        fruits.add(apple);
        fruits.add(banana);
        fruits.add(avocado);

        try (FileOutputStream outputStream = new FileOutputStream(PATH)) {
            MAPPER.writeValue(outputStream, fruits);
        } catch (IOException e) {
            LOGGER.error("Exeption: " + e.getMessage());
        }
    }

    @After
    public void clear() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(PATH);
        } catch (FileNotFoundException e) {
            LOGGER.error("Exception: " + e.getMessage());
        }
        writer.print("");
        try {
            writer = new PrintWriter(SHOP);
        } catch (FileNotFoundException e) {
            LOGGER.error("Exception: " + e.getMessage());
        }
        writer.print("");
        writer.close();
    }

    @Test
    public void testAddFruits() {
        shop.addFruits(PATH);
        assertTrue(shop.getFruitsData().containsAll(fruits));
    }

    @Test
    public void testSave() {
        shop.addFruits(PATH);
        shop.save(SHOP);
        List<Fruit> expected = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(SHOP)) {
            expected = MAPPER.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Exeption : " + e.getMessage());
        }
        assertTrue(fruits.containsAll(expected));
    }

    @Test
    public void testLoad() {
        shop.addFruits(PATH);
        try (FileOutputStream outputStream = new FileOutputStream(SHOP)) {
            MAPPER.writeValue(outputStream, shop.getFruitsData());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        shop.load(SHOP);
        assertTrue(fruits.containsAll(shop.getFruitsData()));
    }

    @Test
    public void testGetSpoiledFruits() {
        shop.addFruits(PATH);
        LocalDate localDate = LocalDate.of(2019, 04, 06);
        List<Fruit> spoiledFruits = shop.getSpoiledFruits(localDate);
        assertTrue(spoiledFruits.size() == 1);
    }

    @Test
    public void testGetSpoiledFruitsWithChosenType() {
        shop.addFruits(PATH);
        LocalDate date = LocalDate.of(2019, 4, 9);
        List<Fruit> spoiled = shop.getSpoiledFruits(date, TypeOfFruits.APPLE);
        assertTrue(spoiled.contains(fruits.get(0)));
    }

    @Test
    public void testGetAvaileableFruits() {
        shop.addFruits(PATH);
        List<Fruit> availableFruits = shop.getAddedFruits(LocalDate.of(2019, 05, 01));
        assertTrue(availableFruits.size() == 1);
    }

    @Test
    public void testGetAvaileableFruitsWithChosenType() {
        shop.addFruits(PATH);
        LocalDate date = LocalDate.of(2019, 4, 7);
        List<Fruit> added = shop.getAddedFruits(date, TypeOfFruits.BANANA);
        assertEquals(added.get(0), fruits.get(1));
    }
}
