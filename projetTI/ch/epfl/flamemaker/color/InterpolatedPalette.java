package ch.epfl.flamemaker.color;
import java.util.List;
import java.util.ArrayList;

/**
 *Classe d' <code>InterpolatedPalette</code>, qui a pour attribut une <code>List</code> de <code>Color</code>. Implemente l'interface <code>Palette</code>.
 * @author  Simon Le Bail-Collet(227359), Louis-Maxence Garret(223892)
 */
public class InterpolatedPalette implements Palette{
	/**
	 * Une <code>List</code> de <code>Color</code> a interpoler, doit contenir au moins 2 <code>Color</code>.
	 */
	private final List<Color> listColor;

	/**
	 * Constructeur de la classe <code>InterpolatedPalette</code>, effectue une copie profonde.
	 * @param listColor Une <code>List</code> de <code>Color</code> a interpoler, doit contenir au moins 2 <code>Color</code>.
	 * @throws IllegalArgumentException si <code>listColor<code> contient moins de 2 <code>Color</code>.
	 */ //<code></code>
	public InterpolatedPalette(List<Color> listColor){
		if(listColor.size()<2){
			throw new IllegalArgumentException("liste de couleurs bien trop courte! 2 minimum");
		}else{
			List<Color> listColorCopy = new ArrayList<Color>();
			for (Color elem: listColor) {
				listColorCopy.add(elem);
			}
			this.listColor=listColorCopy;
		}
	}
	public static InterpolatedPalette palettePrimaryColors(){
		List<Color> listPrimaryColors = new ArrayList<Color>();

		listPrimaryColors.add(Color.RED);
		listPrimaryColors.add(Color.GREEN);
		listPrimaryColors.add(Color.BLUE);

		return new InterpolatedPalette (listPrimaryColors);

	}
	/* (non-Javadoc)
	 * @see ch.epfl.flamemaker.color.Palette#colorForIndex(double)
	 */
	public Color colorForIndex(double index){
		if(index<0 || index>1){
			throw new IllegalArgumentException("l'index doit etre compris entre 0 et 1");
		}else{
			if(index == 1){
				/* si l'index passe en parametre est egale a 1, 
				 * alors on peut directement renvoyer la 
				 * derniere couleur de la liste*/
				return listColor.get(listColor.size()-1);
			}
			else{
				/* tout d'abord on calcule la "position 
				 * en valeur decimale" de la couleur
				 * recherchee a partir de l'index donne et
				 * de la position du dernie element de la liste. */
				double position = index* (listColor.size()-1);

				/* puis on recupere la partie entiere de cette 
				 * position afin de connaitre la couleur listee 
				 * "precedent" la couleur recherchee */
				int i = (int)Math.floor(position); 

				/* enfin pour obtenir la couleur recherchee, on 
				 * melange la couleur qui "suit" dans la liste celle-ci
				 * avec celle la "precedent". La proportion du melange 
				 * correspond à la difference entre la position "decimale" 
				 * et la position de la couleur precedent.*/
				return listColor.get(i+1).mixWith(listColor.get(i), position-i);
			}
		}
	}
}
