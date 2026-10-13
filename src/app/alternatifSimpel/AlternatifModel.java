package app.alternatifSimpel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AlternatifModel {
    private final IntegerProperty id_alternatif = new SimpleIntegerProperty();
    private final StringProperty nama_alternatif = new SimpleStringProperty();
    public AlternatifModel(int id,
                          String nama
                          ) {
        this.id_alternatif.set(id);
        this.nama_alternatif.set(nama);
    }

    // Properties
    public IntegerProperty id_alternatifProperty() { return id_alternatif; }
    public StringProperty nama_alternatifProperty() { return nama_alternatif; }

    // Getters
    public int getId_alternatif() { return id_alternatif.get(); }
    public String getNama_alternatif() { return nama_alternatif.get(); }

    // Optional setters (useful for programmatic updates)
    public void setNama_alternatif(String v) { nama_alternatif.set(v); }

}
