
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * reprsents a project
 *
 * @author euric
 */
public class Projecto implements Serializable {

    private String nome;
    private String acronimo;
    private Date dataInicio;
    private Date dataFim;
    private int duracaoEst;
    private Investigador ip;
    ArrayList<Investigador> investigadores = new ArrayList<>();
    public static final int maxInvestigadores = 6;
    private int totalInvestigadores;
    ArrayList<Tarefa> tarefas = new ArrayList<>();
    public static final int maxTarefas = 12;
    private int totalTarefas;
    private Date dataDeConclusao;
    private int totalCusto;

    public Projecto() {
    }

    /**
     * create constructor to inicialize all variables
     *
     * @param nome
     * @param acronimo
     * @param dataInicio
     * @param dataFim
     * @param duracaoEst
     * @param ip
     * @param maxInvestigadores
     * @param totalInvestigadores
     * @param maxTarefas
     * @param totalTarefas
     * @param dataDeConclusao
     * @param totalCusto
     */
    public Projecto(String nome, String acronimo, Date dataInicio, Date dataFim, int duracaoEst, Investigador ip, int maxInvestigadores, int totalInvestigadores, int maxTarefas, int totalTarefas, Date dataDeConclusao, int totalCusto) {
        this.nome = nome;
        this.acronimo = acronimo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.duracaoEst = duracaoEst;
        this.ip = ip;
        this.totalInvestigadores = 0;
        this.totalTarefas = 0;
        this.dataDeConclusao = dataDeConclusao = null;
        this.totalCusto = totalCusto;
    }

    public int getTotalInvestigadores() {
        return totalInvestigadores;
    }

    public void setTotalInvestigadores() {
        this.totalInvestigadores++;
    }

    public int getTotalTarefas() {
        return totalTarefas;
    }

