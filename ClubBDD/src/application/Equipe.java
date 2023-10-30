package application;

public class Equipe {
	
	private String CodeClub;
    private String CodeJoueur;
    private String DateDebutContrat;
    private String DateFinContrat;

    // Constructeur
    public Equipe(String codeClub, String codeJoueur, String dateDebutContrat, String dateFinContrat) {
        this.CodeClub = codeClub;
        this.CodeJoueur = codeJoueur;
        this.DateDebutContrat = dateDebutContrat;
        this.DateFinContrat = dateFinContrat;
    }

    // Getters et Setters
    public String getCodeClub() {
        return CodeClub;
    }

    public void setCodeClub(String codeClub) {
        CodeClub = codeClub;
    }

    public String getCodeJoueur() {
        return CodeJoueur;
    }

    public void setCodeJoueur(String codeJoueur) {
        CodeJoueur = codeJoueur;
    }

    public String getDateDebutContrat() {
        return DateDebutContrat;
    }

    public void setDateDebutContrat(String dateDebutContrat) {
        DateDebutContrat = dateDebutContrat;
    }

    public String getDateFinContrat() {
        return DateFinContrat;
    }

    public void setDateFinContrat(String dateFinContrat) {
        DateFinContrat = dateFinContrat;
    }

}
