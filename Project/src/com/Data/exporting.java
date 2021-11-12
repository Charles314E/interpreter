package com.Data;

import com.Automata.LRState;
import com.Methods.stringMethods;
import com.N2_Parser.Actions.ParserActionGrid;
import com.N2_Parser.Actions.ParserActionList;
import com.N2_Parser.Grammar.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class exporting {
    public static <T extends Item> boolean exportTable(ParserActionGrid<T> grid, String filename) {
        File dataFolder = new File("data");
        if (!dataFolder.exists()) {
            System.out.println("[DBG]: The '" + dataFolder.getAbsolutePath() + "' folder does not exist.");
            if (!dataFolder.mkdir()) {
                System.out.println("[ERR]: Was not able to create a new folder.");
                return false;
            }
            System.out.println("[DBG]: The folder was successfully created.");
        }
        else if (!dataFolder.isDirectory()) {
            System.out.println("[DBG]: There is a mix-up in 'data' - a '" + dataFolder.getAbsolutePath() + "' file exists, bur not a folder.");
            if (!(dataFolder.delete() && dataFolder.mkdir())) {
                System.out.println("[ERR]: The mix-up could not be resolved.");
                return false;
            }
            System.out.println("[DBG]: The mix-up was successfully resolved.");
        }
        try {
            String fn = dataFolder.getAbsolutePath() + "/" + filename;
            File tableFile = new File(fn);
            if (tableFile.createNewFile()) {
                FileWriter writer = new FileWriter(fn);
                for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> state : grid.getActionGrid().entrySet()) {
                    if (state.getKey().isOpeningState()) {
                        writer.write("OPENING ");
                    }
                    writer.write("STATE " + state.getKey().getID());
                    writer.write("\n");
                    for (Map.Entry<String, ParserActionList<T>> cell : state.getValue().entrySet()) {
                        if (cell.getValue().size() > 0) {
                            String name = grid.getParser().getNameForAlias(cell.getKey());
                            writer.write((name != null ? name : cell.getKey()) + " = " + stringMethods.encapsulatedTuple("", "", ", ", cell.getValue()));
                            writer.write("\n");
                        }
                    }
                    writer.write("\n");
                }
                writer.close();
            }
            else {
                System.out.println("[ERR]: The file already exists.");
                return false;
            }
        }
        catch (IOException e) {
            System.out.println("[ERR]: " + e.getMessage());
            return false;
        }
        System.out.println("[DBG]: File successfully exported.");
        return true;
    }
}
