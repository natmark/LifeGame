import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * ライフゲームで使用するCell
 * @author Atsuya Sato
 */
class LifeCell extends JButton{
	//周囲のセル
	private ArrayList<LifeCell> surroundings;
	//現在の生存フラグ
	private boolean isLiving = false;
	//世代更新後の生存フラグ
	private boolean willLiving = false;
	/**
	 * Constructors
	 */
	public LifeCell(){
		super("");
		surroundings = new ArrayList<LifeCell>();
		setLayout();
	}

	public LifeCell(Rectangle rect) {
		super("");
		this.setBounds(rect);
		surroundings = new ArrayList<LifeCell>();
		setLayout();
	}
	/**
	 * レイアウトの設定
	 */
	private void setLayout(){
		this.setLayout(null);
		this.setBackground(Color.white);
        // these next two lines do the magic..
        this.setContentAreaFilled(true);
        this.setOpaque(true);
        this.setBorderPainted(false);
		LifeCell button = this;
		
		this.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent event){
							if(button.isLiving){
								button.forceKill();
							}else{
								button.forceSpawn();
							}
						}
					}
				);
	}
	/**
	 * 周囲のセルを追加
	 * @param cell:LifeCell
	 */
	public void addSurroundings(LifeCell cell){
		surroundings.add(cell);
	}
	/**
	 * 世代交代を行う
	 */
	public void generationalChange(){
		isLiving = willLiving;
		willLiving = false;
		if(isLiving){
			setBackground(Color.black);
		}else{
			setBackground(Color.white);			
		}
	}
	/**
	 * 周りのセルを調べて、世代交代の準備
	 */
	public void checkSurroundings(){
		int cnt = 0;
		for(LifeCell cell : surroundings){
			if(cell.isLiving) cnt++;
		}
		//誕生
		if(!isLiving){
			//生きているセルが3つ
			if(cnt == Const.BIRTH_CNT){
				this.willLiving = true;
			}
			return;
		}
		//クロージャ内で使用したいので、定数として再定義
		if(isLiving){
			//生存
			if(Const.LIVING_RANGE.includes(cnt)){
				this.willLiving = true;
			}
			//過疎
			if(Const.LIVING_RANGE.lowerBound > cnt){
				this.willLiving = false;
			}
			//過密
			if(Const.LIVING_RANGE.upperBound < cnt){
				this.willLiving = false;
			}
		}
	}
	/**
	 * ボタンを押して生成する場合
	 */
	public void forceSpawn(){
		isLiving = true;
		this.setBackground(Color.black);
	}
	/**
	 * ボタンを押してセルを消す場合
	 */
	public void forceKill(){
		isLiving = false;
		this.setBackground(Color.white);
	}
	/**
	 * 盤面初期化用
	 */
	public static void forceKillAll(ArrayList<LifeCell> cells){
		for(LifeCell cell : cells){
			cell.isLiving = false;
			cell.willLiving = false;
			cell.setBackground(Color.white);			
		}
	}
}