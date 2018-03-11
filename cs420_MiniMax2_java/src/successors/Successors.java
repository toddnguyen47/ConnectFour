package successors;

public abstract interface Successors {
	public int[][] getSuccessors(int prevRow, int prevCol);
	public int[][] getSuccessors(int row, int col, int prevBestRow, int prevBestCol);
}
