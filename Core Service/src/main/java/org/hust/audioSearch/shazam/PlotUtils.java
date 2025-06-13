package org.hust.audioSearch.shazam;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.chart.renderer.LookupPaintScale;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlotUtils {
    public static void plotDoubleArray(double[] data, String title) {
        XYSeries series = new XYSeries("Signal");
        for (int i = 0; i < data.length; i++) {
            series.add(i, data[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Index",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void plotSpectrogram(double[][] spectrogram, double sampleRate, double hopSize, String title) {
        double[] minMax = new double[2];
        XYZDataset dataset = createXYZDataset(spectrogram, minMax, sampleRate, hopSize);
        PaintScale paintScale = createColorPaintScale(0, 1);
        JFreeChart chart = createSpectrogramChart(dataset, paintScale, title);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static XYZDataset createXYZDataset(double[][] spectrogram, double[] minMax, double sampleRate, double hopSize) {
        int timeSteps = spectrogram.length;
        int freqBins = spectrogram[0].length;

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        List<Double> zList = new ArrayList<>();

        double epsilon = 1e-10;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        double[][] logSpec = new double[timeSteps][freqBins];
        for (int t = 0; t < timeSteps; t++) {
            for (int f = 0; f < freqBins; f++) {
                double value = Math.log10(spectrogram[t][f] + epsilon);
                logSpec[t][f] = value;
                if (value < min) min = value;
                if (value > max) max = value;
            }
        }

        for (int t = 0; t < timeSteps; t++) {
            for (int f = 0; f < freqBins; f++) {
                double time = t * hopSize / sampleRate;
                double freq = f * sampleRate / (2.0 * freqBins);
                double norm = (logSpec[t][f] - min) / (max - min);
                xList.add(time);
                yList.add(freq);
                zList.add(norm);
            }
        }

        minMax[0] = min;
        minMax[1] = max;

        double[] x = xList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = yList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] z = zList.stream().mapToDouble(Double::doubleValue).toArray();

        DefaultXYZDataset dataset = new DefaultXYZDataset();
        dataset.addSeries("Spectrogram", new double[][]{x, y, z});
        return dataset;
    }

    private static JFreeChart createSpectrogramChart(XYZDataset dataset, PaintScale paintScale, String title) {
        NumberAxis xAxis = new NumberAxis("Time (s)");
        NumberAxis yAxis = new NumberAxis("Frequency (Hz)");
        yAxis.setAutoRangeIncludesZero(false);

        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockWidth(0.01);   // ~10ms
        renderer.setBlockHeight(10.0);  // ~10Hz
        renderer.setPaintScale(paintScale);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
    }

    private static PaintScale createColorPaintScale(double min, double max) {
        LookupPaintScale scale = new LookupPaintScale(min, max, Color.BLACK);
        scale.add(min, new Color(0, 0, 128));       // Dark blue
        scale.add(min + (max - min) * 0.25, Color.BLUE);
        scale.add(min + (max - min) * 0.5, Color.GREEN);
        scale.add(min + (max - min) * 0.75, Color.YELLOW);
        scale.add(max, Color.RED);                 // Hot
        return scale;
    }

    public static void plotPeaks(Peak[] peaks) {
        XYSeries series = new XYSeries("Peaks");

        for (Peak peak : peaks) {
            series.add(peak.time, peak.frequency);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Peak Frequencies Over Time",
                "Time (s)",
                "Frequency (Hz)",
                dataset
        );

        // Configure plot to show dots only (no lines)
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, false);  // No line
        renderer.setSeriesShapesVisible(0, true);  // Show dots
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesPaint(0, Color.BLUE);    // Optional: set dot color

        plot.setRenderer(renderer);

        // Display the chart
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Peak Plot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ChartPanel(chart));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
