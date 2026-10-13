package app.hitungTopsis;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;

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
import net.sf.jasperreports.pdf.*;
import net.sf.jasperreports.engine.JRParameter;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.TimeZone;

public class HitungTopsisController {
    private static final String DB_URL = "jdbc:sqlite:TA_Topsis.db";

    // TableView 1
    @FXML private TableView<AlternatifRow> hitungTopsis_tv_alternatif;
    @FXML private TableColumn<AlternatifRow, Integer> hitungTopsis_tc_id;
    @FXML private TableColumn<AlternatifRow, String> hitungTopsis_tc_nama;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_frekuensi;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_meninggal;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_hilang;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_terluka;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_pengungsi;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_bangunanRusak;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_rumahSakit;
    @FXML private TableColumn<AlternatifRow, Double> hitungTopsis_tc_tempatEvakuasi;

    // TableView 2
    @FXML private TableView<KriteriaRow> hitungTopsis_tv_kriteria;
    @FXML private TableColumn<KriteriaRow, Integer> hitungTopsis2_tc_id;
    @FXML private TableColumn<KriteriaRow, String> hitungTopsis2_tc_nama;
    @FXML private TableColumn<KriteriaRow, Double> hitungTopsis2_tc_bobot;
    @FXML private TableColumn<KriteriaRow, String> hitungTopsis2_tc_costBenefit;

    // TableView 3
    @FXML private TableView<ResultRow> tv_hasil;
    @FXML private TableColumn<ResultRow, Integer> tc_id;
    @FXML private TableColumn<ResultRow, String>  tc_nama;
    @FXML private TableColumn<ResultRow, Double>  tc_dPlus;
    @FXML private TableColumn<ResultRow, Double>  tc_dMinus;
    @FXML private TableColumn<ResultRow, Double>  tc_pref;
    @FXML private TableColumn<ResultRow, Integer> tc_ranking;

    // pembagi textfields
    @FXML private TextField tf_pembagi_frekuensi, tf_pembagi_meninggal, tf_pembagi_hilang, tf_pembagi_terluka,
            tf_pembagi_pengungsi, tf_pembagi_bangunanRusak, tf_pembagi_rumahSakit, tf_pembagi_tempatEvakuasi;

    // max textfields
    @FXML private TextField tf_max_frekuensi, tf_max_meninggal, tf_max_hilang, tf_max_terluka,
            tf_max_pengungsi, tf_max_bangunanRusak, tf_max_rumahSakit, tf_max_tempatEvakuasi;

    // min textfields
    @FXML private TextField tf_min_frekuensi, tf_min_meninggal, tf_min_hilang, tf_min_terluka,
            tf_min_pengungsi, tf_min_bangunanRusak, tf_min_rumahSakit, tf_min_tempatEvakuasi;

    // buttons
    @FXML private Button bt_rawMatrix, bt_normalizedDecisionMatrix, bt_WeightedNormalizedDecisionMatrix, bt_hitung, bt_export;
    @FXML private Button bt_report;

    @FXML private Button bt_kriteria;
    @FXML private Button bt_alternatif;
    @FXML private Button bt_hitungTopsis;
    @FXML private Button bt_rawMatriks;
    @FXML private Button bt_matriksTernormalisasi;
    @FXML private Button bt_matriksTerbobot;
    @FXML private Button bt_dashboard;

    @FXML private TextField tf_cari;
    @FXML private Button bt_cari;
    @FXML private Button bt_reset;

          
    private final ObservableList<AlternatifRow> masterList = FXCollections.observableArrayList();
    private FilteredList<AlternatifRow> filteredAlt;
    private final ObservableList<ResultRow> hasilList = FXCollections.observableArrayList();
    private FilteredList<ResultRow> filteredHasil;

