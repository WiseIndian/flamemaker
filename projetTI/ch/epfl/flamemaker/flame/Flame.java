package ch.epfl.flamemaker.flame;

import java.util.ArrayList; 
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.*;

/**
 *Classe <code>Flame</code> qui a pour attribut une <code>List</code> de <code>FlameTransformation</code>. 
 *La classe permet principalement de generer un <code>FlameAccumulator</code> a partir de sa methode <code>compute</code>.
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public class Flame {
	/**
	 * <code>List</code> de <code>FlameTransformation</code> caracterisant la fractale <code>Flame</code>.
	 */
	private final List <FlameTransformation> listTransformations;


	/**
	 * Constructeur de la classe <code>Flame</code>, effectue une copie de <code>someTransformations</code>.
	 * @param someTransformations <code>List</code> de <code>FlameTransformation</code>.
	 */
	public Flame(List<FlameTransformation>someTransformations)
	{
		listTransformations= new ArrayList<FlameTransformation>();

		for(FlameTransformation flameTransfoVar:someTransformations){
			listTransformations.add(new FlameTransformation (flameTransfoVar));
		}
	}
	public FlameTransformation getTransformation(int index){
		if(index<0||index>=listTransformations.size()){
			throw new IndexOutOfBoundsException("valeur de l'index incorrecte");
		}
		else{
			return listTransformations.get(index);
		}
	}
	
	public int listTransformationsSize(){
		return listTransformations.size();
	}
	/**
	 * Permet de generer un <code>FlameAccumulator</code> a partir de l'algorithme du chaos.
	 * On effectue d'abord 20 transformations aleatoirement tirees sur un <code>Point</code> <code>p</code> avec (x,y)=(0,0) auquel a� chaque iteration on associe egalement un index de couleur(<code>indexColorPoint</code>)
	 * dans une premiere boucle <code>for</code>, sans sauvegarder le resultat dans un <code>flameAccumulator.Builder</code>.
	 * Puis on applique ensuite <code>m</code> (<code>int</code> tire de la formule <code>height</code>*<code>width</code>*<code>density</code>) transformations (donc dans une boucle <code>for</code> qui fait <code>m</code> iterations) sur le
	 * <code>Point p</code> (donc sur <code>x</code>, <code>y</code> et son <code>indexColorPoint</code> associe) resultant de la premiere boucle, 
	 * cette fois en stockant a�chaque iteration <code>x y</code> et <code>IndexColorPoint</code> a l'aide de la mathode <code>hit</code> de la classe <code>FlameAccumulator</code>.
	 * @param frame un <code>Rectangle</code> qui determine le cadre dans lequel les <code>Point</code> peuvent etre presents,
	 *  autrement c'est la region ou est calculee la fractale <code>Flame</code>.
	 * @param width largeur <code>int</code> du <code>FlameAccumulator</code>.
	 * @param height hauteur <code>int</code> du <code>FlameAccumulator</code>.
	 * @param density un <code>int</code> utilise pour calculer le nombre d'iterations a effectuer.
	 * @return un <code>FlameAccumulator</code> remplis comme explique precedemment. 
	 */
	public FlameAccumulator compute(Rectangle frame, int width, int height, int density){
		// tableau contenant l'indice de couleur de chaque transformation
		double[] indexColorTransformations= new double[listTransformations.size()]; 
		/* on calcule pour chaque transformation son index de couleur associe
		 * resultat qu'on stocke dans le tableau cree precedemment
		 */
		for (int k = 0; k < indexColorTransformations.length; k++) { //boucle for: car on calcul l'index pour chaque transformation de la liste
			if(k==0){
				indexColorTransformations[k]=0;
			}else if(k==1){
				indexColorTransformations[k]=1;
			}
			else{
				//formule pour calculer l'index de couleur associe a� chaque transformation
				double a = Math.pow(2, Math.ceil(Math.log(k)/Math.log(2)));
				// on stocke l'index de couleur de la transformation dans le tableau a� l'indice correspondant a� l'indice de la transformation de la liste 
				indexColorTransformations[k]=(2*k-1-a)/a;
			}
		}
		Point p = new Point(0,0); 
		int i = 0;
		int m = density*height*width; 
		Random rand = new Random(2013);
		double indexColorPoint=0;

		for (int k=0; k<20; k++){ //on effectue 20 itérations sans sauvegarder le résultat à l'aide de la méthode hit
			i = rand.nextInt(listTransformations.size()); //on choisit une transformation au hasard
			p = listTransformations.get(i).transformPoint(p);
			indexColorPoint=0.5*(indexColorPoint+ indexColorTransformations[i]);
		}

		FlameAccumulator.Builder S = new FlameAccumulator.Builder(frame, width, height);
		for (int j = 0; j <m; j++) {
			i = rand.nextInt(listTransformations.size());
			p = listTransformations.get(i).transformPoint(p);
			indexColorPoint=0.5*(indexColorPoint+ indexColorTransformations[i]);
			S.hit(p,indexColorPoint);
		}
		return S.build();
	}
	/**
	 * Classe permettant de construire des fractales <code>Flame</code> de maniere incrementale sans modifier <code>Flame</code>.
	 *
	 */
	public static class Builder{
		/**
		 * <code>List</code> de <code>FlameTransformation</code> caracterisant la fractale <code>Flame</code> a batir plus tard.
		 */
		private List<FlameTransformation.Builder> flameTransformations= new ArrayList<FlameTransformation.Builder>();

		/**Constructeur de la classe <code>Flame.Builder</code>.
		 * @param flame fractale <code>Flame</code> avec laquelle on initialise le <code>Flame.Builder</code>.
		 */
		public Builder(Flame flame){
			for(FlameTransformation var: flame.listTransformations){
				flameTransformations.add(new FlameTransformation.Builder(var));
			}
		}
		/**
		 * @return la taille de la <code>List</code> de <code>FlameTransformation</code>, i.e. la taille de <code>flameTransformations</code>.
		 */
		public int transformationCount(){
			return flameTransformations.size();
		}
		/** 
		 * Ajoute une <code>FlameTransformation</code> a la <code>List</code> qui est l'attribut de la classe de <code>Flame.Builder</code>.
		 * @param transformation une <code>FlameTransformation</code> a rajouter a la <code>List</code> de <code>FlameTransformation</code>.
		 *
		 */
		public  void addTransformation(FlameTransformation transformation){
			flameTransformations.add(new FlameTransformation.Builder(transformation));
		}

		/**
		 * Lance une erreur si <code>index</code> est invalide.
		 * @param index un <code>int</code> correspondant � un index de la <code>FlameTransformation</code> a� selectionner dans la <code>List</code> de <code>FlameTransformation</code>.
		 * @throws IndexOutOfBoundsException si <code>index</code> est strictement inferieur a 0 ou superieur ou egal a la taille de la <code>List</code> de <code>FlameTransformation</code>.
		 */
		private void errorIndex(int index){
			if(index<0 || index >= flameTransformations.size()){
				throw new IndexOutOfBoundsException("Index invalide");
			}
		}
		/**Supprime la <code>FlameTransformation</code> a la position <code>index</code>, seulement si celui-ci est correct, i.e. test avec la methode <code>errorIndex</code>.
		 * @param index un <code>int</code> pointant la position de la <code>FlameTransformation</code> a supprimer dans la <code>List</code> de <code>FlameTransformation</code>.
		 * 
		 */
		public  void removeTransformation(int index){
			errorIndex(index);
			flameTransformations.remove(index);
		}


		/**Permet d'obtenir une l'<code>AffineTransformation</code> correspondante a une <code>FlameTransformation</code> d'index <code>index</code>.
		 * @param index un <code>int</code> representant la position de la <code>FlameTransformation</code> a� selectionner dans la <code>List</code> de <code>FlameTransformation</code>.
		 * @return la transformation affine associee a�la <code>FlameTransformation</code> situee a la position <code>index</code> dans la <code>List</code> de <code>FlameTransformation</code>, en verifiant que cet index est correct a l'aide de la methode <code>errorIndex</code>.
		 */
		public  AffineTransformation affineTransformation(int index){
			errorIndex(index);
			return flameTransformations.get(index).getAffine();
		}



		/**Change l'<code>AffineTransformation</code> associee a la <code>FlameTransformation</code> d'indice <code>index</code> par <code>newTransformation</code>. (description detaillee dans le code)
		 * 
		 * @param index un <code>int</code> representant la position de la <code>FlameTransformation</code> a� selectionner dans la <code>List</code> de <code>FlameTransformation</code>.
		 * @param newTransformation l'<code>AffineTransformation</code> qui remplacera celle associee a la <code>FlameTransformation</code> d'indice <code>index</code> dans la <code>List</code> de <code>FlameTransformation</code>.
		 */


		/* Description:	 
		 * Pour cela on associe a flameTransformationBuilder un FlameTransformation.Builder ayant pour attributs ceux de la FlameTransformation d'indice "index" de la 
		 * liste flameTransformations(attribut de Flame.Builder)
		 * On utilise flameTransformationBuilder pour creer une flameTransformation grace a la methode build de FlameTransformation.Builder
		 * En utilisant ce qu'on a decrit a la ligne precedente on remplace la FlameTransformation d'indice "index" dans la liste flameTransformations 
		 * par la flameTransformation obtenue avec le flameTransformationBuilder.build
		 */
		public void setAffineTransformation(int index, AffineTransformation newTransformation){
			errorIndex(index);
			FlameTransformation.Builder flameTransformationBuilder = new FlameTransformation.Builder(flameTransformations.get(index).build());
			flameTransformationBuilder.setAffineTransformation(newTransformation);
			flameTransformations.set(index, flameTransformationBuilder);
		}
		/**Renvoie le poids de la <code>Varitation</code> voulu.
		 * @param index un <code>int</code> representant la position de la <code>FlameTransformation</code> a� selectionner dans la <code>List</code> de <code>FlameTransformation</code>.
		 * @param variation la <code>Variation</code> dont on veut le poids.
		 * @return retourne le poids de <code>variation</code>. (description detaillee dans le code)
		 */

		/* Description:
		 * on cree un FlameTransformation.Builder a partir de la FlameTransformation d'indice "index" de la liste flameTransformations
		 * qu'on associe a flameTransformationBuilder.
		 * On utilise ensuite la methode index() de variation pour trouver l'index de la variation passe en argument dans la methode courante.
		 * Index de la variation qui est en fait l'index du poids de la variation.
		 * on a du coup juste a retourner ce poids a l'aide de la methode variationWeight de FlameTransformation.Builder
		 */
		public double variationWeight(int index, Variation variation){
			errorIndex(index);
			FlameTransformation.Builder flameTransformationBuilder = new FlameTransformation.Builder(flameTransformations.get(index).build());
			return flameTransformationBuilder.variationWeight(variation.index());
		}
		/**Sert a remplacer le poids d'une <code>variation</code> par un nouveau.
		 * @param index un <code>int</code> representant la position de la <code>FlameTransformation</code> a� selectionner dans la <code>List</code> de <code>FlameTransformation</code>.
		 * @param variation la <code>Variation</code> dont on veut modifier le poids.
		 * @param newWeight le nouveau poids pour <code>variation</code>.
		 */

		/* Description :
		 * On recupere les attributs de la FlameTransformation d'index donne dans un builder de la classe FlameTransformation => flameTransformationBuilder
		 * On utilise la methode setVariationWeight pour modifier le poids de la variation du builder de Flame.
		 * On a plus qu'a remplacer la FlameTransformation d'indice index, par celle obtenue en appliquant build a flameTransformationBuilder
		 */
		public void setVariationWeight(int index, Variation variation, double newWeight){
			errorIndex(index);
			FlameTransformation.Builder flameTransformationBuilder = new FlameTransformation.Builder(flameTransformations.get(index).build());
			flameTransformationBuilder.setVariationWeight(variation.index(), newWeight);
			flameTransformations.set(index, flameTransformationBuilder);
		}

		/**Construit une <code>Flame</code> a partir du <code>Flame.Builder</code> courant.
		 * @return une <code>Flame</code>, construite a partir de la <code>List</code> de <code>FlameTransformation</code> en attribut du <code>Flame.Builder</code>.
		 */
		public  Flame build(){
			List<FlameTransformation> tempList = new ArrayList<FlameTransformation>();
			for(FlameTransformation.Builder var: flameTransformations){
				tempList.add(var.build());
			}
			return new Flame(tempList);
		}
	}
}



