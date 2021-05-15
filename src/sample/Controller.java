package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;


public class Controller implements Initializable {
    korisnici korisnik = new korisnici();
    String url = "jdbc:ucanaccess://baza.accdb";
    ObservableList<tabelaModel> obList = FXCollections.observableArrayList();


    Alert potvrda = new Alert(AlertType.CONFIRMATION);

    @FXML
    private Button dodajbtn;

    @FXML
    private Button obrisibtn;

    @FXML
    private Button izmjenibtn;

    @FXML
    private TextField idtxt;

    @FXML
    private TextField imetxt;

    @FXML
    private TextField prezimetxt;

    @FXML
    private TextField brojtelefonatxt;

    @FXML
    private TextField sektortxt;

    @FXML
    private TextField radnomjestotxt;

    @FXML
    private TableView<tabelaModel> tabela;

    @FXML
    private TableColumn<tabelaModel, String> tabelaID;

    @FXML
    private TableColumn<tabelaModel, String> tabelaIme;

    @FXML
    private TableColumn<tabelaModel, String> tabelaPrezime;

    @FXML
    private TableColumn<tabelaModel, String> tabelaBrojTelefona;

    @FXML
    private TableColumn<tabelaModel, String> tabelaSektor;

    @FXML
    private TableColumn<tabelaModel, String> tabelaRadnoMjesto;

    @FXML
    private Label porukaLabel;

