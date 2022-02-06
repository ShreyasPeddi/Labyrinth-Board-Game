package model;

import java.util.Arrays;

public class Tile {

    private int row;
    private int col;
    private boolean hasTreasure;
    private String treasureName="-1";
    private boolean hasPlayer;
    private boolean isMoveable;
    private boolean hasMultiplePlayers=false;

    private String type; //L path, T path or I path
    private int orientation;
    private boolean [] openings; //0 - top, 3-left
    private String fileName;

    //Constructor for non movable tiles
    public Tile(int row, int col, boolean hasTreasure, boolean hasPlayer, boolean isMoveable, String type, int orientation) {
        super();

        //Note: all fields are not set (hasMultiplePlayers, openings)
        this.row = row;
        this.col = col;
        this.hasTreasure = hasTreasure;
        this.hasPlayer = hasPlayer;
        this.isMoveable = isMoveable;
        this.type = type;
        this.orientation = orientation;
    }

    //Overloaded constructor
    public Tile(int row, int col, boolean hasTreasure, boolean hasPlayer, boolean isMoveable, String type, int orientation, String fileName) {

        //Note: all fields are not set (hasMultiplePlayers, openings)
        this(row, col, hasTreasure, hasPlayer, isMoveable, type, orientation);
        this.fileName = fileName;
    }

    public Tile() {

    }


    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public boolean isHasTreasure() {
        return hasTreasure;
    }
    public void setHasTreasure(boolean hasTreasure) {
        this.hasTreasure = hasTreasure;
    }
    public boolean isHasPlayer() {
        return hasPlayer;
    }
    public void setHasPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }
    public boolean isMoveable() {
        return isMoveable;
    }
    public void setMoveable(boolean isMoveable) {
        this.isMoveable = isMoveable;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public boolean[] getOpenings() {
        return openings;
    }
    public void setOpenings(boolean[] openings) {
        this.openings = openings;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public boolean isHasMultiplePlayers() {
        return hasMultiplePlayers;
    }
    public void setHasMultiplePlayers(boolean hasMultiplePlayers) {
        this.hasMultiplePlayers = hasMultiplePlayers;
    }

    public String getTreasureName() {
        return treasureName;
    }

    public void setTreasureName(String treasureName) {
        this.treasureName = treasureName;
    }

    @Override
    public String toString() {
        return "Tile [row=" + row + ", col=" + col + ", hasTreasure=" + hasTreasure + ", treasureName=" + treasureName
                + ", hasPlayer=" + hasPlayer + ", isMoveable=" + isMoveable + ", hasMultiplePlayers="
                + hasMultiplePlayers + ", type=" + type + ", orientation=" + orientation + ", openings="
                + Arrays.toString(openings) + ", fileName=" + fileName + "]";
    }





}