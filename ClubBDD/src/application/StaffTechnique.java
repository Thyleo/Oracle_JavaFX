package application;

public class StaffTechnique {
	
	private String code;
    private String nom;
    private String codeClub;
    private String fonction;
    
    public StaffTechnique(String code, String nom, String codeClub, String fonction) {
        this.code = code;
        this.nom = nom;
        this.codeClub = codeClub;
        this.fonction = fonction;
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
    
    public String getCodeClub() {
        return codeClub;
    }
    
    public void setCodeClub(String codeClub) {
        this.codeClub = codeClub;
    }
    
    public String getFonction() {
        return fonction;
    }
    
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

}
