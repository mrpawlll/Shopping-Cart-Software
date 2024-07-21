package indivassign;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;


import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;


public class BillingProgram extends JFrame
{
    public static Category[] category;
    public static Item item;

    ShoppingCart shoppingCart;
    BillPanel bill;

    BillingProgram(){
        super("Billing");
        setLayout(new BorderLayout());
        InsertDataToCategory();

        shoppingCart = new ShoppingCart();
        shoppingCart.setVisible(true);
        add(shoppingCart,BorderLayout.CENTER);
        shoppingCart.setBillingProgram(this);

        bill = new BillPanel();
        bill.setVisible(false);
        bill.setBillingProgram(this);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(getSize());
    }

    public void InsertDataToCategory(){
        category = new Category[2];
        category[0] = new Category("Fresh Produce");
        category[1] = new Category("Edibles");

        item = new Item("Meat", new BigDecimal("10"), 0);
        category[0].addItem(item);
        item = new Item("Vege", new BigDecimal("5"), 1);
        category[0].addItem(item);
        item = new Item("Fruits", new BigDecimal("3"), 2);
        category[0].addItem(item);

        item = new Item("Canned Fruits", new BigDecimal("3.0"), 3);
        category[1].addItem(item);

        item = new Item("Drinks", new BigDecimal("1.5"), 4);
        category[1].addItem(item);
    }

    public void showBill(){
        shoppingCart.setVisible(false);
        bill.setVisible(true);
        add(bill,BorderLayout.CENTER);
        revalidate();
        pack();
        repaint();
    }

    public void showShoppingCart(){
        bill.setVisible(false);
        shoppingCart.setVisible(true);
        add(shoppingCart,BorderLayout.CENTER);
        revalidate();
        pack();
        repaint();
    }

        public static void main( String[] args ){
            new BillingProgram();
        }

}
//mainPanels
class ShoppingCart extends JPanel implements ActionListener{
    JButton addItemToShoppingCart, confirm;
    CustomTable table;
    AddItemComponents addItemComponents;
    BillingProgram billingProgram ;

    ShoppingCart(){
        super();
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10,10,10,10);

        addItemComponents = new AddItemComponents();
        add(addItemComponents);
        
        addItemToShoppingCart = new JButton("Add Item");
        constraints.gridx = 1;
        add(addItemToShoppingCart,constraints);

        confirm = new JButton("Confirm");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(confirm,constraints);


        table = new CustomTable();
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        add(table,constraints);


