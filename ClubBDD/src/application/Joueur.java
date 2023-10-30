package application;

public class Joueur {

	private String code;
    private String nom;
    private String prenom;
    private String dateNaissance;
    
    public Joueur(String code, String nom, String prenom, String dateNaissance) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }
    
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
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
	
}
