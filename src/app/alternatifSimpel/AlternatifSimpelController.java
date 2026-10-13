package app.alternatifSimpel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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
import java.util.stream.Collectors;

import app.hitungTopsis.ResultRow;

public class AlternatifSimpelController {

    private static final String DB_URL = "jdbc:sqlite:TA_Topsis.db";

    @FXML private TextField tf_cari;
    @FXML private Button bt_cari;
    @FXML private Button bt_reset;

    @FXML private TableView<AlternatifModel> menuAlternatif_tv_alternatif;
    @FXML private TableColumn<AlternatifModel, Integer> menuAlternatif_tc_id;
    @FXML private TableColumn<AlternatifModel, String>  menuAlternatif_tc_nama;

    @FXML private TextField menuAlternatif_tf_id;
    @FXML private TextField menuAlternatif_tf_nama;
    @FXML private Button menuAlternatif_bt_tambah;
    @FXML private Button menuAlternatif_bt_reset;
    @FXML private Button menuAlternatif_bt_hapus;
    @FXML private Button bt_laporan;

    @FXML private Button bt_kriteria;
    @FXML private Button bt_alternatif;
    @FXML private Button bt_hitungTopsis;
    @FXML private Button bt_rawMatriks;
    @FXML private Button bt_matriksTernormalisasi;
    @FXML private Button bt_matriksTerbobot;
    @FXML private Button bt_dashboard;

    private final ObservableList<AlternatifModel> masterList = FXCollections.observableArrayList();
    private FilteredList<AlternatifModel> filteredAlt;

    @FXML
    public void initialize() {
        // Table wiring
        menuAlternatif_tc_id.setCellValueFactory(new PropertyValueFactory<>("id_alternatif"));
        menuAlternatif_tc_nama.setCellValueFactory(new PropertyValueFactory<>("nama_alternatif"));

        // prepare filtered list and bind to table
        filteredAlt = new FilteredList<>(masterList, p -> true);
        menuAlternatif_tv_alternatif.setItems(filteredAlt);

        // load data
        loadAlternatif();

        // search handlers
        if (bt_cari != null) bt_cari.setOnAction(e -> onCari());
        if (bt_reset != null) bt_reset.setOnAction(e -> resetSearchAndView());

        if (bt_laporan != null) bt_laporan.setOnAction(e -> onReport());

        // CRUD handlers
        if (menuAlternatif_bt_tambah != null) menuAlternatif_bt_tambah.setOnAction(e -> handleTambah());
        if (menuAlternatif_bt_reset != null) menuAlternatif_bt_reset.setOnAction(e -> resetFields());
        if (menuAlternatif_bt_hapus != null) menuAlternatif_bt_hapus.setOnAction(e -> handleHapus());

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
    }