    public void setTotalTarefas() {
        this.totalTarefas++;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
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

    public int getDuracaoEst() {
        return duracaoEst;
    }

    public void setDuracaoEst() {
        //if dates are valid calc estimated duration of project
        long diffInMillies = Math.abs(this.dataInicio.getTime() - this.dataFim.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        this.duracaoEst = (int) (diff / 30);
    }

    public Investigador getIP() {
        return ip;
    }

    public Date getDataDeConclusao() {
        return dataDeConclusao;
    }

    public void setDataDeConclusao(Date dataDeConclusao) {
        this.dataDeConclusao = dataDeConclusao;
    }

    public int getTotalCusto() {
        return totalCusto;
    }

    public void setTotalCusto(int totalCusto) {
        this.totalCusto = totalCusto;
    }

    /**
     * distinguishes between instance of Docente and Bolseiro, verifies if this
     * Porejcto is alredy associated with the instance of Bolseiro/Docente
     * received
     *
     * @param newInvestigator
     * @throws Exception
     */
    public void associaInvestigador(Investigador newInvestigator) throws Exception {

        //if newInvestigator is Docente add him to arraylist of investigadores of the project
        if (newInvestigator.getCustoBolsa() == 0) {

            Docente d = (Docente) newInvestigator;

            if (this.totalInvestigadores < maxInvestigadores) {

                if (d.getTotaProjetos() < d.maxProjetos) {

                    investigadores.add(newInvestigator);

                    //associate this project to the arraylist os project of the investigator and the investigatro to this project
                    newInvestigator.projectos.add(this);

                    //increment total investigators in project
                    this.setTotalInvestigadores();
                } else {
                    throw new Exception("Aviso: um Docente pode estar associado no maximo a 3 projetos.");
                }
            } else {
                throw new Exception("Aviso: limite de investigadores atingido!");
            }
        } else {
            //if it's Bolseiro instance and arraylist is empty associate projecto to bolseiro and bolseiro to project
            if (this.totalInvestigadores < maxInvestigadores) {

                if (newInvestigator.projectos.isEmpty()) {

                    investigadores.add(newInvestigator);

                    newInvestigator.projectos.add(this);

                    //increment total investigators in project
                    this.setTotalInvestigadores();
                } else {
                    throw new Exception("Aviso: este bolseiro ja esta associado a outro projecto: " + newInvestigator.projectos.get(0).getNome());
                }
            } else {
                throw new Exception("Aviso: limite de investigadores atingido!");
            }
        }
    }

    /**
     * associates a stage to a project if the arraylist of stages isn't full and
     * duration of stage is within initial expected duration of project
     *
     * @param novaTarefa
     * @throws Exception
     */
    public void carregaTarefas(Tarefa novaTarefa) throws Exception {

        //checks if stage duration is within duration of project
        if (this.totalTarefas < maxTarefas) {
            if (novaTarefa.getDataInicio().after(this.dataInicio) && novaTarefa.getDataFim().before(this.dataFim)) {
                tarefas.add(novaTarefa);
            } else {
                throw new Exception("Aviso: tarefa tem de estar dentro da duracao inicial do projecto.");
            }
        } else {
            throw new Exception("Aviso: limite de tarefas de um projecto atingido (12).");
        }
    }

    /**
     * validaes in input name exists in arraylist os stages of project
     *
     * @param nomeT
     * @return
     */
    public int validaNomeTarefa(String nomeT) {
        int valida = 0;
        for (Tarefa t : tarefas) {
            if (nomeT.equals(t.getNome())) {
                valida = 0;
                break;
            } else {
                valida = -1;
            }
        }
        return valida;
    }

    /**
     * validtes if a created stages is within project duration and if arraylist
     * of stages isn't full
     *
     * @param datai
     * @param dataf
     * @return 0/-1
     */
    public int validaDatasTarefa(String datai, String dataf) {

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

        //checks if stage duration is within duration of project
        if (this.totalTarefas < maxTarefas) {
            try {
                if (DATE_FORMAT.parse(datai).after(this.dataInicio) && DATE_FORMAT.parse(dataf).before(this.dataFim)) {
                    return 0;
                } else {
                    return -1;
                }
            } catch (ParseException ex) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * validates input data length
     *
     * @param inputLen1
     * @param inputLen2
     * @return 0/-1
     */
    public int validaCompInput(int inputLen1, int inputLen2) {
        if (inputLen1 == 0 || inputLen2 == 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * verify if input name exists in arraylist investigadores of objcet Project
     *
     * @param nomeI
     * @return
     */
    public int validaNomeInvestigador(String nomeI) {
        int valida = 1;
        for (Investigador i : investigadores) {
            if (i.getNome().contains(nomeI)) {
                valida = 0;
                break;
            } else {
                valida = -1;
            }
        }
        return valida;
    }

    /**
     * receives instance of Docente class and verifies if Docente is already IP
     * of another project, if not associate it to the current instance of
     * Projecto and add the porject to arraylist of projects of Docente, if not
     * alredy there
     *
     * @param ip
     * @throws java.lang.Exception
     */
    public void setIP(Investigador ip) throws Exception {

        //if docente isn't IP of any project set him as this projects IP and assoiciate this project to his class instance
        if (ip.getCustoBolsa() == 0) {

            Docente d = (Docente) ip;

            if (d.getDocenteIP() == null) {

                //if this project is in arraylits os projecto of ip
                if (d.projectos.contains(this)) {
                    this.ip = ip;

                    //set docente has IP of this project
                    d.setDocenteIP(this);

                } else {
                    if (d.getTotaProjetos() < d.maxProjetos) {
                        //set docente has IP of this project
                        this.ip = ip;

                        //set this project in class Docente, has the project that Docente (d) is head researcher of
                        d.setDocenteIP(this);

                        //add this project to arraylist of project of Docente
                        ip.projectos.add(this);

                        //incremente number of projects a teacher is working on
                        d.setTotaProjetos();
                    } else {
                        throw new Exception("Aviso: um docente nao pode estar associado a mais que 3 projetos!.");
                    }
                }

            } else {
                throw new Exception("Aviso, este docente ja e IP do projecto: " + d.getDocenteIP().getNome());
            }
        } else {
            throw new Exception("Erro: investigador introduzido nao e Docente!");
        }
    }

    /**
     * print arraylist of Investigadores
     *
     * @return
     */
    public String listaInvestigadores() {
        String nomes = "";
        for (Investigador i : investigadores) {
            nomes += "\n  -" + i.getNome() + ";";
        }
        return nomes;
    }

    /**
     * print arrylist of Tarefas
     *
     * @return
     */
    public String listaTarefas() {
        
        String nomes = "";
        for (Tarefa t : tarefas) {
            nomes += "\n  -" + t.getNome() + ";";
        }
        return nomes;
    }

    /**
     * receives instance of Projecto
     * calculate total cost of the project using getEsforcoReal, calculates the
     * amount of hours to be paid to a certain investigator and does this for
     * every scholar of the project adding the cost of each Bolseiro and saving
     * the value in custoTotal custoBolsa is in € per month, in a month there
     * are 20 days of work so the cost of the total hours is given by:
     * (scholarship/20) * taxaEsforcoReal
     *
     * @param p
     * @return
     */
    public void custoTotalProjeto(Projecto p) {
        int custoTotal = 0;
        for (Tarefa t : p.tarefas) {
            double taxaEsforcoReal = t.getEsforcoReal();
            double custoHoras = t.getResponsavel().getCustoBolsa();//get scholarship cost of every investigator working on a stage
            custoHoras = custoHoras / 20;
            custoHoras = custoHoras * (int) taxaEsforcoReal;
            custoTotal += custoHoras;
            p.setTotalCusto(custoTotal);
        }
    }

    /**
     * receives an instance of the class Porjecto, verifies if all stages are
     * complete if not, return -1 if all stages are complete, blocks options to
     * edit a project by seting the date of conclusion of the project
     *
     * @param p
     * @return
     * @throws java.text.ParseException
     */
    public int terminaProjecto(Projecto p) throws ParseException {
        int valida = 0;

        for (Tarefa t : p.tarefas) {
            if (t.getPercentagemConclusao() != 100) {
                valida = -1;
                break;
            } else {
                valida = 1;
            }
        }
        if (valida == 1) {

            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
            String data = DATE_FORMAT.format(Calendar.getInstance().getTime());

            p.setDataDeConclusao(DATE_FORMAT.parse(data));
        }
        return valida;
    }

    /**
     * print all the information about an instance of a Projecto
     *
     * @return
     */
    @Override
    public String toString() {
        return "Projecto: " + nome + ";   -Acronimo: " + acronimo + ";   -Data de inicio: " + dataInicio + ";   -Data de conclusão: " + dataFim + ";   -Duração estimada em meses: " + duracaoEst + ";   -Investigador principal: " + ip.getNome() + ";   -Investigadores envolvidos: " + listaInvestigadores() + "   -Tarefas do projeto: " + listaTarefas() + "   -Cuto total do projecto (€): "+ totalCusto;
    }
}
