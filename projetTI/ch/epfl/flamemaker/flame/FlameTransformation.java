package ch.epfl.flamemaker.flame;
import ch.epfl.flamemaker.geometry2d.*;

/**Classe de <code>FlameTransformation</code>, ayant pour attribut <code>affine</code> une <code>AffineTransformation</code> et un tableau <code>variationWeight</code> de poids de <code>Variation</code>.
 * Implemente l'interface <code>Transformation</code>. 
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public class FlameTransformation implements Transformation{
	/**
	 * une <code>AffineTransformation</code> qui contient la composante affine de la <code>Transformation</code>.
	 */
	private final AffineTransformation affine;
	/**
	 * tableau de <code>double</code> associaciant a chaque <code>Variation</code> predefinies un certain poids.
	 */
	private final double[] variationWeight;

	/**Constructeur de FlameTransformation
	 * @param affine une <code>AffineTransformation</code> qui contient la composante affine de la <code>Transformation</code>.
	 * @param variationWeight tableau de <code>double</code> associaciant a chaque <code>Variation</code> predefinies un certain poids.
	 * @throws IllegalArgumentException si la taille de <code>variationWeight</code> n'est pas egal au nombre de variations; i.e. 6.
	 */
	public FlameTransformation(AffineTransformation affine, double[] variationWeight){
		if(variationWeight.length != 6){
			throw new IllegalArgumentException("le tableau des poids(dans flameTransformation) doit avoir une taille de 6");
		}
		this.affine=new AffineTransformation(affine); 
		this.variationWeight = variationWeight.clone();
	}

	/**Constructeur de copie de <code>FlameTransformation</code> utile dans le constructeur de <code>Flame</code> pour garantir l'immutabilité des classes
	 * @param flame la <code>FlameTransformation</code> a copier en profondeur.
	 */
	public FlameTransformation(FlameTransformation flame){
		affine = new AffineTransformation(flame.affine);
		variationWeight = flame.variationWeight.clone();
	}

	/* (non-Javadoc)
	 * @see ch.epfl.flamemaker.geometry2d.Transformation#transformPoint(ch.epfl.flamemaker.geometry2d.Point) voir ceci pour @param
	 * 
	 * pour chaque i={0,1,2,..,5} variationWeight[i] fait varier l'intensité 
	 * de l'influence qu'a la variation correspondante sur la position du Point sortant
	 * =>On prend une variation d'indice i de la classe variation pour laquelle on utilise transformPoint, on passe en argument le point
	 * déjà transformé par la transformation affine.
	 * 
	 * @return nouveau point resultant de la somme de la composée de AffineTransformation avec des flameTransformation
	 */
	public Point transformPoint(Point p) {
		double x=0;
		double y=0;

		for(int i=0; i<Variation.ALL_VARIATIONS.size(); i++){
			if(variationWeight[i]!=0){
				x+=variationWeight[i]*Variation.ALL_VARIATIONS.get(i).transformPoint(affine.transformPoint(p)).x();
				y+=variationWeight[i]*Variation.ALL_VARIATIONS.get(i).transformPoint(affine.transformPoint(p)).y();
			}
		}
		return new Point(x, y);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * Permet de copier le tableau de variationWeight, pour le rendre immutable depuis l'extérieur
	 */
	public Object clone()
	{
		double[] varFinal = new double[variationWeight.length]; 
		for (int i = 0; i < variationWeight.length; i++) {
			varFinal[i] = variationWeight[i];
		}	
		return varFinal;
	}

	/**
	 *Classe permettant de construire des <code>FlameTransformation</code> de maniere incrementale et sans modifier cette classe.
	 *Cette classe a pour attributs une <code>AffineTransformation</code> et un tableau de <code>double</code> associant des poids aux <code>Variation</code> predefinies.
	 */
	public static class Builder{
		/**
		 * une <code>AffineTransformation</code> qui contient la composante affine de la <code>Transformation</code> a construire.
		 */
		private AffineTransformation affineTransformation;
		/**
		 * tableau de <code>double</code> associaciant a chaque <code>Variation</code> predefinies un certain poids, pour la construction de la future <code>FlamaTransformation</code>. 
		 */
		private double[] variationWeight;

		/**Constructeur pour la classe <code>FlameTransformation.Builder</code>.
		 * @param flame la <code>FlameTransformation</code> avec laquelle on initialise le <code>FlameTransformation.Builder</code>.
		 */
		public Builder(FlameTransformation flame){
			affineTransformation = flame.affine;
			variationWeight = flame.variationWeight;
		}

		/**Accesseur pour l'<code>AffineTransformation</code> du <code>FlameTransformation.Builder</code>.
		 * @return l'<code>AffineTransformation</code> du <code>FlameTransformation.Builder</code> courant. 
		 */
		public AffineTransformation getAffine(){
			return affineTransformation;
		}
		/**Remplace l'<code>AffineTransformation</code> du <code>FlameTransformation.Builder</code> courant par <code>newAffine</code>.
		 * @param newAffine la nouvelle <code>AffineTransformation</code> pour le <code>FlameTransformation.Builder</code> courant.
		 */
		public void setAffineTransformation(AffineTransformation newAffine){
			affineTransformation=newAffine;
		}

		/**Modifie la valeur du poids du <code>FlameTransformation.Builder</code> a l'indice <code>index</code> donné
		 * @param index un <code>int</code> indiquant la position du poids a modifier dans <code>variationWeight</code>.
		 * @param newVariationWeight le nouveau poids <code>double</code> a l'indice <code>index</code>.
		 */
		public void setVariationWeight(int index, double newVariationWeight){
			variationWeight[index]=newVariationWeight;
		}

		/**Renvoie le poids de la <code>Variation</code> à l'indice <code>index</code>.
		 * @param index un <code>int</code> indiquant la position du poids a retourner dans <code>variationWeight</code>.
		 * @return le poids de la <code>Variation</code> à l'indice <code>index</code>.
		 */
		public double variationWeight(int index){
			return variationWeight[index];
		}


		/**
		 * @return une <code>FlameTransformation</code>, construit a partir du <code>FlameTransformation.Builder</code> courant.
		 */
		public FlameTransformation build(){
			return new FlameTransformation(affineTransformation, variationWeight);
		}
	}

}
