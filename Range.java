/**
 * Range用クラス(IntStreamが使いにくかったので)
 * @see http://d.hatena.ne.jp/torutk/20110924/p1
 */
public class Range<T extends Comparable> {
    public T lowerBound;
    public T upperBound;
	/**
	 * Constructor
	 */
    public Range(T lowerBound, T upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public boolean includes(T value) {
        return lowerBound.compareTo(value) <= 0 && value.compareTo(upperBound) <= 0;
    }
}
