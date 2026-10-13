package app.matriksTernormalisasi;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AlternatifModel {
    private final IntegerProperty id_alternatif = new SimpleIntegerProperty();
    private final StringProperty nama_alternatif = new SimpleStringProperty();
    private final DoubleProperty frekuensi = new SimpleDoubleProperty();
    private final DoubleProperty meninggal = new SimpleDoubleProperty();
    private final DoubleProperty hilang = new SimpleDoubleProperty();
    private final DoubleProperty terluka = new SimpleDoubleProperty();
    private final DoubleProperty pengungsi = new SimpleDoubleProperty();
    private final DoubleProperty bangunan_rusak = new SimpleDoubleProperty();
    private final DoubleProperty rumah_sakit = new SimpleDoubleProperty();
    private final DoubleProperty tempat_evakuasi = new SimpleDoubleProperty();

    public AlternatifModel(int id,
                          String nama,
                          Double frekuensi,
                          Double meninggal,
                          Double hilang,
                          Double terluka,
                          Double pengungsi,
                          Double bangunanRusak,
                          Double rumahSakit,
                          Double tempatEvakuasi) {
        this.id_alternatif.set(id);
        this.nama_alternatif.set(nama);
        this.frekuensi.set(frekuensi);
        this.meninggal.set(meninggal);
        this.hilang.set(hilang);
        this.terluka.set(terluka);
        this.pengungsi.set(pengungsi);
        this.bangunan_rusak.set(bangunanRusak);
        this.rumah_sakit.set(rumahSakit);
        this.tempat_evakuasi.set(tempatEvakuasi);
    }

    // Properties
    public IntegerProperty id_alternatifProperty() { return id_alternatif; }
    public StringProperty nama_alternatifProperty() { return nama_alternatif; }
    public DoubleProperty frekuensiProperty() { return frekuensi; }
    public DoubleProperty meninggalProperty() { return meninggal; }
    public DoubleProperty hilangProperty() { return hilang; }
    public DoubleProperty terlukaProperty() { return terluka; }
    public DoubleProperty pengungsiProperty() { return pengungsi; }
    public DoubleProperty bangunan_rusakProperty() { return bangunan_rusak; }
    public DoubleProperty rumah_sakitProperty() { return rumah_sakit; }
    public DoubleProperty tempat_evakuasiProperty() { return tempat_evakuasi; }

    // Getters
    public int getId_alternatif() { return id_alternatif.get(); }
    public String getNama_alternatif() { return nama_alternatif.get(); }
    public Double getFrekuensi() { return frekuensi.get(); }
    public Double getMeninggal() { return meninggal.get(); }
    public Double getHilang() { return hilang.get(); }
    public Double getTerluka() { return terluka.get(); }
    public Double getPengungsi() { return pengungsi.get(); }
    public Double getBangunan_rusak() { return bangunan_rusak.get(); }
    public Double getRumah_sakit() { return rumah_sakit.get(); }
    public Double getTempat_evakuasi() { return tempat_evakuasi.get(); }

    // Optional setters (useful for programmatic updates)
    public void setNama_alternatif(String v) { nama_alternatif.set(v); }
    public void setFrekuensi(Double v) { frekuensi.set(v); }
    public void setMeninggal(Double v) { meninggal.set(v); }
    public void setHilang(Double v) { hilang.set(v); }
    public void setTerluka(Double v) { terluka.set(v); }
    public void setPengungsi(Double v) { pengungsi.set(v); }
    public void setBangunan_rusak(Double v) { bangunan_rusak.set(v); }
    public void setRumah_sakit(Double v) { rumah_sakit.set(v); }
    public void setTempat_evakuasi(Double v) { tempat_evakuasi.set(v); }
}
