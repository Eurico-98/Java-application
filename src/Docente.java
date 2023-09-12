
/**
 * it's a type of investigator, represents the teachers
 * @author euric
 */
class Docente extends Investigador{
    
    //one teacher can be working on a max of 3 projects at the same time, this value is the same for every Docente
    public static final int maxProjetos = 3;
    private int totaProjetos;
    private int numMecanografico;
    private String areaInvestigacao;
    private Projecto docenteIP;
    
    /**
     * create empty constructor
     */
    public Docente() {
    }
    
     /**
     * create empty constructor to be able to acess this class through others
     */
    public Docente(int numMecanografico, String areaInvestigacao, Projecto docenteIP, int totaProjetos) {
        this.totaProjetos = totaProjetos;
        this.numMecanografico = numMecanografico;
        this.areaInvestigacao = areaInvestigacao;
        //returns false if Docente not ip of a project else returs true
        this.docenteIP = docenteIP;
    }

    public int getTotaProjetos() {
        return totaProjetos;
    }

    public void setTotaProjetos() {
        this.totaProjetos++;
    }

    public Projecto getDocenteIP() {
        return docenteIP;
    }

    public void setDocenteIP(Projecto docenteIP) {
        this.docenteIP = docenteIP;
    }
    
    public int getNumMecanografico() {
        return numMecanografico;
    }

    public void setNumMecanografico(int numMecanografico) {
        this.numMecanografico = numMecanografico;
    }

    public String getAreaInvestigacao() {
        return areaInvestigacao;
    }

    public void setAreaInvestigacao(String areaInvestigacao) {
        this.areaInvestigacao = areaInvestigacao;
    }
    
    /**
     * this type of investigator doesn't have a scholarship, so here it returns 0;
     * @return 
     */
    @Override
    public int getCustoBolsa() {
        return 0;
    }
    
    /**
     * print the identification number of the teacher, and his area of research
     * @return 
     */
    @Override
    public String toString() {
        return "\n---Docente---" + super.toString() + "\nNúmero mecanográfico: " + numMecanografico + "\nÁrea de investigação: " + areaInvestigacao;
    }
}
