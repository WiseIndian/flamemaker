package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.UIManager.*;

import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.color.*;

/**
 * @author (Sciper) Simon Le Bail-Collet(227359) Louis-Maxence Garret(223892)
 *
 */
public class FlameMakerGUI{

	private ObservableFlameBuilder flameBuilder;
	private Color background;
	private Palette palette;
	private Rectangle frame;
	private int density;

	private int selectedTransformationIndex;
	private List<Observer> observers = new ArrayList<Observer>();

	public FlameMakerGUI (ObservableFlameBuilder flameBuilder, Color background, Palette palette, Rectangle frame, int density){
		this.flameBuilder = flameBuilder;
		this.background = background;
		this.palette =palette;
		this.frame = frame;
		this.density = density;
	}

	public int getSelectedTransformationIndex(){
		return selectedTransformationIndex;
	}
	public void setSelectedTransformationIndex(int newValue){
		selectedTransformationIndex=newValue;
		for(int i=0; i<observers.size(); i++){
			observers.get(i).update();
		}
	}
	public void addObserver(Observer o){
		observers.add(o);
	}
	public void removeObserver(Observer o){
		observers.remove(o);
	}

	public void start(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}



		JPanel transfoPanel = new JPanel();
		transfoPanel.setLayout(new BorderLayout());
		transfoPanel.setBorder(BorderFactory.createTitledBorder("Transformations Affines"));

		final AffineTransformationsComponent affineComp = new AffineTransformationsComponent(flameBuilder, frame);
		this.addObserver(new Observer(){
			public void update(){affineComp.setHighlightedTransformationIndex(selectedTransformationIndex);}
		});
		transfoPanel.add(affineComp);

		JPanel fracPanel = new JPanel();
		fracPanel.setLayout(new BorderLayout());
		fracPanel.setBorder(BorderFactory.createTitledBorder("Fractale"));
		fracPanel.add(new FlameBuilderPreviewComponent(flameBuilder, background, palette, frame, density));


