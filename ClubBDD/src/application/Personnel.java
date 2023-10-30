package application;

public class Personnel {
	
	 	private String Code;
	    private String Nom;
	    private String Prenom;
	    private String ville;

	    // Constructeur
	    public Personnel(String code, String nom, String prenom, String ville) {
	        this.Code = code;
	        this.Nom = nom;
	        this.Prenom = prenom;
	        this.ville = ville;
	    }

	    // Getters et Setters
	    public String getCode() {
	        return Code;
	    }

	    public void setCode(String code) {
	        Code = code;
	    }

	    public String getNom() {
	        return Nom;
	    }

	    public void setNom(String nom) {
	        Nom = nom;
	    }

	    public String getPrenom() {
	        return Prenom;
	    }

	    public void setPrenom(String prenom) {
	        Prenom = prenom;
	    }

	    
	    public String getVille() {
	        return ville;
	    }

	    public void setVille(String ville) {
	    	ville = ville;
	    }

}
