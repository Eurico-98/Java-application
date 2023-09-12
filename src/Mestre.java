/**
 * Type of investigator (Mestre) with a scholarship
 * @author euric
 */
class Mestre extends Bolseiro{
    
    //variable to associate a studentsholar with its teacher advisor
    private Docente orientador;
    
    /**
     * create empty constructor
     */
    public Mestre() {
    }

    public Mestre(Docente orientador) {
        this.orientador = orientador;
    }

    public Docente getOrientador() {
        return orientador;
    }

    public void setOrientador(Docente orientador) {
        this.orientador = orientador;
    }
    
    /**
     * set Orientador for object Mestre
     * @param docente 
     */
    @Override
    public void setOrientador(Investigador docente) throws Exception{
        
        if (docente.getCustoBolsa() == 0){
            
            if( !(this.projectos.isEmpty()) ){
                
                if (docente.projectos.contains(this.projectos.get(0))){
                    this.orientador = (Docente) docente;
                }
                else {
                    throw new Exception("Erro: Docente introduzido nao esta associado ao mesmo projecto que este Mestre!");
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
        return 800;
    }
    
    /**
     * print information about this type of scholar
     * @return 
     */
    @Override
    public String toString() {
        return "\n---Bolseiro Mestre---" +"\nOrientador: "+getOrientador()+ super.toString();
    }
}
