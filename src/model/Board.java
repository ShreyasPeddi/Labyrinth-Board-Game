package model;

public class Board {

    private Tile[][] matrix = new Tile[7][7];
    private Tile freeTile;
    private Player[] players = new Player[4];
    private int playerTurn = 0; // player 0 is player 1, range: playerTurn 0-3 inclusive representing players 1-4
    private boolean freeTileInserted=false;
    private boolean isInsertButtonClicked[][] = new boolean[4][3];
    

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }


    public Board() {

        for (int currPlayer = 0; currPlayer < 4; currPlayer++) {
            players[currPlayer] = new Player();
        }

        // set starting player rows and columns
        players[0].setRow(0);
        players[1].setRow(0);
        players[0].setColumn(0);
        players[1].setColumn(6);

        players[2].setRow(6);
        players[3].setRow(6);
        players[2].setColumn(6);
        players[3].setColumn(0);
        
        
        players[0].setFileName("images/red.png");
        players[1].setFileName("images/blue.png");
        players[2].setFileName("images/green.png");
        players[3].setFileName("images/yellow.png");
    }

    public Tile[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    public Tile getFreeTile() {
        return freeTile;
    }

    public void setFreeTile(Tile freeTile) {
        this.freeTile = freeTile;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

	public boolean isFreeTileInserted() {
		return freeTileInserted;
	}

	public void setFreeTileInserted(boolean freeTileInserted) {
		this.freeTileInserted = freeTileInserted;
	}

	public boolean[][] getIsInsertButtonClicked() {
		return isInsertButtonClicked;
	}

	public void setIsInsertButtonClicked(boolean isInsertButtonClicked[][]) {
		this.isInsertButtonClicked = isInsertButtonClicked;
	}
}