		final TransformationsListModel a = new TransformationsListModel();
		final JList<String> listPanel = new JList<String>(a);
		listPanel.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				setSelectedTransformationIndex(listPanel.getSelectedIndex());}});
		listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPanel.setVisibleRowCount(3);
		listPanel.setSelectedIndex(0);


		final JButton removeButton = new JButton("Supprimer");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listPanel.getSelectedIndex()==a.getSize()-1){
					a.removeTransformation(listPanel.getSelectedIndex());
					listPanel.setSelectedIndex(a.getSize()-1);
				}else{
					int i=listPanel.getSelectedIndex();
					a.removeTransformation(listPanel.getSelectedIndex());
					listPanel.setSelectedIndex(i);
				}
				removeButton.setEnabled(a.getSize()!=1);
			}
		});

		final JButton addButton = new JButton("Ajouter");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.addTransformation();
				listPanel.setSelectedIndex(a.getSize()-1);
				removeButton.setEnabled(a.getSize()!=1);
			}
		});
		/*creation textfield et etiquettes*/
		JLabel translationLabel = new JLabel("Translation");
		final JFormattedTextField translationField = new JFormattedTextField( new DecimalFormat("#0.##"));
		translationField.setValue(0.1);
		JLabel rotationLabel = new JLabel("Rotation");
		final JFormattedTextField rotationField = new JFormattedTextField( new DecimalFormat("#0.##"));
		rotationField.setValue(15);
		JLabel scalingLabel = new JLabel("Dilatation");
		final JFormattedTextField scalingField = new JFormattedTextField( new DecimalFormat("#0.##"));
		scalingField.setValue(1.05);
		scalingField.setInputVerifier(new InputVerifier(){
			public boolean verify(JComponent arg0) {
				Number value=0;
				try {
					value = (Number)scalingField.getFormatter().stringToValue(scalingField.getText());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(value.doubleValue()==0){
					try {
						scalingField.setText(scalingField.getFormatter().valueToString(scalingField.getValue()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return true;
			}

		});
		JLabel shearLabel = new JLabel("Transvection");
		final JFormattedTextField shearField = new JFormattedTextField( new DecimalFormat("#0.##"));
		shearField.setValue(0.1);
		/*fin de la creation des textfield et etiquettes*/		


		//← → ↑ ↓  ↔ ↕
		/*creation des boutons pour le panneau d'edition de la partine affine de la transformation*/
		JButton translationLeftButton=new JButton("←");
		translationLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) translationField.getValue();
				Context context = new Context(new TranslationModification());
				context.executeStrategy(-fieldValue.doubleValue(), 0);
			}
		});
		JButton translationRightButton=new JButton("→");
		translationRightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) translationField.getValue();
				Context context = new Context(new TranslationModification());
				context.executeStrategy(fieldValue.doubleValue(), 0);
			}
		});
		JButton translationUpButton=new JButton("↑");
		translationUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) translationField.getValue();
				Context context = new Context(new TranslationModification());
				context.executeStrategy(0, fieldValue.doubleValue());
			}
		});
		JButton translationDownButton=new JButton("↓");
		translationDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) translationField.getValue();
				Context context = new Context(new TranslationModification());
				context.executeStrategy(0, -fieldValue.doubleValue());
			}
		});

		JButton rotationLeftButton=new JButton("↺");
		rotationLeftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) rotationField.getValue();
				double radian = Math.PI * fieldValue.doubleValue() / 180.0;
				Context context = new Context(new RotationModification());
				context.executeStrategy(radian, 0);

			}
		});
		JButton rotationRightButton=new JButton("↻");
		rotationRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) rotationField.getValue();
				double radian = Math.PI * fieldValue.doubleValue() / 180.0;
				Context context = new Context(new RotationModification());
				context.executeStrategy(-radian, 0);

			}
		});

		JButton scalingVerticalPlusButton = new JButton("+ ↕");
		scalingVerticalPlusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) scalingField.getValue();
				Context context = new Context(new ScalingModification());
				context.executeStrategy(1, fieldValue.doubleValue());
			}
		});
		JButton scalingVerticalMinusButton = new JButton("- ↕");
		scalingVerticalMinusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) scalingField.getValue();
				Context context = new Context(new ScalingModification());
				context.executeStrategy(1, 1 / fieldValue.doubleValue());}
		});
		JButton scalingHorizontalPlusButton = new JButton("+ ↔");
		scalingHorizontalPlusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) scalingField.getValue();
				Context context = new Context(new ScalingModification());
				context.executeStrategy(fieldValue.doubleValue(), 1);}
		});
		JButton scalingHorizontalMinusButton = new JButton("- ↔");
		scalingHorizontalMinusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Number fieldValue = (Number) scalingField.getValue();
				Context context = new Context(new ScalingModification());
				context.executeStrategy(1 / fieldValue.doubleValue(), 1);}
		});

		JButton shearLeftButton = new JButton("←");
		shearLeftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Number fieldValue = (Number) scalingField.getValue();

				Context context = new Context(new ShearModification());
				context.executeStrategy(-fieldValue.doubleValue(), 0);

			}
		});
		JButton shearRightButton = new JButton("→");
		shearRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Number fieldValue = (Number) scalingField.getValue();

				Context context = new Context(new ShearModification());
				context.executeStrategy(fieldValue.doubleValue(), 0);

			}
		});
		JButton shearUpButton = new JButton("↑");
		shearUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Number fieldValue = (Number) scalingField.getValue();

				Context context = new Context(new ShearModification());
				context.executeStrategy(0, fieldValue.doubleValue());

			}
		});
		JButton shearDownButton = new JButton("↓");
		shearDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Number fieldValue = (Number) scalingField.getValue();

				Context context = new Context(new ShearModification());
				context.executeStrategy(0, -fieldValue.doubleValue());

			}
		});
		/*fin de la creation des boutons*/

		JPanel affineEditPanel=new JPanel();
		GroupLayout affineEditLayout = new GroupLayout(affineEditPanel);
		GroupLayout.SequentialGroup supHGroup = affineEditLayout.createSequentialGroup();
		GroupLayout.SequentialGroup supVGroup = affineEditLayout.createSequentialGroup();
		affineEditLayout.setHorizontalGroup(supHGroup);
		affineEditLayout.setVerticalGroup(supVGroup);

		/*repartition dans les groupes horizontaux*/
		GroupLayout.ParallelGroup h1Group = affineEditLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		h1Group.addComponent(translationLabel).addComponent(rotationLabel).addComponent(scalingLabel).addComponent(shearLabel);
		GroupLayout.ParallelGroup h2Group = affineEditLayout.createParallelGroup();
		h2Group.addComponent(translationField).addComponent(rotationField).addComponent(scalingField).addComponent(shearField);
		GroupLayout.ParallelGroup h3Group = affineEditLayout.createParallelGroup();
		h3Group.addComponent(translationLeftButton).addComponent(rotationLeftButton).addComponent(scalingHorizontalPlusButton).addComponent(shearLeftButton);
		GroupLayout.ParallelGroup h4Group = affineEditLayout.createParallelGroup();
		h4Group.addComponent(translationRightButton).addComponent(rotationRightButton).addComponent(scalingHorizontalMinusButton).addComponent(shearRightButton);
		GroupLayout.ParallelGroup h5Group = affineEditLayout.createParallelGroup();
		h5Group.addComponent(translationUpButton).addComponent(scalingVerticalPlusButton).addComponent(shearUpButton);
		GroupLayout.ParallelGroup h6Group = affineEditLayout.createParallelGroup();
		h6Group.addComponent(translationDownButton).addComponent(scalingVerticalMinusButton).addComponent(shearDownButton);

		/*repartition dans les groupes verticaux*/
		GroupLayout.ParallelGroup v1Group = affineEditLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v1Group.addComponent(translationLabel).addComponent(translationField).addComponent(translationLeftButton).addComponent(translationRightButton).addComponent(translationUpButton).addComponent(translationDownButton);
		GroupLayout.ParallelGroup v2Group = affineEditLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v2Group.addComponent(rotationLabel).addComponent(rotationField).addComponent(rotationLeftButton).addComponent(rotationRightButton);
		GroupLayout.ParallelGroup v3Group = affineEditLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v3Group.addComponent(scalingLabel).addComponent(scalingField).addComponent(scalingHorizontalPlusButton).addComponent(scalingHorizontalMinusButton).addComponent(scalingVerticalPlusButton).addComponent(scalingVerticalMinusButton);
		GroupLayout.ParallelGroup v4Group = affineEditLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v4Group.addComponent(shearLabel).addComponent(shearField).addComponent(shearLeftButton).addComponent(shearRightButton).addComponent(shearUpButton).addComponent(shearDownButton);

		/*on rajoute les sous groupes aux groupes englobants*/
		supHGroup.addGroup(h1Group).addGroup(h2Group).addGroup(h3Group).addGroup(h4Group).addGroup(h5Group).addGroup(h6Group);
		supVGroup.addGroup(v1Group).addGroup(v2Group).addGroup(v3Group).addGroup(v4Group);

		affineEditPanel.setLayout(affineEditLayout);
		affineEditPanel.setBorder(BorderFactory.createTitledBorder("Transformation Courante" +
				""));

		JPanel flameEditPanel = new JPanel();
		flameEditPanel.setLayout(new BoxLayout(flameEditPanel, BoxLayout.PAGE_AXIS));
		flameEditPanel.add(affineEditPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(removeButton);
		buttonPanel.add(addButton);

		JPanel transfoListEditPanel = new JPanel();
		transfoListEditPanel.setLayout(new BorderLayout());
		transfoListEditPanel.add(buttonPanel, BorderLayout.PAGE_END);

		JScrollPane scrollPanel = new JScrollPane(listPanel);
		transfoListEditPanel.add(scrollPanel, BorderLayout.CENTER);
		Border editionBorder = BorderFactory.createTitledBorder("Transformations");
		transfoListEditPanel.setBorder(editionBorder);



		JPanel lowPanel = new JPanel();
		lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.LINE_AXIS));
		lowPanel.add(transfoListEditPanel);
		lowPanel.add(flameEditPanel);

		JPanel supPanel = new JPanel();
		supPanel.setLayout(new GridLayout(1,2));
		supPanel.add(transfoPanel);
		supPanel.add(fracPanel);

		JFrame mainFrame = new JFrame("Flame Maker");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(supPanel, BorderLayout.CENTER);
		mainFrame.getContentPane().add(lowPanel, BorderLayout.PAGE_END);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);

	}
	@SuppressWarnings("serial")
	class TransformationsListModel extends AbstractListModel<String>{
		public int getSize() {
			return flameBuilder.transformationCount();
		}

		public String getElementAt(int index) {
			return "Transformation n°"+(index+1);
		}

		public void addTransformation(){
			flameBuilder.addTransformation(new FlameTransformation(AffineTransformation.IDENTITY, new double[]{1,0,0,0,0,0}));
			fireIntervalAdded(this, getSize(), getSize());
		}
		public void removeTransformation(int index){
			flameBuilder.removeTransformation(index);
			fireIntervalRemoved(this, index, index);
		}
	}

	interface Observer {
		public void update();
	}
	
	public interface Strategy {
		void modifyAffineTransformation(double x, double y);

	}

	class Context {
		private Strategy strategy;

		public Context(Strategy strategy) {
			this.strategy = strategy;
		}

		public void executeStrategy(double x, double y) {
			this.strategy.modifyAffineTransformation(x, y);
		}
	}

	class TranslationModification implements Strategy {

		@Override
		public void modifyAffineTransformation(double x, double y) {
			flameBuilder
					.setAffineTransformation(
							selectedTransformationIndex,
							AffineTransformation
									.newTranslation(x, y)
									.composeWith(
											flameBuilder
													.affineTransformation(selectedTransformationIndex)));

		}

	}

	class RotationModification implements Strategy {

		@Override
		public void modifyAffineTransformation(double x, double y) {
			Context contextTranslation = new Context(new TranslationModification());
			double tX = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationX();
			double tY = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationY();
			contextTranslation.executeStrategy(-tX, -tY);
			flameBuilder
					.setAffineTransformation(
							selectedTransformationIndex,
							AffineTransformation
									.newRotation(x)
									.composeWith(
											flameBuilder
													.affineTransformation(selectedTransformationIndex)));

			contextTranslation.executeStrategy(tX, tY);

		}

	}

	class ScalingModification implements Strategy {

		@Override
		public void modifyAffineTransformation(double x, double y) {
			Context contextTranslation = new Context(new TranslationModification());
			double tX = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationX();
			double tY = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationY();
			contextTranslation.executeStrategy(-tX, -tY);
			flameBuilder
					.setAffineTransformation(
							selectedTransformationIndex,
							AffineTransformation
									.newScaling(x, y)
									.composeWith(
											flameBuilder
													.affineTransformation(selectedTransformationIndex)));
			contextTranslation.executeStrategy(tX, tY);

		}

	}

	class ShearModification implements Strategy {

		@Override
		public void modifyAffineTransformation(double x, double y) {
			Context contextTranslation = new Context(new TranslationModification());
			double tX = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationX();
			double tY = flameBuilder.affineTransformation(
					selectedTransformationIndex).translationY();
			contextTranslation.executeStrategy(-tX, -tY);
			flameBuilder
					.setAffineTransformation(
							selectedTransformationIndex,
							AffineTransformation
									.newShearX(x)
									.composeWith(
											flameBuilder
													.affineTransformation(selectedTransformationIndex)));
			flameBuilder
					.setAffineTransformation(
							selectedTransformationIndex,
							AffineTransformation
									.newShearY(y)
									.composeWith(
											flameBuilder
													.affineTransformation(selectedTransformationIndex)));
			contextTranslation.executeStrategy(tX, tY);

		}
	}
}