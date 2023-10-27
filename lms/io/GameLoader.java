package lms.io;

import lms.exceptions.FileFormatException;
import lms.exceptions.UnsupportedActionException;
import lms.grid.Coordinate;
import lms.grid.GameGrid;
import lms.grid.GridComponent;
import lms.logistics.Item;
import lms.logistics.Path;
import lms.logistics.Transport;
import lms.logistics.belts.Belt;
import lms.logistics.container.Container;
import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;
//NullPointerException
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Array;
import java.util.*;

/**
 * This class is responsible for loading (reading and parsing)
 * a text file containing details required for the creation of a simulated factory
 * represented in the form of a graphical hexagonal grid.
 * The factory consists of hexagonal nodes (as seen in a beehive)
 * which are linked together to form a complete and symmetrical grid. Each node within this grid provides a depiction of one or more simulated production line(s) nodes. A production line consists of one Producer, one or more Receiver(s) and numerous connected nodes, called Belts.
 * The Producer nodes produce Items while the Receiver Nodes consume them.
 * In between each pair (or more) of a Producer and Receiver, are conveyor belt nodes.
 * Each belt node transports the Items produced by the Producer
 * towards the direction of the connected Receiver(s). Each production line, can have one or more Producer,
 * and one or more Receiver.
 */
public class GameLoader {
    /**
     * GameLoader constructor
     */
    public GameLoader() {
    }

    /**
     * Divides the whole input to sections.
     * Handles error about incorrect input.
     * Pass divided input into loadMap to make grid according to the input.
     *
     * @param reader input File.
     * @return Completed grid with input information.
     * @throws IOException         Invalid input.
     * @throws FileFormatException Input has incorrect format.
     */
    public static GameGrid load(Reader reader) throws IOException, FileFormatException {

        if (reader == null) {
            throw new NullPointerException();
        }
        BufferedReader filereader = new BufferedReader(reader);

        String rangeStr = filereader.readLine();
        if (!rangeStr.matches("\\d+") && Integer.parseInt(rangeStr) > 0) {
            String error = "Range needs to be positive number";
            throw new FileFormatException(error, 47, new IOException());
        }
        int range = Integer.parseInt(rangeStr);
        checkUnderScore(filereader.readLine());

        String producerStr = filereader.readLine();
        if (!producerStr.matches("\\d+") && Integer.parseInt(producerStr) > 0) {
            String error = "The number of producers needs to be positive number";
            throw new FileFormatException(error, 56, new IOException());
        }
        int producer = Integer.parseInt(producerStr);
        String receiverStr = filereader.readLine();
        if (!receiverStr.matches("\\d+") && Integer.parseInt(receiverStr) > 0) {
            String error = "The number of receivers needs to be positive number";
            throw new FileFormatException(error, 62, new IOException());
        }
        checkUnderScore(filereader.readLine());

        ArrayList<String> producerList = new ArrayList<>();
        for (int i = 0; i < producer; i++) {
            String producerName = filereader.readLine();
            producerList.add(producerName);
        }
        checkUnderScore(filereader.readLine());
        int receiver = Integer.parseInt(receiverStr);
        ArrayList<String> receiverList = new ArrayList<>();
        for (int i = 0; i < receiver; i++) {
            String receiverName = filereader.readLine();
            receiverList.add(receiverName);
        }
        checkUnderScore(filereader.readLine());

        ArrayList<String> mapLayout = new ArrayList<>();
        int rowline = range + 1;
        boolean switchBool = true;
        for (int i = 0; i < range * 2 + 1; i++) {
            String mapLine = filereader.readLine();
            String[] parts = mapLine.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String part : parts) {
                sb.append(part);
            }
            String extractedString = sb.toString();
            String[] splitStr = extractedString.split("");

            if (splitStr.length != rowline) {
                String error = "Grid input is incorrect";
                throw new FileFormatException(error, 78, new IOException());
            }
            mapLayout.add(mapLine);
            if (switchBool) {
                rowline++;
            } else {
                rowline--;
            }
            if (rowline == range * 2 + 1) {
                switchBool = false;
            }
        }
        checkUnderScore(filereader.readLine());

