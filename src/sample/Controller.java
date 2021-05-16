package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;


public class Controller implements Initializable {

    korisnici korisnik = new korisnici();
    String url = "jdbc:ucanaccess://baza.accdb";
    ObservableList<tabelaModel> obList = FXCollections.observableArrayList();

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
    public void prikazi(MouseEvent event) {
            prikaziBtn();
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

            Alert informacijaObrisi = new Alert(AlertType.WARNING);
            informacijaObrisi.setTitle("Informacija");
            informacijaObrisi.setHeaderText("Da li želite nastaviti?");
            informacijaObrisi.setContentText("Korisnik: " + imetxt.getText() + " " + prezimetxt.getText() + " će biti obrisan.");
            informacijaObrisi.getButtonTypes().add(ButtonType.YES);
            informacijaObrisi.getButtonTypes().add(ButtonType.NO);
            informacijaObrisi.getButtonTypes().remove(ButtonType.OK);
            informacijaObrisi.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.YES) {
                    int obrisaniKorinik = 0;
                    try {
                        obrisaniKorinik = obrisi.executeUpdate();
                        if(obrisaniKorinik > 0){
                            tabela.getItems().removeAll(obList);
                            ispunaTabele();
                            zabranaIzmjeneID();
                            System.out.println("Korisnik: " + imetxt.getText() + " " + prezimetxt.getText() + " je uspješno obrisan.");
                            obrisiTextPolja();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                } if (rs == ButtonType.NO){
                    try {
                        koneckija.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
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
            informacijaDodaj.setHeaderText("Korisnik uspješno dodan");
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

    public void prikaziBtn(){
        tabela.getItems().removeAll(obList);
        ispunaTabele();
        obrisiTextPolja();
        zabranaIzmjeneID();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ispunaTabele();
        zabranaIzmjeneID();
    }
    private NoviRadnik NoviRadnik;
    public void akcijaNew(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("noviRadnik.fxml"));
        stage.setTitle("Novi radnik");
        stage.setScene(new Scene(root));
        stage.show();

    }
}

