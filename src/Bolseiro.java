import java.util.Date;

/**
 * subclass of Investigador, represents the students with a scholarship
 * implementes abstract method (getCustoBolsa) to get shcolarship cost
 * @author euric
 */
abstract class Bolseiro extends Investigador{
    
    
    //one scholar can only be working on one project, hence the variable projetoAssociado
    private Date bolsaInicio;
    private Date bolsaFim;
    
    /**
     * create empty constructor to be able to acess this class through others
     */
    public Bolseiro() {
    }
    
    /**
     * constructor to access class Bolseiro variables
     * @param bolsaInicio
     * @param bolsaFim 
     */
    public Bolseiro(Date bolsaInicio, Date bolsaFim) {
        this.bolsaInicio = bolsaInicio;
        this.bolsaFim = bolsaFim;
    }
    
    public Date getBolsaInicio() {
        return bolsaInicio;
    }

    public void setBolsaInicio(Date bolsaInicio) {
        this.bolsaInicio = bolsaInicio;
    }

    public Date getBolsaFim() {
        return bolsaFim;
    }

    public void setBolsaFim(Date bolsaFim) {
        this.bolsaFim = bolsaFim;
    }
    
    /**
     * sets orientador for objects Linceniado Mestre
     * @param docente
     * @throws Exception 
     */
    public abstract void setOrientador(Investigador docente) throws Exception;
    
    /** 
     * the cost for each type of scholarship per mounth
     * @return
     */
    @Override
    public abstract int getCustoBolsa();
    
    /**
     * print the start and expiration dates and cost per mounth of the sholarship
     * @return 
     */
    @Override
    public String toString() {
        return super.toString() + "\nData de inicio da bolsa: " + bolsaInicio + "\nData de expiração da bolsa: " + bolsaFim + "\nCusto mensal: " + getCustoBolsa();
    }
}