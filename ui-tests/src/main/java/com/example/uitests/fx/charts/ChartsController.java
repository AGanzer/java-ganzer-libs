package com.example.uitests.fx.charts;

import com.example.uitests.fx.FxTestApp;
import de.ganzer.core.GMath;
import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.TextFormat;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.core.validation.ValidatorExceptionRef;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

public class ChartsController implements Initializable {
    private final ObservableList<DistributionEntry> distributions = FXCollections.observableArrayList(
            new DistributionEntry("Uniform", new UniformFloatingPointDistributionEnvelop()),
            new DistributionEntry("Normal", new NormalDistributionEnvelop()),
            new DistributionEntry("Logarithmic Normal", new LogNormalDistributionEnvelop()),
            new DistributionEntry("Exponential", new ExponentialDistributionEnvelop()),
            new DistributionEntry("Extreme Value", new ExtremeValueDistributionEnvelop()),
            new DistributionEntry("Fisher F", new FisherFDistributionEnvelop()),
            new DistributionEntry("Student T", new StudentTDistributionEnvelop()),
            new DistributionEntry("Chi Squared", new ChiSquaredDistributionEnvelop()),
            new DistributionEntry("Cauchy", new CauchyDistributionEnvelop()),
            new DistributionEntry("Gamma", new GammaDistributionEnvelop()),
            new DistributionEntry("Weibull", new WeibullDistributionEnvelop()));
    private final NumberValidator numValuesValidator = new NumberValidator(10, 500000);
    private boolean running = false;

    public ComboBox<DistributionEntry> distributionBox;
    public TitledPane settingsPane;
    public BarChart<String, Double> barChart;
    public ScatterChart<Double, Double> scatterChart;
    public LineChart<Double, Double> lineChart;
    public PieChart pieChart;
    public TextField numValuesInput;
    public VBox allSettingsBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        distributionBox.setItems(distributions);
        distributionBox.getSelectionModel().select(0);

        new ValidatorTextFormatter(numValuesValidator, numValuesInput);
        numValuesInput.setText(numValuesValidator.formatText("100000", TextFormat.DISPLAY));

