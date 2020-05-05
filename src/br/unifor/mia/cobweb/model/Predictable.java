/*
 * Predictable.java
 *
 * Created on October 22, 2007, 11:00 AM
 */
package br.unifor.mia.cobweb.model;

/**
 * Classe that represents a predictable
 *
 * @author Diego Silveira
 */
public class Predictable {
    
    private int totalOccurrence;
    
    private int totalObservation;
    
    /**
     * Creates a new instance of Predictable
     */
    public Predictable() {
        
    }
    
    /**
     * Creates a new instance of Predictable
     *
     * @param totalOccurrence Total of times that elements appears - Exemple: 2
     * @param totalObservation  Total of observation - Exemple: 3
     */
    public Predictable(int totalOccurrence, int totalObservation){
        this.setTotalOccurrence(totalOccurrence);
        this.setTotalObservation(totalObservation);
    }
    
    public double getPredictable(){
        if (this.totalOccurrence != 0)
            return ((double)this.totalOccurrence) / this.totalObservation;
        return 0d;
    }
    
    /**
     * Method bean getTotalOccurrence
     *
     * @return Total of times that elements appears - Exemple: 2
     */
    public int getTotalOccurrence() {
        return totalOccurrence;
    }
    
    /**
     * Method bean setTotalOccurrence
     *
     * @param totalOccurrence Total of times that elements appears - Exemple: 2
     */
    public void setTotalOccurrence(int totalOccurrence) {
        this.totalOccurrence = totalOccurrence;
    }
    
    /**
     * Method bean getTotalObservation
     *
     * @return Total of observation - Exemple: 3
     */
    public int getTotalObservation() {
        return totalObservation;
    }
    
    /**
     * Method bean setTotalObservation
     *
     * @param totalObservation Total of observation - Exemple: 3
     */
    public void setTotalObservation(int totalObservation) {
        this.totalObservation = totalObservation;
    }
}