package view;

import javax.swing.*;

public class GameFrame extends JFrame{

    private BoardPanel boardPanel = new BoardPanel();
    private ControlPanel controlPanel = new ControlPanel();
    private FreeTilePanel freeTilePanel = new FreeTilePanel();
    private PlayerPanel playerPanel = new PlayerPanel();

    // menu
    private LabMenuBar labMenuBar = new LabMenuBar();

    public GameFrame() {
        setLayout(null);

        add(freeTilePanel);
        add(controlPanel);
        add(boardPanel);
        add(playerPanel);

        add(labMenuBar);

        setSize(1920, 1080);
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public LabMenuBar getLabMenuBar() {
        return labMenuBar;
    }

    public void setLabMenuBar(LabMenuBar labMenuBar) {
        this.labMenuBar = labMenuBar;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public FreeTilePanel getFreeTilePanel() {
        return freeTilePanel;
    }

    public void setFreeTilePanel(FreeTilePanel freeTilePanel) {
        this.freeTilePanel = freeTilePanel;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public void setPlayerPanel(PlayerPanel playerPanel) {
        this.playerPanel = playerPanel;
    }


	public void endGame(int player) {
		
		JOptionPane.showMessageDialog(this, "Congrats to Player "+player+". Better luck next time everyone");
		this.dispose();
		
	}



}