/*
 * Category.java
 *
 * Created on October 22, 2007, 9:54 AM
 */
package br.unifor.mia.cobweb.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class that represents a category
 *
 * @author Diego Silveira
 */
public class Category{
    
    private static int idGenerator = 0;
    
    private String id;
    
    private Map<String,Map> attribute;
    
    private Set<Integer> observation;
    
    private Set<Category> subCategory;
    
    /**
     * Creates a new instance of Category
     *
     * @see this.setAttribute
     * @see this.setObservation
     */
    public Category() {
        this.setAttribute(new HashMap<String, Map>());
        this.setObservation(new HashSet<Integer>());
        this.setSubCategory(new HashSet<Category>());
    }
    
    /**
     * Method to check if has sub-category
     *
     * @return If has sub-category or no. Example: true - yes or false - otherwise
     */
    public boolean hasSubCategory(){
        return subCategory.size() == 0? Boolean.FALSE: Boolean.TRUE;
    }
    
    /**
     * Generates id
     */
    public void generateId(){
        this.id = String.valueOf(idGenerator);
        idGenerator++;
    }
    
    /**
     * Gets size observation
     *
     * @return Size observation. Example: 2.
     */
    public int totalObservation(){
        return this.observation == null? 0: this.observation.size();
    }
    
    /**
     * Details the category
     *
     * @return Detaisl of category.
     */
    public String detail(){
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Category: "+ this.id+"\n\n");
        
        // Attribute
        buffer.append("******************** Attribute ********************\n");
        for(String a: this.attribute.keySet()){
            buffer.append(a+"\n");
            Map<String, Predictable> elements = this.attribute.get(a);
            for(String e: elements.keySet()){
                buffer.append("\t"+e);
                Predictable predictable = elements.get(e);
                buffer.append("\t"+ predictable.getTotalOccurrence() + "/" + predictable.getTotalObservation() + "\n");
            }
        }
        
        // Observation
        buffer.append("\n******************* Observation *******************\n");
        buffer.append("[");
        int i = 0;
        for(Integer o: this.getObservation()){
            if (i != 0)
                buffer.append(", ");
            buffer.append(o.intValue() + 1);
            i++;
        }
        buffer.append("]");
        
        return buffer.toString();
    }
    
    /**
     * Prints nome of category
     *
     * @return Name of category. Example: [Category: 0]
     */
    @Override
    public String toString() {
        return "[Category: "+ this.id+"]";
    }
    
    /**
     * Compares id
     *
     * @param category Category object.
     * @return Result of de same id. Example: true - equals or false - otherwise
     */
    public boolean equals(Category category){
        return this.id.equals(category.getId());
    }
    
    /**
     * Method that calc square some
     *
     * @return square some
     */
    public double getSquareSome(){
        double total = 0d;
        for(String a: this.attribute.keySet()){
            Map<String, Predictable> elements = this.attribute.get(a);
            for(String e: elements.keySet()){
                Predictable predictable = elements.get(e);
                if (predictable.getTotalOccurrence() != 0) {
                    double aux = ((double) predictable.getTotalOccurrence()) / predictable.getTotalObservation();
                    total += Math.pow(aux, 2);
                }
            }
        }
        return total;
    }
    
    /**
     * Removes sub category
     *
     * @param category Category object.
     */
    public void removeSubCategory(Category category){
        this.subCategory.remove(category);
    }
    
    /**
     * Method bean getId
     *
     * @return Identification of categority - Exemplo: C 1
     */
    public String getId() {
        return id;
    }
    
    /**
     * Method bean setId
     *
     * @param id Identification of categority - Exemplo: C 1
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Method bean getAttribute
     *
     * @return Map<String, Map> object
     */
    public Map<String, Map> getAttribute() {
        return attribute;
    }
    
    /**
     * Method bean setAttribute
     *
     * @param attribute Map<String, Map> object
     */
    public void setAttribute(Map<String, Map> attribute) {
        this.attribute = attribute;
    }
    
    /**
     * Method bean setAttribute
     *
     * @param key Value of attribute - Example: Type
     * @param Map<String, Predictable> object
     */
    public void setAttibute(String key, Map<String, Predictable> value){
        this.getAttribute().put(key,value);
    }
    
    /**
     * Method bean setAttribute
     *
     * @param attribute key Value of attribute - Exemplo: Type
     * @param element Key value of element - Exemplo: Type 1
     * @param totalOccurrence Number of occurrence - Example: 2
     * @param totalObservation Number of observation - Example: 3
     */
    public void setAttibute(String attribute, String element, int totalOccurrence, int totalObservation){
        for(String a: this.attribute.keySet()){
            if (a.equals(attribute)) {
                Map elements = this.attribute.get(a);
                for(Object e: elements.keySet()){
                    if( ((String)e).equals(element) ){
                        Predictable predictable = (Predictable) elements.get(e);
                        predictable.setTotalOccurrence(totalOccurrence);
                        predictable.setTotalObservation(totalObservation);
                    }
                }
            }
        }
    }
    
    /**
     * Method bean getObservation
     *
     * @return Set<Integer> of index number of observation. Example: 0 | 1 | 2.
     */
    public Set<Integer> getObservation() {
        return observation;
    }
    
    /**
     * Method bean setObservation
     *
     * @param observation Set<Integer> of index number of observation. Example: 0 | 1 | 2.
     */
    public void setObservation(Set<Integer> observation) {
        this.observation = observation;
    }
    
    /**
     * Method bean setObservation
     *
     * @param observation Set<Integer> of index number of observation. Example: 0 | 1 | 2.
     */
    public void setObservation(Integer observation) {
        this.observation.add(observation);
    }
    
    /**
     * Method bean getSubCatery
     *
     * @return Set<Category> of sub-category.
     */
    public Set<Category> getSubCategory() {
        return subCategory;
    }
    
    /**
     * Method bean setSubCatery
     *
     * @param subCategory Set<Category> of sub-category.
     */
    public void setSubCategory(Set<Category> subCategory) {
        this.subCategory = subCategory;
    }
    
    /**
     * Method bean setSubCatery
     *
     * @param subCategory Sub-Category.
     */
    public void setSubCategory(Category subCategory){
        this.subCategory.add(subCategory);
    }
}