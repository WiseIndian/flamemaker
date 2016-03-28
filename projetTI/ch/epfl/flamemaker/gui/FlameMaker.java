/*
 * Auteurs (sciper): Simon Le Bail-Collet (227359), Louis-Maxence Garret (223892)
 *
 */
package ch.epfl.flamemaker.gui;

import java.util.ArrayList;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.gui.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameMaker {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ArrayList<FlameTransformation> listShark = new ArrayList<FlameTransformation>();

				double[] weightShark1 = {1, 0.1, 0, 0, 0, 0};
				listShark.add(new FlameTransformation(new AffineTransformation(-0.4113504, -0.7124804, -0.4, 0.7124795, -0.4113508, 0.8), weightShark1));

				double[] weightShark2 = {0, 0, 0, 0, 0.8, 1};
				listShark.add(new FlameTransformation(new AffineTransformation(-0.3957339, 0, -1.6, 0, -0.3957337, 0.2), weightShark2));

				double[] weightShark3 = {1,0,0,0,0,0};
				listShark.add(new FlameTransformation(new AffineTransformation(0.4810169, 0, 1, 0, 0.4810169, 0.9), weightShark3));

				//avec la fractale shark (on doit initialiser avec)
				new FlameMakerGUI(new ObservableFlameBuilder(new Flame(listShark)), Color.BLACK, InterpolatedPalette.palettePrimaryColors(), new Rectangle(new Point(-0.25,0), 5, 4), 50).start();




				/*	//avec la fractale turbulence (just for fun)
				ArrayList<FlameTransformation> listTurb = new ArrayList<FlameTransformation>();

				double[] weightTurb1 = {0.5, 0, 0, 0.4, 0, 0};
				listTurb.add(new FlameTransformation(new AffineTransformation(0.7124807, -0.4113509, -0.3, 0.4113513, 0.7124808, -0.7 ), weightTurb1));

				double[] weightTurb2 = {1, 0, 0.1, 0, 0, 0};
				listTurb.add(new FlameTransformation(new AffineTransformation(0.3731079, -0.6462417, 0.4, 0.6462414, 0.3731076, 0.3 ), weightTurb2));

				double[] weightTurb3 = {1, 0, 0, 0, 0, 0};
				listTurb.add(new FlameTransformation(new AffineTransformation(0.0842641, -0.314478, -0.1, 0.314478, 0.0842641, 0.3 ), weightTurb3));

				Flame turb = new Flame(listTurb);
				//new FlameMakerGUI(new Flame.Builder(turb), Color.BLACK, InterpolatedPalette.palettePrimaryColors(), new Rectangle(new Point(0.1,0.1), 3, 3), 50).start();
				 */

			}
		});
	}

}