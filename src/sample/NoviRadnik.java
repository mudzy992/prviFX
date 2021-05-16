package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NoviRadnik {
    String url = "jdbc:ucanaccess://baza.accdb";
    korisnici korisnik = new korisnici();

    @FXML
    private TextField idNoviradnik;

    public Button dodajNoviRadnik;

    @FXML
    private TextField imeNoviradnik;
    @FXML
    private TextField prezimeNoviradnik;
    @FXML
    private TextField telefonNoviradnik;
    @FXML
    private TextField sektorNoviradnik;
    @FXML
    private TextField radnomjestoNoviradnik;

    public void stisniDodaj(ActionEvent actionEvent) {

        try(Connection konekcija = DriverManager.getConnection(url)) {
            String sqlInsert = "INSERT INTO Korisnici (Korisnici_ID, Ime, Prezime, Telefon, Sektor, Radno_mjesto) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = konekcija.prepareStatement(sqlInsert);
            if (idNoviradnik.getText() == ""){
                idNoviradnik.setText(Integer.toString(0));
                insertStmt.setInt(1, 0);
            }else {
                insertStmt.setInt(1, 0);
                korisnik.setId(Integer.parseInt(idNoviradnik.getText()));
            }
            insertStmt.setString(2, imeNoviradnik.getText());
            korisnik.setIme(imeNoviradnik.getText());

            insertStmt.setString(3, prezimeNoviradnik.getText());
            korisnik.setPrezime(prezimeNoviradnik.getText());

            if (telefonNoviradnik.getText() == ""){
                telefonNoviradnik.setText(Integer.toString(0));
                insertStmt.setInt(4, 0);
            } else {
                insertStmt.setInt(4, Integer.parseInt(telefonNoviradnik.getText()));
                korisnik.setTelefon(telefonNoviradnik.getText());
            }
            insertStmt.setString(5, sektorNoviradnik.getText());
            korisnik.setSektor(sektorNoviradnik.getText());

            insertStmt.setString(6, radnomjestoNoviradnik.getText());
            korisnik.setRadnomjesto(radnomjestoNoviradnik.getText());

            int dodaniKorisnik = insertStmt.executeUpdate();
            Controller mainController = new Controller();

            tabelaModel noviRadnik = new tabelaModel();
            noviRadnik.setIDTabela(idNoviradnik.getText());
            noviRadnik.setImeTabela(imeNoviradnik.getText());
            noviRadnik.setPrezimeTabela(prezimeNoviradnik.getText());
            noviRadnik.setTelefonTabela(telefonNoviradnik.getText());
            noviRadnik.setSektorTabela(sektorNoviradnik.getText());
            noviRadnik.setRMTabela(radnomjestoNoviradnik.getText());

            mainController.ispunaTabele();

            cancelAddMemberPanel(actionEvent);



//            Alert informacijaDodaj = new Alert(Alert.AlertType.INFORMATION);
//            informacijaDodaj.setTitle("Informacija");
//            informacijaDodaj.setHeaderText("Korisnik uspješno dodan");
//            informacijaDodaj.setContentText("Korisnik: " + imeNoviradnik.getText() + " " + prezimeNoviradnik.getText() + " je uspješno kreiran.");
//            informacijaDodaj.showAndWait().ifPresent(rs -> {
//                if (rs == ButtonType.OK) {
//                    if(dodaniKorisnik > 0){
//
//
////
////                        tabela.getItems().removeAll(obList);
////                        ispunaTabele();
////                        obrisiTextPolja();
////                        zabranaIzmjeneID();
//                        // konzolna potvrda
//                        System.out.println("Korisnik: " + imeNoviradnik.getText() + " " + prezimeNoviradnik.getText()+ " je uspješno kreiran.");
//                    }
//                }
//            });
        } catch (SQLException e) {
            Alert upozorenjeDodajSQL = new Alert(Alert.AlertType.INFORMATION);
            upozorenjeDodajSQL.setTitle("SQL Greška");
            upozorenjeDodajSQL.setHeaderText("Kontaktirajte administratora");
            upozorenjeDodajSQL.setContentText("Opis greške: \n" + e.getMessage());
        } catch (greske e){
            Alert upozorenjeDodajGreska = new Alert(Alert.AlertType.INFORMATION);
            upozorenjeDodajGreska.setTitle("Greška pri unosu");
            upozorenjeDodajGreska.setHeaderText("Napravili ste grešku prilikom izmjene.");
            upozorenjeDodajGreska.setContentText("Opis greške: \n" + e.getMessage());
            upozorenjeDodajGreska.showAndWait();
        }
    }

    public void cancelAddMemberPanel(ActionEvent event)
    {
        Stage stage = (Stage) dodajNoviRadnik.getScene().getWindow();
        stage.close();
    }

}
