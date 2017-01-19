import java.awt.Dimension;
import java.util.stream.IntStream;

/**
 * 定数値を扱うクラス
 * @author Atsuya Sato
 */
public final class Const {
	private Const(){
		
	}
	//メインフレームのサイズ
	public final static Dimension FRAME_SIZE = new Dimension(500,500);
	
	//セルの大きさ(正方形一辺の長さ)
	//セルサイズはフレームサイズの縦幅及び横幅の約数となっている必要がある
	public final static int CELL_SIZE = 10;
	
	//無限ループ内でのスリープ時間 ( ex. 値を小さくするほど更新速度が上がる)
	public final static long SLEEP_TIME_MS = 50;
	
	//セルが生存するために必要な周辺セル数の範囲
	public final static Range<Integer> LIVING_RANGE = new Range<Integer>(2,3);
	public final static int BIRTH_CNT = 3;
	
	//ボタンによるパターン生成の生成位置
	public final static Dimension sponeLocation = new Dimension(2,2);
		
}