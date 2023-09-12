/**
 * Type of investigator (Doutorado) with a scholarship
 * @author euric
 */
class Doutorado extends Bolseiro{
    
    /**
     * create empty constructor
     */
    public Doutorado() {
    }

    /**
     * cost of shcolarship cost per mounth
     * @return 
     */
    @Override
    public int getCustoBolsa() {
        return 1000;
    }
    
    /**
     * print information about this type of scholar
     * @return 
     */
    @Override
    public String toString() {
        return "\n---Bolseiro Doutorado---" + super.toString();
    }

    @Override
    public void setOrientador(Investigador docente) throws Exception {
        throw new Exception("Aviso: bolseiros doutorados nao tem orienttador.");
    }
}