        addItemToShoppingCart.addActionListener(this);
        confirm.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==addItemToShoppingCart){
           int qty = addItemComponents.getItemQuantity();
           Item item = addItemComponents.getItem();
           BigDecimal itemPrice = ((Item)item).getItemPrice();
            
           if(qty>0){
            BigDecimal totalPrice = itemPrice.multiply(new BigDecimal(qty));
            ItemCart itemCart = new ItemCart(item.getItemName(),qty,totalPrice);
            table.getTableModel().addItemCart(itemCart);
           }
        }

        if(e.getSource()==confirm){
            if((table.getTableModel()).getRowCount() > 0){
                int option = JOptionPane.showConfirmDialog(this, "Proceeding to bill?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION){
                billingProgram.showBill();
                }
            }else{
                JOptionPane.showMessageDialog(this, "No items are added, can't proceed", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    public void setBillingProgram(BillingProgram billingProgram){
        this.billingProgram = billingProgram;
    }

}

class BillPanel extends JPanel implements ActionListener,ItemListener{
private CustomTable table;
private JButton next,back, print,verifyRoyaltyCard;
private BillingProgram billingProgram;
private JComboBox<Discount> discountBox;
private RoyaltyCard royaltyCard;
private Discount noDiscount;
private JTextField customerRoyaltyCard;

    BillPanel(){
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10,10,10,10);

        back = new JButton("Back");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        add(back,constraints);

        next = new JButton("Next");
        constraints.gridx = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(next,constraints);


        table = new CustomTable();
        constraints.gridx = 1;
        constraints.weightx = 2;
        constraints.weighty = 1;
        constraints.gridheight = 5;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        add(table,constraints);

        discountBox = new JComboBox<>();
        royaltyCard = new RoyaltyCard();
        noDiscount = new Discount();
        discountBox.addItem(royaltyCard);
        discountBox.addItem(noDiscount);
        discountBox.setSelectedItem(noDiscount);
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(discountBox, constraints);


        customerRoyaltyCard = new JTextField();
        customerRoyaltyCard.setVisible(false);
        constraints.gridy = 2;
        add(customerRoyaltyCard,constraints);

        verifyRoyaltyCard = new JButton("Verify");
        verifyRoyaltyCard.setVisible(false);
        constraints.gridy = 3;
        add(verifyRoyaltyCard,constraints);

        print = new JButton("Print");
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(print,constraints);


        next.addActionListener(this);
        back.addActionListener(this);

        discountBox.addActionListener(this);
        discountBox.addItemListener(this);
        customerRoyaltyCard.addActionListener(this);
        verifyRoyaltyCard.addActionListener(this);

        print.addActionListener(this);
    }

    public void setBillingProgram(BillingProgram billingProgram){
        this.billingProgram = billingProgram;
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == next){
            this.table.getTableModel().clearTable();
            billingProgram.showShoppingCart();
        }

        if(e.getSource() == back){
            billingProgram.showShoppingCart();
        }

        if(e.getSource()== verifyRoyaltyCard){
            int userInput = Integer.parseInt(customerRoyaltyCard.getText());
            for(int entry : royaltyCard.getRoyaltyIDList()){
                if(userInput == entry){
                    this.table.getTableModel().setDiscount(((Discount)discountBox.getSelectedItem()).getDiscountValue());
                    break;
                }else{
                    this.table.getTableModel().setDiscount(BigDecimal.ZERO);
                }
            }
        }

        if(e.getSource() == print){
            this.table.getTableModel().exportToTextFile();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if(e.getSource() == discountBox){

            if(discountBox.getSelectedItem() instanceof RoyaltyCard){
            customerRoyaltyCard.setVisible(true);
            verifyRoyaltyCard.setVisible(true);
            } if(discountBox.getSelectedItem() instanceof Discount){

            }else{
                customerRoyaltyCard.setVisible(false);
                verifyRoyaltyCard.setVisible(false);
            }
            
            if(discountBox.getSelectedItem() instanceof Discount){
                this.table.getTableModel().setDiscount(BigDecimal.ZERO);
            }
        }
    }

}
//endmain panels

//innerComponents
class AddItemComponents extends JPanel{
    private JComboBox<Item> itemsForSell;
    private JSpinner itemQuantity;
    private GridBagConstraints constraints;


    AddItemComponents(){
        super();
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5,5,5,5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        itemsForSell = new JComboBox<>();
        for(Category category: BillingProgram.category){
            for(Item item: category.itemList){
                itemsForSell.addItem(item);
            }
        }
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(itemsForSell,constraints);

        itemQuantity = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        constraints.gridx = 1;
        add(itemQuantity,constraints);

    }

    public int getItemQuantity(){
        return (int)itemQuantity.getValue();
    }

    public Item getItem(){
        return (Item)itemsForSell.getSelectedItem();
    }
}

class CustomTable extends JScrollPane{
    private JTable table;
    private ItemListTableModel tableModel;

    CustomTable(){
        super();
        tableModel = new ItemListTableModel();
        table = new JTable(tableModel);
        table.setShowGrid(false);
        setViewportView(table);
    }

    public ItemListTableModel getTableModel(){
        return this.tableModel;
    }

}

class ItemListTableModel extends AbstractTableModel{
    private static List<ItemCart> itemCartList;
    private static String[] columnNames = {"Item name", "Quantity", "Price"};
    private static BigDecimal discount = BigDecimal.ZERO;

    ItemListTableModel(){
        itemCartList = new ArrayList<>();
    }

    public void addItemCart(ItemCart itemCart){
        int temp = 0;
        for(ItemCart existingItem : itemCartList){
            if(existingItem.getItemName() == itemCart.getItemName()){
                int newQuantity = existingItem.getItemQuantity() + itemCart.getItemQuantity();
                BigDecimal newPrice = existingItem.getTotalPrice().add(itemCart.getTotalPrice());
                existingItem.setItemQuantity(newQuantity);
                existingItem.setTotalPrice(newPrice);
                fireTableCellUpdated(temp,1);
                fireTableCellUpdated(temp,2);
                fireTableCellUpdated(getTotalPriceRow(),2);
                return;
            }
            temp++; 
        }
        itemCartList.add(itemCart);
        fireTableRowsInserted(itemCartList.size() - 1, itemCartList.size() - 1);
    }

    @Override
    public int getRowCount() {
        return itemCartList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        // Check if it's the last row
        if (rowIndex == this.getTotalPriceRow()) {
            // Calculate and display the total price of all the previous items
            if (columnIndex == 0) {
                return "";
            } else if (columnIndex == 1) {
                return "Total"; // Leave the quantity column empty
            } else if (columnIndex == 2) {
                BigDecimal totalPrice = BigDecimal.ZERO;
                for (ItemCart item : itemCartList) {
                    totalPrice = totalPrice.add(item.getTotalPrice());
                }
                totalPrice = totalPrice.subtract(totalPrice.multiply(discount));
                return totalPrice;
            }
        }

        ItemCart itemCart = itemCartList.get(rowIndex);
        switch(columnIndex){
            case 0:
            return itemCart.getItemName();
            case 1:
            return itemCart.getItemQuantity();
            case 2:
            return itemCart.getTotalPrice();
            default:
            return null;

        }
        
    }

    public int getTotalPriceRow(){
        return itemCartList.size();
    }

    public void setDiscount(BigDecimal discount){
        ItemListTableModel.discount = discount;
        fireTableCellUpdated(getTotalPriceRow(),2);
    }

    public void exportToTextFile() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String fileName = timeStamp + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the column names
            for (int i = 0; i < getColumnCount(); i++) {
                writer.write(getColumnName(i));
                if (i < getColumnCount() - 1) {
                    writer.write("\t"); // Use a tab as a delimiter
                }
            }
            writer.write("\n"); // Move to the next line

            // Write the data rows
            for (int row = 0; row < getRowCount(); row++) {
                for (int col = 0; col < getColumnCount(); col++) {
                    Object value = getValueAt(row, col);
                    writer.write(value.toString());
                    if (col < getColumnCount() - 1) {
                        writer.write("\t\t"); // Use a tab as a delimiter
                    }
                }
                writer.write("\n"); // Move to the next line
            }

            writer.flush();
            System.out.println("Table data exported to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting table data: " + e.getMessage());
        }
    }

    public void clearTable(){
        ItemListTableModel.itemCartList.clear();
        ItemListTableModel.discount = BigDecimal.ZERO;
        fireTableDataChanged();
    }

}

    //responsible for row format in tablemodel
