package app.kriteria;

import javafx.beans.property.StringProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;



public class kriteriaModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final FloatProperty bobot = new SimpleFloatProperty();
    private final StringProperty costBenefit = new SimpleStringProperty();

    public kriteriaModel(int id, String nama, float bobot, String costBenefit) {
        this.id.set(id);
        this.nama.set(nama);
        this.bobot.set(bobot);
        this.costBenefit.set(costBenefit);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty namaProperty() {
        return nama;
    }

    public FloatProperty bobotProperty() {
        return bobot;
    }

    public StringProperty costBenefitProperty() {
        return costBenefit;
    }

    public int getId() {
        return id.get();
    }

    public String getNama() {
        return nama.get();
    }

    public float getBobot() {
        return bobot.get();
    }

    public String getCostBenefit() {
        return costBenefit.get();
    }    
}
