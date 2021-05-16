package sample;

public class tabelaModel {
    String IDTabela, ImeTabela, PrezimeTabela, TelefonTabela, SektorTabela, RMTabela;

    public tabelaModel(String IDTabela, String imeTabela, String prezimeTabela, String telefonTabela, String sektorTabela, String RMTabela) {
        this.IDTabela = IDTabela;
        this.ImeTabela = imeTabela;
        this.PrezimeTabela = prezimeTabela;
        this.TelefonTabela = telefonTabela;
        this.SektorTabela = sektorTabela;
        this.RMTabela = RMTabela;
    }

    public tabelaModel() {
    }

    public String getIDTabela() {
        return IDTabela;
    }

    public void setIDTabela(String IDTabela) {
        this.IDTabela = IDTabela;
    }

    public String getImeTabela() {
        return ImeTabela;
    }

    public void setImeTabela(String imeTabela) {
        ImeTabela = imeTabela;
    }

    public String getPrezimeTabela() {
        return PrezimeTabela;
    }

    public void setPrezimeTabela(String prezimeTabela) {
        PrezimeTabela = prezimeTabela;
    }

    public String getTelefonTabela() {
        return TelefonTabela;
    }

    public void setTelefonTabela(String telefonTabela) {
        TelefonTabela = telefonTabela;
    }

    public String getSektorTabela() {
        return SektorTabela;
    }

    public void setSektorTabela(String sektorTabela) {
        SektorTabela = sektorTabela;
    }

    public String getRMTabela() {
        return RMTabela;
    }

    public void setRMTabela(String RMTabela) {
        this.RMTabela = RMTabela;
    }
}
