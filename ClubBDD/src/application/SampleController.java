package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SampleController {

	@FXML
	private Label title;

	@FXML
	private Button btConnexion;

	@FXML
	private Button btExecuter;

	@FXML
	private TableView listData;

	@FXML
	private TableColumn column1;

	@FXML
	private TableColumn column2;

	@FXML
	private TableColumn column3;

	@FXML
	private TableColumn column4;

	private TableColumn column5;

	private TableColumn column6;

	private TableColumn column7;

	@FXML
	private Label statut;

	@FXML
	private ComboBox<String> listActions;

	@FXML
	private ComboBox<String> listTables;

	@FXML
	private TextField usernameField;

	@FXML
	private TextField passwordField;

	@FXML
	private ComboBox<String> comboList1;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField field1;

	@FXML
	private ComboBox<String> comboList2;

	@FXML
	private ComboBox<String> comboList3;

	@FXML
	private ComboBox<String> comboList4;

	@FXML
	private TextField field2;

	@FXML
	private TextField field3;

	@FXML
	private TextField field4;

	@FXML
	private TextField field5;
	
	@FXML
	private TextField field6;
	
	@FXML
	private Button btInsert;

	@FXML
	private Button btAnnuler;
	
	@FXML
	private Button btUpdate;
	
	@FXML
	private Button btUpdate2;
	
	@FXML
	private ListView<String> updates;

	@FXML
	private Label log;

	private Connection con;

	private String role;

	int compteur = 0;

	// ################################ METHODES JAVAFX ############################### //
	
	@FXML
	public void initialize() {
		System.out.println("Démarrage de l'application...");
		isVisibleInsertComponent(false);
		isVisibleUpdateComponent(false);
		btExecuter.setDisable(true);
		btUpdate.setVisible(false);
	}

	@FXML
	public void actionConnexionDeconnexion(ActionEvent ev) {
		try {

			if ("CONNEXION".equalsIgnoreCase(btConnexion.getText())) {
				System.out.println("Bouton connexion activé !");

				if (connexionToBDD(usernameField.getText(), passwordField.getText())) {
					log.setText("LOG : WELCOME !");
					btExecuter.setDisable(false);
					initializationComponent();
					updates.getItems().add("Connexion établie");

					if(!role.equalsIgnoreCase("system") && !role.equalsIgnoreCase("regionCentre")) {
						isVisibleInsertComponent(false);
						isVisibleList(true);
					}
				}
				
			} else if ("DECONNEXION".equalsIgnoreCase(btConnexion.getText())) {
				System.out.println("Bouton deconnexion activé !");
				desactivateComponent();
				isVisibleUpdateComponent(false);
				btUpdate.setVisible(false);
			}

		} catch (Exception e) {
			log.setText("LOG : ERROR during communication with Oracle !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
	}
	
	@FXML
	public void actionExecuter(ActionEvent ev) {
		try {

			if ("SELECT".equalsIgnoreCase(listActions.getValue())) {
				isVisibleList(true);
				isVisibleInsertComponent(false);
				isVisibleUpdateComponent(false);
				clearFields();
				selectSQL(listTables.getValue());
				updates.getItems().add("SELECT on "+ listTables.getValue());
			}

			if ("INSERT".equalsIgnoreCase(listActions.getValue())) {
				log.setText("LOG : Ready for Insertion in " + listTables.getValue());
				clearFields();
				isVisibleList(false);
				initializeTextField(listTables.getValue());
			}

		} catch (Exception e) {
			log.setText("LOG : ERROR of action EXECUTE. Try to change parameters !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
	}

	@FXML
	public void actionAnnuler(ActionEvent ev) {
		System.out.println("Exécution du bouton ANNULER en cours... !!");
		clearFields();
		initializeTextField(listTables.getValue());
	}

	@FXML
	public void actionInsert(ActionEvent ev) {
		System.out.println("Exécution du bouton INSERT en cours... !!");
		insertSQL(listTables.getValue());
	}

	@FXML
	public void actionUpdate(ActionEvent ev) {
		if(listData.getSelectionModel().isEmpty()) {
			log.setText("LOG : Select before a data !");
			return;
		}
		
		System.out.println("Exécution du bouton UPDATE en cours... !!");
		isVisibleList(false);
		isVisibleUpdateComponent(true);
		
		initializeUpdateFields(listTables.getValue(),listData.getSelectionModel().getSelectedItem());	
		log.setText("LOG : Ready for Update in " + listTables.getValue());
	}
	
	@FXML
	public void actionUpdate2(ActionEvent ev) {	
		System.out.println("Exécution du bouton UPDATE 2 en cours... !!");
		updateSQL(listTables.getValue());
	}
	
	// ########################### METHODES EN COMMUNICATION AVEC ORACLE ############################## //

	private boolean connexionToBDD(String username, String password) {
		System.out.println("Connexion à la BDD en cours...");

		try {

			// Chargement de la classe de driver
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// Création de l'objet de connexion
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", username, password);

			// Affectation du role
			role = username;

			// Validation de la connexion
			return true;

		} catch (Exception e) {
			log.setText("LOG : ERROR during the connection !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		// Message retourné en cas d'echec de connexion
		return false;
	}

	private void selectSQL(String table) {
		try {

			listData.getColumns().remove(column5);
			listData.getColumns().remove(column6);
			listData.getColumns().remove(column7);
		
			
			if (table.contains("CLUBSPORTIF")) {
				ObservableList<Club> values = this.getAllClubs();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("BUREAU")) {
				ObservableList<Bureau> values = this.getAllBureaux();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("ARBITRE")) {
				ObservableList<Arbitre> values = this.getAllArbitres();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("MATCH")) {
				ObservableList<Match> values = this.getAllMatchs();
				listData.setItems(values);
				
				if(role.equalsIgnoreCase("system") || role.equalsIgnoreCase("regionCentre")) {
					btUpdate.setVisible(true);
				}
				
			} else if (table.contains("CALENDRIER")) {
				ObservableList<Calendrier> values = this.getAllCalendriers();
				listData.setItems(values);
				
				if(role.equalsIgnoreCase("system") || role.equalsIgnoreCase("regionCentre")) {
					btUpdate.setVisible(true);
				}
				
			} else if (table.contains("PALMARES")) {
				ObservableList<Palmares> values = this.getAllPalmares();
				listData.setItems(values);
				
				if(role.equalsIgnoreCase("system") || role.equalsIgnoreCase("regionCentre")) {
					btUpdate.setVisible(true);
				}
				
			} else if (table.contains("JOUEUR")) {
				ObservableList<Joueur> values = this.getAllJoueurs();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("STAFFTECHNIQUE")) {
				ObservableList<StaffTechnique> values = this.getAllStaffTechniques();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("DIRIGEANT")) {
				ObservableList<Dirigeant> values = this.getAllDirigeants();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("EQUIPE")) {
				ObservableList<Equipe> values = this.getAllEquipes();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("STADE")) {
				ObservableList<Stade> values = this.getAllStades();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else if (table.contains("PERSONNEL")) {
				ObservableList<Personnel> values = this.getAllPersonnels();
				listData.setItems(values);
				btUpdate.setVisible(false);

			} else {
				log.setText("LOG : No data in " + table);
				clearList();
			}

		} catch (Exception e) {
			updates.getItems().add("ECHEC SELECT SQL on " + table);
			log.setText("LOG : ERROR of action SELECT !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
	}

	private void insertSQL(String table) {

		try {
			
			int rows = 0;
			
			if (table.contains("CALENDRIER")) {
				// Requête de l'Insertion
				String query = "INSERT INTO "+table+ " (CodeMatch, DateCalendrier, Heure, ClubA, ClubB, CodeStade) VALUES (?, ?, ?, ?, ?, ?)";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);
	            
				// Insert prepared statement
				preparedStmt.setLong(1, Long.parseLong(comboList1.getValue()));
				preparedStmt.setDate(2, Date.valueOf(datePicker.getValue()));
				preparedStmt.setTime(3, Time.valueOf((field1.getText())));
				preparedStmt.setLong(4, Long.parseLong(getCodeClub(comboList2.getValue())));
				preparedStmt.setLong(5, Long.parseLong(getCodeClub(comboList3.getValue())));
				preparedStmt.setLong(6, Long.parseLong(getCodeStade(comboList4.getValue())));

				if(!isValidTimeFormat(field1.getText())) {
					log.setText("LOG : WRONG time format ! Must be HH:MM:SS");
					return;
				}
					
				// Execution de la requête
				rows = preparedStmt.executeUpdate();
				
			} else if (table.contains("MATCH")) {
				// Requête de l'Insertion
				String query = "INSERT INTO "+ table +" (CodeMatch, NbreButsClubA, NbreButsClubB, NbreSpectateurs, CodeArbitre, CodeStade) VALUES (?, ?, ?, ?, ?, ?)";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);

				// Insert prepared statement
				preparedStmt.setLong(1, Long.parseLong(comboList1.getValue()));
				preparedStmt.setLong(2, Long.parseLong(field2.getText()));
				preparedStmt.setLong(3, Long.parseLong(field1.getText()));
				preparedStmt.setLong(4, Long.parseLong(field3.getText()));
				preparedStmt.setLong(5, Long.parseLong(getCodeArbitre(comboList3.getValue())));
				preparedStmt.setLong(6, Long.parseLong(getCodeStade(comboList4.getValue())));

				// Execution de la requête
				rows = preparedStmt.executeUpdate();
				
			} else if (table.contains("PALMARES")) {
				// Requête de l'Insertion
				String query = "INSERT INTO "+table +" (CodeClub, Annee, Trophee, NbreMatchsGagnes, NbreMatchsPerdus) VALUES (?, ?, ?, ?, ?)";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);

				// Insert prepared statement
				preparedStmt.setLong(1, Long.parseLong(getCodeClub(comboList1.getValue())));
				preparedStmt.setDate(2, Date.valueOf(datePicker.getValue()));
				preparedStmt.setString(3, field1.getText());
				preparedStmt.setLong(4, Long.parseLong(field3.getText()));
				preparedStmt.setLong(5, Long.parseLong(field4.getText()));


				// Execution de la requête
				rows = preparedStmt.executeUpdate();
				
			} else {
				log.setText("LOG : NO table for action INSERT !");
				System.out.println("ECHEC du bouton INSERT !");
			}

			if(rows>0) {
				log.setText("LOG : INSERT worked successfully");
				updates.getItems().add("INSERT on " + table);
				clearFields();
				initializeTextField(listTables.getValue());
			} else
				log.setText("LOG : INSERT didn't work ! Check all the values");
			
		} catch (Exception e) {
			updates.getItems().add("ECHEC INSERT SQL on " + table);
			log.setText("LOG : ERROR of action INSERT !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
	}

	private void updateSQL(String table) {

		try {
			
			int rows = 0;			
			if (table.contains("CALENDRIER")) {
				// Requête de l'update
				String query = "UPDATE " +table+" SET DateCalendrier = ?, Heure = ?, ClubA = ?, ClubB = ?, CodeStade = ? WHERE CodeMatch = ?";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);
	            
				// Update prepared statement				
				preparedStmt.setLong(6, Long.parseLong(field5.getText()));
				LocalDate date = LocalDate.parse(field2.getText().substring(0, 10));
				preparedStmt.setDate(1, Date.valueOf(date));
				preparedStmt.setTime(2, Time.valueOf(field1.getText().substring(12, field1.getLength() -3)));
				preparedStmt.setLong(3, Long.parseLong(field3.getText()));
				preparedStmt.setLong(4, Long.parseLong(field4.getText()));
				preparedStmt.setLong(5, Long.parseLong(field6.getText()));

				// Execution de la requête
				rows = preparedStmt.executeUpdate();
				
			} else if (table.contains("MATCH")) {
				// Requête de l'update
				String query = "UPDATE "+table+" SET CodeMatch = ?, NbreButsClubA = ?, NbreButsClubB = ?, NbreSpectateurs = ?, CodeArbitre = ?, CodeStade = ?";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);

				// Update prepared statement
				preparedStmt.setLong(6, Long.parseLong(field5.getText()));
				preparedStmt.setLong(1, Long.parseLong(field2.getText()));
				preparedStmt.setLong(2, Long.parseLong(field1.getText()));
				preparedStmt.setLong(3, Long.parseLong(field3.getText()));
				preparedStmt.setLong(4, Long.parseLong(field4.getText()));
				preparedStmt.setLong(5, Long.parseLong(field6.getText()));

				// Execution de la requête
				rows = preparedStmt.executeUpdate();
				
			} else if (table.contains("PALMARES")) {
				// Requête de l'update
				String query = "UPDATE " +table+" SET Annee = ?, Trophee = ?, NbreMatchsGagnes = ?, NbreMatchsPerdus = ? WHERE CodeClub = ?";

				// Création d'un statement
				PreparedStatement preparedStmt = con.prepareStatement(query);

				// Update prepared statement
				preparedStmt.setLong(5, Long.parseLong(field5.getText()));
				LocalDate date = LocalDate.parse(field2.getText().substring(0, 10));
				preparedStmt.setDate(1,  Date.valueOf(date));
				preparedStmt.setString(2, field1.getText());
				preparedStmt.setLong(3, Long.parseLong(field3.getText()));
				preparedStmt.setLong(4, Long.parseLong(field4.getText()));

				// Execution de la requête
				rows = preparedStmt.executeUpdate();
			}

			if(rows>0) {
				log.setText("LOG : UPDATE worked successfully");
				updates.getItems().add("UPDATE on " + table);
				isVisibleList(true);
				isVisibleUpdateComponent(false);
				selectSQL(listTables.getValue());
				
			} else
				log.setText("LOG : UPDATE didn't work ! Check all the values");
			
		} catch (Exception e) {
			updates.getItems().add("ECHEC UPDATE SQL on " + table);
			log.setText("LOG : ERROR of action UPDATE !");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
	}
	
	// ############################### METHODES D'INITIALISATION DE COMPOSANTS ############################### //

	private void initializationComponent() {

		// Modifications des composants de connexion
		usernameField.setEditable(false);
		passwordField.setEditable(false);
		statut.setText("STATUT : Connected as " + role.toUpperCase());
		btConnexion.setText("DECONNEXION");

		// Déploiement des actions sur la table
		listActions.getItems().add("SELECT");

		// Déploiement des tables liées à l'utilisateur
		if ("System".equalsIgnoreCase(role)) {

			listActions.getItems().add("INSERT");
			listTables.getItems().add("CLUBSPORTIF");
			listTables.getItems().add("DIRIGEANT");
			listTables.getItems().add("EQUIPE");
			listTables.getItems().add("JOUEUR");
			listTables.getItems().add("STAFFTECHNIQUE");
			listTables.getItems().add("STADE");
			listTables.getItems().add("ARBITRE");
			listTables.getItems().add("PERSONNEL");
			listTables.getItems().add("MATCH");
			listTables.getItems().add("CALENDRIER");
			listTables.getItems().add("PALMARES");
			listTables.getItems().add("BUREAU");

		} else if ("RegionNord".equalsIgnoreCase(role)) {

			listTables.getItems().add("CLUBSPORTIFNORD");
			listTables.getItems().add("STADENORD");
			listTables.getItems().add("EQUIPENORD");
			listTables.getItems().add("PERSONNELNORD");
			listTables.getItems().add("PALMARESNORD");
			listTables.getItems().add("CALENDRIERNORD");
			listTables.getItems().add("MATCHNORDVM");
			listTables.getItems().add("ARBITRENORDVM");

		} else if ("RegionCentre".equalsIgnoreCase(role)) {

			listActions.getItems().add("INSERT");
			listTables.getItems().add("CLUBSPORTIFCENTRE");
			listTables.getItems().add("STADECENTRE");
			listTables.getItems().add("EQUIPECENTRE");
			listTables.getItems().add("PERSONNELCENTRE");
			listTables.getItems().add("PALMARESCENTRE");
			listTables.getItems().add("CALENDRIERCENTRE");
			listTables.getItems().add("MATCHCENTRE");
			listTables.getItems().add("ARBITRECENTRE");
			listTables.getItems().add("BUREAU");

		} else if ("RegionSud".equalsIgnoreCase(role)) {

			listTables.getItems().add("CLUBSPORTIFSUD");
			listTables.getItems().add("STADESUD");
			listTables.getItems().add("EQUIPESUD");
			listTables.getItems().add("PERSONNELSUD");
			listTables.getItems().add("PALMARESSUD");
			listTables.getItems().add("CALENDRIERSUD");
			listTables.getItems().add("MATCHSUDVM");
			listTables.getItems().add("ARBITRESUDVM");

		} else if ("RegionOuest".equalsIgnoreCase(role)) {

			listTables.getItems().add("CLUBSPORTIFOUEST");
			listTables.getItems().add("STADEOUEST");
			listTables.getItems().add("EQUIPEOUEST");
			listTables.getItems().add("PERSONNELOUEST");
			listTables.getItems().add("PALMARESOUEST");
			listTables.getItems().add("CALENDRIEROUEST");
			listTables.getItems().add("MATCHOUESTVM");
			listTables.getItems().add("ARBITREOUESTVM");

		} else if ("RegionEst".equalsIgnoreCase(role)) {

			listTables.getItems().add("CLUBSPORTIFEST");
			listTables.getItems().add("STADEEST");
			listTables.getItems().add("EQUIPEEST");
			listTables.getItems().add("PERSONNELEST");
			listTables.getItems().add("PALMARESEST");
			listTables.getItems().add("CALENDRIEREST");
			listTables.getItems().add("MATCHESTVM");
			listTables.getItems().add("ARBITREESTVM");
		}

		System.out.println("Initialisation de composants terminée !");
	}
	
	private void initializeTextField(String table) {

		comboList1.getItems().clear();
		comboList2.getItems().clear();
		comboList3.getItems().clear();
		comboList4.getItems().clear();
		
		if (table.contains("CALENDRIER")) {
			isVisibleInsertComponent(true);
			comboList1.setPromptText("CodeMatch");
			datePicker.setPromptText("Date");
			field1.setPromptText("Heure");
			comboList2.setPromptText("CodeClubA");
			comboList3.setPromptText("CodeClubB");
			comboList4.setPromptText("CodeStade");

			// Initialisation des données des listes
			comboList1.getItems().addAll(getAllCodeMatchs());
			comboList2.getItems().addAll(getAllNomClubs());
			comboList3.getItems().addAll(getAllNomClubs());
			comboList4.getItems().addAll(getAllNomStades());

			// Masquage des champs inutiles pour la table
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);

		} else if (table.contains("MATCH")) {
			isVisibleInsertComponent(true);
			comboList1.setPromptText("CodeMatch");
			field2.setPromptText("NbreButsClubA");
			field1.setPromptText("NbreButsClubB");
			field3.setPromptText("NbreSpectateurs");
			comboList3.setPromptText("CodeArbitre");
			comboList4.setPromptText("CodeStade");

			// Initialisation des données des listes
			comboList1.getItems().addAll(getAllCodeMatchs());
			comboList3.getItems().addAll(getAllNomArbitres());
			comboList4.getItems().addAll(getAllNomStades());
			
			// Masquage des champs inutiles pour la table
			comboList2.setVisible(false);
			field4.setVisible(false);
			datePicker.setVisible(false);

		} else if (table.contains("PALMARES")) {
			isVisibleInsertComponent(true);
			comboList1.setPromptText("CodeClub");
			datePicker.setPromptText("Annee");
			field1.setPromptText("Trophee");
			field3.setPromptText("NbreMatchsGagnes");
			field4.setPromptText("NbreMatchsPerdus");

			// Initialisation des données des listes
			comboList1.getItems().addAll(getAllNomClubs());
			
			// Masquage des champs inutiles pour la table
			field2.setVisible(false);
			comboList2.setVisible(false);
			comboList3.setVisible(false);
			comboList4.setVisible(false);

		} else {
			isVisibleInsertComponent(false);
			log.setText("LOG : Insertion prohibited for " + table);
			clearFields();
		}
		
		System.out.println("Affichage des composants pour insertion dans " + table + " terminée !");
	}

	private void initializeUpdateFields(String table, Object line) {
		
		isVisibleUpdateComponent(true);
		
		if (table.contains("CALENDRIER")) {
			Calendrier cal = (Calendrier) line;
			field5.setText(cal.getCodeMatch());
			field2.setText(cal.getDateCalendrier());
			field1.setText(cal.getHeure());
			field3.setText(cal.getClubA());
			field4.setText(cal.getClubB());
			field6.setText(cal.getCodeStade());

		} else if (table.contains("MATCH")) {
			Match match = (Match) line;
			field5.setText(match.getCodeMatch());
			field2.setText(match.getNbreButsClubA());
			field1.setText(match.getNbreButsClubB());
			field3.setText(match.getNbreSpectateurs());
			field4.setText(match.getCodeArbitre());
			field6.setText(match.getCodeStade());

		} else if (table.contains("PALMARES")) {
			Palmares pal = (Palmares) line;
			field5.setText(pal.getCodeClub());
			field2.setText(pal.getAnnee());
			field1.setText(pal.getTrophee());
			field3.setText(pal.getNbreMatchsGagnes());
			field4.setText(pal.getNbreMatchsPerdus());
		}
		
		System.out.println("Affichage des composants pour mise à jour dans " + table + " terminée !");
	}

	// ############################### METHODES DE DESACTIVATION DE COMPOSANTS ############################### //

	private void desactivateComponent() {

		// Modifications des composants de connexion
		usernameField.setEditable(true);
		passwordField.setEditable(true);
		usernameField.setText("");
		passwordField.setText("");
		statut.setText("STATUT : Disconnected ");
		btConnexion.setText("CONNEXION");
		role = "";
		btExecuter.setDisable(true);

		// Retrait des données
		listActions.getItems().clear();
		listTables.getItems().clear();

		// Annulation du pointage sur les anciennes listes
		listActions.getItems().clear();
		listTables.getItems().clear();

		// Nettoyage du tableau
		clearList();

		// Nettoyage des champs
		clearFields();
		clearUpdateFields();
		
		// Nettoyage de l'historique
		updates.getItems().clear();

		log.setText("LOG : GoodBye... ");
		System.out.println("Suppression des composants terminée !");
	}

	private void clearList() {

		listData.getItems().clear();

		column1.setText("Column1");
		column2.setText("Column2");
		column3.setText("Column3");
		column4.setText("Column4");

		listData.getColumns().remove(column5);
		listData.getColumns().remove(column6);
		listData.getColumns().remove(column7);
	}
	
	private void clearFields() {

		field1.setText("");
		field2.setText("");
		field3.setText("");
		field4.setText("");
		datePicker.setValue(null);
		comboList1.getSelectionModel().clearSelection();
		comboList2.getSelectionModel().clearSelection();
		comboList3.getSelectionModel().clearSelection();
		comboList4.getSelectionModel().clearSelection();
	}

	private void clearUpdateFields() {

		field1.setText("");
		field2.setText("");
		field3.setText("");
		field4.setText("");
		field5.setText("");
		field6.setText("");
	}

	// ############################# METHODES DE CHANGEMENT DE "FRAME" ################################# //

	private void isVisibleInsertComponent(boolean visible) {

		field1.setVisible(visible);
		field2.setVisible(visible);
		field3.setVisible(visible);
		field4.setVisible(visible);
		datePicker.setVisible(visible);
		comboList1.setVisible(visible);
		comboList2.setVisible(visible);
		comboList3.setVisible(visible);
		comboList4.setVisible(visible);
		btInsert.setVisible(visible);
		btAnnuler.setDisable(!visible);
	}
	
	private void isVisibleList(boolean visible) {
		listData.setVisible(visible);
		btUpdate.setVisible(visible);
	}
	
	private void isVisibleUpdateComponent(boolean visible) {

		field1.setVisible(visible);
		field2.setVisible(visible);
		field3.setVisible(visible);
		field4.setVisible(visible);
		field5.setVisible(visible);
		field6.setVisible(visible);
		btUpdate2.setVisible(visible);
		btUpdate.setVisible(!visible);
	}
	
	// ########################### METHODES DES COMMANDES SQL ############################## //

	private ObservableList<Club> getAllClubs() {

		ObservableList<Club> values = FXCollections.observableArrayList();

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportif");

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportifNord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportifCentre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportifSud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportifOuest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from ClubSportifEst");

			// Récupération des informations
			while (res.next())
				values.add(new Club(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6)));

			log.setText("LOG : Found " + values.size() + " Club(s)");

			column1.setText("CodeClub");
			column2.setText("Nom");
			column3.setText("DateCreation");
			column4.setText("CodeDirigeant");
			column5 = new TableColumn<>("Ville");
			column6 = new TableColumn<>("CodeBureau");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Club, String>("CodeClub"));
			column2.setCellValueFactory(new PropertyValueFactory<Club, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Club, String>("DateCreation"));
			column4.setCellValueFactory(new PropertyValueFactory<Club, String>("CodeDirigeant"));
			column5.setCellValueFactory(new PropertyValueFactory<Club, String>("Ville"));
			column6.setCellValueFactory(new PropertyValueFactory<Club, String>("CodeBureau"));

			listData.getColumns().add(column5);
			listData.getColumns().add(column6);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Club for List !");
			System.out.println("Récupération des données de club échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<Bureau> getAllBureaux() {

		ObservableList<Bureau> values = FXCollections.observableArrayList();

		try {

			String table = "Bureau";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Bureau(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));

			log.setText("LOG : Found " + values.size() + " Bureau(x)");

			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Adresse");
			column4.setText("DateCreation");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Bureau, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Bureau, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Bureau, String>("Adresse"));
			column4.setCellValueFactory(new PropertyValueFactory<Bureau, String>("DateCreation"));

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Bureau for List !");
			System.out.println("Récupération des données de bureau échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<Arbitre> getAllArbitres() {

		ObservableList<Arbitre> values = FXCollections.observableArrayList();

		try {

			String table = "Arbitre";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Arbitre(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6)));

			log.setText("LOG : Found " + values.size() + " Arbitre(s)");

			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Prenom");
			column4.setText("DateNaissance");
			column5 = new TableColumn<>("CodeBureau");
			column6 = new TableColumn<>("ClubPrefere");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Prenom"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("DateNaissance"));
			column5.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeBureau"));
			column6.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("ClubPrefere"));

			listData.getColumns().add(column5);
			listData.getColumns().add(column6);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Arbitre for List !");
			System.out.println("Récupération des données de arbitre échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<StaffTechnique> getAllStaffTechniques() {
		 
		ObservableList<StaffTechnique> values = FXCollections.observableArrayList();
 
		try {
 
			String table = "StaffTechnique";
 
			// Création d'un statement
			Statement stmt = con.createStatement();
 
			// Exécution de la requête
			ResultSet res = null;
 
			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);
 
			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");
 
			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");
 
			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");
 
			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");
 
			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");
 
			// Récupération des informations
			while (res.next())
				values.add(new StaffTechnique(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
 
			log.setText("LOG : Found " + values.size() + " Joueur(s)");
 
			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeClub"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Fonction"));
 
			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("CodeClub");
			column4.setText("Fonction");
 
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT !");
			System.out.println("Récupération des données de Dirigeant échouées !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
 
		return values;
	}
	
	private ObservableList<Stade> getAllStades() {

		ObservableList<Stade> values = FXCollections.observableArrayList();

		try {

			String table = "Stade";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Stade(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5)));

			log.setText("LOG : Found " + values.size() + " Stade(s)");

			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Ville");
			column4.setText("CodeBureau");
			column5 = new TableColumn<>("Capacite");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Ville"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeBureau"));
			column5.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Capacite"));

			listData.getColumns().add(column5);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Stade for List !");
			System.out.println("Récupération des données de stade échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<Match> getAllMatchs() {

		ObservableList<Match> values = FXCollections.observableArrayList();

		try {

			String table = "Match";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Match(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6)));

			log.setText("LOG : Found " + values.size() + " Match(s)");

			column1.setText("CodeMatch");
			column2.setText("NbreButsClubA");
			column3.setText("NbreButsClubB");
			column4.setText("NbreSpectateurs");
			column5 = new TableColumn<>("CodeArbitre");
			column6 = new TableColumn<>("CodeStade");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeMatch"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("NbreButsClubA"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("NbreButsClubB"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("NbreSpectateurs"));
			column5.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeArbitre"));
			column6.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeStade"));

			listData.getColumns().add(column5);
			listData.getColumns().add(column6);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Match for List !");
			System.out.println("Récupération des données de match échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<Calendrier> getAllCalendriers() {

		ObservableList<Calendrier> values = FXCollections.observableArrayList();

		try {

			String table = "Calendrier";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Calendrier(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6)));

			log.setText("LOG : Found " + values.size() + " Calendrier(s)");

			column1.setText("CodeMatch");
			column2.setText("DateCalendrier");
			column3.setText("Heure");
			column4.setText("ClubA");
			column5 = new TableColumn<>("ClubB");
			column6 = new TableColumn<>("CodeStade");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeMatch"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("DateCalendrier"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Heure"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("ClubA"));
			column5.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("ClubB"));
			column6.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeStade"));

			listData.getColumns().add(column5);
			listData.getColumns().add(column6);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Calendrier for List !");
			System.out.println("Récupération des données de calendrier échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private ObservableList<Palmares> getAllPalmares() {

		ObservableList<Palmares> values = FXCollections.observableArrayList();

		try {

			String table = "Palmares";

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res = null;

			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);

			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");

			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");

			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");

			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");

			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");

			// Récupération des informations
			while (res.next())
				values.add(new Palmares(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5)));

			log.setText("LOG : Found " + values.size() + " Palmare(s)");

			column1.setText("CodeClub");
			column2.setText("Annee");
			column3.setText("Trophee");
			column4.setText("NbreMatchsGagnes");
			column5 = new TableColumn<>("NbreMatchsPerdus");

			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeClub"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Annee"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Trophee"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("NbreMatchsGagnes"));
			column5.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("NbreMatchsPerdus"));

			listData.getColumns().add(column5);

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Palmares for List !");
			System.out.println("Récupération des données de palmares échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}
	
	private ObservableList<Dirigeant> getAllDirigeants() {
		 
		ObservableList<Dirigeant> values = FXCollections.observableArrayList();
 
		try {
 
			String table = "Dirigeant";
 
			// Création d'un statement
			Statement stmt = con.createStatement();
 
			// Exécution de la requête
			ResultSet res = null;
 
			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);
 
			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");
 
			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");
 
			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");
 
			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");
 
			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");
 
			// Récupération des informations
			while (res.next())
				values.add(new Dirigeant(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
 
			log.setText("LOG : Found " + values.size() + " Dirigean(s)");
 
			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Prenom"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Profession"));
 
			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Prenom");
			column4.setText("Profession");
 
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT !");
			System.out.println("Récupération des données de Dirigeant échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
 
		return values;
	}
	
	private ObservableList<Personnel> getAllPersonnels() {
		 
		ObservableList<Personnel> values = FXCollections.observableArrayList();
 
		try {
 
			String table = "Personnel";
 
			// Création d'un statement
			Statement stmt = con.createStatement();
 
			// Exécution de la requête
			ResultSet res = null;
 
			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);
 
			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");
 
			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");
 
			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");
 
			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");
 
			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");
 
			// Récupération des informations
			while (res.next())
				values.add(new Personnel(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
 
			log.setText("LOG : Found " + values.size() + " Personnel(s)");
 
			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Prenom"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Ville"));
 
			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Prenom");
			column4.setText("Ville");
 
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT !");
			System.out.println("Récupération des données de Personnel échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
 
		return values;
	}
	
	private ObservableList<Equipe> getAllEquipes() {
		 
		ObservableList<Equipe> values = FXCollections.observableArrayList();
 
		try {
 
			String table = "Equipe";
 
			// Création d'un statement
			Statement stmt = con.createStatement();
 
			// Exécution de la requête
			ResultSet res = null;
 
			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);
 
			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");
 
			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");
 
			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");
 
			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");
 
			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");
 
			// Récupération des informations
			while (res.next())
				values.add(new Equipe(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
 
			log.setText("LOG : Found " + values.size() + " Equipe(s)");
 
			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeClub"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("CodeJoueur"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("DateDebutContrat"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("DateFinContrat"));
 
			column1.setText("CodeClub");
			column2.setText("CodeJoueur");
			column3.setText("DateDebutContrat");
			column4.setText("DateFinContrat");
 
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT !");
			System.out.println("Récupération des données de Equipe échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
 
		return values;
	}
	
	private ObservableList<Joueur> getAllJoueurs() {
		 
		ObservableList<Joueur> values = FXCollections.observableArrayList();
 
		try {
 
			String table = "Joueur";
 
			// Création d'un statement
			Statement stmt = con.createStatement();
 
			// Exécution de la requête
			ResultSet res = null;
 
			if ("System".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table);
 
			else if ("RegionNord".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Nord");
 
			else if ("RegionCentre".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Centre");
 
			else if ("RegionSud".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Sud");
 
			else if ("RegionOuest".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Ouest");
 
			else if ("RegionEst".equalsIgnoreCase(role))
				res = stmt.executeQuery("select * from " + table + "Est");
 
			// Récupération des informations
			while (res.next())
				values.add(new Joueur(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
 
			log.setText("LOG : Found " + values.size() + " Joueur(s)");
 
			// Organisation dans chaque colonne
			column1.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Code"));
			column2.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Nom"));
			column3.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("Prenom"));
			column4.setCellValueFactory(new PropertyValueFactory<Arbitre, String>("DateNaissance"));
 
			column1.setText("Code");
			column2.setText("Nom");
			column3.setText("Prenom");
			column4.setText("DateNaissance");
 
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT !");
			System.out.println("Récupération des données de Dirigeant échouées !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
 
		return values;
	}
	
	private String getCodeClub(String nom) {

		String value = "";

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select CodeClub from ClubSportif Where NomClub = '" +nom +"'" ); 

			// Récupération des informations
			while (res.next())
				value = res.getString(1);
			
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Club !");
			System.out.println("Récupération du code club échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
		
		return value;
	}
	
	private List<String> getAllNomClubs() {

		List<String> values = new ArrayList<>();

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select NomClub from ClubSportif"); 

			// Récupération des informations
			while (res.next())
				values.add(res.getString(1));

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on CLub !");
			System.out.println("Récupération des tous les noms de clubs échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}
	
	private String getCodeArbitre(String nom) {

		String value = "";

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select Code from Arbitre Where Nom = '" +nom +"'"); 

			// Récupération des informations
			while (res.next())
				value = res.getString(1);
			
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Arbitre !");
			System.out.println("Récupération du code arbitre échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
		
		return value;
	}
	
	private List<String> getAllNomArbitres() {

		List<String> values = new ArrayList<>();

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select Nom from Arbitre"); 

			// Récupération des informations
			while (res.next())
				values.add(res.getString(1));

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Arbitre !");
			System.out.println("Récupération des tous les noms des arbitres échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}
	
	private String getCodeStade(String nom) {

		String value = "";

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select Code from Stade Where Nom = '" +nom +"'"); 

			// Récupération des informations
			while (res.next())
				value = res.getString(1);
			
		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Stade !");
			System.out.println("Récupération du code stade échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}
		
		return value;
	}
	
	private List<String> getAllNomStades() {

		List<String> values = new ArrayList<>();

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select Nom from Stade"); 

			// Récupération des informations
			while (res.next())
				values.add(res.getString(1));

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Stade!");
			System.out.println("Récupération des tous les noms de stades échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}

	private List<String> getAllCodeMatchs() {

		List<String> values = new ArrayList<>();

		try {

			// Création d'un statement
			Statement stmt = con.createStatement();

			// Exécution de la requête
			ResultSet res =stmt.executeQuery("select CodeMatch from Match"); 

			// Récupération des informations
			while (res.next())
				values.add(res.getString(1));

		} catch (Exception e) {
			log.setText("LOG : ERROR while doing SELECT on Match!");
			System.out.println("Récupération des tous les codes matchs échouée !!");
			System.out.println("Message d'erreur : " + e.getMessage());
		}

		return values;
	}
	
	// ########################### METHODES ANNEXES ############################## //
	
    private boolean isValidTimeFormat(String timeString) {
        // Le modèle d'expression régulière pour "hh:mm:ss"
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(timeString);

        // Vérifie si la chaîne correspond au modèle
        return matcher.matches();
    }
}
