package app.user;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class UserController {

    private static final String DB_URL = "jdbc:sqlite:TA_Topsis.db";

    @FXML private Button bt_kembali;
    @FXML private TableView<SimpleUser> tv_user;
    @FXML private TableColumn<SimpleUser, Integer> tc_id;
    @FXML private TableColumn<SimpleUser, String> tc_nama;
    @FXML private TableColumn<SimpleUser, String> tc_password;
    @FXML private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private Button bt_tambah;
    @FXML private TextField tf_idHapus;
    @FXML private Button bt_hapus;

    private final ObservableList<SimpleUser> masterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // wire table columns (property names must match SimpleUser getters)
        tc_id.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        tc_nama.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("username"));
        tc_password.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("password"));

        tv_user.setItems(masterList);
        tv_user.setEditable(true);
        tc_nama.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_nama.setOnEditCommit(evt -> {
            SimpleUser row = evt.getRowValue();
            String newVal = evt.getNewValue() == null ? "" : evt.getNewValue().trim();
            // update model immediately so UI reflects change
            row.setUsername(newVal);
            // persist change in background
            updateUserInDb(row.getId(), "username", newVal);
        });
        tc_password.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_password.setOnEditCommit(evt -> {
            SimpleUser row = evt.getRowValue();
            String newVal = evt.getNewValue() == null ? "" : evt.getNewValue();
            row.setPassword(newVal);
            updateUserInDb(row.getId(), "password", newVal);
        });
        tc_id.setEditable(false);

        // load initial data
        loadUsers();

        // button handlers
        bt_kembali.setOnAction(evt -> {
            try {
                Parent newRoot = FXMLLoader.load(getClass().getResource("/app/login/login_menu.fxml"));
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(new Scene(newRoot));
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation failed", ex.getMessage());
            }
        });

        bt_tambah.setOnAction(evt -> handleAdd());
        bt_hapus.setOnAction(evt -> handleDelete());
    }

    // Load users from DB on background thread
    private void loadUsers() {
        Task<ObservableList<SimpleUser>> task = new Task<>() {
            @Override
            protected ObservableList<SimpleUser> call() throws Exception {
                ObservableList<SimpleUser> out = FXCollections.observableArrayList();
                String sql = "SELECT id, username, password FROM user ORDER BY id";
                try (Connection c = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = c.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new SimpleUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password")
                        ));
                    }
                }
                return out;
            }
        };

        task.setOnSucceeded(e -> {
            masterList.setAll(task.getValue());
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load failed", task.getException().getMessage());
        });

        new Thread(task).start();
    }
    private void updateUserInDb(int id, String column, String value) {
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "UPDATE user SET " + column + " = ? WHERE id = ?";
                try (Connection c = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, value);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                return null;
            }
        };
        t.setOnFailed(ev -> {
            Throwable ex = t.getException();
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update failed", ex == null ? "Unknown error" : ex.getMessage());
            // optional: reload table to restore previous DB state
            Platform.runLater(this::loadUsers);
        });
        new Thread(t).start();
    }

    // Add new user (validates non-empty username/password)
    private void handleAdd() {
        String username = tf_username.getText() == null ? "" : tf_username.getText().trim();
        String password = pf_password.getText() == null ? "" : pf_password.getText();

        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Username must be filled");
            return;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Password must be filled");
            return;
        }

        Task<SimpleUser> task = new Task<>() {
            @Override
            protected SimpleUser call() throws Exception {
                String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
                try (Connection c = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, username);
                    ps.setString(2, password);
                    int affected = ps.executeUpdate();
                    if (affected == 0) throw new SQLException("Insert failed, no rows affected");
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        int newId = -1;
                        if (keys.next()) newId = keys.getInt(1);
                        return new SimpleUser(newId, username, password);
                    }
                }
            }
        };

        task.setOnSucceeded(e -> {
            SimpleUser created = task.getValue();
            if (created != null) {
                masterList.add(created);
                tf_username.clear();
                pf_password.clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added");
            } else {
                showAlert(Alert.AlertType.WARNING, "Add", "User added but no returned id");
                loadUsers();
            }
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Add failed", task.getException().getMessage());
        });

        new Thread(task).start();
    }

    // Delete user by id from tf_idHapus
    private void handleDelete() {
        String idText = tf_idHapus.getText() == null ? "" : tf_idHapus.getText().trim();
        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Enter ID to delete");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Validation", "ID must be a number");
            return;
        }

        // confirm
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete user with ID " + id + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        String sql = "DELETE FROM user WHERE id = ?";
                        try (Connection c = DriverManager.getConnection(DB_URL);
                             PreparedStatement ps = c.prepareStatement(sql)) {
                            ps.setInt(1, id);
                            int affected = ps.executeUpdate();
                            return affected > 0;
                        }
                    }
                };

                task.setOnSucceeded(ev -> {
                    boolean deleted = task.getValue();
                    if (deleted) {
                        masterList.removeIf(u -> u.getId() == id);
                        tf_idHapus.clear();
                        showAlert(Alert.AlertType.INFORMATION, "Deleted", "User deleted");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not found", "No user with ID " + id);
                    }
                });

                task.setOnFailed(ev -> {
                    task.getException().printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Delete failed", task.getException().getMessage());
                });

                new Thread(task).start();
            }
        });
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

    // Simple POJO for table rows
    public static class SimpleUser {
        private final IntegerProperty id = new SimpleIntegerProperty();
        private final StringProperty username = new SimpleStringProperty();
        private final StringProperty password = new SimpleStringProperty();

        public SimpleUser(int id, String username, String password) {
            this.id.set(id);
            this.username.set(username);
            this.password.set(password);
        }

        // id
        public int getId() { return id.get(); }
        public void setId(int v) { id.set(v); }
        public IntegerProperty idProperty() { return id; }

        // username
        public String getUsername() { return username.get(); }
        public void setUsername(String v) { username.set(v); }
        public StringProperty usernameProperty() { return username; }

        // password
        public String getPassword() { return password.get(); }
        public void setPassword(String v) { password.set(v); }
        public StringProperty passwordProperty() { return password; }
    }
}