    // in-memory lists and matrices
    private final ObservableList<AlternatifRow> alternatifList = FXCollections.observableArrayList();
    private final ObservableList<KriteriaRow> kriteriaList = FXCollections.observableArrayList();
    private double[][] normalized;         
    private double[][] weightedNormalized; 
   

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
                // Table 1 wiring
                hitungTopsis_tc_id.setCellValueFactory(new PropertyValueFactory<>("id_alternatif"));
                hitungTopsis_tc_nama.setCellValueFactory(new PropertyValueFactory<>("nama_alternatif"));
                hitungTopsis_tc_frekuensi.setCellValueFactory(new PropertyValueFactory<>("frekuensi"));
                hitungTopsis_tc_meninggal.setCellValueFactory(new PropertyValueFactory<>("meninggal"));
                hitungTopsis_tc_hilang.setCellValueFactory(new PropertyValueFactory<>("hilang"));
                hitungTopsis_tc_terluka.setCellValueFactory(new PropertyValueFactory<>("terluka"));
                hitungTopsis_tc_pengungsi.setCellValueFactory(new PropertyValueFactory<>("pengungsi"));
                hitungTopsis_tc_bangunanRusak.setCellValueFactory(new PropertyValueFactory<>("bangunan_rusak"));
                hitungTopsis_tc_rumahSakit.setCellValueFactory(new PropertyValueFactory<>("rumah_sakit"));
                hitungTopsis_tc_tempatEvakuasi.setCellValueFactory(new PropertyValueFactory<>("tempat_evakuasi"));

                hitungTopsis_tc_frekuensi.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_meninggal.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_hilang.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_terluka.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_pengungsi.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_bangunanRusak.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_rumahSakit.setCellFactory(doubleCellFactory3Generic());
                hitungTopsis_tc_tempatEvakuasi.setCellFactory(doubleCellFactory3Generic());

                // Table 2 wiring
                hitungTopsis2_tc_id.setCellValueFactory(new PropertyValueFactory<>("id_kriteria"));
                hitungTopsis2_tc_nama.setCellValueFactory(new PropertyValueFactory<>("nama_kriteria"));
                hitungTopsis2_tc_bobot.setCellValueFactory(new PropertyValueFactory<>("bobot"));
                hitungTopsis2_tc_costBenefit.setCellValueFactory(new PropertyValueFactory<>("costBenefit"));

                // Table 3 wiring
                tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
                tc_nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
                tc_dPlus.setCellValueFactory(new PropertyValueFactory<>("dplus"));
                tc_dMinus.setCellValueFactory(new PropertyValueFactory<>("dminus")); 
                tc_pref.setCellValueFactory(new PropertyValueFactory<>("preference"));
                tc_ranking.setCellValueFactory(new PropertyValueFactory<>("ranking"));

                tc_dPlus.setCellFactory(doubleCellFactory3Generic());
                tc_dMinus.setCellFactory(doubleCellFactory3Generic());
                tc_pref.setCellFactory(doubleCellFactory3Generic());

                // initial button state
                bt_normalizedDecisionMatrix.setDisable(true);
                bt_WeightedNormalizedDecisionMatrix.setDisable(true);

                // load data
                loadAlternatif();
                loadKriteria();

                // button actions
                bt_normalizedDecisionMatrix.setOnAction(e -> showNormalizedMatrixInAlternatifTable());
                bt_WeightedNormalizedDecisionMatrix.setOnAction(e -> showWeightedNormalizedMatrixInAlternatifTable());
                bt_rawMatrix.setOnAction(e -> hitungTopsis_tv_alternatif.setItems(alternatifList));
                bt_hitung.setOnAction(e -> computeTopsis());

                if (bt_export != null) {
                bt_export.setOnAction(e -> {
                Window w = bt_export.getScene().getWindow();
                exportAllTablesToExcel(w);
            }); 
        }
        
