package application;

public class Calendrier {
	
	 	private String CodeMatch;
	    private String DateCalendrier;
	    private String heure;
	    private String ClubA;
	    private String ClubB;
	    private String codeStade;

	    // Constructeur
	    public Calendrier(String codeMatch, String dateCalendrier, String heure, String clubA, String clubB, String codeStade) {
	        this.CodeMatch = codeMatch;
	        this.DateCalendrier = dateCalendrier;
	        this.heure = heure;
	        this.ClubA = clubA;
	        this.ClubB = clubB;
	        this.codeStade = codeStade;
	    }
	    
	    // Getters et Setters
	    public String getCodeMatch() {
	        return CodeMatch;
	    }
	    
	    public void setCodeMatch(String codeMatch) {
	        this.CodeMatch = codeMatch;
	    }
	    
	    public String getDateCalendrier() {
	        return DateCalendrier;
	    }
	    
	    public void setDateCalendrier(String dateCalendrier) {
	        this.DateCalendrier = dateCalendrier;
	    }
	    	  
	    public String getClubA() {
	        return ClubA;
	    }
	    
	    public void setClubA(String clubA) {
	        this.ClubA = clubA;
	    }
	    
	    public String getClubB() {
	        return ClubB;
	    }
	    
	    public void setClubB(String clubB) {
	        this.ClubB = clubB;
	    }

		public String getHeure() {
			return heure;
		}

		public void setHeure(String heure) {
			this.heure = heure;
		}

		public String getCodeStade() {
			return codeStade;
		}

		public void setCodeStade(String codeStade) {
			this.codeStade = codeStade;
		}
	    
}
