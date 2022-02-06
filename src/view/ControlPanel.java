package view;

import javax.swing.*;

public class ControlPanel extends JPanel {
    private JButton[] movementButtons = new JButton[4]; // starting from top, clockwise
    private JLabel currPlayer = new JLabel("Player 1 is playing");
    private JButton endTurn = new JButton("End Turn");

    public ControlPanel() {
        setBounds(1200, 600, 500, 300);
        setLayout(null);
        movementButtons[0] = new JButton();
        movementButtons[0].setBounds(70, 0, 49, 70);
        movementButtons[0].setOpaque(false);
        movementButtons[0].setContentAreaFilled(false);
        movementButtons[0].setIcon(new ImageIcon(new ImageIcon("images/Arrow0.png").getImage()));

        movementButtons[1] = new JButton();
        movementButtons[1].setBounds(120, 70, 70, 49);
        movementButtons[1].setOpaque(false);
        movementButtons[1].setContentAreaFilled(false);
        movementButtons[1].setIcon(new ImageIcon(new ImageIcon("images/Arrow1.png").getImage()));

        movementButtons[2] = new JButton();
        movementButtons[2].setBounds(70, 120, 49, 70);
        movementButtons[2].setOpaque(false);
        movementButtons[2].setContentAreaFilled(false);
        movementButtons[2].setIcon(new ImageIcon(new ImageIcon("images/Arrow2.png").getImage()));

        movementButtons[3] = new JButton();
        movementButtons[3].setBounds(0, 70, 70, 49);
        movementButtons[3].setOpaque(false);
        movementButtons[3].setContentAreaFilled(false);
        movementButtons[3].setIcon(new ImageIcon(new ImageIcon("images/Arrow3.png").getImage()));

        endTurn = new JButton();
        endTurn.setBounds(70, 70, 50, 50);
        endTurn.setOpaque(false);
        endTurn.setContentAreaFilled(false);
        endTurn.setText("End turn");

        add(movementButtons[0]);
        add(movementButtons[1]);
        add(movementButtons[2]);
        add(movementButtons[3]);
        add(endTurn);
    }

    public JButton[] getMovementButtons() {

        return movementButtons;
    }

    public void setMovementButtons(JButton[] movementButtons) {
        this.movementButtons = movementButtons;
    }

    public JLabel getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(JLabel currPlayer) {
        this.currPlayer = currPlayer;
    }

    public JButton getEndTurn() {
        return endTurn;
    }

    public void setEndTurn(JButton endTurn) {
        this.endTurn = endTurn;
    }

}











