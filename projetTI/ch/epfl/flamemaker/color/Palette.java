package ch.epfl.flamemaker.color;

/**
 * Interface de <code>Palette</code>, qui sont des <code>List</code> de <code>Color</code>.
 * @author  Simon Le Bail-Collet(227359), Louis-Maxence Garret(223892)
 *
 */
public interface Palette {
	/**
	 * @param index Un index de couleur, <code>double</code> compris entre 0 et 1 inclus.
	 * @return la <code>Color</code> correspondant a <code>index</code>, en effectuant un melange entre les <code>Color</code> de la <code>Palette</code> "encadrant" cet index.
	 */
	public Color colorForIndex(double index);
}
