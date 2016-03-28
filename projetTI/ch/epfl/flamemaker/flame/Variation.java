package ch.epfl.flamemaker.flame;
import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.*;

/**Classe abstraite de <code>Variation</code>, ayant pour attribut un <code>String</code> <code>name</code> et un <code>int index</code>. Implemente l'interface <code>Transformation</code>.
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 */
public abstract class Variation implements Transformation {
	/**
	 * le nom de la <code>Variation</code>.
	 */
	private final String name;
	/**
	 * un <code>int</code> qui est l'indice de la <code>Variation</code>.
	 */
	private final int index;

	/**Constructeur de la classe <code>Variation</code>.
	 * @param index un <code>int</code> qui est l'indice de la <code>Variation</code>.
	 * @param name <code>String</code> qui est le nom de la <code>Variation</code>.
	 */
	private Variation(int index, String name) {
		this.index=index;
		this.name=name;
	}

	/**Accesseur pour le nom de la <code>Variation</code>.
	 * @return le nom (<code>String name</code>) de la <code>Variation</code>.
	 */
	public String name(){ 
		return name; 
	}
	/**Accesseur pour l'indice de la <code>Variation</code>.
	 * @return l'indice (<code>int index</code>) de la <code>Variation</code>.
	 */
	public int index(){ 
		return index; 
	}

	/* (non-Javadoc)
	 * @see ch.epfl.flamemaker.geometry2d.Transformation#transformPoint(ch.epfl.flamemaker.geometry2d.Point)
	 * 
	 */
	abstract public Point transformPoint(Point p);

	/**
	 * <code>List</code> de <code>Variation</code> avec leurs propres methodes <code>transformPoint</code>, qui ont toutes un <code>index</code> et un <code>name</code>.
	 */
	public final static List<Variation> ALL_VARIATIONS = Arrays.asList(
			(new Variation(0, "Linear"){ 
				public Point transformPoint(Point p) {
					return new Point (p.x() , p.y());}

			})

			,(new Variation(1, "Sinusoidal"){ 
				public Point transformPoint(Point p) {
					return new Point (Math.sin(p.x()) , Math.sin(p.y()));}
			})

			,(new Variation(2, "Spherical"){ 
				public Point transformPoint(Point p) {
					return new Point (p.x()/(p.r()*p.r()) , p.y()/(p.r()*p.r()));}
			})

			,(new Variation(3, "Swirl"){ 
				public Point transformPoint(Point p) {
					return new Point (p.x()*Math.sin(p.r()*p.r())-p.y()*Math.cos(p.r()*p.r())  ,  p.x()*Math.cos(p.r()*p.r())+p.y()*Math.sin(p.r()*p.r()));}
			}) 

			,(new Variation(4, "Horseshoe"){
				public Point transformPoint(Point p) {
					return new Point (((p.x()-p.y())*(p.x()+p.y()))/p.r() , (p.y()*p.x()*2)/p.r());} 
			})

			,(new Variation(5, "Bubble"){ 
				public Point transformPoint(Point p) {
					return new Point ((4*p.x())/(p.r()*p.r()+4) , (4*p.y())/(p.r()*p.r()+4));}
			})

			);


}
