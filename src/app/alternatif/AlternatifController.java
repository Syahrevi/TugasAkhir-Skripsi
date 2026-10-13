package app.alternatif;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.TimeZone;


public class AlternatifController {
    private static final String DB_URL = "jdbc:sqlite:TA_Topsis.db";

    @FXML private TableView<AlternatifModel> menuAlternatif_tv_alternatif;
    @FXML private TableColumn<AlternatifModel, Integer> menuAlternatif_tc_id;
    @FXML private TableColumn<AlternatifModel, String> menuAlternatif_tc_nama;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_frekuensi;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_meninggal;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_hilang;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_terluka;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_pengungsi;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_bangunanRusak;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_rumahSakit;
    @FXML private TableColumn<AlternatifModel, Double> menuAlternatif_tc_tempatEvakuasi;
    private ObservableList<AlternatifModel> masterList = FXCollections.observableArrayList();

    @FXML private TextField menuAlternatif_tf_id;
    @FXML private TextField menuAlternatif_tf_nama;
    @FXML private TextField  menuAlternatif_tf_frekuensi;
    @FXML private TextField  menuAlternatif_tf_meninggal;
    @FXML private TextField  menuAlternatif_tf_hilang;
    @FXML private TextField  menuAlternatif_tf_terluka;
    @FXML private TextField  menuAlternatif_tf_pengungsi;
    @FXML private TextField  menuAlternatif_tf_bangunanRusak;
    @FXML private TextField  menuAlternatif_tf_rumahSakit;
    @FXML private TextField  menuAlternatif_tf_tempatEvakuasi;

    @FXML private Button menuAlternatif_bt_tambah;
    @FXML private Button menuAlternatif_bt_reset;
    @FXML private Button menuAlternatif_bt_hapus;

    @FXML private Button importTambah;
    @FXML private Button importHapus;

    @FXML private Button bt_laporan;

    @FXML private TextField tf_cari;
    @FXML private Button bt_cari;
    @FXML private Button bt_reset;
    
    @FXML private Button bt_kriteria;
    @FXML private Button bt_alternatif;
    @FXML private Button bt_hitungTopsis;
    @FXML private Button bt_rawMatriks;
    @FXML private Button bt_matriksTernormalisasi;
    @FXML private Button bt_matriksTerbobot;
    @FXML private Button bt_dashboard;


