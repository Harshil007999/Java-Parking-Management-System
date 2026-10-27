import java.util.Scanner;


public class SimpleParking {

    static int TOTAL_SLOTS = 10;          
    static int RATE_PER_HOUR = 20;        
    static String[] slots = new String[TOTAL_SLOTS];  
    static int totalMoney = 0;            

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== CAR PARKING SYSTEM =====");
            System.out.println("1. Park a car");
            System.out.println("2. Remove a car (pay bill)");
            System.out.println("3. Show all slots");
            System.out.println("4. Search a car");
            System.out.println("5. Show total money earned");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                parkCar(sc);
            } else if (choice == 2) {
                removeCar(sc);
            } else if (choice == 3) {
                showSlots();
            } else if (choice == 4) {
                searchCar(sc);
            } else if (choice == 5) {
                System.out.println("Total money earned: Rs. " + totalMoney);
            } else if (choice != 0) {
                System.out.println("Wrong choice! Try again.");
            }
        } while (choice != 0);

        System.out.println("Thank you!");
    }

    
    static void parkCar(Scanner sc) {
        System.out.print("Enter car number: ");
        String number = sc.next().toUpperCase();

        if (findCar(number) != -1) {
            System.out.println("This car is already parked!");
            return;
        }

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                slots[i] = number;
                System.out.println("Car parked in slot number " + (i + 1));
                return;
            }
        }
        System.out.println("Sorry, parking is FULL!");
    }

    
    static void removeCar(Scanner sc) {
        System.out.print("Enter car number: ");
        String number = sc.next().toUpperCase();

        int index = findCar(number);
        if (index == -1) {
            System.out.println("Car not found!");
            return;
        }

        System.out.print("How many hours was the car parked? ");
        int hours = sc.nextInt();
        if (hours < 1) {
            hours = 1;   
        }

        int bill = hours * RATE_PER_HOUR;
        totalMoney = totalMoney + bill;
        slots[index] = null;   

        System.out.println("---- BILL ----");
        System.out.println("Car number : " + number);
        System.out.println("Hours      : " + hours);
        System.out.println("Amount     : Rs. " + bill);
        System.out.println("--------------");
    }

    
    static void showSlots() {
        System.out.println("\nSlot No | Status");
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                System.out.println("   " + (i + 1) + "    | Empty");
            } else {
                System.out.println("   " + (i + 1) + "    | " + slots[i]);
            }
        }
    }

    
    static void searchCar(Scanner sc) {
        System.out.print("Enter car number: ");
        String number = sc.next().toUpperCase();

        int index = findCar(number);
        if (index == -1) {
            System.out.println("Car not found.");
        } else {
            System.out.println("Car is parked in slot number " + (index + 1));
        }
    }

    
    static int findCar(String number) {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] != null && slots[i].equals(number)) {
                return i;
            }
        }
        return -1;
    }
}