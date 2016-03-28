/*
 *	Author:      Joel Theytaz
 *	Date:        4 avr. 2013
 */

package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.gui.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.Rectangle;

@SuppressWarnings("serial")
public class FlameBuilderPreviewComponent extends JComponent{
	
	private ObservableFlameBuilder builder;
	private Color background;
	private Palette palette;
	private Rectangle frame;
	private int density;
	
	public FlameBuilderPreviewComponent(ObservableFlameBuilder builder, Color background, Palette palette, Rectangle frame, int density){
		this.builder = builder;
		this.background = background;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
		
		builder.addObserver(new ObservableFlameBuilder.Observer(){
			public void update(){repaint();}
		});
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(200, 100);
	}
	
	@Override
	public void paintComponent(Graphics g0){
		Graphics2D graphic = (Graphics2D) g0;
		int width = this.getWidth();
		int height = this.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Rectangle rectangle = frame.expandToAspectRatio((double) width / (double) height);
		FlameAccumulator accumulator = builder.build().compute(rectangle, width, height, density);
		Color color = null;
		for (int x = height - 1; x >= 0; x--) {
			for (int y = 0; y < width; y++) {
				color = accumulator.color(palette, background, y, x);
				image.setRGB(y, height - 1 - x, color.asPackedRGB());
			}
		}
		graphic.drawImage(image, 0, 0, null);
	}
}