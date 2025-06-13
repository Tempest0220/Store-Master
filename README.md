# 關於我做了什麼？
- 我重構了StationeryStore與Store，使其可以直接創建不同的商店，而且增加可讀性（GUI跟App沒大改）
- 新增了過期商品，並修改GUI部分（一行）的顯示方式，使其支持顯示過期商品（productItem的子類）特有的資料


## 整理StationeryStore(目標：可以輕鬆完整各類商店)

兩個主要字段

分類樹(categories)
註冊表(inventoryManager)

有動到 categories 的函數：
- initStore()：初始化時建立分類樹與分配商品。
- getCategories()：取得分類樹（供 GUI 用）。
- addCategory()：新增分類節點。
- moveProductToCategory()：將商品移動到指定分類。
- deleteProduct()：從分類樹移除商品。
- removeCategory()：移除分類節點。
- moveCategory()：移動分類到另一分類下。
- getAllCategories()：取得所有分類（含巢狀）。
- isDescendant()：判斷分類是否為另一分類的子孫。

有動到 inventoryManager 的函數：
- initStore()：初始化時註冊商品到庫存管理。
- addNewProductType()：新增商品到庫存管理。
- deleteProduct()：從庫存管理移除商品。
- restockWarehouse()：補貨。
- stockShelf()：補貨（同上）。
- sell()：銷售時扣除庫存。
- isProductNameExist()：查詢商品是否存在。
- showWarehouse()：顯示所有商品庫存。

都有動到的函數：
- deleteProduct(刪除產品, 分類、註冊表)



categories跟inventoryManager的一個商品，共用一個ref


## 計畫：
1. 函數名重命名 (done)
2. 為store增添函數
3. 為store的抽象函數寫好內容（變成不是抽象）
4. 目標是繼承他只需要實現initStore


# 函數重命名計畫（已完成）：
## 重命名(CategoryStore)函數：

重命實例成員 categories -> productCategories
- addCategory() -> CategoryAddNewCategory()
- moveProductToCategory() -> CategoryAddProduct()
- removeCategory() -> CategoryRemoveCategory()
- moveCategory() -> CategoryMoveCategoryAndProducts()
- getCategories() -> CategoryGetCategories()
- getAllCategories() -> CategoryGetAllCategories()
- isDescendant() -> _isCategoryDescendant() / isCategoryDescendant()

## 重命名(InventoryManager)函數：

重命名實例成員InventoryManager -> productRegistry
- addNewProductType() -> RegistryAddNewProduct()
- deleteProduct() -> RegistryDeleteProduct()
- restockWarehouse() -> RegistrySetProductStock()
- showWarehouse() -> RegistryShowAllProducts()
- isProductNameExist() -> RegistryIsProductNameExist()
- 
- stockShelf() -> 移除
- sell() -> keep

# 為Store增添函數：

## 分離private與public函數
### private函數：
- registry 增刪改查
- category 增刪改查

### public函數：
- CategoryAddProduct(String prodName, String catName)：將商品加入指定分類。
- CategoryMoveCategoryAndProducts(String fromCat, String toCat)：移動分類及其下所有商品。
- CategoryAddNewCategory(String name)：新增分類。
- CategoryGetCategories()：取得所有頂層分類（用於判斷是否有分類）。
- CategoryGetAllCategories()：取得所有分類（含巢狀）。
- RegistryIsProductNameExist(String name)：檢查商品名稱是否重複。
- RegistryAddNewProduct(String name, double price, int initQty)：新增商品到註冊表。



# 加入過期商品的支持