package application;

public class Stade {
	
	private String Code;
    private String Nom;
    private String Ville;
    private String CodeBureau;
    public String capacite;

    // Constructeur
    public Stade(String code, String nom, String ville, String codeBureau, String capacite) {
        this.Code = code;
        this.Nom = nom;
        this.Ville = ville;
        this.CodeBureau = codeBureau;
        this.capacite = capacite;
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

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        this.Ville = ville;
    }

    public String getCodeBureau() {
        return CodeBureau;
    }

    public void setCodeBureau(String codeBureau) {
        this.CodeBureau = codeBureau;
    }

	public String getCapacite() {
		return capacite;
	}

	public void setCapacite(String capacite) {
		this.capacite = capacite;
	}
   
}
