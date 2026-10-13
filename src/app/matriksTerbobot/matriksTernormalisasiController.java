package app.matriksTerbobot;

import app.alternatif.AlternatifModel;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class matriksTernormalisasiController {
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
    
    @FXML
    public void initialize() {
        menuAlternatif_tc_id.setCellValueFactory(new PropertyValueFactory<>("id_alternatif"));
        menuAlternatif_tc_nama.setCellValueFactory(new PropertyValueFactory<>("nama_alternatif"));
        menuAlternatif_tc_frekuensi.setCellValueFactory(new PropertyValueFactory<>("frekuensi"));
        menuAlternatif_tc_meninggal.setCellValueFactory(new PropertyValueFactory<>("meninggal"));
        menuAlternatif_tc_hilang.setCellValueFactory(new PropertyValueFactory<>("hilang"));
        menuAlternatif_tc_terluka.setCellValueFactory(new PropertyValueFactory<>("terluka"));
        menuAlternatif_tc_pengungsi.setCellValueFactory(new PropertyValueFactory<>("pengungsi"));
        menuAlternatif_tc_bangunanRusak.setCellValueFactory(new PropertyValueFactory<>("bangunan_rusak"));
        menuAlternatif_tc_rumahSakit.setCellValueFactory(new PropertyValueFactory<>("rumah_sakit"));
        menuAlternatif_tc_tempatEvakuasi.setCellValueFactory(new PropertyValueFactory<>("tempat_evakuasi"));

        menuAlternatif_tv_alternatif.setItems(masterList);

        loadAndShowWeightedNormalized();

        if (bt_laporan != null) bt_laporan.setOnAction(e -> onReportAlternatif());
        bt_cari.setOnAction(e -> onCari());
        if (bt_reset != null) bt_reset.setOnAction(e -> resetTable());

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
    private void loadAndShowWeightedNormalized() {
        Task<List<AlternatifModel>> task = new Task<>() {
            @Override
            protected List<AlternatifModel> call() throws Exception {
                List<AlternatifModel> raw = loadFromDb();
                if (raw.isEmpty()) return List.of();

                double[] weights = loadWeightsOrEqual(raw.size()); // length should be 8; if not, equal weights used
                double[][] V = computeWeightedNormalized(raw, weights);
                return buildWeightedModels(raw, V);
            }
        };

        task.setOnSucceeded(e -> masterList.setAll(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task, "load-weighted-normalize").start();
    }

    // load alternatif rows
    private List<AlternatifModel> loadFromDb() {
        List<AlternatifModel> list = new ArrayList<>();
        String sql = "SELECT id_alternatif, nama_alternatif, frekuensi, meninggal, hilang, terluka, pengungsi, bangunan_rusak, rumah_sakit, tempat_evakuasi FROM alternatif ORDER BY id_alternatif";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AlternatifModel(
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // try to read weights from table 'kriteria' (column 'bobot'), expected 8 rows in order; fallback to equal weights
    private double[] loadWeightsOrEqual(int expectedCriteria) {
        int m = 8;
        double[] weights = new double[m];
        String sql = "SELECT bobot FROM kriteria ORDER BY id_kriteria";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Double> w = new ArrayList<>();
            while (rs.next()) w.add(rs.getDouble("bobot"));
            if (w.size() == m) {
                for (int i = 0; i < m; i++) weights[i] = w.get(i);
                return weights;
            }
        } catch (SQLException ignored) { }
        // fallback: equal weights
        for (int i = 0; i < m; i++) weights[i] = 1.0 / m;
        return weights;
    }

    // compute weighted normalized matrix V = normalized * weights
    private double[][] computeWeightedNormalized(List<AlternatifModel> rows, double[] weights) {
        int n = rows.size();
        int m = 8;
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            AlternatifModel a = rows.get(i);
            A[i][0] = a.getFrekuensi();
            A[i][1] = a.getMeninggal();
            A[i][2] = a.getHilang();
            A[i][3] = a.getTerluka();
            A[i][4] = a.getPengungsi();
            A[i][5] = a.getBangunan_rusak();
            A[i][6] = a.getRumah_sakit();
            A[i][7] = a.getTempat_evakuasi();
        }

        double[] denom = new double[m];
        for (int j = 0; j < m; j++) {
            double s = 0;
            for (int i = 0; i < n; i++) s += A[i][j] * A[i][j];
            denom[j] = Math.sqrt(s);
            if (denom[j] == 0) denom[j] = 1.0;
        }

        double[][] R = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                R[i][j] = A[i][j] / denom[j];

        double[][] V = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                V[i][j] = R[i][j] * (j < weights.length ? weights[j] : 1.0);

        return V;
    }

    // build models with weighted normalized values rounded to 3 decimals
    private List<AlternatifModel> buildWeightedModels(List<AlternatifModel> raw, double[][] V) {
        List<AlternatifModel> out = new ArrayList<>(raw.size());
        for (int i = 0; i < raw.size(); i++) {
            var s = raw.get(i);
            out.add(new AlternatifModel(
                    s.getId_alternatif(),
                    s.getNama_alternatif(),
                    round3(V[i][0]), round3(V[i][1]), round3(V[i][2]), round3(V[i][3]),
                    round3(V[i][4]), round3(V[i][5]), round3(V[i][6]), round3(V[i][7])
            ));
        }
        return out;
    }


    private static double round3(double v) {
        return new BigDecimal(v).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }
    private List<AlternatifModel> buildNormalizedModels(List<AlternatifModel> raw, double[][] R) {
        List<AlternatifModel> out = new ArrayList<>(raw.size());
        for (int i = 0; i < raw.size(); i++) {
            AlternatifModel s = raw.get(i);
            out.add(new AlternatifModel(
                    s.getId_alternatif(),
                    s.getNama_alternatif(),
                    round3(R[i][0]), round3(R[i][1]), round3(R[i][2]), round3(R[i][3]),
                    round3(R[i][4]), round3(R[i][5]), round3(R[i][6]), round3(R[i][7])
            ));
        }
        return out;
    }
    private void resetTable() {
        tf_cari.clear();
        Task<List<AlternatifModel>> t = new Task<>() {
            @Override protected List<AlternatifModel> call() throws Exception {
                return loadFromDb();
            }
        };
        t.setOnSucceeded(ev -> {
            masterList.setAll(t.getValue());
            menuAlternatif_tv_alternatif.setItems(masterList);
        });
        t.setOnFailed(ev -> t.getException().printStackTrace());
        new Thread(t).start();
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
    private void onReportAlternatif() {
        List<AlternatifModel> snapshot = new ArrayList<>(masterList);

        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                try (InputStream jrxml = getClass().getResourceAsStream("/reports/reportMatriksTerbobot.jrxml")) {
                    if (jrxml == null) throw new IllegalStateException("Report template not found at /reports/reportMatriksTerbobot.jrxml");
                    JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(snapshot);

                    InputStream logoStream = getClass().getResourceAsStream("/reports/logo.png");
                    if (logoStream == null) throw new IllegalStateException("Logo not found at /reports/logo.png");
                    Map<String,Object> params = new HashMap<>();
                    params.put("logo", logoStream);

                    params.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
                    params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("Asia/Jakarta"));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                    File outPdf = new File("MatriksTerbobot.pdf"); // save locally, not temp
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
}
