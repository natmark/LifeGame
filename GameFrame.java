import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * ライフゲーム用のFrameクラス
 * @author Atsuya Sato
 */
public class GameFrame extends JFrame implements Runnable{
	private int width;
	private int height;
	private boolean running = false;

	//盤面に配置されたセルの配列
	private ArrayList<LifeCell> cells;
	//プリセットパターンを生成する際に使用。cellsを二次元配列にしたもの。
	private LifeCell[][] cell_matrix;
	/**
	 * Constructors
	 */
	public GameFrame(int width, int height){
		super();

		this.width = width;
		this.height = height;
		setSize(width,height);	
		cells = new ArrayList<LifeCell>();
	}
	public GameFrame(String title){
		super(title);
		cells = new ArrayList<LifeCell>();
	}
	/**
	 * セルサイズの変更
	 * @param cellSize
	 * @throws Exception 
	 * @throws cellSizeがFrameSizeに合わない場合 
	 */
	public void setCells(int cellSize) throws Exception {	
		if((width % cellSize != 0) || (width < cellSize)){
			throw new Exception("セルのサイズがフレームの幅に合っていないか、セルサイズがフレームの幅より大きいです。");
		}
		if(height % cellSize != 0 || (height < cellSize)){
			throw new Exception("セルのサイズがフレームの高さに合っていません、セルサイズがフレームの高さより大きいです。");
		}		
		//縦の総セル数
		int v_cell_cnt = height / cellSize; 
		//横の総セル数
		int h_cell_cnt = width / cellSize; 
		
		//JFrameの高さには、タイトルバーが含まれてしまうため、heightを更新
		setVisible(true);
		//visible状態でないと、Insetsが取得不可
		Insets insets = getInsets(); 
		setVisible(false);			

		//セル用パネルの生成
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBounds(0, 0, width + h_cell_cnt, height + v_cell_cnt);
		p.setBackground(Color.black);

		//ツールパネルの生成
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(null);
		toolPanel.setBounds(0, p.getBounds().height, p.getBounds().width, 100);
		toolPanel.setBackground(Color.white);
		
		//パネルへのボタン追加
		addButtonsOnPanel(toolPanel);
		
		//フレームサイズの更新
		int insets_height = insets.top + insets.bottom;
		setResizable(true);
		setSize(width + h_cell_cnt,height + insets_height + v_cell_cnt + toolPanel.getBounds().height);
		setResizable(false);

		cell_matrix = new LifeCell[v_cell_cnt][h_cell_cnt];
		//セルの生成
		for(int y = 0; y < v_cell_cnt; y ++){
			for(int x = 0; x < h_cell_cnt; x++){
				LifeCell cell = new LifeCell(new Rectangle(x * (cellSize + 1),y * (cellSize + 1),cellSize,cellSize));
			    p.add(cell);
			    cell_matrix[y][x] = cell;
			    cells.add(cell);
			}			
		}
		
		//セルの外周をセット。
		for(int y = 0; y < v_cell_cnt ;y++){
			for(int x = 0; x < h_cell_cnt ;x++){
				LifeCell cell = cell_matrix[y][x];
				
				int dx_min = -1;
				int dy_min = -1;
				int dx_max = 1;
				int dy_max = 1;				
				//yが一番上
				if(y == 0){ dy_min = 0; }
				//xが一番左
				if(x == 0){ dx_min = 0; }
				//yが一番下
				if(y == v_cell_cnt - 1){ dy_max = 0; }
				//xが一番右
				if(x == h_cell_cnt - 1){ dx_max = 0; }
				
				for(int dx = dx_min; dx <= dx_max;dx++){
					for(int dy = dy_min; dy <= dy_max;dy++){
						if(!(dx == 0 && dy == 0)){
							cell.addSurroundings(cell_matrix[y+dy][x+dx]);
						}
					}					
				}
			}			
		}
		
		Container contentPane = getContentPane();
		contentPane.add(p);
		contentPane.add(toolPanel);
	}
	
	/**
	 * ツールパネルへのボタン追加 
	 */
	private void addButtonsOnPanel(JPanel panel){		
		//ボタンのアクションリスナー作成
		ActionListener RunBtnAction = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//動作状態を切り替え
				running = !running;
				JButton btn = (JButton)event.getSource();
				if(running){
					btn.setText("Stop");
				}else{
					btn.setText("Run");								
				}
			}
		};
		ActionListener ClearBtnAction = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//盤面全てを初期化
				LifeCell.forceKillAll(cells);
			}
		};
		ActionListener generateGliderBtnAction = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//グライダーのパターン生成
				CellPattern.patternGenerator(cell_matrix, CellPattern.Pattern.GLIDER);
			}
		};
		ActionListener generateSpaceShipBtnAction = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//宇宙船のパターン生成
				CellPattern.patternGenerator(cell_matrix, CellPattern.Pattern.SPACESHIP);
			}
		};
		ActionListener generateGalaxyBtnAction = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//銀河のパターン生成
				CellPattern.patternGenerator(cell_matrix, CellPattern.Pattern.GALAXY);
			}
		};
		//格納順を保持したいため、LinkedHashMapを使用
		LinkedHashMap<String,ActionListener> btnSources = new LinkedHashMap<String,ActionListener>();
		btnSources.put("Run", RunBtnAction);
		btnSources.put("Clear", ClearBtnAction);
		//スペーサー
		btnSources.put("", null);
		btnSources.put("グライダー", generateGliderBtnAction);
		btnSources.put("宇宙船", generateSpaceShipBtnAction);
		btnSources.put("銀河", generateGalaxyBtnAction);

		//ボタン生成
		int i = 0;
		for(Map.Entry<String, ActionListener>  btnSrc : btnSources.entrySet()){
			//空文字の場合、インクリメントしてスキップ
			if(btnSrc.getKey().equals("")) { i++; continue; }
			
			JButton button = new JButton(btnSrc.getKey());
			button.setBackground(Color.black);
			button.setBounds(10 + i * 80,panel.getBounds().y, 80, 50);
			button.addActionListener(btnSrc.getValue());
			panel.add(button);						
			i++;
		}
	}
	public void run(){
		while(true){
			//Startボタンが押されていない場合、処理を中断
			while(!running){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {	e.printStackTrace(); } 
			}
			
			try {
				//更新速度
				Thread.sleep(Const.SLEEP_TIME_MS);
			} catch (InterruptedException e) {	e.printStackTrace(); }
			
			for(LifeCell cell : cells){
				//周囲のセルの状況を確認
				cell.checkSurroundings();
			}
			for(LifeCell cell : cells){
				//世代交代(セルの塗り替え)
				cell.generationalChange();
			}
		}
	}
}