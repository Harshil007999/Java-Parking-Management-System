import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Car Parking Management System - Swing GUI version.
 * Same logic as the console version (10 slots, Rs. 20 per hour),
 * but all input/output happens through windows, buttons and a table.
 */
public class SimpleParkingGUI extends JFrame {

    // ---------- Parking data (same as the console program) ----------
    static final int TOTAL_SLOTS = 10;
    static final int RATE_PER_HOUR = 20;
    private final String[] slots = new String[TOTAL_SLOTS];
    private int totalMoney = 0;

    // ---------- GUI components ----------
    private final JTextField carNumberField = new JTextField(14);
    private final JSpinner hoursSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"Slot No", "Status"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;   // user cannot edit the table
                }
            };
    private final JTable table = new JTable(tableModel);
    private final JLabel earnedLabel = new JLabel("Total money earned: Rs. 0");
    private final JLabel messageLabel = new JLabel("Welcome! Enter a car number to begin.");

    public SimpleParkingGUI() {
        super("Car Parking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        refreshTable();
        setSize(560, 520);
        setLocationRelativeTo(null);   // centre on screen
    }

    // ---------- Building the screen ----------
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Car number:"));
        row1.add(carNumberField);
        JButton parkBtn = new JButton("Park Car");
        JButton searchBtn = new JButton("Search Car");
        row1.add(parkBtn);
        row1.add(searchBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Hours parked:"));
        row2.add(hoursSpinner);
        JButton removeBtn = new JButton("Remove Car (Pay Bill)");
        row2.add(removeBtn);

        panel.add(row1);
        panel.add(row2);

        // Button actions
        parkBtn.addActionListener(e -> parkCar());
        searchBtn.addActionListener(e -> searchCar());
        removeBtn.addActionListener(e -> removeCar());
        carNumberField.addActionListener(e -> parkCar());   // Enter key parks the car

        return panel;
    }

    private JScrollPane buildTablePanel() {
        table.setRowHeight(26);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Green row = empty slot, red row = occupied slot
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                boolean empty = "Empty".equals(t.getValueAt(row, 1));
                if (!isSelected) {
                    c.setBackground(empty ? new Color(210, 240, 210) : new Color(245, 205, 205));
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10),
                BorderFactory.createTitledBorder("Parking Slots")));
        return scroll;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        earnedLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(earnedLabel);
        panel.add(messageLabel);
        return panel;
    }

    // ---------- Parking logic (converted from the console methods) ----------
    private void parkCar() {
        String number = readCarNumber();
        if (number == null) return;

        if (findCar(number) != -1) {
            showMessage("This car is already parked!", true);
            return;
        }

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                slots[i] = number;
                refreshTable();
                showMessage("Car " + number + " parked in slot number " + (i + 1), false);
                carNumberField.setText("");
                return;
            }
        }
        showMessage("Sorry, parking is FULL!", true);
    }

    private void removeCar() {
        String number = readCarNumber();
        if (number == null) return;

        int index = findCar(number);
        if (index == -1) {
            showMessage("Car not found!", true);
            return;
        }

        int hours = (Integer) hoursSpinner.getValue();
        if (hours < 1) {
            hours = 1;
        }

        int bill = hours * RATE_PER_HOUR;
        totalMoney += bill;
        slots[index] = null;

        refreshTable();
        earnedLabel.setText("Total money earned: Rs. " + totalMoney);
        carNumberField.setText("");
        hoursSpinner.setValue(1);
        showMessage("Car " + number + " removed. Bill: Rs. " + bill, false);

        JOptionPane.showMessageDialog(this,
                "---- BILL ----\n"
                        + "Car number : " + number + "\n"
                        + "Hours      : " + hours + "\n"
                        + "Amount     : Rs. " + bill,
                "Parking Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchCar() {
        String number = readCarNumber();
        if (number == null) return;

        int index = findCar(number);
        if (index == -1) {
            showMessage("Car not found.", true);
        } else {
            showMessage("Car " + number + " is parked in slot number " + (index + 1), false);
            table.setRowSelectionInterval(index, index);   // highlight the slot
        }
    }

    private int findCar(String number) {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] != null && slots[i].equals(number)) {
                return i;
            }
        }
        return -1;
    }

    // ---------- Helpers ----------
    /** Reads the car number from the text field. Returns null if it is empty. */
    private String readCarNumber() {
        String number = carNumberField.getText().trim().toUpperCase();
        if (number.isEmpty()) {
            showMessage("Please enter a car number first.", true);
            return null;
        }
        return number;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            tableModel.addRow(new Object[]{i + 1, slots[i] == null ? "Empty" : slots[i]});
        }
    }

    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.setForeground(isError ? new Color(180, 0, 0) : new Color(0, 120, 0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleParkingGUI().setVisible(true));
    }
}