    private static final String[] EXPECTED_HEADERS = {
        "nama_alternatif", "frekuensi", "meninggal", "hilang", "terluka", "pengungsi", "bangunan_rusak", "rumah_sakit", "tempat_evakuasi"
    };
    @FXML
    public void initialize() {
        menuAlternatif_tv_alternatif.setItems(masterList);

        if (importTambah != null) importTambah.setOnAction(e -> chooseAndImport(false));
        if (importHapus != null) importHapus.setOnAction(e -> chooseAndImport(true));
        if (bt_laporan != null) bt_laporan.setOnAction(e -> onReportAlternatif());

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

        System.out.println("DEBUG DB_URL = " + DB_URL);
        if (menuAlternatif_tv_alternatif == null) {
            throw new IllegalStateException("FXML injection failed: check fx:id and fx:controller.");
        }

        // Cell value factories
        menuAlternatif_tc_id.setCellValueFactory(c -> c.getValue().id_alternatifProperty().asObject());
        menuAlternatif_tc_nama.setCellValueFactory(c -> c.getValue().nama_alternatifProperty());
        menuAlternatif_tc_frekuensi.setCellValueFactory(c -> c.getValue().frekuensiProperty().asObject());
        menuAlternatif_tc_meninggal.setCellValueFactory(c -> c.getValue().meninggalProperty().asObject());
        menuAlternatif_tc_hilang.setCellValueFactory(c -> c.getValue().hilangProperty().asObject());
        menuAlternatif_tc_terluka.setCellValueFactory(c -> c.getValue().terlukaProperty().asObject());
        menuAlternatif_tc_pengungsi.setCellValueFactory(c -> c.getValue().pengungsiProperty().asObject());
        menuAlternatif_tc_bangunanRusak.setCellValueFactory(c -> c.getValue().bangunan_rusakProperty().asObject());
        menuAlternatif_tc_rumahSakit.setCellValueFactory(c -> c.getValue().rumah_sakitProperty().asObject());
        menuAlternatif_tc_tempatEvakuasi.setCellValueFactory(c -> c.getValue().tempat_evakuasiProperty().asObject());

        // Make table editable and set cell factories for editable columns
        menuAlternatif_tv_alternatif.setEditable(true);
        DoubleStringConverter DoubleConverter = new DoubleStringConverter();

        // Name column editable as text
        menuAlternatif_tc_nama.setCellFactory(TextFieldTableCell.forTableColumn());
        menuAlternatif_tc_nama.setOnEditCommit(event -> {
            AlternatifModel row = event.getRowValue();
            String newVal = event.getNewValue();
            row.setNama_alternatif(newVal);
            runBackground(() -> updateStringColumn(row.getId_alternatif(), "nama_alternatif", newVal));
        });

        // Double columns: use TextFieldTableCell with DoubleStringConverter
        setDoubleEditable(menuAlternatif_tc_frekuensi, (m, v) -> m.setFrekuensi(v), "frekuensi");
        setDoubleEditable(menuAlternatif_tc_meninggal, (m, v) -> m.setMeninggal(v), "meninggal");
        setDoubleEditable(menuAlternatif_tc_hilang, (m, v) -> m.setHilang(v), "hilang");
        setDoubleEditable(menuAlternatif_tc_terluka, (m, v) -> m.setTerluka(v), "terluka");
        setDoubleEditable(menuAlternatif_tc_pengungsi, (m, v) -> m.setPengungsi(v), "pengungsi");
        setDoubleEditable(menuAlternatif_tc_bangunanRusak, (m, v) -> m.setBangunan_rusak(v), "bangunan_rusak");
        setDoubleEditable(menuAlternatif_tc_rumahSakit, (m, v) -> m.setRumah_sakit(v), "rumah_sakit");
        setDoubleEditable(menuAlternatif_tc_tempatEvakuasi, (m, v) -> m.setTempat_evakuasi(v), "tempat_evakuasi");

        // Load data
        Task<ObservableList<AlternatifModel>> loadTask = new Task<>() {
            @Override
            protected ObservableList<AlternatifModel> call() throws Exception {
                List<AlternatifModel> list = readAlternatifFromDb();
                return FXCollections.observableArrayList(list);
            }
        };
        loadTask.setOnSucceeded(e -> {
            masterList.setAll(loadTask.getValue());
            menuAlternatif_tv_alternatif.setItems(masterList);
        });
        loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());
        new Thread(loadTask).start();

