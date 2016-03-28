
package ch.epfl.flamemaker.color;

/**
 * Classe des Couleurs, representees par un triplet de <code>double</code> donnant respectivement la composante rouge <code>r</code>, verte <code>g</code> et bleue <code>b</code> de la couleur. Chacune de ces composantes est un <code>double</code> compris entre 0 et 1 (inclus).
 * @author  Simon Le Bail-Collet(227359), Louis-Maxence Garret(223892)
 *
 */
public class Color {
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color WHITE = new Color(1, 1, 1);
	public final static Color RED = new Color(1, 0, 0);
	public final static Color GREEN = new Color(0, 1, 0);
	public final static Color BLUE = new Color(0, 0, 1);

	/**
	 * La composante rouge, representee par un <code>double</code> compris entre 0 et 1.
	 */
	private final double r;
	/**
	 * La composante verte, representee par un <code>double</code> compris entre 0 et 1.
	 */
	private final double g;
	/**
	 * La composante bleue, representee par un <code>double</code> compris entre 0 et 1.
	 */
	private final double b;

	/**
	 * Constructeur de la classe <code>Color</code>
	 * @param r La composante rouge, representee par un <code>double</code> compris entre 0 et 1.
	 * @param g La composante verte, representee par un <code>double</code> compris entre 0 et 1.
	 * @param b La composante bleue, representee par un <code>double</code> compris entre 0 et 1.
	 * @throws IllegalArgumentException si <code>r</code>, <code>g</code>, et/ou <code>b</code> ne sont pas compris entre 0 et 1 inclus.
	 */
	public Color(double r, double g, double b){
		if (r<0 || g<0 || b<0 || r>1 || g>1 || b>1){
			throw new IllegalArgumentException("vos valeurs pour les couleurs sont invalides (doivent appartenir a [0,1]");
		}else{
			this.r=r;
			this.g=g;
			this.b=b;
		}
	}

	/**
	 * @return la composante rouge de la couleur, sous forme de <code>double</code> compris entre 0 et 1 inclus.
	 */
	public double red(){
		return r;
	}
	/**
	 * @return la composante verte de la couleur, sous forme de <code>double</code> compris entre 0 et 1 inclus.
	 */
	public double green(){
		return g;
	}
	/**
	 * @return la composante bleue de la couleur, sous forme de <code>double</code> compris entre 0 et 1 inclus.
	 */
	public double blue(){
		return b;
	}
	/**
	 * @param that La 2eme <code>Color</code> utilisee pour le melange.
	 * @param proportion Un <code>double</code> representant la proportion du melange.
	 * @return une nouvelle <code>Color</code>, obtenue en melangeant la couleur <code>this</code> avec la couleur <code>that</code> selon la proportion <code>proportion</code>.
	 * @throws IllegalArgumentException si <code>proportion</code> n'est pas compris entre 0 et 1 inclus.
	 */
	public Color mixWith(Color that, double proportion){
		if (proportion<0 || proportion>1){
			throw new IllegalArgumentException("la proportion doit appartenir a l'intervalle [0,1]");
		}else{
			return new Color(r*proportion+that.red()*(1-proportion), g*proportion+that.green()*(1-proportion), b*proportion+that.blue()*(1-proportion));
		}
	}
	/**
	 * @return la couleur encodee dans un <code>int</code>, avec chaque composante occupant 8 bits : la rouge (<code>r</code>) occupe les bits 23 a 16, la verte (<code>g</code>) les bits 15 a 8 et la bleue (<code>b</code>) les bits 7 a 0
	 */
	public int asPackedRGB(){ 
		int val = sRGBEncode(b, 255);
		val += (sRGBEncode(g,255)) << 8;
		val += (sRGBEncode(r,255)) << 16;

		return val;
	}
	/**
	 * @param v Un <code>double</code> representant un index de couleur
	 * @param max Un <code>int</code> representant la valeur maximale que peut prendre un index apres encodage avec la formule sRGB
	 * @return la valeur <code>v</code> gamma-encodee au moyen de la formule sRGB, qui est un <code>int</code> compris entre 0 et <code>max</code>.
	 */
	public static int sRGBEncode(double v, int max){
		int retour;
		if(v <= 0.0031308){
			retour = (int)(max * 12.92 * v);
		}
		else{
			retour = (int)(max *(1.055*Math.pow(v, 1.0/2.4) -0.055));
		}
		return retour;
	}
}

