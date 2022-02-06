package controller;

import model.Board;
import model.Card;
import model.Player;
import model.Tile;
import view.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
 * TODO: Multiple players at one cell? Overlap
 * Until the overlapped player turn comes up, the player icon doesn't show up
 */
public class LabyrinthController implements ActionListener {

    int fileNum = 0;
    private boolean gameStarted = false;

    // game frames
    private GameFrame gameFrame = new GameFrame();
    // player
    private Player[] players = board.getPlayers();
    // menu bar

    // board
    public static Board board = new Board();

    // data
    private ArrayList<String> treasures = new ArrayList<>();

    // arrow buttons
    JButton[][] moveTileButtons;

    public LabyrinthController() {
        setupMenu();
        setupFrame();
        loadUpBoard();

        gameFrame.getFreeTilePanel().setupFreeTile();
        freeTileSetup();
        arrowSetupUp();
        fileNum = board.getFreeTile().getOrientation();
        for (int currButton = 0; currButton < 4; currButton++) {
            gameFrame.getControlPanel().getMovementButtons()[currButton].addActionListener(this);
        }

    }

    private void freeTileSetup() {
        gameFrame.getFreeTilePanel().getRotateAntiClock().addActionListener(this);
        gameFrame.getFreeTilePanel().getRotateClock().addActionListener(this);
        gameFrame.getControlPanel().getEndTurn().addActionListener(this);
    }

    
    private void loadUpBoard() {
        try {

            Stack<Tile> treasures = new Stack<Tile>();

            // Read the csv(comma seperated values) file
            Scanner input = new Scanner(new File("files/Treasures.csv"));

            // Set up a delimeter which seperates each word by ',' or a new line
            // character(\n)
            input.useDelimiter(",|\\r\\n");

            int orientation;

            // Add all the treasures to the temporary stack
            for (int index = 0; index < 24; index++) {

                Tile tile = new Tile();
                tile.setHasTreasure(true);

                tile.setTreasureName(input.next().replaceAll("\\r|\\n", ""));
                tile.setType(input.next());

                // Randomly select the orientation if it hasn't been selected yet
                orientation = input.nextInt();
                if (orientation == -1)
                    orientation = (int) ((Math.random() * 3) + 1);

                tile.setOrientation(orientation);

                setTileOpenings(tile);

                tile.setMoveable(input.nextBoolean());
                tile.setRow(input.nextInt());
                tile.setCol(input.nextInt());

                // Store the unmovable(static) objects in the board matrix
                if (!tile.isMoveable()) {
                    board.getMatrix()[tile.getRow()][tile.getCol()] = tile;
                }

                // Set the file location for movable tiles
                else
                    tile.setFileName(tile.getTreasureName());

                // Add each treasure tile to the stack
                treasures.add(tile);
            }

            int type;

            // Add pathways - 22 movable tiles
            for (int index = 0; index < 22; index++) {
                Tile tile = new Tile();

                tile.setHasTreasure(false);

                // Randomly select what type of pathway it is
                type = (int) ((Math.random() * 3));

                // Assign the pathway based on the randomly selected value
                if (type == 1)
                    tile.setType("L");
                else
                    tile.setType("I");

                // Randomly select the orientation
                orientation = (int) ((Math.random() * 3) + 1);
                tile.setOrientation(orientation);

                tile.setMoveable(true);
                tile.setFileName(tile.getType());

                setTileOpenings(tile);

                // Decide the location after
                tile.setRow(-1);
                tile.setCol(-1);

                treasures.add(tile);

            }

            // Non movable tiles - corners
            Tile topLeftTile = new Tile(0, 0, false, true, false, "L", 1);
            setTileOpenings(topLeftTile);

            Tile topRightTile = new Tile(0, 6, false, true, false, "L", 2);
            setTileOpenings(topRightTile);

            Tile bottomRightTile = new Tile(6, 6, false, true, false, "L", 3);
            setTileOpenings(bottomRightTile);

            Tile bottomLeftTile = new Tile(6, 0, false, true, false, "L", 0);
            setTileOpenings(bottomLeftTile);

            treasures.add(topRightTile);
            treasures.add(bottomLeftTile);
            treasures.add(topLeftTile);
            treasures.add(bottomRightTile);

            board.getMatrix()[0][0] = topLeftTile;
            board.getMatrix()[0][6] = topRightTile;
            board.getMatrix()[6][6] = bottomRightTile;
            board.getMatrix()[6][0] = bottomLeftTile;

            // Sort the stack (so that row and col with -1 come first). Movable objects will
            // be popped out
            Collections.sort(treasures, Comparator.comparing(Tile::getRow));

            // Create a new stack which will shuffled to place the movable objects in random
            // locations
            Stack<Tile> randomList = new Stack<Tile>();
            for (int i = 0; i < 34; i++)
                randomList.add(treasures.get(i));

            // Shuffle the movable objects
            Collections.shuffle(randomList);

            // Add and store the randomized movable objects to the matrix
            for (int row = 0; row < 7; row++)

                for (int col = 0; col < 7; col++) {

                    if (board.getMatrix()[row][col] == null) {

                        board.getMatrix()[row][col] = randomList.pop();
                        board.getMatrix()[row][col].setRow(row);
                        board.getMatrix()[row][col].setCol(col);

                    }
                
                }

            // Set up the free tile (last time remaining)
            board.setFreeTile(randomList.pop());

        } catch (FileNotFoundException error) {
            error.printStackTrace();

        }

        // Set up the front end side
        setUpBoardGUI();
    }

    
    // siddhesh
    private void setupMenu() {
        // setup menu
        for (JMenuItem item: gameFrame.getLabMenuBar().getCardNum()) {
            gameFrame.getLabMenuBar().getSetup().add(item);
            item.addActionListener(this);
        }

        // add menu items to the menu bar
        for (JMenuItem item: gameFrame.getLabMenuBar().getCardNum()) {
            gameFrame.getLabMenuBar().getSetup().add(item);
            item.addActionListener(this);
        }

        // add menu to the menu bar and add the menu bar to the frame
        gameFrame.getLabMenuBar().add(gameFrame.getLabMenuBar().getSetup());
        gameFrame.setJMenuBar(gameFrame.getLabMenuBar());
    }