        // Button handlers
        menuAlternatif_bt_reset.setOnAction(e -> resetFields());
        menuAlternatif_bt_tambah.setOnAction(e -> handleTambah());
        menuAlternatif_bt_hapus.setOnAction(e -> handleHapus());
        bt_cari.setOnAction(e -> onCari());
        if (bt_reset != null) bt_reset.setOnAction(e -> resetTable());
    }

    private void setDoubleEditable(TableColumn<AlternatifModel, Double> column,
                                  BiDoubleSetter setter,
                                  String columnName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit((TableColumn.CellEditEvent<AlternatifModel, Double> event) -> {
            AlternatifModel row = event.getRowValue();
            Double newVal = event.getNewValue();
            if (newVal == null) return;
            setter.set(row, newVal);
            runBackground(() -> updateDoubleColumn(row.getId_alternatif(), columnName, newVal));
        });
    }
    private void resetTable() {
        tf_cari.clear();
        Task<List<AlternatifModel>> t = new Task<>() {
            @Override protected List<AlternatifModel> call() throws Exception {
                return readAlternatifFromDb();
            }
        };
        t.setOnSucceeded(ev -> {
            masterList.setAll(t.getValue());
            menuAlternatif_tv_alternatif.setItems(masterList);
        });
        t.setOnFailed(ev -> t.getException().printStackTrace());
        new Thread(t).start();
    }

    // Add new entry (ignores id field; DB must auto-increment id_alternatif)
    private void handleTambah() {
    String nama_alternatif = menuAlternatif_tf_nama.getText().trim();
    if (nama_alternatif.isEmpty()) {
        showAlert("Nama harus diisi");
        return;
    }

    
    // read values from controls (use getText() for TextField or spinner value if Spinner)
    Double frekuensi   = parseDoubleOrZero(menuAlternatif_tf_frekuensi.getText());
    Double meninggal      = parseDoubleOrZero(menuAlternatif_tf_meninggal.getText());
    Double hilang         = parseDoubleOrZero(menuAlternatif_tf_hilang.getText());
    Double terluka        = parseDoubleOrZero(menuAlternatif_tf_terluka.getText());
    Double pengungsi      = parseDoubleOrZero(menuAlternatif_tf_pengungsi.getText());
    Double bangunanRusak  = parseDoubleOrZero(menuAlternatif_tf_bangunanRusak.getText());
    Double rumahSakit     = parseDoubleOrZero(menuAlternatif_tf_rumahSakit.getText());
    Double tempatEvakuasi = parseDoubleOrZero(menuAlternatif_tf_tempatEvakuasi.getText());

    System.out.println("DEBUG insert values: nama=" + nama_alternatif
        + " frekuensi=" + frekuensi
        + " meninggal=" + meninggal
        + " hilang=" + hilang
        + " terluka=" + terluka
        + " pengungsi=" + pengungsi
        + " bangunanRusak=" + bangunanRusak
        + " rumahSakit=" + rumahSakit
        + " tempatEvakuasi=" + tempatEvakuasi);

    Task<Void> t = new Task<>() {
        @Override protected Void call() throws Exception {
            String sql = "INSERT INTO alternatif (nama_alternatif, frekuensi, meninggal, hilang, terluka, pengungsi, bangunan_rusak, rumah_sakit, tempat_evakuasi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nama_alternatif);
                ps.setDouble(2, frekuensi);
                ps.setDouble(3, meninggal);
                ps.setDouble(4, hilang);
                ps.setDouble(5, terluka);
                ps.setDouble(6, pengungsi);
                ps.setDouble(7, bangunanRusak);
                ps.setDouble(8, rumahSakit);
                ps.setDouble(9, tempatEvakuasi);

                int affected = ps.executeUpdate();
                System.out.println("DEBUG: affected rows = " + affected);

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    int newId = -1;
                    if (keys.next()) newId = keys.getInt(1);
                    final int idToUse = newId;
                    Platform.runLater(() -> {
                        AlternatifModel m = new AlternatifModel(
                                idToUse,
                                nama_alternatif,
                                frekuensi,
                                meninggal,
                                hilang,
                                terluka,
                                pengungsi,
                                bangunanRusak,
                                rumahSakit,
                                tempatEvakuasi
                        );
                        if (menuAlternatif_tv_alternatif.getItems() == null) {
                            menuAlternatif_tv_alternatif.setItems(FXCollections.observableArrayList(m));
                        } else {
                            menuAlternatif_tv_alternatif.getItems().add(m);
                        }
                        resetFields();
                    });
                }
            }
            return null;
        }
    };
    t.setOnFailed(e -> t.getException().printStackTrace());
    new Thread(t).start();
}

    private void onCari() {
        String q = tf_cari.getText() == null ? "" : tf_cari.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            menuAlternatif_tv_alternatif.setItems(masterList);
            return;
        }
        List<AlternatifModel> contains = masterList.stream()
            .filter(a -> (a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase()).contains(q))
            .collect(Collectors.toList());
        if (!contains.isEmpty()) {
            menuAlternatif_tv_alternatif.setItems(FXCollections.observableArrayList(contains));
            return;
        }
        AlternatifModel best = null;
        int bestDist = Integer.MAX_VALUE;
        for (AlternatifModel a : masterList) {
            String name = a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase();
            int d = levenshtein(q, name);
            if (d < bestDist) { bestDist = d; best = a; }
        }
        menuAlternatif_tv_alternatif.setItems(best == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(best));
    }

    private int levenshtein(String s, String t) {
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

    // Delete entry by id (id must be filled)
    private void handleHapus() {
        String idText = menuAlternatif_tf_id.getText().trim();
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

        Task<Void> t = new Task<>() {
            @Override protected Void call() throws Exception {
                String sql = "DELETE FROM alternatif WHERE id_alternatif = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int affected = ps.executeUpdate();
                    Platform.runLater(() -> {
                        if (affected > 0) {
                            // remove from table view if present
                            ObservableList<AlternatifModel> items = menuAlternatif_tv_alternatif.getItems();
                            if (items != null) {
                                items.removeIf(m -> m.getId_alternatif() == id);
                            }
                            resetFields();
                        } else {
                            showAlert("ID tidak ditemukan");
                        }
                    });
                }
                return null;
            }
        };
        t.setOnFailed(e -> t.getException().printStackTrace());
        new Thread(t).start();
    }

    private void resetFields() {
        menuAlternatif_tf_id.clear();
        menuAlternatif_tf_nama.clear();
        menuAlternatif_tf_frekuensi.clear();
        menuAlternatif_tf_meninggal.clear();
        menuAlternatif_tf_hilang.clear();
        menuAlternatif_tf_terluka.clear();
        menuAlternatif_tf_pengungsi.clear();
        menuAlternatif_tf_bangunanRusak.clear();
        menuAlternatif_tf_rumahSakit.clear();
        menuAlternatif_tf_tempatEvakuasi.clear();
    }

    // DB read
    private List<AlternatifModel> readAlternatifFromDb() throws SQLException {
        String sql = "SELECT id_alternatif, nama_alternatif, frekuensi, meninggal, hilang, terluka, pengungsi, bangunan_rusak, rumah_sakit, tempat_evakuasi FROM alternatif";
        List<AlternatifModel> out = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new AlternatifModel(
                        rs.getInt("id_alternatif"),
                        rs.getString("nama_alternatif"),
                        rs.getDouble("frekuensi"),
                        rs.getDouble("meninggal"),
                        rs.getDouble("hilang"),
                        rs.getDouble("terluka"),
                        rs.getDouble("pengungsi"),
                        rs.getDouble("bangunan_rusak"),
                        rs.getDouble("rumah_sakit"),
                        rs.getDouble("tempat_evakuasi")
                ));
            }
        }
        return out;
    }
    private void loadAlternatif() {
        Task<ObservableList<AlternatifModel>> loadTask = new Task<>() {
            @Override
            protected ObservableList<AlternatifModel> call() throws Exception {
                List<AlternatifModel> list = readAlternatifFromDb();
                return FXCollections.observableArrayList(list);
            }
        };
        loadTask.setOnSucceeded(e -> {
            masterList.setAll(loadTask.getValue());
            menuAlternatif_tv_alternatif.setItems(masterList);
        });
        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            ex.printStackTrace();
            Platform.runLater(() -> showAlert("Gagal memuat data: " + (ex == null ? "unknown" : ex.getMessage())));
        });
        new Thread(loadTask).start();
    }

    // Update helpers
    private void updateDoubleColumn(int id, String columnName, Double value) {
        String sql = "UPDATE alternatif SET " + columnName + " = ? WHERE id_alternatif = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateStringColumn(int id, String columnName, String value) {
        String sql = "UPDATE alternatif SET " + columnName + " = ? WHERE id_alternatif = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Utility: run DB work off FX thread
    private void runBackground(Runnable r) {
        Task<Void> t = new Task<>() {
            @Override protected Void call() throws Exception { r.run(); return null; }
        };
        t.setOnFailed(e -> t.getException().printStackTrace());
        new Thread(t).start();
    }

    private Double parseDoubleOrZero(String s) {
    if (s == null || s.trim().isEmpty()) return 0.0;
    try {
        return Double.parseDouble(s.trim());
    } catch (NumberFormatException ex) {
        return 0.0;
    }
}

    private void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            a.setContentText(msg);
            a.showAndWait();
        });
    }
    private void onReportAlternatif() {
        List<AlternatifModel> snapshot = new ArrayList<>(masterList);

        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                try (InputStream jrxml = getClass().getResourceAsStream("/reports/reportRawMatriks.jrxml")) {
                    if (jrxml == null) throw new IllegalStateException("Report template not found at /reports/reportRawMatriks.jrxml");
                    JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(snapshot);

                    InputStream logoStream = getClass().getResourceAsStream("/reports/logo.png");
                    if (logoStream == null) throw new IllegalStateException("Logo not found at /reports/logo.png");
                    Map<String,Object> params = new HashMap<>();
                    params.put("logo", logoStream);

                    params.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
                    params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("Asia/Jakarta"));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                    File outPdf = new File("Alternatif.pdf"); // save locally, not temp
                    JRPdfExporter pdfExporter = new JRPdfExporter();
                    pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outPdf));
                    pdfExporter.exportReport();

                    File out = File.createTempFile("topsis_report_", ".html");
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
                s.setTitle("Laporan Alternatif");
                s.setScene(new Scene(web, 900, 700));
                s.show();
            });
        });

        task.setOnFailed(ev -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR, "Gagal membuat laporan: " + (ex == null ? "unknown" : ex.getMessage()), ButtonType.OK);
                a.showAndWait();
            });
        });
        new Thread(task).start();
    }
    private void chooseAndImport(boolean replaceAll) {
        Window owner = (importTambah != null) ? importTambah.getScene().getWindow() : null;
        FileChooser chooser = new FileChooser();
        chooser.setTitle(replaceAll ? "Import CSV (replace all)" : "Import CSV (append)");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(owner);
        if (file == null) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // parse CSV
                List<Map<String, String>> rows = parseCsv(file);
                if (rows.isEmpty()) {
                    throw new IllegalArgumentException("CSV kosong atau tidak berisi baris data.");
                }

                // normalize header keys (trim + lowercase) and remap rows to canonical keys
                Map<String, String> headerMap = new LinkedHashMap<>(); // original -> canonical
                String[] firstHeaders = rows.get(0).keySet().toArray(new String[0]);
                for (String h : firstHeaders) {
                    if (h == null) continue;
                    String normalized = h.trim().toLowerCase();
                    // find matching expected header (case-insensitive)
                    String match = null;
                    for (String exp : EXPECTED_HEADERS) {
                        if (exp.equalsIgnoreCase(normalized)) { match = exp; break; }
                    }
                    if (match != null) headerMap.put(h, match);
                    else headerMap.put(h, h.trim()); // keep original if no canonical match
                }

                // rebuild rows with canonical keys
                List<Map<String,String>> normalizedRows = new ArrayList<>();
                for (Map<String,String> r : rows) {
                    Map<String,String> nr = new LinkedHashMap<>();
                    for (Map.Entry<String,String> e : r.entrySet()) {
                        String origKey = e.getKey();
                        String canonical = headerMap.getOrDefault(origKey, origKey).trim();
                        nr.put(canonical, e.getValue());
                    }
                    normalizedRows.add(nr);
                }

                // validate headers (use normalized keys)
                Set<String> keys = normalizedRows.get(0).keySet();
                if (!validateHeaders(keys)) {
                    throw new IllegalArgumentException("Header CSV tidak sesuai. Diharapkan: " + String.join(", ", EXPECTED_HEADERS));
                }

                // import
                importToDatabase(normalizedRows, replaceAll);
                return null;
            }
        };

        // success handler: refresh table on FX thread
        task.setOnSucceeded(ev -> Platform.runLater(() -> {
            try {
                loadAlternatif(); // reload DB into masterList and TableView
                resetFields();    // optional: clear input fields
                showAlert("Import berhasil");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Import berhasil tetapi refresh gagal: " + ex.getMessage());
            }
        }));

        // failure handler: show error and print stacktrace
        task.setOnFailed(ev -> {
            Throwable ex = task.getException();
            if (ex != null) ex.printStackTrace();
            String msg = (ex == null) ? "Unknown error" : ex.getMessage();
            Platform.runLater(() -> showAlert("Import gagal: " + msg));
        });

        // IMPORTANT: start the task on a background thread
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    // Parse CSV into list of maps (header -> value). Handles quoted fields.
    private List<Map<String, String>> parseCsv(File file) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) return result;
            String[] headers = splitCsvLine(headerLine);
            for (int i = 0; i < headers.length; i++) headers[i] = headers[i].trim();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] values = splitCsvLine(line);
                // If row has fewer columns, pad with empty strings
                if (values.length < headers.length) {
                    values = Arrays.copyOf(values, headers.length);
                    for (int i = 0; i < values.length; i++) if (values[i] == null) values[i] = "";
                }
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i];
                    String val = (i < values.length) ? values[i] : "";
                    row.put(key, val == null ? "" : val);
                }
                result.add(row);
            }
        }
        return result;
    }

    // Basic CSV line splitter that supports quoted fields and escaped quotes
    private String[] splitCsvLine(String line) {
        if (line == null) return new String[0];
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // escaped quote
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    // Validate that CSV headers contain all expected headers (case-insensitive)
    private boolean validateHeaders(Set<String> csvHeaders) {
        Set<String> lower = new HashSet<>();
        for (String h : csvHeaders) lower.add(h == null ? "" : h.toLowerCase());
        // require all expected headers except "id"
        for (String expected : EXPECTED_HEADERS) {
            if ("id".equalsIgnoreCase(expected)) continue; // id is optional
            if (!lower.contains(expected.toLowerCase())) return false;
        }
        return true;
    }

    // Insert rows into DB. If replaceAll==true, delete existing rows first.
    private void importToDatabase(List<Map<String, String>> rows, boolean replaceAll) throws SQLException {
        if (rows.isEmpty()) return;

        // determine which canonical columns are present in the CSV (use first row keys)
        List<String> presentCols = new ArrayList<>();
        Set<String> firstKeys = rows.get(0).keySet();
        for (String col : EXPECTED_HEADERS) {
            if (firstKeys.contains(col)) presentCols.add(col);
        }
        // ensure at least the required columns (except id) are present
        for (String expected : EXPECTED_HEADERS) {
            if ("id".equalsIgnoreCase(expected)) continue;
            if (!presentCols.contains(expected)) {
                throw new SQLException("CSV missing required column: " + expected);
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try {
                if (replaceAll) {
                    try (Statement st = conn.createStatement()) {
                        st.executeUpdate("DELETE FROM alternatif");
                    }
                }

                // build dynamic INSERT using presentCols
                String colsPart = String.join(",", presentCols);
                String placeholders = String.join(",", Collections.nCopies(presentCols.size(), "?"));
                String sql = "INSERT INTO alternatif (" + colsPart + ") VALUES (" + placeholders + ")";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (Map<String, String> row : rows) {
                        for (int i = 0; i < presentCols.size(); i++) {
                            String key = presentCols.get(i);
                            String val = row.getOrDefault(key, "");
                            if (isNumericColumn(key)) {
                                if (val == null || val.trim().isEmpty()) {
                                    ps.setNull(i + 1, Types.REAL);
                                } else {
                                    try {
                                        double d = Double.parseDouble(val.trim());
                                        ps.setDouble(i + 1, d);
                                    } catch (NumberFormatException nfe) {
                                        throw new SQLException("Nilai numerik tidak valid untuk kolom '" + key + "': " + val);
                                    }
                                }
                            } else {
                                // if id column is present but empty, set NULL to allow autoincrement
                                if ("id".equalsIgnoreCase(key) && (val == null || val.trim().isEmpty())) {
                                    ps.setNull(i + 1, Types.INTEGER);
                                } else {
                                    ps.setString(i + 1, val);
                                }
                            }
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    

    // Decide which columns are numeric (adjust if your schema differs)
    private boolean isNumericColumn(String columnName) {
        String lower = columnName.toLowerCase();
        return lower.contains("meninggal") || lower.contains("hilang") || lower.contains("terluka")
                || lower.contains("pengungsi") || lower.contains("bangunan") || lower.contains("rumah")
                || lower.contains("tempat") || lower.equals("id");
    }

    // Simple alert helper (runs on FX thread)
    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(type);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }

    // Functional interface for setter used in setDoubleEditable
    @FunctionalInterface
    private interface BiDoubleSetter {
        void set(AlternatifModel model, Double value);
    }
}
