/**
 * Type of investigator (Licenciado) with a scholarship
 * @author euric
 */
class Licenciado extends Bolseiro{
    
    //variable to associate a studentsholar with its teacher advisor
    private Docente orientador;
    
    /**
     * create empty constructor
     */
    public Licenciado() {
    }
    
    public Licenciado(Docente orientador) {
        this.orientador = orientador;
    }

    public Docente getOrientador() {
        return orientador;
    }
    
    /**
     * set orientador (instance of Docente) for object Licencidado
     * @param docente 
     */
    @Override
    public void setOrientador(Investigador investigador) throws Exception{
        
        if (investigador.getCustoBolsa() == 0){
            
            //if arraylist of this. project isn't empty
            if( !(this.projectos.isEmpty()) ){
                //if investigator is associated in the same project as Bolseiro
                if (investigador.projectos.contains(this.projectos.get(0))){
                    this.orientador = (Docente) investigador;
                }
                else {
                    throw new Exception("Erro: Docente introduzido nao esta associado ao mesmo projecto que este Bolseiro!");
                }
            }
            else {
                throw new Exception("Erro: bolseiro tem de estar associado a um projeto para poder ter orienttador!");
            }
        }
        else {
            throw new Exception("Erro: investigador introduzido nao e Docente!");
        }
    }
    
    /**
     * cost of shcolarship cost per mounth
     * @return 
     */
    @Override
    public int getCustoBolsa(){
        return 500;
    }
    
    /**
     * print information about this type of scholar
     * @return 
     */
    @Override
    public String toString() {
        return "\n---Bolseiro Licenciado---" +"\nOrientador: "+getOrientador()+ super.toString();
    }
}
