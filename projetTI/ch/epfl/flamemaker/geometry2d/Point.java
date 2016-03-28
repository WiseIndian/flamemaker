package ch.epfl.flamemaker.geometry2d;

/**Classe de <code>Point</code>,  cararcterise par ses coordonnees cartesiennes <code>x</code> et <code>y</code>.
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public class Point {
	/**
	 * origine du repere
	 */
	public final static Point ORIGIN = new Point (0,0);
	/**
	 * abscisse du <code>Point</code>.
	 */
	private final double x;
	/**
	 * ordonnee du <code>Point</code>.
	 */
	private final double y;

	/**Constructeur de la classe <code>Point</code>.
	 * @param x abscisse du <code>Point</code>.
	 * @param y ordonnee du <code>Point</code>.
	 */
	public Point(double x, double y){
		this.x=x;
		this.y=y;
	}

	/**Renvoie l'abscisse  <code>x</code> du <code>Point</code>.
	 * @return l'abscisse  <code>x</code> du <code>Point</code>.
	 */
	public double x(){
		return x;
	}
	/**Renvoie l'ordonnee  <code>y</code> du <code>Point</code>.
	 * @return Renvoie l'ordonnee  <code>y</code> du <code>Point</code>.
	 */
	public double y(){
		return y;
	}
	/**Renvoie la coordonnee polaire <code>r</code> du <code>Point</code>.
	 * @return la distance par à rapport à l'origine de ce point, c'est à dire la coordonnee polaire <code>r</code> du <code>Point</code>.
	 */
	public double r(){
		return Math.sqrt(x*x + y*y);
	}
	/**Renvoie angle définit par les coordonnées polaires du point, c'est a dire l'angle entre l'axe des abscisses et l'axe (<code>ORIGIN</code><code> Point</code>).
	 * @return retourne l'angle définit par les coordonnées polaires du point, c'est a dire l'angle entre l'axe des abscisses et l'axe (<code>ORIGIN</code><code> Point</code>).
	 */
	public double theta(){
		return Math.atan2(y, x);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * retourne simplement une chaîne de caractère de la forme (x,y)
	 */
	public String toString(){
		return "("+x+","+y+")";
	}
}