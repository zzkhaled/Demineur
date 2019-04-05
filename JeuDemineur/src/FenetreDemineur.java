import javax.swing.JFrame;


public class FenetreDemineur extends JFrame{
	private static final long serialVersionUID = 1L ; 
	
	public FenetreDemineur() {
		setTitle("Démineur") ; 
		setSize(800,800) ; 

		this.add(new PanelDemineur()) ;
		
		this.setVisible(true);
	
	}
	

}
