package ch.epfl.flamemaker.geometry2d;

/**Interface de <code>Transformation</code>, qui propose d'implementer la methode <code>transformPoint</code>
 * @author Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 *Interface des Transformation, elle comporte la méthode transformPoint qui selon les transformations
 *change la position de ce point
 */
public interface Transformation {
	/**Calcul de l'application d'une <code>Transformation</code> a un <code>Point</code>.
	 * @param p le <code>Point</code> a transformer
	 * @return un nouveau <code>Point</code> calculer a partir de la <code>Transformation</code>
	 */
	public Point transformPoint(Point p);
}
