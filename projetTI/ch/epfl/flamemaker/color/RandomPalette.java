package ch.epfl.flamemaker.color;

import java.util.List;
import java.util.Random;

/**
 * Classe de <code>RandomPalette</code>, dont l'attribut est une <code>List</code> de <code>Color</code> generee de maniere aleatoire. Implemente l'interface <code>Palette<code>.
 * @author  Simon Le Bail-Collet(227359), Louis-Maxence Garret(223892)
 *
 */
public class RandomPalette implements Palette{
	/**
	 * Une <code>List</code> de <code>Color</code> a interpoler, doit contenir au moins 2 <code>Color</code>.
	 */
	private List<Color> listColor;
	/**
	 * cette <code>Palette</code> permet de faire appel a la methode <code>colorForIndex</code> de la classe <code>InterpolatedPalette</code>. Evite ainsi la duplication de code.
	 */
	InterpolatedPalette palette;

	/**
	 * Constructeur de la classe <code>RandomPalette</code>, remplissant aleatoirement <code>listColor</code> avec un nombre <code>numberOfColor</code> de <code>Color</code>, en effectuant une copie de cette <code>List<code>.
	 * @param numberOfColor Un <code>int</code> correspondant au nombre de couleurs souhaitees dans la palette.
	 */
	public RandomPalette(int numberOfColor){
		if(numberOfColor<2){
			throw new IllegalArgumentException("bien trop peu de couleurs! 2 minimum");
		}else{
			Random aleatoire = new Random();
			for (int i=0; i<numberOfColor; i++){
				Color tempColor = new Color (aleatoire.nextInt(1), aleatoire.nextInt(1), aleatoire.nextInt(1));
				listColor.add(tempColor);
			}
			palette= new InterpolatedPalette(listColor);
		}
	}
	/* (non-Javadoc)
	 * @see ch.epfl.flamemaker.color.Palette#colorForIndex(double)
	 * appel de la methode colorForIndex via une InterpolatedPalette creee a cet effet.
	 */
	public Color colorForIndex(double index){
		return palette.colorForIndex(index);
	}
}
