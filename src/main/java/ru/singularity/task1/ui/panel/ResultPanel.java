package ru.singularity.task1.ui.panel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.singularity.task1.model.DeliveredItem;
import ru.singularity.task1.model.OptimizationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
@ConditionalOnExpression("!T(java.awt.GraphicsEnvironment).isHeadless()")
public class ResultPanel extends JPanel {

    private final JLabel rLabel;
    private final DefaultTableModel tableModel;

    public ResultPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(320, 0));

        rLabel = new JLabel("Коэффициент r: —");
        rLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        tableModel = new DefaultTableModel(new String[]{"От", "Кому", "Объём"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);

        add(rLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void showResult(OptimizationResult result) {
        SwingUtilities.invokeLater(() -> {
            rLabel.setText(String.format("Коэффициент r: %.4f", result.getR()));
            tableModel.setRowCount(0);
            List<DeliveredItem> items = result.getDelivered();
            if (items != null) {
                for (DeliveredItem item : items) {
                    tableModel.addRow(new Object[]{
                            item.getFrom(),
                            item.getTo(),
                            String.format("%.2f", item.getDelivered())
                    });
                }
            }
        });
    }
}
