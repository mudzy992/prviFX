package sample;

public class korisnici {
    private int id;
    private String ime;
    private String prezime;
    private int telefon;
    private String sektor;
    private String radnomjesto;

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public int getTelefon() {
        return telefon;
    }

    public String getSektor() {
        return sektor;
    }

    public String getRadnomjesto() {
        return radnomjesto;
    }

    public void setId(int id) throws greske {
        if (id <=0){
            throw new greske("ID ne može biti manji ili jedna 0");
        }
        this.id = id;
    }

    public void setIme(String ime) throws greske {
        if (ime == ""){
            throw new greske("Ime mora biti unešeno");
        }
        this.ime = ime;
    }

    public void setPrezime(String prezime) throws greske {
        if (prezime == ""){
            throw new greske("Prezime mora biti unešeno");
        }
        this.prezime = prezime;
    }

    public void setTelefon(int telefon) throws greske {
        if (telefon <= 0){
            throw new greske("Telefon mora biti unešen");
        }
        this.telefon = telefon;
    }

    public void setSektor(String sektor) throws greske {
        if (sektor == ""){
            throw new greske("Sektor mora biti unešen");
        }
        this.sektor = sektor;
    }

    public void setRadnomjesto(String radnomjesto) throws greske {
        if (radnomjesto == ""){
            throw new greske("Radno mjesto mora biti unešeno");
        }
        this.radnomjesto = radnomjesto;
    }

    @Override
    public String toString() {
        return "korisnici{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", telefon=" + telefon +
                ", sektor='" + sektor + '\'' +
                ", radnomjesto='" + radnomjesto + '\'' +
                '}';
    }
}
