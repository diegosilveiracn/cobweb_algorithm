/*
 * Cobweb.java
 *
 * Created on October 25, 2007, 4:13 PM
 */

package br.unifor.mia.cobweb.control;

import br.unifor.mia.cobweb.model.Category;
import br.unifor.mia.cobweb.model.Predictable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe that represents cobweb operations
 *
 * @author Diego Silveira
 */
public class Cobweb {
    
    private Category rootCategory;
    
    /**
     * Creates a new instance of Cobweb
     */
    public Cobweb() {
        
    }
    
    public Category getRootCategory(){
        return this.rootCategory;
    }
    
    public void init(String[] column, String[][] data){
        for(int lineIndex = 0; lineIndex < data.length; lineIndex++){
            if(lineIndex == 0){
                this.rootCategory = this.createCategory(column, data);
                this.rootCategory.generateId();
                this.updateCategory(this.rootCategory, column, this.getSubMartrix(data,lineIndex));
                this.rootCategory.setObservation(new Integer(lineIndex));
            }else{
                this.updatePartition(this.rootCategory, column, data, lineIndex, null);
            }
        }
    }
    
    private void updatePartition(Category root, String[] column, String[][] data, int observation, Set<Category> partition){
        if(partition == null && !root.hasSubCategory()){
            this.insertCategory(root,column, data, observation);
        }else if(partition == null && root.hasSubCategory()){
            this.updatePartition(root, column, data, observation, root.getSubCategory());
        }else{
            root.setObservation(new Integer(observation));
            this.updateCategory(root,column,this.getSubMartrix(data, root.getObservation()));
            
            String[] bestUtility = this.categoryUtility(root, partition, column, data, observation);
            
            if ("new".equals(bestUtility[0])) { // New ----------------------
                this.insertCategory(root, column, data, observation);
            }else if ("add".equals(bestUtility[0])) { // Add ----------------
                for(Category c: partition){
                    if (c.getId().equals(bestUtility[1])) {
                        if (c.hasSubCategory()){
                            this.updatePartition(c, column, data, observation, c.getSubCategory());
                        }else{
                            this.insertCategory(c, column, data, observation);
                        }
                    }
                }
            }else if ("split".equals(bestUtility[0])) { // Splipt -----------
                this.splitCategory(root, partition, bestUtility[1]);
            }else if ("merge".equals(bestUtility[0])) { // Merge ------------
                
            }
        }
    }
    
    private String[] categoryUtility(Category root, Set<Category> subCategory, String[] column, String[][] data, int observation){
        String[] bestUtility = new String[2];
        double utilityValue = 0d;
        
        // For incorporate on root
        Category rootIncorporate = this.createCategory(column, data);
        this.updateCategory(rootIncorporate, column, this.getSubMartrix(data,observation));
        utilityValue += (1d / root.totalObservation()) * (rootIncorporate.getSquareSome() - root.getSquareSome());
        for(Category c: subCategory){
            utilityValue += ( (double) c.totalObservation() / root.totalObservation() ) * ( c.getSquareSome() - root.getSquareSome() );
        }
        bestUtility[0] = "new";
        bestUtility[1] = root.getId();
        utilityValue = utilityValue / (subCategory.size() + 1);
        
        // For incorporate on sub category
        for(Category c: subCategory){
            double auxUtilityValue = 0d;
            Category auxCategory = this.createCategory(column, data);
            for(Integer o: c.getObservation()){
                auxCategory.setObservation(new Integer(o.intValue()));
            }
            auxCategory.setObservation(new Integer(observation));
            this.updateCategory(auxCategory, column, this.getSubMartrix(data, auxCategory.getObservation()));
            auxUtilityValue += ( (double) auxCategory.totalObservation() / root.totalObservation() ) * (auxCategory.getSquareSome() - root.getSquareSome());
            
            // For others nodes
            for(Category co: subCategory){
                if (!co.equals(c)) {
                    auxUtilityValue += ( (double) co.totalObservation() / root.totalObservation() ) * (co.getSquareSome() - root.getSquareSome());
                }
            }
            auxUtilityValue = auxUtilityValue / subCategory.size();
            
            if(auxUtilityValue > utilityValue){
                bestUtility[0] = "add";
                bestUtility[1] = c.getId();
                utilityValue = auxUtilityValue;
            }
        }
        
        // For split categority
        for(Category c: subCategory){
            if (c.hasSubCategory()) {
                double auxUtilityValue = 0d;
                
                // Sub Partition
                for(Category cat: c.getSubCategory()){
                    auxUtilityValue += (cat.totalObservation() / (root.totalObservation()) ) * ( cat.getSquareSome() - root.getSquareSome());
                }
                
                // Partition
                for(Category cat: subCategory){
                    if (!cat.equals(c)) {
                        auxUtilityValue += (1d / root.totalObservation()) * (cat.getSquareSome() - root.getSquareSome());
                    }
                }
                auxUtilityValue = auxUtilityValue / (c.getSubCategory().size() + root.getSubCategory().size() - 1);
                
                if(auxUtilityValue > utilityValue){
                    bestUtility[0] = "split";
                    bestUtility[1] = c.getId();
                    utilityValue = auxUtilityValue;
                }
            }
        }
        
        return bestUtility;
    }
    
