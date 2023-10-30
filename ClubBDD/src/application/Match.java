package application;

public class Match {
	
	private String codeMatch;
    private String nbreButsClubA;
    private String nbreButsClubB;
    private String nbreSpectateurs;
    private String codeArbitre;
    private String codeStade;

    public Match(String codeMatch, String nbreButsClubA, String nbreButsClubB, String nbreSpectateurs, String codeArbitre, String codeStade) {
        this.codeMatch = codeMatch;
        this.nbreButsClubA = nbreButsClubA;
        this.nbreButsClubB = nbreButsClubB;
        this.nbreSpectateurs = nbreSpectateurs;
        this.codeArbitre = codeArbitre;
        this.codeStade = codeStade;
    }

    // Getters and setters
    public String getCodeMatch() {
        return codeMatch;
    }

    public void setCodeMatch(String codeMatch) {
        this.codeMatch = codeMatch;
    }

    public String getNbreButsClubA() {
        return nbreButsClubA;
    }

    public void setNbreButsClubA(String nbreButsClubA) {
        this.nbreButsClubA = nbreButsClubA;
    }

    public String getNbreButsClubB() {
        return nbreButsClubB;
    }

    public void setNbreButsClubB(String nbreButsClubB) {
        this.nbreButsClubB = nbreButsClubB;
    }

    public String getCodeArbitre() {
        return codeArbitre;
    }

    public void setCodeArbitre(String codeArbitre) {
        this.codeArbitre = codeArbitre;
    }

	public String getNbreSpectateurs() {
		return nbreSpectateurs;
	}

	public void setNbreSpectateurs(String nbreSpectateurs) {
		this.nbreSpectateurs = nbreSpectateurs;
	}

	public String getCodeStade() {
		return codeStade;
	}

	public void setCodeStade(String codeStade) {
		this.codeStade = codeStade;
	}

}
