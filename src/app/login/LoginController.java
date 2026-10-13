package app.login;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Node;
import java.io.IOException;

import java.sql.*;

public class LoginController {

    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label prompt;
    @FXML private Button login;
    @FXML private Button bt_tambahUser;

    private static final String JDBC_URL = "jdbc:sqlite:TA_Topsis.db";
    private Connection getConnection() throws SQLException { return DriverManager.getConnection(JDBC_URL); }

    @FXML
    public void initialize() {
        if (login != null) login.setOnAction(e -> attemptLogin());
    }
    public void bt_tambahUser(ActionEvent event) {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource("/app/user/user.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.setTitle("User Management");
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void attemptLogin() {
        final String u = username.getText().trim();
        final String p = password.getText();

        if (u.isEmpty() && p.isEmpty()) {
            prompt.setText("username salah dan password salah");
            return;
        }

        Task<String> t = new Task<>() {
            @Override
            protected String call() {
                boolean userExists = false, passExists = false, match = false;
                try (Connection c = getConnection()) {
                    try (PreparedStatement ps = c.prepareStatement("SELECT password FROM user WHERE username = ?")) {
                        ps.setString(1, u);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                userExists = true;
                                String stored = rs.getString("password");
                                if (stored != null && stored.equals(p)) match = true;
                            }
                        }
                    }
                    try (PreparedStatement ps2 = c.prepareStatement("SELECT COUNT(1) cnt FROM user WHERE password = ?")) {
                        ps2.setString(1, p);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) passExists = rs2.getInt("cnt") > 0;
                        }
                    }
                } catch (SQLException ex) {
                    return "DBERR:" + ex.getMessage();
                }
                if (match) return "OK";
                if (!userExists && !passExists) return "BOTH";
                if (!userExists) return "USER";
                return "PASS";
            }
        };

        t.setOnSucceeded(ev -> {
            String r = t.getValue();
            if (r == null) { prompt.setText("Database error"); return; }
            if (r.startsWith("DBERR:")) { prompt.setText("Database error: " + r.substring(6)); return; }
            switch (r) {
                case "OK":
                    prompt.setText("login berhasil");
                    dashboard();
                    break;
                case "BOTH":
                    prompt.setText("username salah dan password salah");
                    break;
                case "USER":
                    prompt.setText("username salah");
                    break;
                case "PASS":
                    prompt.setText("password salah");
                    break;
                default:
                    prompt.setText("Terjadi kesalahan");
            }
        });

        t.setOnFailed(ev -> prompt.setText("Database error: " + t.getException().getMessage()));
        Thread th = new Thread(t); th.setDaemon(true); th.start();
    }

    private void dashboard() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/dashboard/dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) login.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Aplikasi Pehitungan Topsis Bencana");
                stage.sizeToScene();
                stage.centerOnScreen();
            } catch (IOException | NullPointerException ex) {
                prompt.setText("Gagal membuka layar: " + ex.getMessage());
            }
        });
    }
}