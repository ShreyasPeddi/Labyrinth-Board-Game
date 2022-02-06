package model;

import java.util.Arrays;

public class Player {
    private Card[] cards = new Card[6];
    private boolean[] isCardsTaken = new boolean[6];
    private int score = 0;
    private String fileName;
    private int row;
    private int column;

    public Player() {
    	
    }
    
    public boolean[] getIsCardsTaken() {
        return isCardsTaken;
    }

    public void setIsCardsTaken(boolean[] isCardsTaken) {
        this.isCardsTaken = isCardsTaken;
    }

    public void setIsCardsTakenByCard (boolean isCardsTakenByCard, int card) {
        isCardsTaken[card] = isCardsTakenByCard;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    
    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Player{" + "cards=" + Arrays.toString(cards) + '}';
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
