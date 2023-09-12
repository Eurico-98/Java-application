import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * represents a stage of a project
 * @author euric
 */
public abstract class Tarefa implements Serializable{
    
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private Date dataDeConclusao;
    private int duracaoEst;
    private int percentagemConclusao;
    private Investigador responsavel;
    
    /**
     * create empty constructor
     */
    public Tarefa() {
    }
    
    /**
     * constructor to access class Tarefa variables
     * @param nome
     * @param dataInicio
     * @param dataFim
     * @param dataDeConclusao
     * @param duracaoEst
     * @param percentagemConclusao
     * @param responsavel
     */
    public Tarefa(String nome, Date dataInicio, Date dataFim, Date dataDeConclusao, int duracaoEst, int percentagemConclusao, Investigador responsavel) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataDeConclusao = dataDeConclusao;
        this.duracaoEst = duracaoEst;
        this.percentagemConclusao = percentagemConclusao = 0;//set it to zero because if the a atage is not started it is 0% done
        this.responsavel = responsavel;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataDeConclusao() {
        return dataDeConclusao;
    }

    public void setDataDeConclusao(Date dataDeConclusao) {
        this.dataDeConclusao = dataDeConclusao;
    }

    public int getDuracaoEst() {
        return duracaoEst;
    }

    public void setDuracaoEst(int duracaoEst) {
        this.duracaoEst = duracaoEst;
    }

    public int getPercentagemConclusao() {
        return percentagemConclusao;
    }

    public void setPercentagemConclusao(int percentagemConclusao) {
        this.percentagemConclusao = percentagemConclusao;
    }

    public Investigador getResponsavel() {
        return responsavel;
        
    }
    
    /**
     * receives instance of Tarefa and verifies if received percentage is valid, varifies if start date of stage has passed to make sure the , and updates % done
     * if % is 100% calculates date of conclusion
     * @param t
     * @param percentagem 
     * @throws java.text.ParseException 
     */
    public void atualizaConclusaoTarefa(Tarefa t, int percentagem) throws ParseException, Exception{
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        String data = DATE_FORMAT.format(Calendar.getInstance().getTime());
        
        if (percentagem > 0 && percentagem <= 100 && (t.getDataInicio().before(DATE_FORMAT.parse(data)))){
            if (percentagem == 100){
                
                t.setDataDeConclusao(DATE_FORMAT.parse(data));
                t.setPercentagemConclusao(percentagem);
            }
            else {
                t.setPercentagemConclusao(percentagem);
            }
        }
        else {
            throw new Exception ("");
        }
    }
    
    /**
     * validates if dataf comes after datai, if yes sets value of duracaEst 
     * @param datai
     * @param dataf
     * @return 0/-1
     */
    public int validaDatas(String datai, String dataf) {
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            if (DATE_FORMAT.parse(datai).before( DATE_FORMAT.parse(dataf) )) {
                
                //convert input data string to date type and calculate estimated duration in weeks set duracaoEst
                long diffInMillies = Math.abs( DATE_FORMAT.parse(datai).getTime() - DATE_FORMAT.parse(dataf).getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                setDuracaoEst((int) (diff / 7));
                
                return 0;
            } else {
                return -1;
            }
        } catch (ParseException ex) {
            return -1;
        }
    }
    
    /**
     * validates input for type of stage 
     * @param tipo
     * @return 0 / -1
     */
    public int validaTipoInput(String tipo) {
        if ( !(tipo.equals("Design")) && !(tipo.equals("Documentacao")) && !(tipo.equals("Desenvolvimento"))){
            return -1;
        }
        else return 0;
    }
   
    /**
     * sets the Investigador responssible for the stage
     * @param responsavel
     * @throws Exception 
     */
    public void setResponsavel(Investigador responsavel) throws Exception{
        
        //if object received is instance of Docente
        if (responsavel.getCustoBolsa() == 0){
            
            //cast responsavel to object class Docente
            Docente d = (Docente) responsavel;
            
            //if Total stages not reached yet
            if (d.getNumTarefas() < d.totalTarefas){
                this.responsavel = responsavel;
                //increment total stages
                d.setNumTarefas();
            }
            else{
                throw new Exception("Aviso: cada investigador pode ter atribuidas no máximo 2!");
            }
        }
        else {
            
            //if object received is instance of Bolseiro
            Bolseiro b = (Bolseiro) responsavel;
            
            if (b.getBolsaInicio().before(dataInicio) && b.getBolsaFim().after(dataFim) && b.getNumTarefas() < b.totalTarefas){
                this.responsavel = responsavel;
                //increment total stages
                b.setNumTarefas();
            }
            else {
                throw new Exception ("Aviso: duração da tarefa tem de estar contida na duração da Bolsa do investigador!");
            }
        }
    }

    /**
     * abstract method that recieves the value of TaxaEsforco needed to calculate the cost of each stage of a project
     * @return 
     */
    public abstract double getTaxaEsforco();
    
    /**
     * receives the real amont of hours spent by an investigator in this stage
     * @param taxaEsforcoReal 
     */
    public abstract void setEsforcoReal(double taxaEsforcoReal);
    
    /**
     * returns amount of hours spent by an investigator on a stage
     * @return 
     */
    public abstract double getEsforcoReal();
    
    /**
     * print all the information about a stage of a project
     * @return 
     */
    @Override
    public String toString() {
        return "> "+nome;
    }
}