    /**
     * Updates the category
     *
     * @param categoty Category object.
     * @param column Values of attributes of column - Exemplo: {"Type","Value"}
     * @observation Number of observation. Example: 0.
     */
    private void updateCategory(Category category,  String[] column, String[][] observation){
        Map<String, Map> attributes = category.getAttribute();
        for(String a: attributes.keySet()){
            Map<String, Predictable> elements = attributes.get(a);
            for(String e: elements.keySet()){
                Predictable predictable = elements.get(e);
                predictable.setTotalOccurrence(this.countElement(observation, this.getColumnIndex(column, a), e));
                predictable.setTotalObservation(observation.length);
            }
        }
    }
    
    /**
     * Inserts category
     *
     * @param root Root category object.
     * @param column Values of attributes of column - Exemplo: {"Type","Value"}
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}
     * @observation Number of observation. Example: 0.
     */
    private void insertCategory(Category root, String[] column, String[][] data, int observation ){
        root.setObservation(new Integer(observation));
        this.updateCategory(root, column, this.getSubMartrix(data, root.getObservation()));
        
        if(root.hasSubCategory()){
            String[][] dataObservation = this.getSubMartrix(data, observation);
            
            Category category = this.createCategory(column,data);
            category.generateId();
            category.setObservation(observation);
            this.updateCategory(category, column, this.getSubMartrix(data, observation));
            
            root.setSubCategory(category);
        }else{
            for(Integer obs: root.getObservation()){
                Category category = this.createCategory(column,data);
                category.generateId();
                category.setObservation(new Integer(obs.intValue()));
                this.updateCategory(category, column, this.getSubMartrix(data, obs.intValue()));
                
                root.setSubCategory(category);
            }
        }
    }
    
    /**
     * Splits the category
     *
     * @param root Root category object.
     * @param partition Partition
     * @param idSubCategory Id of sub category. Example: 0.
     */
    private void splitCategory(Category root, Set<Category> partition, String idSubCategory){
        for(Category c: partition){
            if (c.getId().equals(idSubCategory)) {
                for(Category s: c.getSubCategory()){
                    root.setSubCategory(s);
                }
                root.removeSubCategory(c);
                break;
            }
        }
    }
    
    /**
     * Create a empty Category objecty from column and data values
     *
     * @param column Values of attributes of column - Exemplo: {"Type","Value"}
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}
     * @return Category object
     */
    private Category createCategory(String[] column, String[][] data){
        Category categority = new Category();
        
        // For each attribute
        for(int columnIndex = 0; columnIndex < column.length; columnIndex++){
            Map<String, Predictable> elements = new HashMap<String, Predictable>();
            // For each element
            for(String element: this.getElements(data, columnIndex))
                elements.put(element, new Predictable());
            categority.setAttibute(column[columnIndex], elements);
        }
        return categority;
    }
    
    /**
     * Method to make a submatrix
     *
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}
     * @param lineNumber Number of line of out - Exemple: 1
     * @return New matrix - Exemplo: {{"Type 1", "1"}}
     */
    private String[][] getSubMartrix(String[][] data, int lineNumber){
        int column = data[0].length;
        String[][] out = new String[1][column];
        
        for(int columnIndex = 0; columnIndex < column; columnIndex++ )
            out[0][columnIndex] = data[lineNumber][columnIndex];
        return out;
    }
    
    /**
     * Method to make a submatrix
     *
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}.
     * @param Set<Integer> of line of out.
     * @return New matrix - Exemplo: {{"Type 1", "1"}}
     */
    private String[][] getSubMartrix(String[][] data, Set<Integer> lineIndex){
        int column = data[0].length;
        int line = lineIndex.size();
        String[][] out = new String[line][column];
        
        int i = 0;
        for(Integer l: lineIndex){
            for(int columnIndex = 0; columnIndex < column; columnIndex++)
                out[i][columnIndex] = data[l.intValue()][columnIndex];
            i++;
        }
        
        return out;
    }
    
    /**
     * Returns the index of column
     *
     * @param column Values of attributes of column - Exemplo: {"Type","Value"}
     * @param attribute Value of attribute - Exemple: Type
     * @return Column index - Exemple: 0
     */
    private int getColumnIndex(String[] column, String attribute){
        int indexOut = -1;
        
        for(int index = 0; index < column.length; index++){
            if (column[index].equals(attribute)){
                indexOut = index;
                break;
            }
        }
        return indexOut;
    }
    
    /**
     * Makes a list with the elements of a column
     *
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}
     * @param columnIndex Column index - Exemple: 0
     * @return Return a list of elements of data - Exemple: Type 1, Type 2, Type 3
     */
    private Set<String> getElements(String[][] data, int columnIndex){
        Set<String> outList = new  HashSet<String>();
        
        for(int lineIndex = 0; lineIndex < data.length; lineIndex++){
            if(!outList.contains(data[lineIndex][columnIndex]))
                outList.add(data[lineIndex][columnIndex]);
        }
        
        return outList;
    }
    
    /**
     * Counts the number of times that an element appears
     *
     * @param data Data of observations - Exemple: {{"Type 1", "1"},{"Type 2","2"}}
     * @param columnIndex Column index - Exemple: 0
     * @param element Element to be counted - Exemple: Type 1
     * @return Quantity of times that an element appears - Exemplo: 1
     */
    private int countElement(String[][] data, int columnIndex, String element){
        int outNumber = 0;
        
        for(int lineIndex = 0; lineIndex < data.length; lineIndex++){
            if ( data[lineIndex][columnIndex].equals(element) )
                outNumber++;
        }
        return outNumber;
    }
}