package app.hitungTopsis;
import javafx.beans.property.*;
public class KriteriaRow {
    private final IntegerProperty id_kriteria = new SimpleIntegerProperty();
    private final StringProperty nama_kriteria = new SimpleStringProperty();
    private final FloatProperty bobot = new SimpleFloatProperty();
    private final StringProperty costBenefit = new SimpleStringProperty();

    public KriteriaRow(int id_kriteria, String nama_kriteria, float bobot, String cb) {
        this.id_kriteria.set(id_kriteria);
        this.nama_kriteria.set(nama_kriteria);
        this.bobot.set(bobot);
        this.costBenefit.set(cb);
    }

    public int getId_kriteria() { return id_kriteria.get(); }
    public String getNama_kriteria() { return nama_kriteria.get(); }
    public float getBobot() { return bobot.get(); }
    public String getCostBenefit() { return costBenefit.get(); }
}
