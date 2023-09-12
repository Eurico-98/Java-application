
import java.io.Serializable;
import java.util.ArrayList;

/**
 * represents an investigator
 * @author euric
 */
public abstract class Investigador implements Serializable{
    
    private String nome;
    private String mail;
    private int numTarefas;
    //this variable is the same for every investigator os any project
    public static final int totalTarefas = 2;
    //each bolseiro can only work on one project, but each Docente can work on a total of 3 project at the same time
    ArrayList<Projecto> projectos = new ArrayList<>();
    
    /**
     * create empty constructor to be able to acess this class through others
     */
    public Investigador() {
    }
    
    /**
     * constructor to access class Investigador variables
     * @param nome
     * @param mail
     * @param numTarefas 
     * @param projetoAssociado 
     */
    public Investigador(String nome, String mail, int numTarefas, Projecto projetoAssociado) {
        this.nome = nome;
        this.mail = mail;
        this.numTarefas = numTarefas = 0;//variable must be inicialized with zero, because if the investigator is not working in any projects is has zero stages to work on
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getNumTarefas() {
        return numTarefas;
    }

    public void setNumTarefas() {
        this.numTarefas++;
    }
    
    /**
     * returns the scholarship cost from class Bolseiro
     * @return 
     */
    public abstract int getCustoBolsa();
    
    /**
     * prints all the information about an investigator
     * @return 
     */
    @Override
    public String toString() {
        return "\nNome: " + nome + "\nConta de e-mail: " + mail + "\nNumero de tarefas em que está a trabalhar: " + numTarefas;
    }
}
