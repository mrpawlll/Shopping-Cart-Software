# Individual Assignment - Shopping Cart
Project task is to create a Shopping Cart software. Prices for each item are hardcoded inside the program.

# Steps to Run Program
1. Recompile the source code file located at `src/main/java/indivassign/`. If you're using VSCode, you can use the integrated plugin. Just open the project at the root directory where the `pom.xml` is located.
1. Run `BillingProgram.java`

or

1. Run `java -jar Shopping-Cart-Software.jar` if Java is installed in your system's machine.

# How to Use
1. Select the item that the customer wishes to buy i.e Meat, then select the quantity that the customer is buying for that item.
1. Then, click the `Add Item` once the selected item and quantity is added.

Do this for each unique item that the customer is buying. Once all the unique items are added into the bil, click the `Confirm` button located at the bottom-right of the program's screen. A prompt screen will pop-up asking the cashier to confirm whether all the items are already added.

Then, the cashier can see the total price of all the items combined. If the customer has a `Royalty Card`, cashier can select `Royalty Card` at the dropdown list located at the right-side of the program's screen. This will show-up a text-field below the dropdown list. Currently, there are only two unique `Royalty Card IDs`, those being:

1. 1171103354
1. 1171103355

Once the `Royalty Card IDs` are entered, click `Verify`. The total price should be reduced to a lower price if the `Royalty Card ID` is valid. If the cashier wishes to printout the bill, click the `Print` button located at the bottom-right of the program's screen. This will output a .txt file at the root directory of the project.

Once the current customer has done doing the transaction, cashier can click the `Next` button to serve the next customer.