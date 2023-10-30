package application;

public class Club {
	
	private String codeClub;
	private String nom;
	private String dateCreation;
	private String codeDirigeant;
	private String ville;
	private String codeBureau;
	
	public Club(String codeClub, String nom, String dateCreation, String codeDirigeant, String ville, String codeBureau){
		this.codeClub = codeClub;
		this.nom = nom;
		this.dateCreation = dateCreation;
		this.codeDirigeant = codeDirigeant;
		this.ville = ville;
		this.codeBureau = codeBureau;
	}
	
	public String getCodeClub() {
		return codeClub;
	}
	public void setCodeClub(String codeClub) {
		this.codeClub = codeClub;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getCodeBureau() {
		return codeBureau;
	}
	public void setCodeBureau(String codeBureau) {
		this.codeBureau = codeBureau;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getCodeDirigeant() {
		return codeDirigeant;
	}

	public void setCodeDirigeant(String codeDirigeant) {
		this.codeDirigeant = codeDirigeant;
	}

}
