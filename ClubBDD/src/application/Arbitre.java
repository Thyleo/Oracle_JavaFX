package application;

public class Arbitre {
	
	private String Code;
    private String Nom;
    private String prenom;
    private String DateNaissance;
    private String CodeBureau;
    private String clubPrefere;

    //Constructeur
    public Arbitre(String code, String nom, String prenom, String dateNaissance, String codeBureau, String clubPrefere) {
        this.Code = code;
        this.Nom = nom;
        this.prenom = prenom;
        this.DateNaissance = dateNaissance;
        this.CodeBureau = codeBureau;
        this.clubPrefere = clubPrefere;
    }

    // Getters et Setters
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.DateNaissance = dateNaissance;
    }

    public String getCodeBureau() {
        return CodeBureau;
    }

    public void setCodeBureau(String codeBureau) {
        this.CodeBureau = codeBureau;
    }

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getClubPrefere() {
		return clubPrefere;
	}

	public void setClubPrefere(String clubPrefere) {
		this.clubPrefere = clubPrefere;
	}

}
