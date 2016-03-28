package ch.epfl.flamemaker.geometry2d;


/**Classe de <code>Rectangle</code>, defini par un centre et par sa hauteur et sa largeur.
 * @author Simon Le Bail-Collet(227359), Louis-Maxence Garret(223892)
 */
public class Rectangle {
	/**
	 * largeur du <code>Rectangle</code>.
	 */
	private final double width;
	/**
	 * hauteur du <code>Rectangle</code>.
	 */
	private final double height;
	/**
	 * centre du <code>Rectangle</code>, les coordonnees sont evidemment cartesiennes.
	 */
	private final Point center;

	/**
	 * Constructeur de la classe <code>Rectangle</code>
	 * @param center centre du <code>Rectangle</code>, les coordonnees sont evidemment cartesiennes.
	 * @param width largeur du <code>Rectangle</code>.
	 * @param height hauteur du <code>Rectangle</code>.
	 * @throws IllegalARugmentException si <code>widht</code> et/ou <code>height</code> sont negatifs ou nuls ou des valeurs non valides.
	 */
	public Rectangle(Point center, double width, double height){
		if(width<=0 || height<=0 || width==Double.NaN || height==Double.NaN){
			throw new IllegalArgumentException();
		}else{
			this.center=center;
			this.width=width;
			this.height=height;
		}
	}
	

	/**Renvoie la plus petite coordonnee <code>x</code> du <code>Rectangle</code>. 
	 * @return la plus petite coordonnee <code>x</code> du <code>Rectangle</code>. 
	 */
	public double left(){
		return(center.x() -width/2);
	}
	/**Renvoie la plus grande coordonnee <code>x</code> du <code>Rectangle</code>. 
	 * @return la plus grande coordonnee <code>x</code> du <code>Rectangle</code>. 
	 */
	public double right(){
		return(center.x()+ width/2);
	}
	/**Renvoie la plus petite coordonnee <code>y</code> du <code>Rectangle</code>. 
	 * @return la plus petite coordonnee <code>y</code> du <code>Rectangle</code>. 
	 */
	public double bottom(){
		return(center.y()-height/2);
	}

	/**Renvoie la plus grande coordonnee <code>y</code> du <code>Rectangle</code>. 
	 * @return la plus grande coordonnee <code>y</code> du <code>Rectangle</code>. 
	 */
	public double top(){
		return(center.y() + height/2);
	}

	/**Renvoie la largeur <code>width</code> du <code>Rectangle</code>.
	 * @return la largeur <code>width</code> du <code>Rectangle</code>.
	 */
	public double width(){	
		return width;
	}

	/**Renvoie la hauteur <code>height</code> du <code>Rectangle</code>.
	 * @return la hauteur <code>height</code> du <code>Rectangle</code>.
	 */
	public double height(){
		return height;
	}

	/**Renvoie le centre <code>center</code> du <code>Rectangle</code>.
	 * @return le centre <code>center</code> du <code>Rectangle</code>.
	 */
	public Point center(){
		return center;
	}

	/**Teste si un <code>Point</code> est contenu dans le <code>Rectangle</code>.
	 * @param p le <code>Point</code> dont l'appartenance au <code>Rectangle</code> est testee.
	 * @return <code>true</code> si et seulement si <code>p</code> est dans le <code>Rectangle</code>. On considere que les cotes haut et droite n'appartiennent pas au <code>Rectangle</code>.
	 */
	public boolean contains(Point p){
		return(p.y()<top() && p.y() >= bottom() && p.x() >= left() && p.x() < right());
	}

	/**Renvoie le rapport largeur/hauteur (donc <code>width</code>/<code>height</code>) du <code>Rectangle</code> 
	 * @return le rapport largeur/hauteur (donc <code>width</code>/<code>height</code>) du <code>Rectangle</code>.
	 */
	public double aspectRatio(){
		return width/height;
	}

	/**Cree un nouveau <code>Rectangle</code>, le plus petit <code>Rectangle</code> possible qui contient le recepteur et ayant le même centre <code>center</code> que le récepteur, et  
	 *  le nouveau rapport <code>width</code>/<code>heigth</code> : <code>aspectRatio</code>.
	 * @param aspectRatio le ratio voulu pour le nouveau <code>Rectangle</code>.
	 * @return le plus petit <code>Rectangle</code> possible qui contient le recepteur et ayant le même centre <code>center</code> que le récepteur, et  
	 *  le nouveau rapport <code>width</code>/<code>heigth</code> : <code>aspectRatio</code>.
	 * @throws IllegalArgumentException si <code>aspectRatio</code> n'est pas un <code>double</code> (ou <code>int</code>) et si <code>aspectRatio</code> est negatif ou nul.
	 */
	public Rectangle expandToAspectRatio(double aspectRatio){
		if(aspectRatio== Double.NaN || aspectRatio<=0){
			throw new IllegalArgumentException();
		}
		double finalHeight;
		double finalWidth;
		if(aspectRatio < aspectRatio()){ //ici width < height, donc on fixe width et on agrandit height pour obtenir le nouveau ratio; ainsi on est sur d'avoir le plus petit rectangle contenant le recepteur (car width du recepteur=width du nouveau rectangle)
			finalWidth = width;
			finalHeight = width/aspectRatio;
		}else{
			finalHeight =height;//ici width > height, donc on fixe height et on agrandit width pour obtenir le nouveau ratio; ainsi on est sur d'avoir le plus petit rectangle contenant le recepteur (car height du recepteur=height du nouveau rectangle)
			finalWidth = height*aspectRatio; // produit en croix : base sur le fait qu'on veut que R=lengthFinal/heightFinal			
		}
		return new Rectangle(center, finalWidth, finalHeight);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * ici retourne une chaîne de caractère de la forme: ((x,y),width,hauteur)
	 */
	public String toString(){
		return "("+center.toString()+","+width+","+height+")";
	}
}