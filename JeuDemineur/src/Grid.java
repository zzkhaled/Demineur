
import java.util.Random;

public class Grid {

	private int size;
	private Cell[][] cells;

	Grid(int size) {
		this.size = size;
		cells = new Cell[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				cells[i][j] = new Cell();
		this.tirage();
	}

	public void tirage() {
		int nbrBomb = nbrBomb();
		Random random = new Random();
		do {
			int l = random.nextInt(size);
			int c = random.nextInt(size);

			if (cells[l][c].getBomb())
				continue;

			cells[l][c].setBomb(true);
			nbrBomb--;
		} while (nbrBomb > 0);
	}
	
	public int nbrBomb() {
		return size*size / 5 ; 
	}
	
	public String toString() {
		String chaine = "";
		for (int ligne = 0; ligne < size; ligne++) {
			for (int colonne = 0; colonne < size; colonne++) {
				if(cells[ligne][colonne].getBomb())
					chaine += "   X"  ;
				else
					chaine += "   " + bombArround(ligne,colonne) ; 
					
			}
				
			chaine += "\n";
		}
		return chaine;
	}

	public int bombArround(int ligne , int colonne) {
		
		int counter = 0 ; 
		
		for (int l = ligne - 1 ; l <= ligne + 1; l++) {
			for (int c = colonne - 1 ; c <= colonne + 1 ; c++) {
			
				if(c == colonne && l == ligne)
					continue ;
				if(!estCellValid(l,c))
					continue ;
				if(cells[l][c].getBomb())
					counter++ ; 
			}
		}
		return counter ; 
	}
	
	public boolean estCellValid(int ligne , int colonne) {
		if(ligne < 0 || colonne < 0 || ligne >= size || colonne >= size)
			return false ;
		
		return true ;	
	}
	
	public int getSize() {
		return size;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
}
					
				
			