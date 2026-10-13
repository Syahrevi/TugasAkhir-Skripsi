package app.hitungTopsis;
import javafx.beans.property.*;

public class ResultRow {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final DoubleProperty dplus = new SimpleDoubleProperty();
    private final DoubleProperty dminus = new SimpleDoubleProperty();
    private final DoubleProperty preference = new SimpleDoubleProperty();
    private final IntegerProperty ranking = new SimpleIntegerProperty();

    public ResultRow(int id, String nama, double dplus, double dminus, double pref) {
        this.id.set(id);
        this.nama.set(nama);
        this.dplus.set(dplus);
        this.dminus.set(dminus);
        this.preference.set(pref);
        this.ranking.set(0);
    }

    // id
    public Integer getId() { return Integer.valueOf(id.get()); }
    public void setId(Integer v) { this.id.set(v); }
    public IntegerProperty idProperty() { return id; }

    // nama
    public String getNama() { return nama.get(); }
    public void setNama(String v) { this.nama.set(v); }
    public StringProperty namaProperty() { return nama; }

    // dplus
    public Double getDplus() { return Double.valueOf(dplus.get()); }
    public void setDplus(Double v) { this.dplus.set(v); }
    public DoubleProperty dplusProperty() { return dplus; }

    // dminus
    public Double getDminus() { return Double.valueOf(dminus.get()); }
    public void setDminus(Double v) { this.dminus.set(v); }
    public DoubleProperty dminusProperty() { return dminus; }

    // preference
    public Double getPreference() { return Double.valueOf(preference.get()); }
    public void setPreference(Double v) { this.preference.set(v); }
    public DoubleProperty preferenceProperty() { return preference; }

    // ranking
    public Integer getRanking() { return Integer.valueOf(ranking.get()); }
    public void setRanking(Integer r) { this.ranking.set(r); }
    public IntegerProperty rankingProperty() { return ranking; }
}