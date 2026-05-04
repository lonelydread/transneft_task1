package ru.singularity.task1.ui;

import ru.singularity.task1.model.ShapFeature;
import ru.singularity.task1.model.ShapResult;
import ru.singularity.task1.service.ShapService;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class InterpretationDialog extends JDialog {

    private final ShapService shapService;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    public InterpretationDialog(Frame parent, ShapService shapService) {
        super(parent, "Интерпретация результатов (SHAP-анализ)", true);
        this.shapService = shapService;
        setSize(740, 540);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel loading = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel("Выполняется SHAP-анализ...", SwingConstants.CENTER);
        loadingLabel.setFont(loadingLabel.getFont().deriveFont(14f));
        loading.add(loadingLabel, BorderLayout.CENTER);
        cards.add(loading, "loading");

        add(cards);
        cardLayout.show(cards, "loading");

        new SwingWorker<ShapResult, Void>() {
            @Override
            protected ShapResult doInBackground() {
                return shapService.analyze();
            }

            @Override
            protected void done() {
                try {
                    ShapResult result = get();
                    JPanel results = buildResultsPanel(result);
                    cards.add(results, "results");
                    cardLayout.show(cards, "results");
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(InterpretationDialog.this,
                            "Ошибка анализа: " + cause.getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        }.execute();
    }

    private JPanel buildResultsPanel(ShapResult result) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));

        double maxImp = result.getFeatures().stream()
                .mapToDouble(ShapFeature::getImportance)
                .max().orElse(1.0);

        String[] cols = {"Ранг", "Параметр", "Важность (|SHAP|)"};
        Object[][] rows = result.getFeatures().stream().map(f -> {
            int filled = (int) Math.round(f.getImportance() / maxImp * 20);
            String bar = "█".repeat(filled) + "░".repeat(20 - filled)
                    + String.format("  %.4f", f.getImportance());
            return new Object[]{f.getRank(), f.getName(), bar};
        }).toArray(Object[][]::new);

        JTable table = new JTable(rows, cols) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(340);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JLabel tableTitle = new JLabel("Топ-" + result.getFeatures().size() + " факторов влияния на прогноз модели:");
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JPanel tablePanel = new JPanel(new BorderLayout(0, 4));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        String text = result.getRecommendations().stream()
                .map(r -> "• " + r)
                .collect(Collectors.joining("\n\n"));
        textArea.setText(text);
        textArea.setCaretPosition(0);

        JLabel recTitle = new JLabel("Рекомендации:");
        recTitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JPanel recPanel = new JPanel(new BorderLayout(0, 4));
        recPanel.add(recTitle, BorderLayout.NORTH);
        recPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, recPanel);
        split.setResizeWeight(0.62);
        split.setDividerLocation(270);
        split.setBorder(null);

        JButton closeBtn = new JButton("Закрыть");
        closeBtn.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.add(closeBtn);

        panel.add(split, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }
}
