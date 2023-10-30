package application;

public class Bureau {
	
	private String code;
	private String nom;
	private String adresse;
	private String dateCreation ;
	
	//Constructeur
    public Bureau(String code, String nom, String adresse, String dateCreation) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

}
