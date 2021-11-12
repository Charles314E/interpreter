package com.Data;

import com.Automata.LRState;
import com.N2_Parser.Actions.ParserActionGrid;
import com.N2_Parser.Actions.ParserActionList;
import com.N2_Parser.Grammar.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class importing {
    public static <T extends Item> boolean importTable(ParserActionGrid<T> grid, String filename) {
        File tableFile = new File("data/" + filename);
        try {
            FileReader reader = new FileReader(tableFile);
            StringBuilder text = new StringBuilder();
            int c;
            while ((c = reader.read()) > -1) {
                text.append((char) c);
            }
            reader.close();
            String[] lines = text.toString().split("\n");
            LRState<T> state = null;
            int id;
            String token;
            HashMap<LRState<T>, HashMap<String, String[]>> actionGrid = new HashMap<>();
            for (String line : lines) {
                if (!(line.isBlank() || line.isEmpty())) {
                    if (line.contains("STATE")) {
                        try {
                            state = new LRState<>(grid.getParser().getLRA());
                        }
                        catch (ClassCastException e) {
                            return false;
                        }
                        grid.getParser().getLRA().addState(state);
                        actionGrid.put(state, new HashMap<>());
                        grid.addState(state);
                        if (line.startsWith("STATE")) {
                            System.out.println(line.substring("STATE ".length()));
                            id = Integer.parseInt(line.substring("STATE ".length()));
                        } else {
                            id = Integer.parseInt(line.substring("OPENING STATE ".length()));
                            grid.getParser().getLRA().setStart(state);
                        }
                        state.setID(id);
                        System.out.println("Formulated state " + state.toString(true) + ".");
                    }
                    else if (state != null) {
                        token = line.substring(0, line.indexOf(" = "));
                        actionGrid.get(state).put(token, line.substring(token.length() + 3).split(", "));
                        grid.getNames().add(token);
                    }
                }
            }
            ParserActionList<T> actions;
            for (Map.Entry<LRState<T>, HashMap<String, String[]>> row : actionGrid.entrySet()) {
                for (Map.Entry<String, String[]> cell : row.getValue().entrySet()) {
                    System.out.println("[DBG]: Creating state " + row.getKey().toString(true) + "...");
                    actions = grid.getActionGrid().get(row.getKey()).get(cell.getKey());
                    if (actions == null) {
                        actions = new ParserActionList<>();
                        grid.getActionGrid().get(row.getKey()).put(cell.getKey(), actions);
                    }
                    for (String code : cell.getValue()) {
                        actions.add(grid.constructAction(cell.getKey(), code));
                    }
                }
                System.out.println("[DBG]: " + grid.getParser().getLRA().getState(row.getKey().getID()));
                System.out.println("[DBG]: Created state " + row.getKey().toString(true) + ".");
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("[ERR]: The file '" + tableFile.getAbsolutePath() + "' cannot be found.");
            return false;
        }
        catch (IOException e) {
            System.out.println("[ERR]: " + e.getMessage());
            return false;
        }
        System.out.println("[DBG]: File successfully imported.");
        return true;
    }

    public static boolean fileExists(String filename) {
        return new File("data/" + filename).exists();
    }
}