class ItemCart{
    private String itemName;
    private int itemQuantity;
    private BigDecimal totalPrice;

    ItemCart(String itemName, int itemQuantity, BigDecimal totalPrice){
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
    }

    public String getItemName(){
        return this.itemName;
    }

    public int getItemQuantity(){
        return this.itemQuantity;
    }

    public void setItemQuantity(int qty){
        this.itemQuantity = qty;
    }

    public BigDecimal getTotalPrice(){
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public ItemCart getItemCart(){
        return this;
    }
}
//endinnerComponents

//discount classes
class Discount{
    protected BigDecimal discountValue = BigDecimal.ZERO;
    protected String discountName;
    Discount(){
        this.discountName = "None";
    }

    public void setDiscountValue(BigDecimal discountValue){
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountValue(){
        return this.discountValue;
    }

    @Override
    public String toString(){
        return this.discountName;
    }

}

class RoyaltyCard extends Discount{
    private int royaltyID[] = {1171103354,1171103355};
    RoyaltyCard(){
        this.discountName = "Royalty Card";
        setDiscountValue();
    }

    public void setDiscountValue(){
        BigDecimal royaltyCard = new BigDecimal("0.05");
        this.discountValue = royaltyCard;
    }

    public int[] getRoyaltyIDList(){
        return this.royaltyID;
    }

}

//implementing one-to-many relationship with items
class Category{
String category; //String for category name
ArrayList<Item>itemList;

    Category(String category){
        this.category = category;
        itemList = new ArrayList<>();
    }

    public void addItem(Item item){
        itemList.add(item);
    }

    public void removeItem(Item item){
        itemList.remove(item);
    }

    public int getNumberOfItems(){
        return itemList.size();
    }

}

//implementing many-to-one relationship with category
class Item{
    String itemName;
    BigDecimal itemPrice;
    int itemID;

    Item(String itemName, BigDecimal itemPrice,int itemID){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemID = itemID;
    }

    @Override
    public String toString(){
        return this.itemName;
    }

    public BigDecimal getItemPrice(){
        return this.itemPrice;
    }

    public String getItemName(){
        return this.itemName;

    }
    
}