    // Load alternatif rows from DB (id + name)
    private void loadAlternatif() {
        Task<List<AlternatifModel>> task = new Task<>() {
            @Override
            protected List<AlternatifModel> call() throws Exception {
                String sql = "SELECT id_alternatif, nama_alternatif FROM alternatif ORDER BY id_alternatif";
                try (Connection c = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = c.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    ObservableList<AlternatifModel> out = FXCollections.observableArrayList();
                    while (rs.next()) {
                        out.add(new AlternatifModel(
                                rs.getInt("id_alternatif"),
                                rs.getString("nama_alternatif")
                        ));
                    }
                    return out;
                }
            }
        };

        task.setOnSucceeded(e -> {
            masterList.setAll(task.getValue());
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert("Gagal memuat data: " + (task.getException() == null ? "unknown" : task.getException().getMessage()));
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    // Search: exact contains first, otherwise nearest by Levenshtein
    private void onCari() {
        String q = tf_cari.getText() == null ? "" : tf_cari.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            filteredAlt.setPredicate(p -> true);
            menuAlternatif_tv_alternatif.setItems(filteredAlt);
            return;
        }

        // contains filter
        List<AlternatifModel> contains = masterList.stream()
                .filter(a -> (a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase()).contains(q))
                .collect(Collectors.toList());
        if (!contains.isEmpty()) {
            menuAlternatif_tv_alternatif.setItems(FXCollections.observableArrayList(contains));
            return;
        }

        // nearest fallback
        AlternatifModel best = null;
        int bestDist = Integer.MAX_VALUE;
        for (AlternatifModel a : masterList) {
            String name = a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase();
            int d = levenshtein(q, name);
            if (d < bestDist) { bestDist = d; best = a; }
        }
        menuAlternatif_tv_alternatif.setItems(best == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(best));
    }

    private void resetSearchAndView() {
        tf_cari.clear();
        filteredAlt.setPredicate(p -> true);
        menuAlternatif_tv_alternatif.setItems(filteredAlt);
    }

    // Add new alternatif (only nama_alternatif)
    private void handleTambah() {
        String nama = menuAlternatif_tf_nama.getText() == null ? "" : menuAlternatif_tf_nama.getText().trim();
        if (nama.isEmpty()) {
            showAlert("Nama alternatif harus diisi");
            return;
        }

        Task<AlternatifModel> task = new Task<>() {
            @Override
            protected AlternatifModel call() throws Exception {
                String sql = "INSERT INTO alternatif (nama_alternatif) VALUES (?)";
                try (Connection c = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, nama);
                    int affected = ps.executeUpdate();
                    if (affected == 0) throw new SQLException("Insert gagal");
                    int newId = -1;
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys != null && keys.next()) newId = keys.getInt(1);
                    }
                    if (newId == -1) {
                        // fallback for SQLite
                        try (Statement s2 = c.createStatement();
                             ResultSet rs2 = s2.executeQuery("SELECT last_insert_rowid() AS id")) {
                            if (rs2.next()) newId = rs2.getInt("id");
                        }
                    }
                    return new AlternatifModel(newId, nama);
                }
            }
        };

        task.setOnSucceeded(e -> {
            AlternatifModel created = task.getValue();
            if (created != null) masterList.add(created);
            resetFields();
            showAlert("Alternatif berhasil ditambahkan");
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert("Tambah gagal: " + (task.getException() == null ? "unknown" : task.getException().getMessage()));
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    // Delete by id (menuAlternatif_tf_id)
    private void handleHapus() {
        String idText = menuAlternatif_tf_id.getText() == null ? "" : menuAlternatif_tf_id.getText().trim();
        if (idText.isEmpty()) {
            showAlert("Isi ID untuk menghapus");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showAlert("ID harus berupa angka");
            return;
        }

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                String sql = "DELETE FROM alternatif WHERE id_alternatif = ?";
                try (Connection c = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int affected = ps.executeUpdate();
                    return affected > 0;
                }
            }
        };

        task.setOnSucceeded(e -> {
            boolean deleted = task.getValue();
            if (deleted) {
                masterList.removeIf(m -> m.getId_alternatif() == id);
                resetFields();
                showAlert("Alternatif dihapus");
            } else {
                showAlert("ID tidak ditemukan");
            }
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert("Hapus gagal: " + (task.getException() == null ? "unknown" : task.getException().getMessage()));
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    private void onReport() {
        List<AlternatifModel> data = new ArrayList<>(menuAlternatif_tv_alternatif.getItems());
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                try (InputStream jrxml = getClass().getResourceAsStream("/reports/reportAlternatif.jrxml")) {
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

                    File outPdf = new File("alternatif_report.pdf");
                    JRPdfExporter pdfExporter = new JRPdfExporter();
                    pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outPdf));
                    pdfExporter.exportReport();

                    File out = File.createTempFile("alternatif_report_", ".html");
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

    private void resetFields() {
        menuAlternatif_tf_id.clear();
        menuAlternatif_tf_nama.clear();
    }

    private void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            a.setContentText(msg);
            a.showAndWait();
        });
    }

    // Levenshtein distance (memory efficient)
    private int levenshtein(String s, String t) {
        if (s == null) s = "";
        if (t == null) t = "";
        int n = s.length(), m = t.length();
        if (n == 0) return m;
        if (m == 0) return n;
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[m];
    }
}