        filteredAlt = new FilteredList<>(masterList, p -> true);
        filteredHasil = new FilteredList<>(hasilList, p -> true);
        hitungTopsis_tv_alternatif.setItems(filteredAlt);
        tv_hasil.setItems(filteredHasil);
        if (bt_cari != null) bt_cari.setOnAction(this::onCari);
        if (bt_reset != null) bt_reset.setOnAction(ev -> resetSearchAndView());
        if (bt_report != null) bt_report.setOnAction(e -> onReport());
    }
    private <S> Callback<TableColumn<S, Double>, TableCell<S, Double>> doubleCellFactory3Generic() {
        return col -> new TableCell<S, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(round3(value)));
                }
            }
        };
    }
    // nearest match in alternatif list
    private AlternatifRow nearestAlt(String q) {
        if (q == null || q.isEmpty()) return null;
        AlternatifRow best = null;
        int bestDist = Integer.MAX_VALUE;
        for (AlternatifRow a : masterList) {
            String name = a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase();
            int d = levenshtein(q, name);
            if (d < bestDist) {
                bestDist = d;
                best = a;
            }
        }
        return best;
    }

    // nearest match in hasil list
    private ResultRow nearestRes(String q) {
        if (q == null || q.isEmpty()) return null;
        ResultRow best = null;
        int bestDist = Integer.MAX_VALUE;
        for (ResultRow r : hasilList) {
            String name = r.getNama() == null ? "" : r.getNama().toLowerCase();
            int d = levenshtein(q, name);
            if (d < bestDist) {
                bestDist = d;
                best = r;
            }
        }
        return best;
    }

    // Levenshtein distance (keeps memory small by using two rows)
    private int levenshtein(String s, String t) {
        if (s == null) s = "";
        if (t == null) t = "";
        s = s.toLowerCase();
        t = t.toLowerCase();
        int n = s.length();
        int m = t.length();
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

    private void onCari(ActionEvent e) {
        String q = tf_cari.getText() == null ? "" : tf_cari.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            filteredAlt.setPredicate(p -> true);
            filteredHasil.setPredicate(p -> true);
            hitungTopsis_tv_alternatif.setItems(filteredAlt);
            tv_hasil.setItems(filteredHasil);
            return;
        }

        // apply predicates
        filteredAlt.setPredicate(a -> {
            String name = a.getNama_alternatif() == null ? "" : a.getNama_alternatif().toLowerCase();
            return name.contains(q);
        });

        filteredHasil.setPredicate(r -> {
            String name = r.getNama() == null ? "" : r.getNama().toLowerCase();
            return name.contains(q);
        });

        if (filteredAlt.isEmpty()) {
            AlternatifRow nearestAlt = nearestAlt(q); // implement nearestAlt(q) to return closest AlternatifRow or null
            hitungTopsis_tv_alternatif.setItems(nearestAlt == null
                ? FXCollections.observableArrayList()
                : FXCollections.observableArrayList(nearestAlt));
        } else {
            hitungTopsis_tv_alternatif.setItems(filteredAlt);
        }

        if (filteredHasil.isEmpty()) {
            ResultRow best = nearestRes(q); // your existing nearestRes(q)
            tv_hasil.setItems(best == null
                ? FXCollections.observableArrayList()
                : FXCollections.observableArrayList(best));
        } else {
            tv_hasil.setItems(filteredHasil);
        }
    }
    private void onReport() {
        List<ResultRow> data = new ArrayList<>(tv_hasil.getItems());
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                try (InputStream jrxml = getClass().getResourceAsStream("/reports/reportTopsis.jrxml")) {
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

                    File outPdf = new File("topsis_report.pdf");
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
    private void resetSearchAndView() {
        tf_cari.clear();
        masterList.setAll(alternatifList); 
        filteredAlt.setPredicate(p -> true);
        hitungTopsis_tv_alternatif.setItems(filteredAlt);

        hasilList.clear();
        filteredHasil.setPredicate(p -> true);
        tv_hasil.setItems(filteredHasil);
    }
    private void loadAlternatif() {
        alternatifList.clear();
        String sql = "SELECT id_alternatif, nama_alternatif, frekuensi, meninggal, hilang, terluka, pengungsi, bangunan_rusak, rumah_sakit, tempat_evakuasi FROM alternatif";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                alternatifList.add(new AlternatifRow(
                        rs.getInt("id_alternatif"),
                        rs.getString("nama_alternatif"),
                        round3(rs.getDouble("frekuensi")),
                        round3(rs.getDouble("meninggal")),
                        round3(rs.getDouble("hilang")),
                        round3(rs.getDouble("terluka")),
                        round3(rs.getDouble("pengungsi")),
                        round3(rs.getDouble("bangunan_rusak")),
                        round3(rs.getDouble("rumah_sakit")),
                        round3(rs.getDouble("tempat_evakuasi"))
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        masterList.setAll(alternatifList);
        hitungTopsis_tv_alternatif.setItems(filteredAlt);
    }

    private void loadKriteria() {
        kriteriaList.clear();
        String sql = "SELECT id_kriteria, nama_kriteria, bobot, costBenefit FROM kriteria ORDER BY id_kriteria";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                kriteriaList.add(new KriteriaRow(
                        rs.getInt("id_kriteria"),
                        rs.getString("nama_kriteria"),
                        rs.getFloat("bobot"),
                        rs.getString("costBenefit")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        hitungTopsis_tv_kriteria.setItems(kriteriaList);
    }

    // -------------------------
    // TOPSIS
    // -------------------------
    private void computeTopsis() {
        if (alternatifList.isEmpty() || kriteriaList.isEmpty()) return;

        int n = alternatifList.size();
        int m = kriteriaList.size(); // expected 8

        // build raw matrix A[n][m]
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            AlternatifRow a = alternatifList.get(i);
            A[i][0] = a.getFrekuensi();
            A[i][1] = a.getMeninggal();
            A[i][2] = a.getHilang();
            A[i][3] = a.getTerluka();
            A[i][4] = a.getPengungsi();
            A[i][5] = a.getBangunan_rusak();
            A[i][6] = a.getRumah_sakit();
            A[i][7] = a.getTempat_evakuasi();
        }

        // pembagi (norma kolom)
        double[] pembagi = new double[m];
        for (int j = 0; j < m; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) sum += A[i][j] * A[i][j];
            pembagi[j] = Math.sqrt(sum);
            if (pembagi[j] == 0) pembagi[j] = 1.0; // hindari div 0
        }
        // tampilkan versi dibulatkan
        double[] pembagiRounded = new double[m];
        for (int j = 0; j < m; j++) pembagiRounded[j] = round3(pembagi[j]);
        setPembagiFields(pembagiRounded);

        // normalized R
        normalized = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                normalized[i][j] = A[i][j] / pembagi[j];

        // weighted normalized V
        weightedNormalized = new double[n][m];
        double[] weights = new double[m];
        for (int j = 0; j < m; j++) weights[j] = kriteriaList.get(j).getBobot();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                weightedNormalized[i][j] = normalized[i][j] * weights[j];

        // ideal best/worst (pakai nilai mentah)
        double[] idealBest = new double[m];
        double[] idealWorst = new double[m];
        for (int j = 0; j < m; j++) {
            boolean isCost = "cost".equalsIgnoreCase(kriteriaList.get(j).getCostBenefit());
            double max = Double.NEGATIVE_INFINITY;
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                double v = weightedNormalized[i][j];
                if (v > max) max = v;
                if (v < min) min = v;
            }
            idealBest[j] = isCost ? min : max;
            idealWorst[j] = isCost ? max : min;
        }
        // tampilkan versi dibulatkan
        double[] idealBestRounded = new double[m];
        double[] idealWorstRounded = new double[m];
        for (int j = 0; j < m; j++) {
            idealBestRounded[j] = round3(idealBest[j]);
            idealWorstRounded[j] = round3(idealWorst[j]);
        }
        setMaxMinFields(idealBestRounded, idealWorstRounded);

        // distances and preference
        List<ResultRow> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double dplusRaw = 0, dminusRaw = 0;
            for (int j = 0; j < m; j++) {
                double dp = weightedNormalized[i][j] - idealBest[j];
                double dm = weightedNormalized[i][j] - idealWorst[j];
                dplusRaw += dp * dp;
                dminusRaw += dm * dm;
            }
            dplusRaw = Math.sqrt(dplusRaw);
            dminusRaw = Math.sqrt(dminusRaw);
            double prefRaw = (dplusRaw + dminusRaw == 0) ? 0 : dminusRaw / (dplusRaw + dminusRaw);

            // bulatkan hanya untuk output
            double dplus = round3(dplusRaw);
            double dminus = round3(dminusRaw);
            double pref  = round3(prefRaw);

            int id = alternatifList.get(i).getId_alternatif();
            String nama = alternatifList.get(i).getNama_alternatif();
            results.add(new ResultRow(id, nama, dplus, dminus, pref));
        }

        // sort and rank
        results.sort(Comparator.comparingDouble(ResultRow::getPreference).reversed());
        int rank = 1;
        for (int i = 0; i < results.size(); i++) {
            if (i == 0) {
                results.get(i).setRanking(rank);
            } else {
                if (Double.compare(results.get(i).getPreference(), results.get(i - 1).getPreference()) == 0) {
                    results.get(i).setRanking(rank);
                } else {
                    rank = i + 1;
                    results.get(i).setRanking(rank);
                }
            }
        }

        Platform.runLater(() -> {
            hasilList.setAll(results);
            tv_hasil.setItems(filteredHasil);
            bt_normalizedDecisionMatrix.setDisable(false);
            bt_WeightedNormalizedDecisionMatrix.setDisable(false);
        });
    }

    // -------------------------
    // matrix view helpers
    // -------------------------
    // private void showRawMatrix() {
    //     hitungTopsis_tv_kriteria.setItems(kriteriaList);
    // }

    private void showNormalizedMatrixInAlternatifTable() {
        if (normalized == null) return;
            ObservableList<AlternatifRow> view = FXCollections.observableArrayList();
            int n = alternatifList.size();
            int m = kriteriaList.size();
            for (int i = 0; i < n; i++) {
                AlternatifRow src = alternatifList.get(i);
                double jb  = (m > 0) ? round3(normalized[i][0]) : 0.0;
                double mng = (m > 1) ? round3(normalized[i][1]) : 0.0;
                double h   = (m > 2) ? round3(normalized[i][2]) : 0.0;
                double t   = (m > 3) ? round3(normalized[i][3]) : 0.0;
                double p   = (m > 4) ? round3(normalized[i][4]) : 0.0;
                double br  = (m > 5) ? round3(normalized[i][5]) : 0.0;
                double rs  = (m > 6) ? round3(normalized[i][6]) : 0.0;
                double te  = (m > 7) ? round3(normalized[i][7]) : 0.0;

                view.add(new AlternatifRow(
                    src.getId_alternatif(),
                    src.getNama_alternatif(),
                    jb, mng, h, t, p, br, rs, te
                ));
            }
        masterList.setAll(view);
        hitungTopsis_tv_alternatif.setItems(filteredAlt);
    }

    private void showWeightedNormalizedMatrixInAlternatifTable() {
        if (weightedNormalized == null) return;
        ObservableList<AlternatifRow> view = FXCollections.observableArrayList();
        int n = alternatifList.size();
        int m = kriteriaList.size();
        for (int i = 0; i < n; i++) {
            AlternatifRow src = alternatifList.get(i);
            double jb  = (m > 0) ? round3(weightedNormalized[i][0]) : 0.0;
            double mng = (m > 1) ? round3(weightedNormalized[i][1]) : 0.0;
            double h   = (m > 2) ? round3(weightedNormalized[i][2]) : 0.0;
            double t   = (m > 3) ? round3(weightedNormalized[i][3]) : 0.0;
            double p   = (m > 4) ? round3(weightedNormalized[i][4]) : 0.0;
            double br  = (m > 5) ? round3(weightedNormalized[i][5]) : 0.0;
            double rs  = (m > 6) ? round3(weightedNormalized[i][6]) : 0.0;
            double te  = (m > 7) ? round3(weightedNormalized[i][7]) : 0.0;

            view.add(new AlternatifRow(
                src.getId_alternatif(),
                src.getNama_alternatif(),
                jb, mng, h, t, p, br, rs, te
            ));
        }
        
        masterList.setAll(view);
        hitungTopsis_tv_alternatif.setItems(filteredAlt);
    }
    // -------------------------
    // textfield setters
    // -------------------------
    private void setPembagiFields(double[] pembagi) {
        Platform.runLater(() -> {
            tf_pembagi_frekuensi.setText(fmt(pembagi,0));
            tf_pembagi_meninggal.setText(fmt(pembagi,1));
            tf_pembagi_hilang.setText(fmt(pembagi,2));
            tf_pembagi_terluka.setText(fmt(pembagi,3));
            tf_pembagi_pengungsi.setText(fmt(pembagi,4));
            tf_pembagi_bangunanRusak.setText(fmt(pembagi,5));
            tf_pembagi_rumahSakit.setText(fmt(pembagi,6));
            tf_pembagi_tempatEvakuasi.setText(fmt(pembagi,7));
        });
    }

    private void setMaxMinFields(double[] max, double[] min) {
        Platform.runLater(() -> {
            tf_max_frekuensi.setText(fmt(max,0));
            tf_max_meninggal.setText(fmt(max,1));
            tf_max_hilang.setText(fmt(max,2));
            tf_max_terluka.setText(fmt(max,3));
            tf_max_pengungsi.setText(fmt(max,4));
            tf_max_bangunanRusak.setText(fmt(max,5));
            tf_max_rumahSakit.setText(fmt(max,6));
            tf_max_tempatEvakuasi.setText(fmt(max,7));

            tf_min_frekuensi.setText(fmt(min,0));
            tf_min_meninggal.setText(fmt(min,1));
            tf_min_hilang.setText(fmt(min,2));
            tf_min_terluka.setText(fmt(min,3));
            tf_min_pengungsi.setText(fmt(min,4));
            tf_min_bangunanRusak.setText(fmt(min,5));
            tf_min_rumahSakit.setText(fmt(min,6));
            tf_min_tempatEvakuasi.setText(fmt(min,7));
        });
    }

    // main export method
    private void exportAllTablesToExcel(Window ownerWindow) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save TOPSIS export");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"));
        File file = chooser.showSaveDialog(ownerWindow);
        if (file == null) return;
        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            file = new File(file.getParentFile(), file.getName() + ".xlsx");
        }

        try (Workbook wb = new XSSFWorkbook()) {
            // styles
            CellStyle headerStyle = wb.createCellStyle();
            Font bold = wb.createFont(); bold.setBold(true); headerStyle.setFont(bold);

            DataFormat df = wb.createDataFormat();
            CellStyle num3 = wb.createCellStyle();
            num3.setDataFormat(df.getFormat("0.0##"));

            

            // sheet 1: Alternatif (TableView 1)
            Sheet s1 = wb.createSheet("Alternatif");
            writeAlternatifSheet(s1, headerStyle, num3);

            // sheet 2: Kriteria (TableView 2)
            Sheet s2 = wb.createSheet("Kriteria");
            writeKriteriaSheet(s2, headerStyle, num3);

            // sheet 3: Hasil (TableView 3)
            Sheet s3 = wb.createSheet("Hasil");
            writeHasilSheet(s3, headerStyle, num3);

            autosizeAllSheets(wb);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
            showAlert(Alert.AlertType.INFORMATION, "Export successful", "Saved to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Export failed", ex.getMessage());
        }
    }

    // helpers: write sheets (use your model getters)
    private void writeAlternatifSheet(Sheet sheet, CellStyle headerStyle, CellStyle numStyle) {
        Row header = sheet.createRow(0);
        String[] cols = {"ID", "Nama", "Frekuensi", "meninggal", "hilang", "terluka", "pengungsi", "bangunanRusak", "rumahSakit", "tempatEvakuasi"};
        for (int c = 0; c < cols.length; c++) {
            Cell cell = header.createCell(c);
            cell.setCellValue(cols[c]);
            cell.setCellStyle(headerStyle);
        }

        List<AlternatifRow> items = hitungTopsis_tv_alternatif.getItems();
        for (int r = 0; r < items.size(); r++) {
            AlternatifRow a = items.get(r);
            Row row = sheet.createRow(r + 1);
            row.createCell(0).setCellValue(a.getId_alternatif());
            row.createCell(1).setCellValue(a.getNama_alternatif());

            Cell c2 = row.createCell(2); c2.setCellValue(a.getFrekuensi()); c2.setCellStyle(numStyle);
            Cell c3 = row.createCell(3); c3.setCellValue(a.getMeninggal()); c3.setCellStyle(numStyle);
            Cell c4 = row.createCell(4); c4.setCellValue(a.getHilang()); c4.setCellStyle(numStyle);
            Cell c5 = row.createCell(5); c5.setCellValue(a.getTerluka()); c5.setCellStyle(numStyle);
            Cell c6 = row.createCell(6); c6.setCellValue(a.getPengungsi()); c6.setCellStyle(numStyle);
            Cell c7 = row.createCell(7); c7.setCellValue(a.getBangunan_rusak()); c7.setCellStyle(numStyle);
            Cell c8 = row.createCell(8); c8.setCellValue(a.getRumah_sakit()); c8.setCellStyle(numStyle);
            Cell c9 = row.createCell(9); c9.setCellValue(a.getTempat_evakuasi()); c9.setCellStyle(numStyle);
        }
    }

    private void writeKriteriaSheet(Sheet sheet, CellStyle headerStyle, CellStyle numStyle) {
        Row header = sheet.createRow(0);
        String[] cols = {"ID", "Nama", "Bobot", "CostBenefit"};
        for (int c = 0; c < cols.length; c++) {
            Cell cell = header.createCell(c);
            cell.setCellValue(cols[c]);
            cell.setCellStyle(headerStyle);
        }

        List<KriteriaRow> items = hitungTopsis_tv_kriteria.getItems();
        for (int r = 0; r < items.size(); r++) {
            KriteriaRow k = items.get(r);
            Row row = sheet.createRow(r + 1);
            row.createCell(0).setCellValue(k.getId_kriteria());
            row.createCell(1).setCellValue(k.getNama_kriteria());
            Cell cb = row.createCell(2); cb.setCellValue(k.getBobot()); cb.setCellStyle(numStyle);
            row.createCell(3).setCellValue(k.getCostBenefit());
        }
    }

    private void writeHasilSheet(Sheet sheet, CellStyle headerStyle, CellStyle numStyle) {
        Row header = sheet.createRow(0);
        String[] cols = {"Nama", "D+", "D-", "Preference", "Ranking"};
        for (int c = 0; c < cols.length; c++) {
            Cell cell = header.createCell(c);
            cell.setCellValue(cols[c]);
            cell.setCellStyle(headerStyle);
        }

        List<ResultRow> items = tv_hasil.getItems();
        for (int r = 0; r < items.size(); r++) {
            ResultRow res = items.get(r);
            Row row = sheet.createRow(r + 1);
            row.createCell(0).setCellValue(res.getNama());
            Cell d1 = row.createCell(1); d1.setCellValue(res.getDplus()); d1.setCellStyle(numStyle);
            Cell d2 = row.createCell(2); d2.setCellValue(res.getDminus()); d2.setCellStyle(numStyle);
            Cell p  = row.createCell(3); p.setCellValue(res.getPreference()); p.setCellStyle(numStyle);
            Cell ra  = row.createCell(4); ra.setCellValue(res.getRanking()); ra.setCellStyle(numStyle);

        }
    }

    private void autosizeAllSheets(Workbook wb) {
        for (int si = 0; si < wb.getNumberOfSheets(); si++) {
            Sheet s = wb.getSheetAt(si);
            if (s.getPhysicalNumberOfRows() == 0) continue;
            Row header = s.getRow(0);
            if (header == null) continue;
            int lastCol = header.getLastCellNum();
            for (int c = 0; c < lastCol; c++) s.autoSizeColumn(c);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(type, msg, ButtonType.OK);
            a.setTitle(title);
            a.setHeaderText(null);
            a.showAndWait();
        });
    }
    private static final DecimalFormat DF3;
        static {
            DF3 = new DecimalFormat("#.###", DecimalFormatSymbols.getInstance(Locale.US));
            DF3.setMaximumFractionDigits(3);
            DF3.setMinimumFractionDigits(0); // no forced trailing zeros
        }

        private String fmtSmart(double v) {
            if (Double.isNaN(v) || Double.isInfinite(v)) return "";
            return DF3.format(v);
        }

        private String fmt(double[] arr, int idx) {
            if (arr == null || idx < 0 || idx >= arr.length) return "";
            return fmtSmart(arr[idx]);
        }
        private static double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}