package ch.epfl.flamemaker.flame;
import ch.epfl.flamemaker.color.*;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**Classe de <code>FlameAccumulator</code>, ayant pour attribut <code>hitCount</code> un tableau d'<code>int</code> comptabilisant le nombre de point dans une certaine zone par quadrillage, <code>colorIndexSum</code> tableau de <code>double</code>qui permet d'obtenir la couleur d'une case, et <code>maxCount</code> qui represente le nombre maximum de point dans une case de l'accumulateur.
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public class FlameAccumulator {
	/**
	 * un tableau bidimensionnel d'<code>int</code> permettant de compter le nombres de <code>Point</code> associes a chaque pixel de l'image.
	 */
	private int [][]hitCount;
	/**
	 * un tableau bidimensionnel de <code>double</code> qui permet de stocker l'index de couleur pour chaque pixel de l'image.
	 */
	private double[][] colorIndexSum;
	/**
	 * correspond a la valeur maximale <code>int</code> que peut atteindre une case du tableau <code>hitCount</code>.
	 */
	private int maxCount;

	/**Constructeur de <code>FlameAccumulator</code>, effectuant une copie profonde des deux tableaux attributs.
	 * @param hitCount un tableau bidimensionnel d'<code>int</code> permettant de compter le nombres de <code>Point</code> associes a chaque pixel de l'image.
	 * @param colorIndexSum un tableau bidimensionnel de <code>double</code> qui permet de stocker l'index de couleur pour chaque pixel de l'image.
	 */
	private FlameAccumulator(int[][]hitCount, double[][] colorIndexSum){
		int[][]hitCountCopy= new int[hitCount.length][hitCount[0].length];
		double[][] colorIndexSumCopy= new double[colorIndexSum.length][colorIndexSum[0].length];
		maxCount=0;

		for (int i = 0; i < hitCount.length; i++) {
			for (int j = 0; j < hitCount[0].length; j++) {
				hitCountCopy[i][j] = hitCount[i][j];
				colorIndexSumCopy[i][j] = colorIndexSum[i][j];
				if(hitCount[i][j]>maxCount){
					maxCount = hitCount[i][j];
				}
			}
		}
		this.colorIndexSum=colorIndexSumCopy;
		this.hitCount=hitCountCopy;
	}
	/**
	 * @return la largeur du tableau <code>hitCount</code> qui est aussi la largeur de la future image.
	 */
	public int width(){
		return hitCount.length;
	}
	/**
	 * @return la hauteur du tableau <code>hitCount</code> qui est aussi la largeur de la future image.
	 */
	public int height(){
		return hitCount[0].length;
	}
	/**Calcul de l'intensite en fonction du nombre de <code>Point</code> presents dans la case designee du <code>FlameAccumulator</code>.
	 * @param x l'abscisse <code>int</code> de la case du <code>FlameAccumulator</code> dont on veut calculer l'intensite.
	 * @param y l'ordonnee <code>int</code> de la case du <code>FlameAccumulator</code> dont on veut calculer l'intensite.
	 * @return l'intensite <code>double</code> correspondant a la case (<code>x</code>,<code>y</code>) selon la formule logarithmique donnee. (plus de details dans le code)
	 * @throws IndexOutOfBoundException si les coordonnees <code>x</code> et <code>y</code> ne peuvent correspondre a une position dans le <code>FlameAccumulator</code>.
	 */
	public double intensity(int x, int y){
		if(x<0|| x >width()||y<0||y>height()){
			throw new IndexOutOfBoundsException("coordonnees dans Flame accumulator en dehors du cadre");
		}
		return Math.log(hitCount[x][y]+1)/Math.log(maxCount+1);
	}
	/**Renvoie la <code>Color</code> de la case du <code>FlameAccumulator</code> aux coordonnées <code>x</code> et <code>y</code> données. (plus de details dans le code)
	 * @param palette la <code>Palette</code> qui definit quelles nuances de <code>Color</code> pourront etre obtenues.
	 * @param background la <code>Color</code> de fond d'une case. Si il n'y a pas de <code>Point</code> dans une case, <code>background</code> est retournee.
	 * @param x l'abscisse <code>int</code> de la case du <code>FlameAccumulator</code> dont on veut calculer la <code>Color</code>.
	 * @param y l'ordonnee <code>int</code> de la case du <code>FlameAccumulator</code> dont on veut calculer la <code>Color</code>.
	 * @return la la <code>Color</code> de la case determinee par un melange entre <code>background</code> et la <code>Color</code> moyenne des points.
	 */

	/* Description :
	 * Le calcul est fait de la maniere suivante:
	 * si hitCount=0, alors on renvoie la couleur de fond
	 * sinon on renvoie la couleur obtenue en melangeant la couleur de fond avec la couleur moyenne des points de la case
	 * et cela avec une proportion determinee par la methode intensity(int x,int y)
	 */
	public Color color(Palette palette, Color background, int x, int y){
		if(x>=hitCount.length || y>=hitCount[0].length || x<0 || y<0){
			throw new IndexOutOfBoundsException("en dehors du cadre de l'accumulateur");
		}else if (hitCount[x][y]==0){
			return background;
		}else{
			return palette.colorForIndex(colorIndexSum[x][y]/hitCount[x][y]).mixWith(background,intensity(x,y));
		}
	}

	/**Classe permettant de construire des <code>FlameAccumulator</code> de manieres incrementales sans modifier <code>FlameAccumulator</code>.
	 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
	 */
	static class Builder{
		/**
		 * cadre (ou <code>Rectangle</code>) delimitant la region a laquelle on s'interesse; un passage des coordonees de <code>frame</code> a <code>builderCount</code> se fera par l'<code>AffineTransformation</code> <code>conversionCoord</code> calculee dans ce constructeur.
		 */
		private Rectangle frame;
		/**
		 * tableau bidimensionnel d'<code>int</code> comptant le nombre de <code>Point</code> dans chacune de ses cases.
		 */
		private int[][]builderCount;
		/**
		 * tableau bidimensionnel de <code>double</code> accumulant les index de couleur des <code>Point</code> contenus dans chacune de ses cases.
		 */
		private double[][]colorBuilder;
		/**
		 * une <code>AffineTransformation</code> permettant de convertir les coordonnees de <code>Point</code> dans <code>frame</code> a des coordonnees dans le tableau <code>builderCount</code>. Cette composee de matrice sera calculee dans le constructeur de cette classe.
		 */
		private AffineTransformation conversionCoord = AffineTransformation.IDENTITY;

		/**Constructeur de la classe <code>FlameAccumulator.Builder</code>. Construit en plus des deux deux tableaux attributs <code>builderCount</code> (qui stockera le nombre <code>int</code> de <code>Point</code> dans chaque case) et <code>colorBuilder</code> (qui permettra de calculer la <code>Color</code> de chaque case); une <code>AffineTransformation</code> permettant une conversion de coordonnees dans un <code>Rectangle</code> aux coordonnees dans les tableaux attributs. (plus de details dans le code)
		 * @param frame cadre (ou <code>Rectangle</code>) delimitant la region a laquelle on s'interesse; un passage des coordonees de <code>frame</code> a <code>builderCount</code> se fera par l'<code>AffineTransformation</code> <code>conversionCoord</code> calculee dans ce constructeur.
		 * @param width largeur <code>int</code> des deux tableaux.
		 * @param height hauteur <code>int</code> des deux tableaux.
		 * @throws IllegalArgumentException si la hauteur <code>height</code> et/ou la largeur <code>width</code> sont inferieures ou egales a 0.
		 */

		/* Description :
		 * On construit les deux tableaux sans mettre de valeurs dans leurs cases(role de hit(Point p, double indexColor))
		 * 
		 * ensuite on cree une transformation affine composee d'une translation et d'une remise a l'echelle determinees 
		 * la remise a l'echelle determinee par la hauteur du rectangle et des tableaux, et par la largeur du rectangle et
		 * des tableaux.
		 * Ce qui caracterise la translation est l'oppose de la largeur du rectangle, et l'oppose de la hauteur de ce meme rectangle
		 * pour que le point en bas a gauche du rectangle soit centre sur l'origine.
		 * 
		 */
		public Builder(Rectangle frame, int width, int height){
			if(width <=0 || height <=0){
				throw new IllegalArgumentException();
			}
			else{
				this.frame=frame;
				builderCount = new int[width][height];
				colorBuilder = new double[width][height];
				AffineTransformation frameTranslation = AffineTransformation.newTranslation(-frame.left(), -frame.bottom());
				conversionCoord =AffineTransformation.newScaling(width/frame.width(), height/frame.height()).composeWith(frameTranslation);
			}
		}

		/**
		 *Si le <code>Point</code> <code>p</code> est dans une case du <code>FlameAccumulatorBuilder</code>, cette methode incremente la case de <code>builderCount</code>
		 *et ajoute a la case correspondante de <code>colorBuilder</code> l'<code>indexColor</code> de <code>p</code>. Une conversion de coordonnees est effectuee pour faire correspondre les coordonnees de <code>p</code> a la case a remplir.
		 * 
		 * @param p le <code>Point</code> auquel on s'interesse.
		 * @param indexColor l'index de couleur du <code>Point p</code>.
		 */
		public void hit(Point p, double indexColor){
			if(frame.contains(p)){
				builderCount[(int)Math.floor(conversionCoord.transformPoint(p).x())][(int)Math.floor(conversionCoord.transformPoint(p).y())]++;
				colorBuilder[(int)Math.floor(conversionCoord.transformPoint(p).x())][(int)Math.floor(conversionCoord.transformPoint(p).y())]+=indexColor;
			}
		}

		/**
		 * @return un <code>FlameAccumulator</code>, construit a partir de <code>builderCount</code> et de <code>colorBuilder</code>.
		 */
		public FlameAccumulator build(){
			return new FlameAccumulator(builderCount, colorBuilder);
		}
	}
}
