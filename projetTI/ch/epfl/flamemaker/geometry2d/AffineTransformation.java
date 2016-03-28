package ch.epfl.flamemaker.geometry2d;

/**Classe d'<code>AffineTransformation</code>, representee par une matrice d'<code>int</code>.
 * Avec <code>a</code>,<code>b</code>,<code>c</code>,<code>d</code> la matrice A et <code>c</code>,<code>f</code> la matrice B telles que la matrice de l'<code>AffineTransformation</code> est F = Ax + b
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public class AffineTransformation implements Transformation{
	/**
	 * valeur <code>double</code> du membre a la position (0,0) dans la matrice.
	 */
	private final double a;
	/**
	 * valeur <code>double</code> du membre a la position (0,1) dans la matrice.
	 */
	private final double b;
	/**
	 * valeur <code>double</code> du membre a la position (0,2) dans la matrice.
	 */
	private final double c;
	/**
	 * valeur <code>double</code> du membre a la position (1,0) dans la matrice.
	 */
	private final double d;
	/**
	 * valeur <code>double</code> du membre a la position (1,1) dans la matrice.
	 */
	private final double e;
	/**
	 * valeur <code>double</code> du membre a la position (1,2) dans la matrice.
	 */
	private final double f;

	/**
	 * representation de la matrice Identit� en <code>AffineTransformation</code>.
	 */
	public static final AffineTransformation IDENTITY = new AffineTransformation(1,0,0,0,1,0);

	/**Constructeur de la classe <code>AffineTransformation</code>.
	 * @param a valeur <code>double</code> du membre a la position (0,0) dans la matrice.
	 * @param b valeur <code>double</code> du membre a la position (0,1) dans la matrice.
	 * @param c valeur <code>double</code> du membre a la position (0,2) dans la matrice.
	 * @param d valeur <code>double</code> du membre a la position (1,0) dans la matrice.
	 * @param e valeur <code>double</code> du membre a la position (1,1) dans la matrice.
	 * @param f valeur <code>double</code> du membre a la position (1,2) dans la matrice.
	 */
	public AffineTransformation(double a, double b, double c, double d, double e, double f){
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.e=e;
		this.f=f;
	}
	/**Constructeur de copie d'<code>AffineTransformation</code>.
	 * @param original l'<code>AffineTransfoemation</code> a copier.
	 */
	public AffineTransformation(AffineTransformation original)
	{
		this.a = original.a;
		this.b = original.b;
		this.c = original.c;
		this.d = original.d;
		this.e = original.e;
		this.f = original.f;
	}

	/**Cree une matrice de translation de <code>dx</code> et <code>dy</code> unites.
	 * @param dx nombres d'unites pour la translation parallelement a l'axe des abscisses.
	 * @param dy nombres d'unites pour la translation parallelement a l'axe des ordonnees.
	 * @return une nouvelle <code>AffineTransformation</code>, une translation de <code>dx</code> unit�s parall�lement � l'abscisse et <code>dy</code> unit�s parall�lement � l'ordonn�e .
	 */
	public static AffineTransformation newTranslation(double dx, double dy){
		return new AffineTransformation(1, 0, dx, 0, 1, dy);
	}

	/**Cree une matrice de rotation d'angle <code>theta</code>.
	 * @param theta angle de la rotation
	 * @return une nouvelle<code>AffineTransformation</code>, une rotation d'angle <code>theta</code>.
	 */
	public static AffineTransformation newRotation(double theta){
		return new AffineTransformation(Math.cos(theta), -Math.sin(theta), 0, Math.sin(theta), Math.cos(theta), 0);

	}
	/**Cree une matrice de dilatation de facteur <code>sx</code> et <code>sy</code>.
	 * @param sx facteur <code>double</code> de dilatation horizontale.
	 * @param sy facteur <code>double</code> de dilatation verticale.
	 * @return une nouvelle <code>AffineTransformation</code>, une dilatation d'un facteur <code>sx</code> parall�lement � l'abscisse et d'un facteur <code>sy</code> parall�lement � l'ordonn�e
	 */
	public static AffineTransformation newScaling(double sx, double sy){
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}
	/**Cree une matrice de transvection d'un facteur <code>sx</code> parall�lement � l'axe des abscisses.
	 * @param sx facteur de transvection parall�lement � l'axe des abscisses.
	 * @return une matrice de transvection d'un facteur <code>sx</code> parall�lement � l'axe des abscisses.
	 */
	public static AffineTransformation newShearX(double sx){
		return new AffineTransformation(1, sx, 0, 0, 1, 0);	
	}
	/**Cree une matrice de transvection d'un facteur <code>sy</code> parall�lement � l'axe des ordonnees.
	 * @param sy facteur de transvection parall�lement � l'axe des ordonnees.
	 * @return une matrice de transvection d'un facteur <code>sy</code> parall�lement � l'axe des ordonnees.
	 */
	public static AffineTransformation newShearY(double sy){
		return new AffineTransformation(1, 0, 0, sy, 1, 0);
	}

	/* (non-Javadoc)
	 * @see ch.epfl.flamemaker.geometry2d.Transformation#transformPoint(ch.epfl.flamemaker.geometry2d.Point)
	 */
	public Point transformPoint(Point p){
		return new Point(a*p.x()+b*p.y()+c, d*p.x()+e*p.y()+f);
	}
	/**Renvoie la composante horizontale de l'<code>AffineTransformation<code> : <code>c</code>.
	 * @return la composante horizontale de l'<code>AffineTransformation<code> : <code>c</code>.
	 */
	public double translationX(){
		return c;
	}
	/**Renvoie la composante verticale de l'<code>AffineTransformation<code> : <code>f</code>.
	 * @return la composante verticale de l'<code>AffineTransformation<code> : <code>f</code>.
	 */
	public double translationY(){
		return f;
	}
	/**Permet de composer entre deux <code>AffineTransformation</code> en multipliant celles-ci.
	 * @param that l'autre <code>AffineTransformation</code> avec laquelle va etre calculee la nouvelle <code>AffineTransformation</code>.
	 * @return la multiplication de la matrice courante avec la matrice <code>that</code>.
	 */
	public AffineTransformation composeWith(AffineTransformation that){
		return new AffineTransformation(a*that.a+b*that.d, a*that.b+b*that.e, a*that.c+b*that.f+c,d*that.a+e*that.d, d*that.b+e*that.e, d*that.c+e*that.f+f);
	}
}
