package org.example.Frame;

import java.util.HashMap;

// Singleton
public class ProductManager {
    ProductFolder productRoot; // hierarchy: 0
    final String rootName = "root";
    private ProductManager(){
        productRoot = new ProductFolder(1, rootName);
    }
    static ProductManager instance;
    public static ProductManager getInstance(){
        if (instance == null)
            instance = new ProductManager();
        return instance;
    }

    HashMap<Integer, Product> products = new HashMap<>();
    int productSerial = 1000;

    Product getProduct(int id){
        return products.get(id); // 直接回傳參考，讓外部可以透過這個產品管理器取得並增減庫存
    }

    Product addProduct(String path, int price){
        String[] splitPath = prepend(path.split("/"), rootName);
        return createProduct(splitPath, productRoot, 1, price);
    }
    Product createProduct(String[] splitPath, ProductFolder currentFolder, int hierarchy, int price){
        String targetName = splitPath[hierarchy];
        if (hierarchy == splitPath.length-1){
            return createProduct(currentFolder, targetName, price);
        }else{
            Product product = currentFolder.getComposite(targetName);
            if (product instanceof ProductFolder folder){
                return createProduct(splitPath, folder, hierarchy+1, price);
            }else{
                throw new ProductExistenceException("the name " + targetName + "has existed");
            }
        }
    }
    Product createProduct(ProductFolder folder, String name, int price){
        int theSerial = productSerial++;
        Product newProduct = new Product(theSerial, name, price);
        folder.add(newProduct);
        products.put(theSerial, newProduct);
        return newProduct;
    }
    int folderSerial = 2;
    final static int folderMax = 999;

    private static String[] prepend(String[] originalArray, String newElement) {
        // 建立一個新陣列，長度比原來的多 1
        String[] newArray = new String[originalArray.length + 1];

        // 將新的字串放在第一個位置
        newArray[0] = newElement;

        // 將原陣列的內容複製到新陣列的後面
        System.arraycopy(originalArray, 0, newArray, 1, originalArray.length);

        return newArray;
    }

    private ProductFolder createFolder(ProductFolder folder, String folderName){
        ProductFolder newFolder = new ProductFolder(folderSerial++, folderName);
        folder.add(newFolder);
        return newFolder;
    }
    // currentFolder is folder of name splitPaths[targetHierarchy-1]
    private void createFolder(String[] splitPaths, ProductFolder currentFolder, int targetHierarchy){
        String targetName = splitPaths[targetHierarchy];
        Product targetProduct;

        try{
            targetProduct = currentFolder.getComposite(targetName);
        } catch (ProductExistenceException e){
            targetProduct = createFolder(currentFolder, targetName);
        }

        if (targetProduct instanceof ProductFolder targetFolder && targetHierarchy != splitPaths.length-1){
            createFolder(splitPaths, targetFolder, targetHierarchy+1);
        }else System.out.println("Error: the path " + targetName + "has exists and it is a product.");
    }
    void addFolder(String path){

        String[] splitPath = prepend(path.split("/"), rootName);
        createFolder(splitPath, productRoot, 1);
    }
    public static void main(String[] args){
        ProductManager manager = ProductManager.getInstance();
        manager.addFolder("pens");
        manager.addProduct("pens/pencil", 20);
        manager.addFolder("erasers");
        Product bigEraser = manager.addProduct("erasers/big eraser", 30);
        bigEraser.addAmount(100);
        manager.productRoot.display();
    }

}
