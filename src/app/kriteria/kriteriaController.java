package app.kriteria;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class kriteriaController {
    private static final String DB_URL = "jdbc:sqlite:TA_Topsis.db";

    @FXML private TableView<kriteriaModel> menukriteria_rv_kriteria;
    @FXML private TableColumn<kriteriaModel, Integer> menuKriteria_tc_id;
    @FXML private TableColumn<kriteriaModel, String> menuKriteria_tc_nama;
    @FXML private TableColumn<kriteriaModel, Float> menuKriteria_tc_bobot;
    @FXML private TableColumn<kriteriaModel, String> menuKriteria_tc_costBenefit;

    @FXML private Button bt_kriteria;
    @FXML private Button bt_alternatif;
    @FXML private Button bt_hitungTopsis;
    @FXML private Button bt_rawMatriks;
    @FXML private Button bt_matriksTernormalisasi;
    @FXML private Button bt_matriksTerbobot;
    @FXML private Button bt_dashboard;

    @FXML private Button bt_laporan;

    @FXML
    public void initialize() {
        bt_kriteria.setOnAction(evt -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/kriteria/menu_kriteria.fxml"));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(newScene); 

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        });
        bt_alternatif.setOnAction(evt -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/alternatifSimpel/alternatifSimpel.fxml"));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(newScene); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        });

        bt_hitungTopsis.setOnAction(evt -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/hitungTopsis/hitungTopsis.fxml"));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(newScene); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        });
        bt_rawMatriks.setOnAction(evt -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/alternatif/menu_alternatif.fxml"));
                Parent newRoot = loader.load();
                Scene newScene = new Scene(newRoot);
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(newScene); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        bt_matriksTernormalisasi.setOnAction(evt -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/matriksTernormalisasi/matriksTernormalisasi.fxml"));
                Parent newRoot = loader.load();
                Scene newScene = new Scene(newRoot);
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(newScene); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        bt_matriksTerbobot.setOnAction(evt -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/matriksTerbobot/matriksTerbobot.fxml"));
                Parent newRoot = loader.load();
                Scene newScene = new Scene(newRoot);
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(newScene); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        bt_dashboard.setOnAction(evt -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/dashboard/dashboard.fxml"));
                Parent newRoot = loader.load();
                Scene newScene = new Scene(newRoot);
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(newScene); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        if (bt_laporan != null) bt_laporan.setOnAction(e -> onReport());

        menuKriteria_tc_id.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        menuKriteria_tc_nama.setCellValueFactory(c -> c.getValue().namaProperty());
        menuKriteria_tc_bobot.setCellValueFactory(c -> c.getValue().bobotProperty().asObject());
        menuKriteria_tc_costBenefit.setCellValueFactory(c -> c.getValue().costBenefitProperty());

        menuKriteria_tc_bobot.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        menuKriteria_tc_bobot.setOnEditCommit((TableColumn.CellEditEvent<kriteriaModel, Float> event) -> {
            kriteriaModel item = event.getRowValue();
            Float newValue = event.getNewValue();
            if (newValue == null) return;

            // update model
            item.bobotProperty().set(newValue);

            // persist in background
            Task<Void> t = new Task<>() {
                @Override protected Void call() throws Exception {
                    updateKriteriaBobot(item.getId(), newValue);
                    return null;
                }
            };
            t.setOnFailed(e -> t.getException().printStackTrace());
            new Thread(t).start();
        });

        // --- costBenefit: click-to-toggle between "Cost" and "Benefit" ---
        menuKriteria_tc_costBenefit.setCellFactory(column -> new TableCell<kriteriaModel, String>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(value);
                    setStyle("-fx-cursor: hand;");
                }
            }

            {
                setOnMouseClicked(evt -> {
                    if (isEmpty()) return;
                    kriteriaModel row = getTableView().getItems().get(getIndex());
                    String current = row.getCostBenefit();
                    String toggled = toggleCostBenefit(current);

                    // update model on FX thread
                    Platform.runLater(() -> row.costBenefitProperty().set(toggled));

                    // persist change in background
                    Task<Void> t = new Task<>() {
                        @Override protected Void call() throws Exception {
                            updateKriteriaCostBenefit(row.getId(), toggled);
                            return null;
                        }
                    };
                    t.setOnFailed(e -> t.getException().printStackTrace());
                    new Thread(t).start();

                    // update cell immediately
                    setText(toggled);
                });
            }
        });

        // Load data in background
        Task<ObservableList<kriteriaModel>> loadTask = new Task<>() {
            @Override
            protected ObservableList<kriteriaModel> call() throws Exception {
                List<kriteriaModel> list = readKriteriaFromDb();
                return FXCollections.observableArrayList(list);
            }
        };

        loadTask.setOnSucceeded(e -> menukriteria_rv_kriteria.setItems(loadTask.getValue()));
        loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());

        Thread loaderThread = new Thread(loadTask);
        loaderThread.setDaemon(true);
        loaderThread.start();
    }
    private void onReport() {
        List<kriteriaModel> data = new ArrayList<>(menukriteria_rv_kriteria.getItems());
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                try (InputStream jrxml = getClass().getResourceAsStream("/reports/reportKriteria.jrxml")) {
                    if (jrxml == null) throw new IllegalStateException("Report template not found");
                    JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);
                    
                    InputStream logoStream = getClass().getResourceAsStream("/reports/logo.png");
                    if (logoStream == null) throw new IllegalStateException("Logo not found at /reports/logo.png");
                    Map<String,Object> params = new HashMap<>();
                    params.put("logo", logoStream);

                    params.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
                    params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("Asia/Jakarta"));

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                    File outPdf = new File("kriteria_report.pdf");
                    JRPdfExporter pdfExporter = new JRPdfExporter();
                    pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outPdf));
                    pdfExporter.exportReport();

                    File out = File.createTempFile("kriteria_report_", ".html");
                    out.deleteOnExit();
                    HtmlExporter exporter = new HtmlExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
                    exporter.exportReport();

                    return out;
                }
            }
        };

        task.setOnSucceeded(ev -> {
            File html = task.getValue();
            Platform.runLater(() -> {
                WebView web = new WebView();
                web.getEngine().load(html.toURI().toString());
                Stage s = new Stage();
                s.setTitle("Report Preview");
                s.setScene(new Scene(web, 900, 700));
                s.show();
            });
        });

        task.setOnFailed(ev -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR, "Report generation failed: " + ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            });
        });

        new Thread(task).start();
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static List<kriteriaModel> readKriteriaFromDb() throws SQLException {
        String sql = "SELECT id_kriteria, nama_kriteria, bobot, costBenefit FROM kriteria";
        List<kriteriaModel> out = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new kriteriaModel(
                        rs.getInt("id_kriteria"),
                        rs.getString("nama_kriteria"),
                        rs.getFloat("bobot"),
                        rs.getString("costBenefit")
                ));
            }
        }
        return out;
    }

    // Specific update for bobot (removes unused-parameter warning)
    private static void updateKriteriaBobot(int id, float value) {
        String sql = "UPDATE kriteria SET bobot = ? WHERE id_kriteria = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setFloat(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Specific update for costBenefit
    private static void updateKriteriaCostBenefit(int id, String value) {
        String sql = "UPDATE kriteria SET costBenefit = ? WHERE id_kriteria = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static String toggleCostBenefit(String current) {
        if (current == null) return "cost";
        String c = current.trim().toLowerCase();
        if (c.equals("cost")) return "benefit";
        if (c.equals("benefit")) return "cost";
        return "cost";
    }
}
