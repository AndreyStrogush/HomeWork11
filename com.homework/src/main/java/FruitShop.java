import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FruitShop {
    private static final Logger LOGGER = Logger.getLogger(FruitShop.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private List<Fruit> fruitsData = new ArrayList<>();

    public FruitShop() {
        fruitsData = new ArrayList<>();
    }

    public void addFruits(String pathToJsonFile) {
        List<Fruit> delivery;
        try (FileInputStream inputStream = new FileInputStream(pathToJsonFile)) {
            delivery = mapper.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
            fruitsData.addAll(delivery);
        } catch (IOException e) {
            LOGGER.error("Exeption: " + e.getMessage());
        }
    }

    public void save(String pathToJsonFile) {
        try (FileOutputStream outputStream = new FileOutputStream(pathToJsonFile)) {
            mapper.writeValue(outputStream, fruitsData);
        } catch (IOException e) {
            LOGGER.error("Exeption: " + e.getMessage());
        }
    }

    public void load(String pathToJsonFile) {
        fruitsData.clear();
        try (FileInputStream inputStream = new FileInputStream(pathToJsonFile)) {
            fruitsData = mapper.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Exeption: " + e.getMessage());
        }
    }

    public List<Fruit> getSpoiledFruits(LocalDate date) {
        return filterByDate(date, true);
    }

    public List<Fruit> getSpoiledFruits(LocalDate date, TypeOfFruits type) {
        return filterByType(type, getSpoiledFruits(date));
    }

    public List<Fruit> getAvailableFruits(LocalDate date) {
        return filterByDate(date, false);
    }

    public List<Fruit> getAvailableFruits(LocalDate date, TypeOfFruits type) {
        return filterByType(type, getAvailableFruits(date));
    }

    public List<Fruit> getAddedFruits(LocalDate date) {
        List<Fruit> listOfAdedFruits = new ArrayList<>();
        fruitsData.forEach(fruit -> {
            if (fruit.getDateOfDelivery().isEqual(date)) {
                listOfAdedFruits.add(fruit);
            }
        });
        return listOfAdedFruits;
    }

    public List<Fruit> getAddedFruits(LocalDate date, TypeOfFruits type) {
        return filterByType(type, getAddedFruits(date));
    }

    private List<Fruit> filterByDate(LocalDate targetDate, boolean spoiled) {
        List<Fruit> listOfFruits = new ArrayList<>();
        fruitsData.forEach(fruit -> {
            LocalDate dateOfSpoil = fruit.getDateOfSpoiling();
            if (spoiled) {
                if (targetDate.isAfter(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            } else {
                if (targetDate.isBefore(dateOfSpoil) || targetDate.isEqual(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            }
        });
        return listOfFruits;
    }

    private List<Fruit> filterByType(TypeOfFruits type, List<Fruit> list) {
        List<Fruit> listOfFruits =
                list.stream().filter(fruit -> fruit.getType()
                        .equals(type)).collect(Collectors.toList());
        return listOfFruits;
    }

    public List<Fruit> getFruitsData() {
        return fruitsData;
    }
}
