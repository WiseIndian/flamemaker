package ch.epfl.flamemaker.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

public class ObservableFlameBuilder{
	private Flame.Builder nonObservableFlameBuilder;
	private List<Observer> observers = new ArrayList<Observer>();

	public ObservableFlameBuilder(Flame flame){
		nonObservableFlameBuilder=new Flame.Builder(flame);
	}
	
	public int transformationCount(){
		return nonObservableFlameBuilder.transformationCount();
	}
	
	public  void addTransformation(FlameTransformation transformation){
		nonObservableFlameBuilder.addTransformation(transformation);
		notifyObservers();
	}
	
	public  void removeTransformation(int index){
		nonObservableFlameBuilder.removeTransformation(index);
		notifyObservers();
	}
	
	public  AffineTransformation affineTransformation(int index){
		return nonObservableFlameBuilder.affineTransformation(index);
	}

	public void setAffineTransformation(int index, AffineTransformation newTransformation){
		nonObservableFlameBuilder.setAffineTransformation(index, newTransformation);
		notifyObservers();
	}
	
	public double variationWeight(int index, Variation variation){
		return nonObservableFlameBuilder.variationWeight(index, variation);
	}
	
	public void setVariationWeight(int index, Variation variation, double newWeight){
		nonObservableFlameBuilder.setVariationWeight(index, variation, newWeight);
		
	}
	
	public  Flame build(){
		return nonObservableFlameBuilder.build();
	}
	
	public void addObserver(Observer o){
		observers.add(o);
	}
	public void removeObserver(Observer o){
		observers.remove(o);
	}
	public void notifyObservers(){
		for (int i=0; i<observers.size(); i++){
			observers.get(i).update();
		}
	}
	interface Observer {
		public void update();
	}
}