    private void refreshFreeTile() {
        gameFrame.getFreeTilePanel().getFreeTile().setIcon(new ImageIcon(new ImageIcon(
                "images/" + board.getFreeTile().getFileName() + "" + board.getFreeTile().getOrientation() + ".png")
                .getImage()));

        gameFrame.getFreeTilePanel().getFreeTile().setOpaque(true);
        gameFrame.getFreeTilePanel().repaint();
    }

    private void arrowSetupUp() {
        moveTileButtons = gameFrame.getBoardPanel().getInsertButtons();
        int i;
        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                moveTileButtons[i][j].addActionListener(this);
            }
        }
    }

    private void setupFrame() {
        gameFrame.setLayout(null);
        gameFrame.setTitle("Labyrinth Application");
        gameFrame.setSize(1920, 1080);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JMenuItem && !gameStarted) {
            setupPlayerCards(event);
            gameStarted = true;
        } // TODO message for restart

        // based on what button player has pressed, try to move the player in that direction.
        if (event.getSource() == gameFrame.getControlPanel().getMovementButtons()[0]) {
            playerMoved(0);
        }
        else if (event.getSource() == gameFrame.getControlPanel().getMovementButtons()[1])
            playerMoved(1);
        else if (event.getSource() == gameFrame.getControlPanel().getMovementButtons()[2])
            playerMoved(2);
        else if (event.getSource() == gameFrame.getControlPanel().getMovementButtons()[3])
            playerMoved(3);
        else if (event.getSource() == gameFrame.getControlPanel().getEndTurn()) {
            endPlayerTurn();
            board.setFreeTileInserted(false);

            
            
        }

        if (event.getSource() == gameFrame.getFreeTilePanel().getRotateAntiClock()) {

            fileNum -= 1;
            if (fileNum < 0) {
                fileNum = 3;
            }
            rotateFreeTile();
        }
        if (event.getSource() == gameFrame.getFreeTilePanel().getRotateClock()) {

            fileNum += 1;
            if (fileNum > 3) {
                fileNum = 0;
            }
            rotateFreeTile();

        }

        // arrows
        for (int side = 0; side < 4; side++) {
            for (int button = 0; button < 3; button++) {
                if (event.getSource() == moveTileButtons[side][button] && !board.isFreeTileInserted() && !board.getIsInsertButtonClicked()[side][button]) {
                	
                	board.setFreeTileInserted(true);
                	
                	resetInsertButtons();
                		
                		
                	
                	if (side == 0) {
                        moveColumnBottom(((button * 2) + 1));
                        
                        board.getIsInsertButtonClicked()[side+2][button]=true;
                        
                       
                    } else if (side == 1) {
                        moveRowRight(((button * 2) + 1));
                        
                        board.getIsInsertButtonClicked()[side+2][button]=true;
                        
                    } else if (side == 2) {
                        moveColumnTop(((button * 2) + 1));
                        
                        board.getIsInsertButtonClicked()[side-2][button]=true;
                       
                    } else if (side == 3) {
                        moveRowLeft(((button * 2) + 1));
                        
                        board.getIsInsertButtonClicked()[side-2][button]=true;
                       
                    }
                }
            }
        }
    }
    
    private void moveRowRight(int row) {
    	
    	Tile temp = board.getFreeTile();
        board.setFreeTile(board.getMatrix()[row][6]);

        for (int newCol = 5; newCol >= 0; newCol--) {
            

            board.getMatrix()[row][newCol + 1] = board.getMatrix()[row][newCol];
            board.getMatrix()[row][newCol + 1].setCol(newCol + 1);
            
        }
      
        board.getMatrix()[row][0] = temp;
        board.getMatrix()[row][0].setRow(row);
        board.getMatrix()[row][0].setCol(0);

        
        refreshFreeTile();
        setUpBoardGUI();
        
       moveAnyPlayerOnRowRight(row);	
    }

    //Moves any player on the current row to the right
    private void moveAnyPlayerOnRowRight(int row) {
    	
    	boolean movePlayerToOtherEnd=false;
    	int playerTemp=0;
    
    	for(int player=0;player<4;player++) {
    		
    		if(players[player].getRow()==row) {
    			
    			if(players[player].getColumn()==6) {
    				movePlayerToOtherEnd=true;
    				playerTemp=player;
    				gameFrame.getBoardPanel().getPlayerLabels()[row][6].setVisible(false);
    			}
    			
    			for(int col=6;col>0;col--) {
    				
    				if(players[player].getColumn()==col-1) {
    					
    	            	gameFrame.getBoardPanel().getPlayerLabels()[row][col-1].setVisible(false);
        	    		players[player].setColumn(col);
        	    		
        	    		// move players to new row and column
        		        updatePlayerPositionGUI(player,row,col);
    				}
    			
    			}
    		}
    	}
    	
    	if(movePlayerToOtherEnd) {
    		players[playerTemp].setColumn(0);
    		updatePlayerPositionGUI(playerTemp,row,0);
    	}
    	
    }
    	

    private void moveRowLeft(int row) {
        Tile temp = board.getFreeTile();
        board.setFreeTile(board.getMatrix()[row][0]);

        for (int newCol = 1; newCol <= 6; newCol++) {

            board.getMatrix()[row][newCol - 1] = board.getMatrix()[row][newCol];
            board.getMatrix()[row][newCol - 1].setCol(newCol - 1);
        }
        board.getMatrix()[row][6] = temp;
        board.getMatrix()[row][6].setRow(row);
        board.getMatrix()[row][6].setCol(6);

        refreshFreeTile();
        setUpBoardGUI();
        moveAnyPlayerOnRowLeft(row);

    }
    
    
  //Moves any player on the current row to the left
    private void moveAnyPlayerOnRowLeft(int row) {
    	
    	boolean movePlayerToOtherEnd=false;
    	int playerTemp=0;
    
    	for(int player=0;player<4;player++) {
    		
    		if(players[player].getRow()==row) {
    			
    			if(players[player].getColumn()==0) {
    				movePlayerToOtherEnd=true;
    				playerTemp=player;
    				gameFrame.getBoardPanel().getPlayerLabels()[row][0].setVisible(false);
    			}
    			
    			for(int col=0;col<6;col++) {
    				
    				if(players[player].getColumn()==col+1) {
    					
    	            	gameFrame.getBoardPanel().getPlayerLabels()[row][col+1].setVisible(false);
        	    		players[player].setColumn(col);
        	    		
        	    		// move players to new row and column
        		        updatePlayerPositionGUI(player,row,col);
    				}
    			
    			}
    		}
    	}
    	
    	if(movePlayerToOtherEnd) {
    		players[playerTemp].setColumn(6);
    		updatePlayerPositionGUI(playerTemp,row,6);
    	}
    	
    }

    //Shifts all the tiles up. Pops the the last tile and sets it as free tile
    private void moveColumnTop(int col) {
        Tile temp = board.getFreeTile();
        board.setFreeTile(board.getMatrix()[0][col]);

        for (int newRow = 1; newRow <= 6; newRow++) {

            board.getMatrix()[newRow - 1][col] = board.getMatrix()[newRow][col];
            board.getMatrix()[newRow - 1][col].setRow(newRow - 1);
        }
        board.getMatrix()[6][col] = temp;
        board.getMatrix()[6][col].setRow(6);
        board.getMatrix()[6][col].setCol(col);
   

        refreshFreeTile();
        setUpBoardGUI();
        moveAnyPlayerOnColumnUp(col);

    }
    
    
  //Moves any player on the current column up
    private void moveAnyPlayerOnColumnUp(int col) {
    	
    	boolean movePlayerToOtherEnd=false;
    	int playerTemp=0;
    
    	for(int player=0;player<4;player++) {
    		
    		if(players[player].getColumn()==col) {
    			
    			if(players[player].getRow()==0) {
    				movePlayerToOtherEnd=true;
    				playerTemp=player;
    				gameFrame.getBoardPanel().getPlayerLabels()[0][col].setVisible(false);
    			}
    			
    			for(int row=0;row<6;row++) {
    				
    				if(players[player].getRow()==row+1) {
    					
    	            	gameFrame.getBoardPanel().getPlayerLabels()[row+1][col].setVisible(false);
        	    		players[player].setRow(row);
        	    		
        	    		// move players to new row and column
        		        updatePlayerPositionGUI(player,row,col);
    				}
    			
    			}
    		}
    	}
    	
    	if(movePlayerToOtherEnd) {
    		players[playerTemp].setRow(6);
    		updatePlayerPositionGUI(playerTemp,6,col);
    	}
    	
    }
    
    
    private void moveColumnBottom(int col) {

        Tile temp = board.getFreeTile();
        board.setFreeTile(board.getMatrix()[6][col]);

        for (int newRow = 5; newRow >= 0; newRow--) {
            // tempTile = board.getMatrix()[newRow+1][col];

            board.getMatrix()[newRow + 1][col] = board.getMatrix()[newRow][col];
            board.getMatrix()[newRow + 1][col].setRow(newRow + 1);
        }
        board.getMatrix()[0][col] = temp;
        board.getMatrix()[0][col].setRow(0);
        board.getMatrix()[0][col].setCol(col);

        refreshFreeTile();
        setUpBoardGUI();
        moveAnyPlayerOnColumnDown(col);

    }
    
    
  //Moves any player on the current column up
    private void moveAnyPlayerOnColumnDown(int col) {
    	
    	boolean movePlayerToOtherEnd=false;
    	int playerTemp=0;
    
    	for(int player=0;player<4;player++) {
    		
    		if(players[player].getColumn()==col) {
    			
    			if(players[player].getRow()==6) {
    				movePlayerToOtherEnd=true;
    				playerTemp=player;
    				gameFrame.getBoardPanel().getPlayerLabels()[6][col].setVisible(false);
    			}
    			
    			for(int row=6;row>0;row--) {
    				
    				if(players[player].getRow()==row-1) {
    					
    	            	gameFrame.getBoardPanel().getPlayerLabels()[row-1][col].setVisible(false);
        	    		players[player].setRow(row);
        	    		
        	    		// move players to new row and column
        		        updatePlayerPositionGUI(player,row,col);
    				}
    			
    			}
    		}
    	}
    	
    	if(movePlayerToOtherEnd) {
    		players[playerTemp].setRow(0);
    		updatePlayerPositionGUI(playerTemp,0,col);
    	}
    	
    }

    private void resetInsertButtons() {
    	
    	for(int side=0; side<4;side++) {
    		
    		for(int button=0;button<3;button++) {
    			board.getIsInsertButtonClicked()[side][button]=false;
    		}
    	}
	}

	private void rotateFreeTile() {
        JLabel tile = gameFrame.getFreeTilePanel().getFreeTile();
        gameFrame.getFreeTilePanel().remove(gameFrame.getFreeTilePanel().getFreeTile());

        Tile freeTile = board.getFreeTile();

        String name = "images/" + freeTile.getFileName() + fileNum + ".png";

        BufferedImage myPicture;
        try {
            myPicture = ImageIO.read(new File(name));
            tile = new JLabel(new ImageIcon(myPicture));

            gameFrame.getFreeTilePanel().setFreeTile(tile);
            board.getFreeTile().setOrientation(fileNum);
            tile.setBounds(0, 0, 100, 100);
            gameFrame.getFreeTilePanel().repaint();
            gameFrame.getFreeTilePanel().add(tile);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }



    // siddhesh
    private void playerMoved(int direction) {
        System.out.println(direction);
        // calculate change in rows or columns based on the direction
        int dX = 0;
        int dY = 0;

        if (direction == 0) {
            dY = -1;
        }
        else if (direction == 1) {
            dX = 1;
        }
        else if (direction == 2) {
            dY = 1;
        }
        else if (direction == 3) {
            dX = -1;
        }

        // if player can move in that direction, move them to the new row/column
        if (canPlayerMove(direction, players[board.getPlayerTurn()], dX, dY)) {
            int newRow = players[board.getPlayerTurn()].getRow() + dY;
            int newCol = players[board.getPlayerTurn()].getColumn() + dX;

            // move players to new row and column
            board.getMatrix()[players[board.getPlayerTurn()].getRow()][players[board.getPlayerTurn()].getColumn()].setHasPlayer(false);
            
            //Make the current label of the player invisible
			gameFrame.getBoardPanel().getPlayerLabels()[players[board.getPlayerTurn()].getRow()][players[board.getPlayerTurn()].getColumn()].setVisible(false);
			
			gameFrame.repaint();
            players[board.getPlayerTurn()].setRow(newRow);
            players[board.getPlayerTurn()].setColumn(newCol);

            board.getMatrix()[players[board.getPlayerTurn()].getRow()][players[board.getPlayerTurn()].getColumn()].setHasPlayer(true);

            // check if player landed on treasure
            checkPlayerTreasure(players[board.getPlayerTurn()]);

            // update playerPanel GUI
            updatePlayerPanelGUI();

            updatePlayerPositionGUI(newRow, newCol); //

            System.out.println(players[board.getPlayerTurn()].getFileName());
            System.out.print(players[board.getPlayerTurn()].getRow()+" ");
            System.out.println(players[board.getPlayerTurn()].getColumn());
            
            if(gameEnded()) {
            	endGame();
            }
        }
    }

    private void endGame() {
    	
    	gameFrame.endGame(board.getPlayerTurn());
	}

	private boolean gameEnded() {
		
		for(int card=0;card<players[board.getPlayerTurn()].getCards().length;card++) {
			
			if(!players[board.getPlayerTurn()].getIsCardsTaken()[card])
				return false;
				
		}
		return true;
	}

	private void updatePlayerPositionGUI(int playerNum,int newRow,int newCol) {
    	String fileName=players[playerNum].getFileName();
    	gameFrame.getBoardPanel().getPlayerLabels()[newRow][newCol].setVisible(true);
		gameFrame.getBoardPanel().getPlayerLabels()[newRow][newCol].setIcon(new ImageIcon(new ImageIcon(fileName).getImage()));
    		
    	
    	gameFrame.repaint();
    	
    }
	
    private void updatePlayerPositionGUI(int newRow, int newCol) {
    		
    	int row,col;
		for(int player=0;player<4;player++) {
			
			row=players[player].getRow();
			col=players[player].getColumn();
			
			gameFrame.getBoardPanel().getPlayerLabels()[row][col].setIcon(new ImageIcon(new ImageIcon(players[player].getFileName()
					).getImage()));
			gameFrame.getBoardPanel().getPlayerLabels()[row][col].setVisible(true);
		}
    	gameFrame.repaint();
    	
    	
	}

	// siddhesh
    private boolean canPlayerMove(int direction, Player player, int dX, int dY) {
        int playerRow = player.getRow();
        int playerCol = player.getColumn();

        // if player tried to move outside board, return false
        if (playerCol + dX < 0 || playerRow + dY < 0 || playerRow + dY > 6 || playerCol +dX > 6)
            return false;


        if (board.getMatrix()[playerRow][playerCol].getOpenings()[direction] &&
                board.getMatrix()[playerRow + dY][playerCol + dX].getOpenings()[(direction + 2) % 4]) {
            return true;
        }
        else
            return false;
    }

    // siddhesh
    private void endPlayerTurn() {
        board.setPlayerTurn((board.getPlayerTurn() + 1) % 4);
    }

    // check if the player is on a treasure
    private void checkPlayerTreasure (Player player) {
        for (int currCard = 0; currCard < player.getCards().length; currCard++) {
            if (Objects.equals(player.getCards()[currCard].getTreasure(), board.getMatrix()[player.getRow()][player.getColumn()].getTreasureName())) {
                // only increase player score if they have not already earned the card
                if (player.getIsCardsTaken()[currCard] == false) {
                    player.setScore(player.getScore() + 1); // TODO is Tile.hasMultiplePlayers needed?
                    player.setIsCardsTakenByCard(true, currCard); //TODO update player GUI
                    player.getCards()[currCard].setTreasure("CardFound");
                    player.getCards()[currCard].setFileName("images/CardFound.png");
                }
            }
        }

    }
    
    private void updatePlayerPanelGUI() {
        for (int currPlayer = 0; currPlayer < 4; currPlayer++) {
            gameFrame.getPlayerPanel().getPlayersScores()[currPlayer].setText("Score: " + board.getPlayers()[currPlayer].getScore());
            for (int currCard = 0; currCard < board.getPlayers()[currPlayer].getCards().length; currCard++) {
                gameFrame.getPlayerPanel().getTreasureCards()[currPlayer][currCard].setIcon(new ImageIcon
                        (new ImageIcon(players[currPlayer].getCards()[currCard].getFileName()).getImage()));
            }
        }
        gameFrame.getPlayerPanel().repaint();
    }

   

    // Siddhesh and Shreyas TODO set percentage of work divided
    private void setTileOpenings(Tile tile) {
        boolean[] openings = new boolean[4];

        for(int i=0;i<4;i++) {
            openings[i]=false;
        }

        if(tile.getType().equals("L")) {
            int orientation= tile.getOrientation();
            openings[(orientation) % 4]=true;
            openings[(1+orientation) % 4]=true;
        }

        else if(tile.getType().equals("T")) {
            int orientation= tile.getOrientation();
            openings[(2+orientation) % 4]=true;
            openings[(1+orientation) % 4]=true;
            openings[(3+orientation) % 4]=true;
        }

        else if(tile.getType().equals("I")) {
            int orientation= tile.getOrientation();
            openings[(2+orientation) % 4]=true;
            openings[(orientation) % 4]=true;

        }
        tile.setOpenings(openings);
    }

    private void setUpBoardGUI() {

        for (int row = 0; row < 7; row++)

            for (int col = 0; col < 7; col++) {

                if (board.getMatrix()[row][col].isMoveable()) {

                    gameFrame.getBoardPanel().getTileLabels()[row][col]
                            .setIcon(new ImageIcon(new ImageIcon("images/" + board.getMatrix()[row][col].getFileName()
                                    + "" + board.getMatrix()[row][col].getOrientation() + ".png").getImage()));

                    gameFrame.getBoardPanel().getTileLabels()[row][col].setOpaque(true);

                }
                
                
            }
        gameFrame.repaint();

    }

    private void setupPlayerCards(ActionEvent event) {
        // shuffle and read cards
        try {
            Scanner input = new Scanner(new File("files/Treasures.csv"));
            while (input.hasNextLine()) {
                treasures.add(input.nextLine().split(",")[0]);
            }

        }
        catch (FileNotFoundException error) {
            System.out.println("File not found: " + error);
        }

        // treasures choices
        Stack<Card> possibleCards = new Stack<>();
        for (String currTreasure: treasures) {
            possibleCards.add(new Card(currTreasure));
        }

        Collections.shuffle(possibleCards);

        // find number of cards per player as requested by user
        JMenuItem menuItem = (JMenuItem) event.getSource();
        int numberOfCards = Integer.parseInt(String.valueOf(menuItem.getText().toCharArray()[0])); //TODO fix this nonsense

        // deal each player their cards
        for (int currPlayer = 0; currPlayer < 4; currPlayer++) {
            Card[] tempCards = new Card[numberOfCards]; // TODO comment proper temp value term thingy
            // takes some number of cards from user and assigns it to the player.
            for (int currCard = 0; currCard < numberOfCards; currCard++)
                tempCards[currCard] = possibleCards.pop();
            players[currPlayer].setCards(tempCards);
        }

        setupPlayerPanelGUI();
    }

    private void setupPlayerPanelGUI() {

        // setup and display player panel, player names, and player scores
        gameFrame.getPlayerPanel().setupPlayerNamesGUI();

        // set players' cards
        for (int currPlayer = 0; currPlayer < 4; currPlayer++) {

            // for each player, loop through their cards and then display them
            for (int currCard = 0; currCard < players[currPlayer].getCards().length; currCard++) {

                // create labels for each players' cards
                gameFrame.getPlayerPanel().getTreasureCards()[currPlayer][currCard].setIcon(new ImageIcon
                        (new ImageIcon (players[currPlayer].getCards()[currCard].getFileName()).getImage()));
                // set the player card visible to true
                gameFrame.getPlayerPanel().getTreasureCards()[currPlayer][currCard].setVisible(true);
            }
        }
        gameFrame.getPlayerPanel().repaint();

    }


    
}