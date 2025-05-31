import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SuperComposite root = new SuperComposite("Root Composite");
        
        SuperLeaf leaf1 = new SuperLeaf("Leaf 1", 10.0);
        SuperLeaf leaf2 = new SuperLeaf("Leaf 2", 20.0);
        SuperLeaf leaf3 = new SuperLeaf("Leaf 3", 30.0);
        
        root.addChild(leaf1);
        root.addChild(leaf2);
        root.addChild(leaf3);
        
        System.out.println(root);
        System.out.println("Total Price: " + root.getPrice());
        
        root.removeChild(leaf2);
        
        System.out.println("After removing Leaf 2:");
        System.out.println(root);
        System.out.println("Total Price: " + root.getPrice());
        
    }
}



abstract class Component {
    public abstract double getPrice();
}

abstract class Composite<T extends Leaf> extends Component {
    protected ArrayList<T> children = new ArrayList<>();
    public abstract void addChild(T child);
    public abstract void removeChild(T child);
    public abstract ArrayList<T> getChildren();

}

abstract class Leaf extends Component {
    protected String id;
    protected double price;
}









class SuperLeaf extends Leaf {
    public SuperLeaf(String id, double price) {
        this.id = id;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "SuperLeaf{id='" + id + "', price=" + price + '}';
    }
}


class SuperComposite extends Composite<SuperLeaf> {
    private String name;

    public SuperComposite(String name) {
        this.name = name;
    }

    @Override
    public void addChild(SuperLeaf child) {
        children.add(child);
    }

    @Override
    public void removeChild(SuperLeaf child) {
        children.remove(child);
    }

    @Override
    public List<SuperLeaf> getChildren() {
        return children;
    }

    @Override
    public double getPrice() {
        double total = 0.0;
        for (SuperLeaf child : children) {
            total += child.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "SuperComposite{name='" + name + "', children=" + children + '}';
    }
}