        String line = "";
        ArrayList<String> mapPath = new ArrayList<>();
        while ((line = filereader.readLine()) != null) {
            mapPath.add(line);
        }
        GameGrid gameGrid = new GameGrid(range);
        loadMap(gameGrid, mapLayout, producerList, receiverList, mapPath);
        return gameGrid;
    }

    /**
     * Check if the String starts with 5 underscores.
     *
     * @param s input String to check if the division between sections is correct.
     * @throws FileFormatException Throws FileFormatException when input String does not start with 5 underscores.
     */
    private static void checkUnderScore(String s) throws FileFormatException {
        if (!s.startsWith("_____")) {
            String error = "Underscores should be used to divide lines";
            throw new FileFormatException(error, 105, new IOException());
        }
    }

    /**
     * Adding components with the information provided in the input file.
     *
     * @param gameGrid     Grid made according to the input range.
     * @param mapList      Map of grid in List format.
     * @param producerList List of producer item key.
     * @param receiverList List of receiver item key.
     * @param mapPath      List of paths of the grid.
     * @throws FileFormatException if the number of Producers/Receivers does not match with the grid map provided.
     *                             if the grid map provided contains invalid alphabet.
     *                             if the given path contains non-number character.
     */
    private static void loadMap(GameGrid gameGrid, ArrayList<String> mapList,
                                ArrayList<String> producerList, ArrayList<String> receiverList,
                                ArrayList<String> mapPath) throws FileFormatException {

        int idIndex = 1;

        ArrayList<ArrayList<String[]>> array = new ArrayList<>();
        ArrayList<Integer> receivers = new ArrayList<>();
        ArrayList<Integer> producers = new ArrayList<>();
        ArrayList<Integer> belts = new ArrayList<>();

        Map<Integer, Receiver> receiverMap = new HashMap<>();
        Map<Integer, Producer> producerMap = new HashMap<>();
        Map<Integer, Belt> beltMap = new HashMap<>();

        for (String s : mapList) {
            ArrayList<String[]> arrayItems = new ArrayList<>();

            int producerCount = 0;
            int receiverCount = 0;

            String[] stringArr = s.split(" ");
            for (String ss : stringArr) {
                String[] arrayItem = new String[3];
                if (ss.equals("p") || ss.equals("b") || ss.equals("r")) {
                    arrayItem[0] = ss;
                    arrayItem[1] = idIndex + "";

                    switch (ss) {
                        case "p" -> {
                            if (producerList.size() - 1 < producerCount) {
                                String error = "The number of producers is incorrect";
                                throw new FileFormatException(error, 158, new IOException());
                            }
                            producers.add(idIndex);
                            arrayItem[2] = producerList.get(producerCount);
                            producerCount++;
                        }
                        case "r" -> {
                            if (receiverList.size() - 1 < receiverCount) {
                                String error = "The number of receivers is incorrect";
                                throw new FileFormatException(error, 167, new IOException());
                            }
                            receivers.add(idIndex);
                            arrayItem[2] = receiverList.get(receiverCount);
                            receiverCount++;
                        }
                        case "b" -> belts.add(idIndex);
                    }
                    idIndex++;
                    arrayItems.add(arrayItem);

                } else if (ss.equals("w") || ss.equals("o")) {
                    arrayItem[0] = ss;
                    arrayItems.add(arrayItem);
                } else if (!ss.equals("")) {
                    String error = "Invalid input is in the grip map";
                    throw new FileFormatException(error, 184, new IOException());
                }
            }
            array.add(arrayItems);
        }

        if (producerList.size() != producers.size()) {
            String error = "The number of producers does not match with the grid";
            throw new FileFormatException(error, 197, new IOException());
        }
        if (receiverList.size() != receivers.size()) {
            String error = "The number of receivers does not match with the grid";
            throw new FileFormatException(error, 201, new IOException());
        }
        int range = gameGrid.getRange();

        String startItem = array.get(range).get(range)[0];

        Coordinate startCoordinate = new Coordinate(0, 0, 0);
        if (startItem.equals("p")) {
            int startId = Integer.parseInt(array.get(range).get(range)[1]);
            String startName = array.get(range).get(range)[2];
            Producer start = new Producer(startId, new Item(startName));
            gameGrid.setCoordinate(startCoordinate, start);
            producerMap.put(startId, start);
        }
        if (startItem.equals("r")) {
            int startId = Integer.parseInt(array.get(range).get(range)[1]);

            String startName = array.get(range).get(range)[2];
            Receiver start = new Receiver(startId, new Item(startName));
            gameGrid.setCoordinate(startCoordinate, start);
            receiverMap.put(startId, start);
        }
        if (startItem.equals("b")) {
            int startId = Integer.parseInt(array.get(range).get(range)[1]);

            Belt start = new Belt(startId);
            gameGrid.setCoordinate(startCoordinate, start);
            beltMap.put(startId, start);
        } else {
            gameGrid.setCoordinate(startCoordinate, () -> startItem);
        }
        boolean check = true;
        Coordinate checkCoordinate;
        while (check) {
            checkCoordinate = startCoordinate.getTopLeft();
            check = gameGrid.getGrid().containsKey(checkCoordinate);
            if (check) {
                startCoordinate = checkCoordinate;
            }
        }

        for (ArrayList<String[]> line : array) {
            Coordinate getBottomLeft = startCoordinate.getBottomLeft();
            Coordinate getBottomRight = startCoordinate.getBottomRight();

            boolean checkgetBottomRight = gameGrid.getGrid().containsKey(getBottomRight);
            boolean checkgetBottomLeft = gameGrid.getGrid().containsKey(getBottomLeft);

            for (String[] item : line) {
                String type = item[0];
                if (type.equals("r")) {
                    int id = Integer.parseInt(item[1]);
                    String name = item[2];
                    Receiver receiver = new Receiver(id, new Item(name));
                    gameGrid.setCoordinate(startCoordinate, receiver);
                    receiverMap.put(id, receiver);
                }
                if (type.equals("p")) {
                    int id = Integer.parseInt(item[1]);
                    String name = item[2];
                    Producer producer = new Producer(id, new Item(name));
                    gameGrid.setCoordinate(startCoordinate, producer);
                    producerMap.put(id, producer);
                }
                if (type.equals("b")) {
                    int id = Integer.parseInt(item[1]);
                    Belt belt = new Belt(id);
                    gameGrid.setCoordinate(startCoordinate, belt);
                    beltMap.put(id, belt);
                }
                if (type.equals("w") || type.equals("o")) {
                    gameGrid.setCoordinate(startCoordinate, () -> type);
                }
                startCoordinate = startCoordinate.getRight();
            }
            if (checkgetBottomLeft) {
                startCoordinate = getBottomLeft;
            } else if (checkgetBottomRight) {
                startCoordinate = getBottomRight;
            }
        }
        for (String s : mapPath) {
            String[] splitResult = s.split("-");
            String inputA = splitResult[0];
            String inputB = splitResult[1];
            if (!inputA.matches("\\d+")) {
                String error = "Path contains non-number character";
                throw new FileFormatException(error, 285, new IOException());
            }
            int inputAtoInt = Integer.parseInt(inputA);
            if (receiverMap.containsKey(inputAtoInt)) {
                Receiver receiver = receiverMap.get(inputAtoInt);
                Path receiverPath = receiver.getPath();
                if (!inputB.matches("\\d+")) {
                    String error = "Path contains non-number character";
                    throw new FileFormatException(error, 293, new IOException());
                }
                int inputBtoInt = Integer.parseInt(inputB);
                if (beltMap.containsKey(inputBtoInt)) {
                    Belt belt = beltMap.get(inputBtoInt);
                    Path input = belt.getPath();
                    receiverPath.setPrevious(input);
                    input.setNext(receiverPath);
                }
                if (producerMap.containsKey(inputBtoInt)) {
                    Producer producer = producerMap.get(inputBtoInt);
                    Path input = producer.getPath();
                    receiverPath.setPrevious(input);
                    input.setNext(receiverPath);
                }
            } else if (producerMap.containsKey(inputAtoInt)) {
                Producer producer = producerMap.get(inputAtoInt);
                Path producerPath = producer.getPath();
                if (!inputB.matches("\\d+")) {
                    String error = "Path contains non-number character";
                    throw new FileFormatException(error, 311, new IOException());
                }
                int inputBtoInt = Integer.parseInt(inputB);
                if (beltMap.containsKey(inputBtoInt)) {
                    Belt belt = beltMap.get(inputBtoInt);
                    Path output = belt.getPath();
                    producerPath.setNext(output);
                    output.setPrevious(producerPath);
                }
                if (receiverMap.containsKey(inputBtoInt)) {
                    Receiver receiver = receiverMap.get(inputBtoInt);
                    Path output = receiver.getPath();
                    producerPath.setNext(output);
                    output.setPrevious(producerPath);
                }
            } else {
                Belt belt = beltMap.get(inputAtoInt);
                Path beltPath = belt.getPath();
                if (inputB.charAt(0) == ',') {
                    String[] splitB = inputB.split(",");
                    for (String ss : splitB) {
                        if (!ss.equals("")) {
                            int sstoInt = Integer.parseInt(ss);
                            if (producerMap.containsKey(sstoInt)) {
                                Path path = producerMap.get(sstoInt).getPath();
                                beltPath.setNext(path);
                                path.setPrevious(beltPath);
                                beltPath = path;
                            } else if (receiverMap.containsKey(sstoInt)) {
                                Path path = receiverMap.get(sstoInt).getPath();
                                beltPath.setNext(path);
                                path.setPrevious(beltPath);
                                beltPath = path;
                            } else if (beltMap.containsKey(sstoInt)) {
                                Path path = beltMap.get(sstoInt).getPath();
                                beltPath.setNext(path);
                                path.setPrevious(beltPath);
                                beltPath = path;
                            }
                        }
                    }
                } else {
                    String[] splitB = inputB.split(",");
                    String ss = splitB[0];
                    if (!ss.matches("\\d+")) {
                        String error = "Path contains non-number character";
                        throw new FileFormatException(error, 358, new IOException());
                    }
                    int sstoInt = Integer.parseInt(ss);
                    if (producerMap.containsKey(sstoInt)) {
                        Path path = producerMap.get(sstoInt).getPath();
                        beltPath.setPrevious(path);
                        path.setNext(beltPath);
                    } else if (receiverMap.containsKey(sstoInt)) {
                        Path path = receiverMap.get(sstoInt).getPath();
                        beltPath.setPrevious(path);
                        path.setNext(beltPath);
                    } else {
                        Path path = beltMap.get(sstoInt).getPath();
                        beltPath.setPrevious(path);
                        path.setNext(beltPath);
                    }
                    if (splitB.length >= 2) {
                        for (int i = 1; i < splitB.length; i++) {
                            if (!splitB[i].matches("\\d+")) {
                                String error = "Path contains non-number character";
                                throw new FileFormatException(error, 377, new IOException());
                            }
                            int id = Integer.parseInt(splitB[i]);
                            if (producerMap.containsKey(id)) {
                                Path path = producerMap.get(id).getPath();
                                beltPath.setNext(path);
                                beltPath = path;
                            } else if (receiverMap.containsKey(id)) {
                                Path path = receiverMap.get(id).getPath();
                                beltPath.setNext(path);
                                path.setPrevious(beltPath);
                                beltPath = path;
                            } else if (beltMap.containsKey(id)) {
                                Path path = beltMap.get(id).getPath();
                                beltPath.setNext(path);
                                path.setPrevious(beltPath);
                                beltPath = path;
                            }
                        }
                    }
                }
            }
        }
    }
}
