package org.example;
import org.example.model.ProductOrServiceComposite;
import org.example.model.ProductOrServiceLeaf;
import org.example.pattern.*;


// Composite Pattern Test
public class Main {
    public static void main(String[] args) {
        ProductOrServiceLeaf product1 = new ProductOrServiceLeaf("P001", "Product 1", "Description of Product 1", 100.0);
        ProductOrServiceLeaf product2 = new ProductOrServiceLeaf("P002", "Product 2", "Description of Product 2", 200.0);
        ProductOrServiceLeaf service1 = new ProductOrServiceLeaf("S001", "Service 1", "Description of Service 1", 300.0);
        ProductOrServiceLeaf service2 = new ProductOrServiceLeaf("S002", "Service 2", "Description of Service 2", 400.0);

        // Create a composite object
        ProductOrServiceComposite composite1 = new ProductOrServiceComposite("C001", "Composite 1", "Description of Composite 1");
        ProductOrServiceComposite composite2 = new ProductOrServiceComposite("C002", "Composite 2", "Description of Composite 2");
        ProductOrServiceComposite composite3 = new ProductOrServiceComposite("C003", "Composite 3", "Description of Composite 3");
        composite1.add(product1);
        composite1.add(product2);
        composite2.add(service1);
        composite2.add(service2);

        composite3.add(composite1);
        composite3.add(composite2);

        // Print the display names of the composite objects
        System.out.println(composite3.getDisplayName());

        
        
    }
}
