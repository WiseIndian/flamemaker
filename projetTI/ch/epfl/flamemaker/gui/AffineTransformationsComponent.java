/*
 * Auteurs (sciper): Simon Le Bail-Collet (227359), Louis-Maxence Garret (223892)
 *
 */
package ch.epfl.flamemaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import ch.epfl.flamemaker.gui.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

@SuppressWarnings("serial")
public class AffineTransformationsComponent extends JComponent{
	private ObservableFlameBuilder builder;
	private Rectangle frame;
	private int highlightedTransformationIndex;

	public AffineTransformationsComponent(ObservableFlameBuilder builder, Rectangle frame){
		this.builder=builder;
		this.frame=frame;
		
		builder.addObserver(new ObservableFlameBuilder.Observer(){
			public void update(){repaint();}
		});
	}

	public int getHighlightedTransformationIndex(){
		return highlightedTransformationIndex;
	}

	public void setHighlightedTransformationIndex(int value){
		highlightedTransformationIndex=value;
		repaint();
	}
	public void paintComponent(Graphics g0){
		Rectangle newFrame=frame.expandToAspectRatio((double)getWidth()/(double)getHeight());
		AffineTransformation translation =AffineTransformation.newTranslation(getWidth()/2-frame.center().x()*(getWidth()/newFrame.width()), getHeight()/2-frame.center().y()*(getHeight()/newFrame.height()));
		AffineTransformation scaling = AffineTransformation.newScaling(getWidth()/newFrame.width(), -getHeight()/newFrame.height());
		AffineTransformation conversionCoord=translation.composeWith(scaling);
		
		
		/*boucle qui permet de quadriller, en creant deux axes verticaux a chaque iteration*/
		g0.setColor(Color.LIGHT_GRAY);
		for(int i=1; i<newFrame.height()+1; i++){
			/* creation des points definissant les axes verticaux*/
			Point startPoint1 = conversionCoord.transformPoint(new Point(newFrame.width()+1, i));
			Point endPoint1 = conversionCoord.transformPoint(new Point(-newFrame.width()-1, i));
			
			Point startPoint2 = conversionCoord.transformPoint(new Point(newFrame.width()+1, -i));
			Point endPoint2 = conversionCoord.transformPoint(new Point(-newFrame.width()-1, -i));
			
			/*dessin de ces axes*/
			((Graphics2D)g0).draw(new Line2D.Double(startPoint1.x(), startPoint1.y(), endPoint1.x(), endPoint1.y()));
			((Graphics2D)g0).draw(new Line2D.Double(startPoint2.x(), startPoint2.y(), endPoint2.x(), endPoint2.y()));
		}
		
		/*boucle qui permet de quadriller, en creant deux axes horizontaux a chaque iteration*/
		for (int i=0; i<newFrame.width(); i++){
			/* creation des points definissant les axes horizontaux*/
			Point startPoint1 =conversionCoord.transformPoint( new Point(i, newFrame.height()+1));
			Point endPoint1 = conversionCoord.transformPoint(new Point(i, -newFrame.height()-1));
			
			Point startPoint2 = conversionCoord.transformPoint(new Point(-i, newFrame.height()+1));
			Point endPoint2 = conversionCoord.transformPoint(new Point(-i, -newFrame.height()-1));
			
			((Graphics2D)g0).draw(new Line2D.Double(startPoint1.x(), startPoint1.y(), endPoint1.x(), endPoint1.y()));			
			((Graphics2D)g0).draw(new Line2D.Double(startPoint2.x(), startPoint2.y(), endPoint2.x(), endPoint2.y()));
			
		}
		/*on cree ici les points de debut et de de fin des axes du repere*/
		Point axeXstart = conversionCoord.transformPoint(new Point(newFrame.width()+1, 0));
		Point axeXend = conversionCoord.transformPoint(new Point(-newFrame.width()-1, 0));
		Point axeYstart = conversionCoord.transformPoint(new Point(0,newFrame.height()+1));
		Point axeYend = conversionCoord.transformPoint(new Point(0, -newFrame.height()-1));
		
		/*les axes du repere*/
		Line2D.Double axeX = new Line2D.Double(axeXstart.x(), axeXstart.y(), axeXend.x(), axeXend.y());
		Line2D.Double axeY = new Line2D.Double(axeYstart.x(), axeYstart.y(), axeYend.x(), axeYend.y());

		((Graphics2D)g0).setColor(Color.WHITE);
		((Graphics2D)g0).draw(axeX);
		((Graphics2D)g0).draw(axeY);

		/*boucle qui associe a chaque trqnsformation, exceptee celle selectionnee, une croix flechee representant la transformation affine*/
		for (int i=0; i<builder.transformationCount(); i++){
			if(i!=highlightedTransformationIndex){
				/*creation des points definissant la croix, puis de ceux definissant la fleche*/
				Point startPoint1 = new Point(-1,0);
				Point endPoint1 = new Point(1,0);
				Point startPoint2 = new Point(0,-1);
				Point endPoint2 = new Point(0, 1);
				
				Point pointAArrow1 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(new Point(endPoint1.x()-0.1, endPoint1.y()+0.1)));
				Point pointBArrow1 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(new Point(endPoint1.x()-0.1, endPoint1.y()-0.1)));
				
				Point pointAArrow2 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(new Point(endPoint2.x()+0.1, endPoint2.y()-0.1)));
				Point pointBArrow2 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(new Point(endPoint2.x()-0.1, endPoint2.y()-0.1)));
				
				/*on applique ici la transformation aux points de la croix flechee*/
				startPoint1 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(startPoint1));
				endPoint1 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(endPoint1));
				startPoint2 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(startPoint2));
				endPoint2 = conversionCoord.transformPoint(builder.affineTransformation(i).transformPoint(endPoint2));
				
				
				
				g0.setColor(Color.BLACK);
				
				/*dessin de cette croix*/
				((Graphics2D)g0).draw(new Line2D.Double(startPoint1.x(),  startPoint1.y(),endPoint1.x(), endPoint1.y()));
				((Graphics2D)g0).draw(new Line2D.Double(pointAArrow1.x(), pointAArrow1.y(), endPoint1.x(), endPoint1.y()));
				((Graphics2D)g0).draw(new Line2D.Double(pointBArrow1.x(), pointBArrow1.y(), endPoint1.x(), endPoint1.y()));

				((Graphics2D)g0).draw(new Line2D.Double(startPoint2.x(),  startPoint2.y(),endPoint2.x(), endPoint2.y()));
				((Graphics2D)g0).draw(new Line2D.Double(pointAArrow2.x(), pointAArrow2.y(), endPoint2.x(), endPoint2.y()));
				((Graphics2D)g0).draw(new Line2D.Double(pointBArrow2.x(), pointBArrow2.y(), endPoint2.x(), endPoint2.y()));

			}
		}
		/*on fait la meme chose que dans la boucle precedente, mais pour la transformation selectionnee : dessinee en rouge, elle sera ainsi par dessus les autres*/
		Point startPoint1 = new Point(-1,0);
		Point endPoint1 = new Point(1,0);
		Point startPoint2 = new Point(0,-1);
		Point endPoint2 = new Point(0, 1);
		
		Point pointAArrow1 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(new Point(endPoint1.x()-0.1, endPoint1.y()+0.1)));
		Point pointBArrow1 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(new Point(endPoint1.x()-0.1, endPoint1.y()-0.1)));
		
		Point pointAArrow2 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(new Point(endPoint2.x()+0.1, endPoint2.y()-0.1)));
		Point pointBArrow2 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(new Point(endPoint2.x()-0.1, endPoint2.y()-0.1)));
		

		startPoint1 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(startPoint1));
		endPoint1 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(endPoint1));
		startPoint2 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(startPoint2));
		endPoint2 = conversionCoord.transformPoint(builder.affineTransformation(highlightedTransformationIndex).transformPoint(endPoint2));

		g0.setColor(Color.RED);

		((Graphics2D)g0).draw(new Line2D.Double(startPoint1.x(),  startPoint1.y(),endPoint1.x(), endPoint1.y()));
		((Graphics2D)g0).draw(new Line2D.Double(pointAArrow1.x(), pointAArrow1.y(), endPoint1.x(), endPoint1.y()));
		((Graphics2D)g0).draw(new Line2D.Double(pointBArrow1.x(), pointBArrow1.y(), endPoint1.x(), endPoint1.y()));

		((Graphics2D)g0).draw(new Line2D.Double(startPoint2.x(),  startPoint2.y(),endPoint2.x(), endPoint2.y()));
		((Graphics2D)g0).draw(new Line2D.Double(pointAArrow2.x(), pointAArrow2.y(), endPoint2.x(), endPoint2.y()));
		((Graphics2D)g0).draw(new Line2D.Double(pointBArrow2.x(), pointBArrow2.y(), endPoint2.x(), endPoint2.y()));

	}
	public Dimension getPreferredSize(){
		return new Dimension(200, 100);
	}
}
