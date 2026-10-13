package app.hitungTopsis;
import javafx.beans.property.*;

public class AlternatifRow {
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

    public AlternatifRow(int id, String nama, double jb, double m, double h, double t, double p, double br, double rs, double te) {
        id_alternatif.set(id);
        nama_alternatif.set(nama);
        frekuensi.set(jb);
        meninggal.set(m);
        hilang.set(h);
        terluka.set(t);
        pengungsi.set(p);
        bangunan_rusak.set(br);
        rumah_sakit.set(rs);
        tempat_evakuasi.set(te);
    }

    public int getId_alternatif() { return id_alternatif.get(); }
    public String getNama_alternatif() { return nama_alternatif.get(); }
    public double getFrekuensi() { return frekuensi.get(); }
    public double getMeninggal() { return meninggal.get(); }
    public double getHilang() { return hilang.get(); }
    public double getTerluka() { return terluka.get(); }
    public double getPengungsi() { return pengungsi.get(); }
    public double getBangunan_rusak() { return bangunan_rusak.get(); }
    public double getRumah_sakit() { return rumah_sakit.get(); }
    public double getTempat_evakuasi() { return tempat_evakuasi.get(); }
}