        updateSettings();
    }

    public void generateNumbers(ActionEvent ignored) {
        try {
            if (!running)
                generate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void distributionChanged(ActionEvent ignored) {
        if (running)
            return;

        updateSettings();
        clearChartData();
    }

    private void setRunning(boolean running) {
        allSettingsBox.setDisable(running);
        this.running = running;
    }

    private void updateSettings() {
        var entry = distributionBox.getSelectionModel().getSelectedItem();
        settingsPane.setContent(entry.getDistribution().getEditor());
    }

    private boolean validate() {
        if (!validateField(numValuesInput))
            return false;

        var settingsController = distributionBox
                .getSelectionModel()
                .getSelectedItem()
                .getDistribution()
                .getController();

        var eRef = new ValidatorExceptionRef();
        var field = settingsController.validate(eRef);

        if (field == null)
            return true;

        showValidationError(field, eRef.getException());

        return false;
    }

    private boolean validateField(TextField field) {
        try {
            ((ValidatorTextFormatter)field.getTextFormatter()).validate();
        } catch (ValidatorException e) {
            showValidationError(field, e);
            return false;
        }

        return true;
    }

    private static void showValidationError(TextField field, ValidatorException e) {
        FxTestApp.alertInfo(e.getLocalizedMessage());

        field.requestFocus();
        field.selectAll();
    }

    private void clearChartData() {
        barChart.getData().clear();
        scatterChart.getData().clear();
        lineChart.getData().clear();
    }

    private void generate() throws ParseException {
        if (!validate())
            return;

        setRunning(true);
        clearChartData();

        var task = new Task<>() {
            @Override
            protected Object call() throws Exception {
                try {
                    setCursor(Cursor.WAIT);
                    generateData();
                } finally {
                    setCursor(Cursor.DEFAULT);
                    Platform.runLater(() -> setRunning(false));
                }

                return null;
            }
        };

        new Thread(task).start();
    }

    private void setCursor(Cursor cursor) {
        Platform.runLater(() -> barChart.getScene().setCursor(cursor));
    }

    private void generateData() throws ParseException {
        var count = NumberFormat.getInstance().parse(numValuesInput.getText()).intValue();
        var dist = distributionBox.getSelectionModel().getSelectedItem();
        var values = new double[count];
        var minValue = Double.MAX_VALUE;
        var maxValue = Double.MIN_VALUE;

        for (int i = 0; i < count; ++i) {
            var value = (double)dist.getDistribution().getNext();

            if (value < minValue)
                minValue = value;

            if (value > maxValue)
                maxValue = value;

            values[i] = value;
        }

        fillCharts(values, minValue, maxValue);
    }

    private void fillCharts(double[] values, double minValue, double maxValue) {
        createBarData(values, minValue, maxValue);
        createScatterData(values, minValue, maxValue);
        createLineData(values, minValue, maxValue);
        createPieChartData(values, minValue, maxValue);
    }

    @SuppressWarnings("DuplicatedCode")
    private void createBarData(double[] values, double minValue, double maxValue) {
        final int BAR_COUNT = 80;

        var bars = new double[BAR_COUNT];
        var diff = maxValue - minValue;
        var fac = bars.length / diff;

        for (var value: values) {
            var index = GMath.toRange((int)((value - minValue) * fac), 0, bars.length - 1);
            ++bars[index];
        }

        var series = new XYChart.Series<String, Double>();

        for (int i = 0; i < BAR_COUNT; ++i)
            series.getData().add(new XYChart.Data<>(Integer.toString(i + 1), bars[i]));

        Platform.runLater(() -> {
            barChart.setTitle(String.format("Values in the range from %,f to %,f", minValue, maxValue));
            barChart.getData().add(series);
        });
    }

    private void createScatterData(double[] values, double minValue, double maxValue) {
        final int NUM_VALUES = 6000;

        var series = new XYChart.Series<Double, Double>();

        for (int i = 0; i < Math.min(NUM_VALUES, values.length) - 1; i += 2)
            series.getData().add(new XYChart.Data<>(values[i], values[i + 1]));

        Platform.runLater(() -> {
            scatterChart.setTitle(String.format("Values in the range from %,f to %,f\nFirst %,d values", minValue, maxValue, Math.min(NUM_VALUES, values.length)));
            scatterChart.getData().add(series);
        });
    }

    private void createLineData(double[] values, double minValue, double maxValue) {
        var series = new XYChart.Series<Double, Double>();

        for (int i = 0; i < Math.min(200, values.length); ++i)
            series.getData().add(new XYChart.Data<>((double)i, values[i]));

        Platform.runLater(() -> {
            lineChart.setTitle(String.format("Values in the range from %,f to %,f\nFirst %,d values", minValue, maxValue, Math.min(200, values.length)));
            lineChart.getData().add(series);
        });
    }

    @SuppressWarnings("DuplicatedCode")
    private void createPieChartData(double[] values, double minValue, double maxValue) {
        final int SEGMENT_COUNT = 20;

        var segments = new int[SEGMENT_COUNT];
        var diff = maxValue - minValue;
        var fac = segments.length / diff;

        for (var value: values) {
            var index = GMath.toRange((int)((value - minValue) * fac), 0, segments.length - 1);
            ++segments[index];
        }

        ObservableList<PieChart.Data> slices = FXCollections.observableArrayList();

        for (int i = 0; i < SEGMENT_COUNT; ++i)
            slices.add(new PieChart.Data(String.format("%d: %,d", i + 1, segments[i]), segments[i]));

        Platform.runLater(() -> {
            pieChart.setTitle(String.format("Values in the range from %,f to %,f", minValue, maxValue));
            pieChart.setData(slices);
        });
    }
}