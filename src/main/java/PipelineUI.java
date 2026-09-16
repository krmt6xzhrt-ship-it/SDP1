import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PipelineUI extends JFrame {

    private JTextField nameField;
    private JComboBox<SourceType> sourceBox;
    private JTextField destinationField;

    private JSpinner intervalSpinner;
    private JSpinner batchSpinner;
    private JSpinner retrySpinner;
    private JSpinner parallelSpinner;

    private JComboBox<String> formatBox;

    private JCheckBox compressionBox;
    private JCheckBox encryptionBox;
    private JCheckBox monitoringBox;

    private JTextArea resultArea;


    public PipelineUI() {

        setTitle("Data Pipeline Builder");
        setSize(650, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }


    private void createUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15,15));
        mainPanel.setBorder(new EmptyBorder(20,25,20,25));


        // title
        JLabel title = new JLabel("Data Pipeline Builder");
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel description =
                new JLabel("Create and validate your data pipeline configuration");


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(description);

        mainPanel.add(titlePanel, BorderLayout.NORTH);



        // configuration form
        JPanel form = new JPanel(new GridLayout(0,2,10,12));


        nameField = new JTextField("My Pipeline");

        sourceBox =
                new JComboBox<>(SourceType.values());

        destinationField =
                new JTextField("https://example.com/data");


        intervalSpinner =
                new JSpinner(
                        new SpinnerNumberModel(60,10,3600,10)
                );


        batchSpinner =
                new JSpinner(
                        new SpinnerNumberModel(500,100,10000,100)
                );


        retrySpinner =
                new JSpinner(
                        new SpinnerNumberModel(3,0,10,1)
                );


        parallelSpinner =
                new JSpinner(
                        new SpinnerNumberModel(1,1,16,1)
                );


        formatBox =
                new JComboBox<>(
                        new String[]{"JSON","CSV","PARQUET"}
                );


        compressionBox =
                new JCheckBox("Enable compression");

        encryptionBox =
                new JCheckBox("Enable encryption");

        monitoringBox =
                new JCheckBox("Enable monitoring");


        form.add(new JLabel("Pipeline name:"));
        form.add(nameField);

        form.add(new JLabel("Source type:"));
        form.add(sourceBox);

        form.add(new JLabel("Destination:"));
        form.add(destinationField);

        form.add(new JLabel("Interval (seconds):"));
        form.add(intervalSpinner);

        form.add(new JLabel("Batch size:"));
        form.add(batchSpinner);

        form.add(new JLabel("Retry count:"));
        form.add(retrySpinner);

        form.add(new JLabel("Parallelism:"));
        form.add(parallelSpinner);

        form.add(new JLabel("Format:"));
        form.add(formatBox);

        form.add(compressionBox);
        form.add(encryptionBox);

        form.add(monitoringBox);
        form.add(new JLabel(""));



        // presets
        JButton basicButton =
                new JButton("BASIC");

        JButton safeButton =
                new JButton("SAFE");

        JButton performanceButton =
                new JButton("PERFORMANCE");


        JPanel presetPanel = new JPanel();

        presetPanel.add(basicButton);
        presetPanel.add(safeButton);
        presetPanel.add(performanceButton);


        JPanel centerPanel =
                new JPanel(new BorderLayout(10,10));

        centerPanel.add(form,BorderLayout.CENTER);
        centerPanel.add(presetPanel,BorderLayout.SOUTH);


        mainPanel.add(centerPanel,BorderLayout.CENTER);



        // bottom
        JButton buildButton =
                new JButton("BUILD PIPELINE");

        buildButton.setFont(
                new Font("Arial",Font.BOLD,16)
        );


        resultArea = new JTextArea(5,30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);


        JScrollPane scrollPane =
                new JScrollPane(resultArea);


        JPanel bottomPanel =
                new JPanel(new BorderLayout(10,10));

        bottomPanel.add(buildButton,BorderLayout.NORTH);
        bottomPanel.add(scrollPane,BorderLayout.CENTER);


        mainPanel.add(bottomPanel,BorderLayout.SOUTH);


        add(mainPanel);



        // buttons
        buildButton.addActionListener(e -> buildPipeline());

        basicButton.addActionListener(e ->
                showPreset(PipelinePresets.basic())
        );

        safeButton.addActionListener(e ->
                showPreset(PipelinePresets.safe())
        );

        performanceButton.addActionListener(e ->
                showPreset(PipelinePresets.performance())
        );
    }



    private void buildPipeline() {

        try {

            String name =
                    nameField.getText();

            SourceType source =
                    (SourceType) sourceBox.getSelectedItem();

            String destination =
                    destinationField.getText();

            int interval =
                    (int) intervalSpinner.getValue();

            int batch =
                    (int) batchSpinner.getValue();

            int retry =
                    (int) retrySpinner.getValue();

            int parallel =
                    (int) parallelSpinner.getValue();

            String format =
                    (String) formatBox.getSelectedItem();



            ScheduleConfig schedule =
                    new ScheduleConfig(interval,true);



            DataPipeline.Builder builder =
                    new DataPipeline.Builder(
                            name,
                            source,
                            destination,
                            schedule
                    );


            builder
                    .batchSize(batch)
                    .retryCount(retry)
                    .parallelism(parallel)
                    .format(format);



            if(compressionBox.isSelected()){
                builder.enableCompression();
            }


            if(encryptionBox.isSelected()){
                builder.enableEncryption();
            }


            if(monitoringBox.isSelected()){
                builder.enableMonitoring();
            }



            DataPipeline pipeline =
                    builder.build();


            resultArea.setText(
                    "Pipeline created successfully!\n\n"
                            + pipeline
            );


        } catch(Exception ex){

            resultArea.setText(
                    "Validation error:\n\n"
                            + ex.getMessage()
            );
        }
    }



    private void showPreset(DataPipeline pipeline) {

        nameField.setText(
                pipeline.getName()
        );

        sourceBox.setSelectedItem(
                pipeline.getSourceType()
        );

        destinationField.setText(
                pipeline.getDestination()
        );

        intervalSpinner.setValue(
                pipeline.getSchedule().intervalSeconds()
        );

        batchSpinner.setValue(
                pipeline.getBatchSize()
        );

        retrySpinner.setValue(
                pipeline.getRetryCount()
        );

        parallelSpinner.setValue(
                pipeline.getParallelism()
        );

        formatBox.setSelectedItem(
                pipeline.getFormat()
        );

        compressionBox.setSelected(
                pipeline.isCompression()
        );

        encryptionBox.setSelected(
                pipeline.isEncryption()
        );

        monitoringBox.setSelected(
                pipeline.isMonitoring()
        );


        resultArea.setText(
                "Preset loaded.\n\n"
                        + pipeline
        );
    }



    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            PipelineUI window =
                    new PipelineUI();

            window.setVisible(true);

        });
    }
}