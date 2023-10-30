package application;

public class Dirigeant {
	
	private String code;
    private String nom;
    private String prenom;
    private String profession;
    
    public Dirigeant(String code, String nom, String prenom, String profession) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
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
    
    public String getProfession() {
        return profession;
    }
    
    public void setProfession(String profession) {
        this.profession = profession;
    }

}