    @FXML
    public void prikazi(MouseEvent event) {
            try (Connection konekcija = DriverManager.getConnection(url)) {
                String sql = "SELECT * from Korisnici";
                ResultSet rezultatResultSet = konekcija.createStatement().executeQuery(sql);
                //Prikaz u konzoli
                System.out.println("Podaci iz tabele Korisnici");
                System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", "ID", "IME", "PREZIME", "BROJ TELEFONA", "SEKTOR", "RADNO MJESTO");
                while (rezultatResultSet.next()) {
                    idtxt.setText(Integer.toString(rezultatResultSet.getInt(1)));
                    imetxt.setText(rezultatResultSet.getString(2));
                    prezimetxt.setText(rezultatResultSet.getString(3));
                    brojtelefonatxt.setText(rezultatResultSet.getString(4));
                    sektortxt.setText(rezultatResultSet.getString(5));
                    radnomjestotxt.setText(rezultatResultSet.getString(6));
                    System.out.printf("%-20d%-20s%-20s%-20d%-20s%-20s\n",
                            rezultatResultSet.getInt(1),
                            rezultatResultSet.getString(2),
                            rezultatResultSet.getString(3),
                            rezultatResultSet.getInt(4),
                            rezultatResultSet.getString(5),
                            rezultatResultSet.getString(6));
                    tabela.getItems().removeAll(obList);
                    ispunaTabele();
                    obrisiTextPolja();
                    zabranaIzmjeneID();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
    }
    @FXML
    void izmjeniStisni(MouseEvent event) {
            try (Connection konekcija = DriverManager.getConnection(url)) {
                String sqlInsert = "UPDATE Korisnici SET Ime = ?, Prezime = ?, Telefon = ?, Sektor = ?, Radno_mjesto =? WHERE Korisnici_ID = ?";
                PreparedStatement insertStmt = konekcija.prepareStatement(sqlInsert);

                insertStmt.setInt(6, Integer.parseInt(idtxt.getText()));
                korisnik.setId(Integer.parseInt(idtxt.getText()));

                insertStmt.setString(1, imetxt.getText());
                korisnik.setIme(imetxt.getText());

                insertStmt.setString(2, prezimetxt.getText());
                korisnik.setPrezime(prezimetxt.getText());

                if (brojtelefonatxt.getText() == ""){
                    brojtelefonatxt.setText(Integer.toString(0));
                    insertStmt.setInt(3, 0);
                } else {
                    insertStmt.setInt(3, Integer.parseInt(brojtelefonatxt.getText()));
                    korisnik.setTelefon(brojtelefonatxt.getText());
                }
                insertStmt.setString(4, sektortxt.getText());
                korisnik.setSektor(sektortxt.getText());

                insertStmt.setString(5, radnomjestotxt.getText());
                korisnik.setRadnomjesto(radnomjestotxt.getText());

                int dodaniKorisnik = insertStmt.executeUpdate();

                Alert informacijaIzmjena = new Alert(AlertType.INFORMATION);
                informacijaIzmjena.setTitle("Informacija");
                informacijaIzmjena.setHeaderText("Podaci za korisnika su uspješno izmjenjeni");
                informacijaIzmjena.setContentText("Korisnik: " + imetxt.getText() + " " + prezimetxt.getText() + " je uspješno izmjenjen.");
                informacijaIzmjena.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        if (dodaniKorisnik > 0) {
                            tabela.getItems().removeAll(obList);
                            ispunaTabele();
                            obrisiTextPolja();
                            zabranaIzmjeneID();
                        }
                    }
                });
            } catch (SQLException e) {
                Alert upozorenjeIzmjenaSQL = new Alert(AlertType.WARNING);
                upozorenjeIzmjenaSQL.setTitle("SQL Greška");
                upozorenjeIzmjenaSQL.setHeaderText("Kontaktirajte administratora");
                upozorenjeIzmjenaSQL.setContentText("Opis greške: \n" + e.getMessage());
            } catch (greske e){
                Alert upozorenjeIzmjenaGreska = new Alert(AlertType.WARNING);
                upozorenjeIzmjenaGreska.setTitle("Greška pri unosu");
                upozorenjeIzmjenaGreska.setHeaderText("Napravili ste grešku prilikom izmjene.");
                upozorenjeIzmjenaGreska.setContentText("Opis greške: \n" + e.getMessage());
                upozorenjeIzmjenaGreska.showAndWait();
            }
    }
    @FXML
    void obrisiStisni(MouseEvent event) {
        try(Connection koneckija = DriverManager.getConnection(url)) {
            String delete = "DELETE FROM Korisnici WHERE Korisnici_ID = ?";
            PreparedStatement obrisi = koneckija.prepareStatement(delete);

            obrisi.setInt(1, Integer.parseInt(idtxt.getText()));

            int obrisaniKorinik = obrisi.executeUpdate();
            Alert informacijaObrisi = new Alert(AlertType.INFORMATION);
            informacijaObrisi.setTitle("Informacija");
            informacijaObrisi.setHeaderText("Podaci za korisnika su uspješno izmjenjeni");
            informacijaObrisi.setContentText("Korisnik: " + imetxt.getText() + " " + prezimetxt.getText() + " je uspješno obrisan.");
            informacijaObrisi.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    if(obrisaniKorinik >0){
                        tabela.getItems().removeAll(obList);
                        ispunaTabele();
                        obrisiTextPolja();
                        zabranaIzmjeneID();
                        System.out.println("Korisnik: " + imetxt.getText() + " " +prezimetxt.getText()+ " je uspješno obrisan.");
                    }
                }
            });

        } catch (SQLException e){
            Alert upozorenjeObrisiSQL = new Alert(AlertType.WARNING);
            upozorenjeObrisiSQL.setTitle("SQL Greška");
            upozorenjeObrisiSQL.setHeaderText("Kontaktirajte administratora");
            upozorenjeObrisiSQL.setContentText("Opis greške: \n" + e.getMessage());
        }
    }
    @FXML
    public void stisniDodaj(MouseEvent event){
        try(Connection konekcija = DriverManager.getConnection(url)) {
            String sqlInsert = "INSERT INTO Korisnici (Korisnici_ID, Ime, Prezime, Telefon, Sektor, Radno_mjesto) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = konekcija.prepareStatement(sqlInsert);
            if (idtxt.getText() == ""){
                idtxt.setText(Integer.toString(0));
                insertStmt.setInt(1, 0);
            }else {
                insertStmt.setInt(1, 0);
                korisnik.setId(Integer.parseInt(idtxt.getText()));
            }
            insertStmt.setString(2, imetxt.getText());
            korisnik.setIme(imetxt.getText());

            insertStmt.setString(3, prezimetxt.getText());
            korisnik.setPrezime(prezimetxt.getText());

            if (brojtelefonatxt.getText() == ""){
                brojtelefonatxt.setText(Integer.toString(0));
                insertStmt.setInt(4, 0);
            } else {
                insertStmt.setInt(4, Integer.parseInt(brojtelefonatxt.getText()));
                korisnik.setTelefon(brojtelefonatxt.getText());
            }
            insertStmt.setString(5, sektortxt.getText());
            korisnik.setSektor(sektortxt.getText());

            insertStmt.setString(6, radnomjestotxt.getText());
            korisnik.setRadnomjesto(radnomjestotxt.getText());

            int dodaniKorisnik = insertStmt.executeUpdate();
            Alert informacijaDodaj = new Alert(AlertType.INFORMATION);
            informacijaDodaj.setTitle("Informacija");
            informacijaDodaj.setHeaderText("Podaci za korisnika su uspješno izmjenjeni");
            informacijaDodaj.setContentText("Korisnik: " + imetxt.getText() + " " + prezimetxt.getText() + " je uspješno kreiran.");
            informacijaDodaj.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    if(dodaniKorisnik > 0){
                        tabela.getItems().removeAll(obList);
                        ispunaTabele();
                        obrisiTextPolja();
                        zabranaIzmjeneID();
                        // konzolna potvrda
                        System.out.println("Korisnik: " + imetxt.getText() + " " +prezimetxt.getText()+ " je uspješno kreiran.");
                    }
                }
            });
        } catch (SQLException e) {
            Alert upozorenjeDodajSQL = new Alert(AlertType.INFORMATION);
            upozorenjeDodajSQL.setTitle("SQL Greška");
            upozorenjeDodajSQL.setHeaderText("Kontaktirajte administratora");
            upozorenjeDodajSQL.setContentText("Opis greške: \n" + e.getMessage());
        } catch (greske e){
            Alert upozorenjeDodajGreska = new Alert(AlertType.INFORMATION);
            upozorenjeDodajGreska.setTitle("Greška pri unosu");
            upozorenjeDodajGreska.setHeaderText("Napravili ste grešku prilikom izmjene.");
            upozorenjeDodajGreska.setContentText("Opis greške: \n" + e.getMessage());
            upozorenjeDodajGreska.showAndWait();
        }
    }

    Integer index = -1;
    @FXML
    public void prikaziNaKlikUTabeli(MouseEvent event) {
        index = tabela.getSelectionModel().getSelectedIndex();
        if (index <= -1)
        {
            return;
        }
        idtxt.setText(tabelaID.getCellData(index));
        imetxt.setText(tabelaIme.getCellData(index).toString());
        prezimetxt.setText(tabelaPrezime.getCellData(index).toString());
        brojtelefonatxt.setText(tabelaBrojTelefona.getCellData(index));
        sektortxt.setText(tabelaSektor.getCellData(index).toString());
        radnomjestotxt.setText(tabelaRadnoMjesto.getCellData(index).toString());
    }

    public void ispunaTabele(){
        try (Connection konekcija = DriverManager.getConnection(url)) {
            String sql = "SELECT * from Korisnici";
            ResultSet rs = konekcija.createStatement().executeQuery(sql);
            while (rs.next()) {
                obList.add(new tabelaModel(
                        rs.getString("Korisnici_ID"),
                        rs.getString("Ime"),
                        rs.getString("Prezime"),
                        rs.getString("Telefon"),
                        rs.getString("Sektor"),
                        rs.getString("Radno_mjesto")));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        tabelaID.setCellValueFactory(new PropertyValueFactory<>("IDTabela"));
        tabelaIme.setCellValueFactory(new PropertyValueFactory<>("imeTabela"));
        tabelaPrezime.setCellValueFactory(new PropertyValueFactory<>("prezimeTabela"));
        tabelaBrojTelefona.setCellValueFactory(new PropertyValueFactory<>("telefonTabela"));
        tabelaSektor.setCellValueFactory(new PropertyValueFactory<>("sektorTabela"));
        tabelaRadnoMjesto.setCellValueFactory(new PropertyValueFactory<>("RMTabela"));
        tabela.setItems(obList);
    }

    public void obrisiTextPolja(){
        idtxt.clear();
        imetxt.clear();
        prezimetxt.clear();
        sektortxt.clear();
        brojtelefonatxt.clear();
        radnomjestotxt.clear();
    }

    public void zabranaIzmjeneID(){
        try (Connection konekcija = DriverManager.getConnection(url)) {
            String sql = "SELECT * from Korisnici ORDER BY Korisnici_ID DESC LIMIT 1";
            ResultSet rezultatResultSet = konekcija.createStatement().executeQuery(sql);
            while (rezultatResultSet.next()) {
                idtxt.setText(Integer.toString(rezultatResultSet.getInt(1) + 1));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ispunaTabele();
        zabranaIzmjeneID();
    }
}

