package game;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


public class GameState {

    private final Cell[] cells;
    private final String currentPlayer;
    private final String winner;

    private GameState(Cell[] cells, String currentPlayer, String winner) {
        this.cells = cells;
        this.currentPlayer = currentPlayer;
        this.winner = winner;

    }
    public static void generateTextFile(String filename, String content) {
        try {
            Files.write(Paths.get(filename), content.getBytes());
            System.out.println("File created successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error creating the file: " + e.getMessage());
        }
    }
    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        int currentPlayer = game.getPlayer().value;
        int winner = game.getWinner() == null ? -1 : game.getWinner().value;

        String currentPlayerString = switch (currentPlayer) {
            case 0 -> "X";
            case 1 -> "O";
            default -> "-1";
        };
        
        String winnerString = switch (winner) {
            case 0 -> "X";
            case 1 -> "O";
            default -> "-1";
        };
        
        generateTextFile("gamestate.txt", "Current player: " + currentPlayerString + "\nWinner: " + winnerString);
        return new GameState(cells, currentPlayerString, winnerString);
    }

    public Cell[] getCells() {
        return this.cells;
    }

    /**
     * toString() of GameState will return the string representing
     * the GameState in JSON format.
     */
    @Override
    public String toString() {
        String cellsJson = Arrays.stream(this.cells)
                              .map(Cell::toString)
                              .collect(Collectors.joining(", ", "[", "]"));

    return String.format("""
            { 
              "cells": %s, 
              "currentPlayer": "%s", 
              "winner": "%s" 
            }
            """, cellsJson, this.currentPlayer, this.winner.isEmpty() ? null : this.winner);
    }

    private static Cell[] getCells(Game game) {
        Cell cells[] = new Cell[9];
        Board board = game.getBoard();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                String text = "";
                boolean playable = false;
                Player player = board.getCell(x, y);
                if (player == Player.PLAYER0)
                    text = "X";
                else if (player == Player.PLAYER1)
                    text = "O";
                else if (player == null) {
                    playable = true;
                }
                cells[3 * y + x] = new Cell(x, y, text, playable);
            }
        }
        return cells;
    }
}

class Cell {
    private final int x;
    private final int y;
    private final String text;
    private final boolean playable;

    Cell(int x, int y, String text, boolean playable) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.playable = playable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return this.text;
    }

    public boolean isPlayable() {
        return this.playable;
    }

    @Override
    public String toString() {
        return """
                {
                    "text": "%s",
                    "playable": %b,
                    "x": %d,
                    "y": %d 
                }
                """.formatted(this.text, this.playable, this.x, this.y);
    }
}