
public class MAP {
	char[][] dat = new char[120][120];
	int limitx=120;
	int limity=120;
	public MAP() {
		// TODO Auto-generated constructor stub
		for(int i=0;i<limitx;i++){
			for (int j = 0; j < limity; j++) {
				dat[i][j]=' ';
			}
		}
	}
	public void setDat(int x,int y) {
		dat[x][y] = '.';
	}
	public void show() {
		for(int i=0;i<limitx;i++){
			for (int j = 0; j < limity; j++) {
				System.out.print(dat[i][j]);
			}
			System.out.println();
		}
	}

}
