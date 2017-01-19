import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * ライフゲーム用のメインクラス
 * @author Atsuya Sato
 */
public class LifeGame {
	public static void main(String[] args){
		GameFrame frame = new GameFrame(Const.FRAME_SIZE.width,Const.FRAME_SIZE.height);
		frame.setTitle("LifeGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			frame.setCells(Const.CELL_SIZE);
		} catch (Exception e) {	e.printStackTrace(); }
		
		frame.setVisible(true);
		
	     Thread t = new Thread(frame);
	     t.start();
	}
}
