package application;

public class Palmares {

	 	private String CodeClub;
	 	private String annee;
	    private String Trophee;
	    private String NbreMatchsGagnes;
	    private String NbreMatchsPerdus;
	    
	    public Palmares(String codeClub, String annee, String trophee, String nbreMatchsGagnes, String nbreMatchsPerdus) {
	        this.CodeClub = codeClub;
	        this.annee = annee;
	        this.Trophee = trophee;
	        this.NbreMatchsGagnes = nbreMatchsGagnes;
	        this.NbreMatchsPerdus = nbreMatchsPerdus;
	    }

	    public String getCodeClub() {
	        return CodeClub;
	    }

	    public void setCodeClub(String codeClub) {
	        this.CodeClub = codeClub;
	    }

	    public String getTrophee() {
	        return Trophee;
	    }

	    public void setTrophee(String trophee) {
	        this.Trophee = trophee;
	    }

	    public String getNbreMatchsGagnes() {
	        return NbreMatchsGagnes;
	    }

	    public void setNbreMatchsGagnes(String nbreMatchsGagnes) {
	        this.NbreMatchsGagnes = nbreMatchsGagnes;
	    }

	    public String getNbreMatchsPerdus() {
	        return NbreMatchsPerdus;
	    }

	    public void setNbreMatchsPerdus(String nbreMatchsPerdus) {
	        this.NbreMatchsPerdus = nbreMatchsPerdus;
	    }

		public String getAnnee() {
			return annee;
		}

		public void setAnnee(String annee) {
			this.annee = annee;
		}
	    
}
