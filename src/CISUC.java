import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CISUC extends JFrame{
    
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    //for window that clears invalid inputs in method limpar, and for method validaInputs()
    private int bolseiro, opcao, acao, insere, subseccao, listaTarefasOpcao, ERRO;
    private String nomeProjecto, nomeTarefa;
    
    //buttons, labels, frame, and textfields
    private JList<Tarefa> jtarefas, projectoTarefas;
    private JList<Projecto> listaProjectosCISUC;
    private JTextField input, tarefaT, nomeT, acronimoT, datainicioT, datafimT, tipoTarefaT, duracaoT, perConcT, responsavelT;
    private JLabel nomeL, acronimoL, datainicioL, datafimL, tipoTarefaL, blank, listaP1, listaP2, mensagem;
    
    //main window buttons
    private JButton novoProjeto, associaPessoas, listarP1, listarP2, gerirTarefas, terminaProjecto;
    
    //buttons for auxiliar windows to confirm inputs, clear textfields, ask quastion
    private JButton confirmarProjecto, confirmarTarefa, confirmarTaxa, confirmaAtribuicao, confirmarIP, confirmarInvestigador, limpar, sim, nao;
    private JFrame pagina1, pagina2, erro, pergunta, inputProjecto, inputTarefa, inputTaxa, inputDocente, inputIP, listas;
    
    //buttons and frames for page 5: gere tarefas
    private JButton listarTarefasProjecto, listarTarefas, listaPessoas, criarTarefas, eliminarTarefas, atribuiTarefa, atualizaConc, listarNaoIniciadas, listarComp1, listarComp2;
    private JFrame pagina5, pagina5B, pagina5C, pagina5D;
    
    //create new instances of classes to be able to create new instances
    Projecto newProject = new Projecto();
    Design newDesignTarefa = new Design();
    Desenvolvimento newDesenvolvimentonTarefa = new Desenvolvimento();
    Documentacao newDocumentacaoTarefa = new Documentacao();
    Tarefa newTarefa = new Tarefa() {
        public double getTaxaEsforco() {return 0;}
        public void setEsforcoReal(double taxaEsforcoReal) {}
        public double getEsforcoReal() {return 0;}
    };
    Mestre newMestre = new Mestre();
    Licenciado newLicenciado = new Licenciado();
   
    //project variables
    private String nome, acronimo;
    private Date dataInicio, dataFim;
    
    ArrayList<Investigador> investigadores = new ArrayList<>();
    ArrayList<Projecto> projectos = new ArrayList<>();
    ArrayList<Tarefa> tarefas = new ArrayList<>();
    ArrayList<Tarefa> tarefasNaoIniciadas = new ArrayList<>();
    ArrayList<Tarefa> tarefasForaPrazo = new ArrayList<>();
    ArrayList<Tarefa> tarefasConcluidas = new ArrayList<>();
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {

        CISUC cisuc = new CISUC();
        //define main window
        cisuc.setTitle("CISUC - Pagina Inicial");
        cisuc.setSize(500,300);
        cisuc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * constructor, first loads data, than creates new window
     * @throws Exception 
     */
    public CISUC() throws Exception{
        //main frame
        super();
        
        if (!(carregaobjetos().equals("Successful"))) {
            carregadados();
            guardaObjectos();
        }
        
        //main window nuttons
        novoProjeto = new JButton ("Criar Projecto");
        associaPessoas = new JButton ("Associar Investigadores");
        listaPessoas = new JButton ("Lista de Projectos");
        listarP1 = new JButton ("Projetos concluidos na data prevista");
        listarP2 = new JButton ("Projetos não concluidos na data prevista");
        gerirTarefas = new JButton ("Gerir Tarefas");
        terminaProjecto = new JButton ("Terminar Projecto");
        
        //main window layout
        JPanel paginalInicial = new JPanel();
        paginalInicial.setLayout(new GridLayout(7, 2));
        paginalInicial.add(novoProjeto);
        paginalInicial.add(associaPessoas);
        paginalInicial.add(listaPessoas);
        paginalInicial.add(listarP1);
        paginalInicial.add(listarP2);
        paginalInicial.add(gerirTarefas);
        paginalInicial.add(terminaProjecto);
        
        //set actionlisteners
        novoProjeto.addActionListener(new BotaoCriaProjeto());
        associaPessoas.addActionListener(new BotaoAssociaPessoa());
        listaPessoas.addActionListener(new BotaoListaProjectos());
        listarP1.addActionListener(new BotaoProjectosDentroPrazo());
        listarP2.addActionListener(new BotaoProjectosForaPrazo());
        gerirTarefas.addActionListener(new botaoGerirTarefas());
        terminaProjecto.addActionListener(new botaoTerminaProjecto());
        
        this.add(paginalInicial);
        this.setVisible(true);
    }
    
    /**
     * load data from object files
     * @return 
     */
    public String carregaobjetos() {
        String load;
        File dadosInvestigadoresObjeto = new File("investigadores.obj");
        File dadosProjectosObjeto = new File("projectos.obj");
        File dadosTarefasObjeto = new File("tarefas.obj");
        
        try {
            FileInputStream fisI = new FileInputStream(dadosInvestigadoresObjeto);
            ObjectInputStream oisI = new ObjectInputStream(fisI);
            investigadores = (ArrayList<Investigador>) oisI.readObject();
            oisI.close();
            
            FileInputStream fisP = new FileInputStream(dadosProjectosObjeto);
            ObjectInputStream oisP = new ObjectInputStream(fisP);
            projectos = (ArrayList<Projecto>) oisP.readObject();
            oisP.close();
            
            FileInputStream fisT = new FileInputStream(dadosTarefasObjeto);
            ObjectInputStream oisT = new ObjectInputStream(fisT);
            tarefas = (ArrayList<Tarefa>) oisT.readObject();
            oisT.close();
            load = "Successful";

        } catch (FileNotFoundException ex) {
            System.out.println("Erro: ficheiros nao existentes.");
            load = "Unsuccsseful";
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
            load = "Unsuccsseful";
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto.");
            load = "Unsuccsseful";
        }

        return load;
    }
    /**
     * load data from text file
     * @throws ParseException
     * @throws Exception 
     */
    public void carregadados() throws ParseException, Exception {
        File dadosInput = new File("dadosInput.txt");

        if (dadosInput.exists() && dadosInput.isFile()) {
            try {

                FileReader fr = new FileReader(dadosInput);
                BufferedReader br = new BufferedReader(fr);
                String line;

                while ((line = br.readLine()) != null) {


                    String[] split = line.split(";");
                    if (split[0].equals("INVESTIGADOR")) {
                        if (split[1].equals("DOCENTE")) {
                            Docente newDocente = new Docente();
                            newDocente.setNome(split[2]);
                            newDocente.setMail(split[3]);
                            newDocente.setNumMecanografico(Integer.parseInt(split[4]));
                            newDocente.setAreaInvestigacao(split[5]);
                            //add to list of investigators
                            investigadores.add(newDocente);
                        } 
                        else if (split[1].equals("BOLSEIRO")) {

                            if (split[2].equals("LICENCIADO")) {

                                Licenciado newLicenciado = new Licenciado();
                                newLicenciado.setNome(split[3]);
                                newLicenciado.setMail(split[4]);
                                newLicenciado.setBolsaInicio(DATE_FORMAT.parse(split[5]));
                                newLicenciado.setBolsaFim(DATE_FORMAT.parse(split[6]));
                                for (Investigador i : investigadores) {
                                    if (i.getNome().equals(split[7])) {
                                        try{
                                            newLicenciado.setOrientador(i);
                                        } catch (Exception e){}
                                    }
                                }
                                //add new licencidado to arraylist of investigators
                                investigadores.add(newLicenciado);

                            } else if (split[2].equals("MESTRE")) {

                                Mestre newMestre = new Mestre();
                                newMestre.setNome(split[3]);
                                newMestre.setMail(split[4]);
                                newMestre.setBolsaInicio(DATE_FORMAT.parse(split[5]));
                                newMestre.setBolsaFim(DATE_FORMAT.parse(split[6]));
                                for (Investigador i : investigadores) {
                                    if (i.getNome().equals(split[7])) {
                                        try{
                                            newMestre.setOrientador(i);
                                        } catch (Exception e){}
                                    }
                                }
                                //add new master to arraylist of investigators
                                investigadores.add(newMestre);

                            } else if (split[2].equals("DOUTORADO")) {

                                Doutorado newDoutorado = new Doutorado();
                                newDoutorado.setNome(split[3]);
                                newDoutorado.setMail(split[4]);
                                newDoutorado.setBolsaInicio(DATE_FORMAT.parse(split[5]));
                                newDoutorado.setBolsaFim(DATE_FORMAT.parse(split[6]));
                                //add to arraylist of investigators
                                investigadores.add(newDoutorado);
                            }
                        }
                    } 
                    else if (split[0].equals("TAREFA")) {
                        int valida;
                        if (split[1].equals("DESIGN")) {
                            Design newDesignStage = new Design();
                            if( !(split[2].equals(""))) newDesignStage.setNome(split[2]);
                            
                            //calls method to validate dates just to set de estimated duration
                            if (newDesignStage.validaDatas(split[3], split[4]) == 0){
                                newDesignStage.setDataInicio(DATE_FORMAT.parse(split[3]));
                                newDesignStage.setDataFim(DATE_FORMAT.parse(split[4]));
                            }
                            newDesignStage.setPercentagemConclusao(Integer.parseInt(split[5]));
                            //associarte stage to investigator if is scholarship lenght of stage is within his scholarship
                            for (Investigador i : investigadores) {
                                if (i.getNome().equals(split[6])) {
                                    try{
                                        newDesignStage.setResponsavel(i);
                                    } catch (Exception e){
                                        System.out.println("Erra a atribuir tarefa de design!\n"+e.getMessage());
                                    }
                                }
                            }
                            newDesignStage.setEsforcoReal(Integer.parseInt(split[7]));
                            //add to arraylist of stages
                            tarefas.add(newDesignStage);

                        } else if (split[1].equals("DESENVOLVIMENTO")) {

                            Desenvolvimento newDesenvolvimentoStage = new Desenvolvimento();
                            if( !(split[2].equals(""))) newDesenvolvimentoStage.setNome(split[2]);
                            
                            //calls method to validate dates just to set de estimated duration
                            if (newDesenvolvimentoStage.validaDatas(split[3], split[4]) == 0){
                                newDesenvolvimentoStage.setDataInicio(DATE_FORMAT.parse(split[3]));
                                newDesenvolvimentoStage.setDataFim(DATE_FORMAT.parse(split[4]));
                            }
                            newDesenvolvimentoStage.setPercentagemConclusao(Integer.parseInt(split[5]));
                            //associarte stage to investigator if is scholarship lenght of stage is within his scholarship
                            for (Investigador i : investigadores) {
                                if (i.getNome().equals(split[6])) {
                                    try{
                                        newDesenvolvimentoStage.setResponsavel(i);
                                    } catch (Exception e){
                                        System.out.println("Err atribuir tarefa de desenvolvimento!\n"+e.getMessage());
                                    }
                                }
                            }
                            newDesenvolvimentoStage.setEsforcoReal(Integer.parseInt(split[7]));
                            //add to arraylist of stages
                            tarefas.add(newDesenvolvimentoStage);

                        } else if (split[1].equals("DOCUMENTACAO")) {

                            Documentacao newDocumentacaoStage = new Documentacao();
                            if( !(split[2].equals(""))) newDocumentacaoStage.setNome(split[2]);
                            
                            //calls method to validate dates just to set de estimated duration
                            if (newDocumentacaoStage.validaDatas(split[3], split[4]) == 0){
                                newDocumentacaoStage.setDataInicio(DATE_FORMAT.parse(split[3]));
                                newDocumentacaoStage.setDataFim(DATE_FORMAT.parse(split[4]));
                            }
                            newDocumentacaoStage.setPercentagemConclusao(Integer.parseInt(split[5]));
                            //associarte stage to investigator if is scholarship lenght of stage is within his scholarship
                            for (Investigador i : investigadores) {
                                if (i.getNome().equals(split[6])) {
                                    try{
                                        newDocumentacaoStage.setResponsavel(i);
                                    } catch (Exception e){
                                        System.out.println("Err atribuir tarefa de documentação!\n"+e.getMessage());
                                    }
                                }
                            }
                            newDocumentacaoStage.setEsforcoReal(Integer.parseInt(split[7]));
                            //add to arraylist of stages
                            tarefas.add(newDocumentacaoStage);
                        }
                    } 
                    else if (split[0].equals("PROJECTO")) {

                        Projecto newProject = new Projecto();
                        newProject.setNome(split[1]);
                        newProject.setAcronimo(split[2]);
                        newProject.setDataInicio(DATE_FORMAT.parse(split[3]));
                        newProject.setDataFim(DATE_FORMAT.parse(split[4]));
                        newProject.setDuracaoEst();
                        
                        for (Investigador i : investigadores) {
                            //set IP
                            if (i.getNome().equals(split[5])) {
                                newProject.setIP(i);
                            }
                            //associate investigators
                            if (i.getNome().equals(split[6]) || i.getNome().equals(split[7]) || i.getNome().equals(split[8])) {
                                try{
                                    newProject.associaInvestigador(i);
                                } catch (Exception e) {}
                            }
                        }
                       
                        for (Tarefa t : tarefas) {
                            //associate stages to project
                            if (t.getNome().equals(split[9]) || t.getNome().equals(split[10]) || t.getNome().equals(split[11]) || t.getNome().equals(split[12])) {
                                try{
                                    newProject.carregaTarefas(t);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        projectos.add(newProject);
                    }
                }
                br.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Erro a abrir ficheiro de texto.");
            } catch (IOException ex) {
                System.out.println("Erro a ler ficheiro de texto.");
            }
        } else {
            System.out.println("Ficheiro não existe.");
        }
    }
    public void guardaObjectos() {

        File investigadoresObjectos = new File("investigadores.obj");
        try {
            
            FileOutputStream fos = new FileOutputStream(investigadoresObjectos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(investigadores);
            oos.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }

        File tarefasObjectos = new File("tarefas.obj");
        try {
            
            FileOutputStream fos = new FileOutputStream(tarefasObjectos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tarefas);
            oos.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }

        File projectosObjectos = new File("projectos.obj");
        try {
            
            FileOutputStream fos = new FileOutputStream(projectosObjectos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(projectos);
            oos.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }
    }
    
    private class BotaoCriaProjeto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                criaProjecto();
            } catch (Exception ex) {
                Logger.getLogger(CISUC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private class BotaoAssociaPessoa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 4;
            pedeProjecto();
        }  
    }
    private class BotaoListaProjectos implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            subseccao = 10;
            listarProjectosCISUC();
        }
    }
    private class BotaoProjectosDentroPrazo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame pagina3 = new JFrame();
            pagina3.setTitle("Projetos concluidos na data prevista - Pagina 3");
            pagina3.setSize(800, 100);
            pagina3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pagina3.setLayout(new BorderLayout());
            listaP1 = new JLabel(listarProjectosConcluidosNaData());
            pagina3.add(listaP1, BorderLayout.NORTH);
            pagina3.setVisible(true);
        }
    }
    private class BotaoProjectosForaPrazo implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame pagina4 = new JFrame();
            pagina4.setTitle("Projetos nao concluidos na data prevista - Pagina 4");
            pagina4.setSize(800, 100);
            pagina4.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pagina4.setLayout(new BorderLayout());
            listaP2 = new JLabel(listarProjectosNaoConcluidosNaData());
            pagina4.add(listaP2, BorderLayout.NORTH);
            pagina4.setVisible(true);
        }
    }
    private class botaoGerirTarefas implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pagina5 = new JFrame();
            
            listarTarefas = new JButton("Listar todas as tarefas");
            criarTarefas = new JButton("Criar tarefas");
            eliminarTarefas = new JButton("Eliminar tarefas");
            atribuiTarefa = new JButton("Atribuir tarefas");
            atualizaConc = new JButton("Atualizar conclusao");
            listarNaoIniciadas = new JButton("Listar tarefas nao iniciadas");
            listarComp1 = new JButton("Listar tarefas completas fora do prazo");
            listarComp2 = new JButton("Listas tarefas concluidas");
            listarTarefasProjecto = new JButton("Listar tarefas de um projecto");
            
            pagina5.setTitle("Gerir tarefas - página 5");
            pagina5.setSize(400, 400);
            pagina5.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pagina5.setLayout(new GridLayout(9, 1));
            pagina5.add(listarTarefas);
            pagina5.add(criarTarefas);
            pagina5.add(eliminarTarefas);
            pagina5.add(atribuiTarefa);
            pagina5.add(atualizaConc);
            pagina5.add(listarNaoIniciadas);
            pagina5.add(listarComp1);
            pagina5.add(listarComp2);
            pagina5.add(listarTarefasProjecto);

            listarTarefas.addActionListener(new BotaoImprimeTodasAsTarefas());
            criarTarefas.addActionListener(new BotaoCriaNovaTarefa());
            eliminarTarefas.addActionListener(new BotaoEliminarTarefa());
            atribuiTarefa.addActionListener(new BotaoAtribuirTarefa());
            atualizaConc.addActionListener(new BotaoAtualizaPercent());
            listarNaoIniciadas.addActionListener(new BotaoTarefasNaoIniciadas());
            listarComp1.addActionListener(new BotaoTarefasForaPrazo());
            listarComp2.addActionListener(new BotaoTarefasConcluidas());
            listarTarefasProjecto.addActionListener(new BotaoTarefasDeUmProjecto());
            
            pagina5.setVisible(true);
        }
    }
    private class botaoTerminaProjecto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 5;
            pedeProjecto();
        } 
    }
    
    //botoes do opcao gerir tarefas
    private class BotaoImprimeTodasAsTarefas implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            subseccao = 9;
            gereTarefas();
        }
    }
    private class BotaoCriaNovaTarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 1;
        }
    }
    private class BotaoEliminarTarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 2;
        }
    }
    private class BotaoAtribuirTarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pedeProjecto();
            subseccao = 3;
        }
    }
    private class BotaoAtualizaPercent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 4;
        }
    }
    private class BotaoTarefasNaoIniciadas implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 5;
        }
    }
    private class BotaoTarefasForaPrazo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 6;
        }
    }
    private class BotaoTarefasConcluidas implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 7;
        }
    }
    private class BotaoTarefasDeUmProjecto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insere = 2;
            pedeProjecto();
            subseccao = 8;
        }
    }
    
    /**
     * creats new project with no investigators and at least 1 stage
     * @throws ParseException
     * @throws Exception 
     */
    public void criaProjecto() throws ParseException, Exception {
        
        //labels setings
        nomeL = new JLabel("Nome: ");
        acronimoL = new JLabel("Acronimo: ");
        datainicioL = new JLabel("Data de inicio (dd/MM/yyyy): ");
        datafimL = new JLabel("Data de finalização (dd/MM/yyyy): ");
        confirmarProjecto = new JButton("Confirmar dados");
        blank = new JLabel("");
        
        //texte fields seting
        nomeT = new JTextField(10);
        acronimoT = new JTextField(5);
        datainicioT = new JTextField(5);
        datafimT = new JTextField(10);
        
        //wiondow seting
        pagina1 = new JFrame();
        pagina1.setTitle("Novo projecto");
        pagina1.setSize(400, 200);
        pagina1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagina1.setLayout(new GridLayout(5,2));
        pagina1.add(nomeL);
        pagina1.add(nomeT);
        pagina1.add(acronimoL);
        pagina1.add(acronimoT);
        pagina1.add(datainicioL);
        pagina1.add(datainicioT);
        pagina1.add(datafimL);
        pagina1.add(datafimT);
        pagina1.add(blank);
        pagina1.add(confirmarProjecto);
        confirmarProjecto.addActionListener(new BotaoValidaInputs());
        pagina1.setVisible(true);
        //for method validInputs
        acao = 1;
        //for ading stage in the new created project 
        insere = 1;
    }
    
    private void selecionarIP() {
        
        inputIP= new JFrame();
        inputIP.setTitle("Selecionar um Investigador principal");
        inputIP.setSize(500, 100);
        inputIP.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        confirmarIP = new JButton("Confirmar");
        //to be used in method listaInvestigadores(), to search possible teacher to be associated to the new project
        insere = 3;
        listaP1 = new JLabel(listaInvestigadores());
        input = new JTextField(10);

        inputIP.setLayout(new BorderLayout());
        inputIP.add(listaP1, BorderLayout.NORTH);
        inputIP.add(input, BorderLayout.CENTER);
        inputIP.add(confirmarIP, BorderLayout.SOUTH);
        confirmarIP.addActionListener(new ConfirmaIP());
        inputIP.setVisible(true);
    }
    private class ConfirmaIP implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!(listaInvestigadores().contains(input.getText())) || input.getText().length() == 0) {
                mensagemErro();
                //for method limpar
                opcao = 3;
            } 
            else {
                for (Investigador i: investigadores){
                    if (i.getNome().contains(input.getText())){
                        try {
                            newProject.setIP(i);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
                inputIP.dispose();
                
                //after seting head researher call method to create stages, skiping the (for) cicle that searches for th input project, because this is a new created project
                insere = 1;
                subseccao = 1;
                gereTarefas();
            }
        }
    }
    
    private void pedeProjecto() {
        
        inputProjecto = new JFrame();
        inputProjecto.setTitle("Selecionar um projeto");
        inputProjecto.setSize(500, 100);
        inputProjecto.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        confirmarProjecto = new JButton("Confirmar");
        listaP1 = new JLabel(listaProjectos());
        input = new JTextField(10);

        inputProjecto.setLayout(new BorderLayout());
        inputProjecto.add(listaP1, BorderLayout.NORTH);
        inputProjecto.add(input, BorderLayout.CENTER);
        inputProjecto.add(confirmarProjecto, BorderLayout.SOUTH);
        confirmarProjecto.addActionListener(new ConfirmaProjeto());
        inputProjecto.setVisible(true);
    }
    private class ConfirmaProjeto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!(listaProjectos().contains(input.getText())) || input.getText().length() == 0) {
                mensagemErro();
                //for method limpar
                opcao = 3;
            } 
            else if (insere < 4) {
                nomeProjecto = input.getText();
                inputProjecto.dispose();
                //after an option selected it will call the method do ask for project input name if valid call gereTarefas()
                gereTarefas();
            }
            else if (insere == 4) {
                nomeProjecto = input.getText();
                pedeInvestigador();
            }
            else if (insere == 5) {
                inputProjecto.dispose();
                
                for (Projecto p: projectos){
                    if (p.getNome().contains(input.getText())){
                        try {
                            if (p.terminaProjecto(p) == 1){
                                //if project is finished set total cost
                                p.custoTotalProjeto(p);
                                guardaObjectos();
                                break;
                            }
                            else {
                                opcao = 3;
                                ERRO = 1;
                                mensagem = new JLabel("Operação Inválida!");
                                
                            }
                        } 
                        catch (ParseException ex) {}
                    }
                }
            }
        }
    }
    
    public void pedeInvestigador(){
        
        inputProjecto.dispose();
        
        //obtain input project
        for (Projecto p : projectos) {
            if (p.getNome().contentEquals(nomeProjecto)) {
                newProject = p;
                break;
            }
        }
        
        pagina2 = new JFrame();
        pagina2.setTitle("Associar Investigador - Pagina 2");
        pagina2.setSize(800, 100);
        pagina2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagina2.setLayout(new GridLayout(3, 1));

        confirmarInvestigador = new JButton("Confirmar");
        listaP1 = new JLabel(listaInvestigadores());
        input = new JTextField(10);

        pagina2.setLayout(new BorderLayout());
        pagina2.add(listaP1, BorderLayout.NORTH);
        pagina2.add(input, BorderLayout.CENTER);
        pagina2.add(confirmarInvestigador, BorderLayout.SOUTH);

        confirmarInvestigador.addActionListener(new ConfirmaPessoa());
        pagina2.setVisible(true);
    }
    private class ConfirmaPessoa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pagina2.dispose();
            for (Investigador i : investigadores) {
                if (i.getNome().contains(input.getText())) {
                    {
                        try {
                            newProject.associaInvestigador(i);
                            
                            //if investigator is Mester or Licenciado type object
                            if (i.getCustoBolsa() == 800){
                                newMestre = (Mestre) i;
                                if (newMestre.getOrientador() == null){
                                    bolseiro = 2;
                                    defineOrientador();
                                }
                            }
                            else if (i.getCustoBolsa() == 500){
                                newLicenciado = (Licenciado) i;
                                if (newLicenciado.getOrientador() == null){
                                    bolseiro = 1;
                                    defineOrientador();
                                }
                            }
                            
                        } catch (Exception ex) {
                            opcao = 3;
                            ERRO = 1;
                            mensagem = new JLabel(ex.getMessage());   
                        }
                        break;
                    }
                }
                else if ( !(i.getNome().contains(input.getText()))){
                    opcao = 3;
                    mensagemErro();
                }
            }
        }
    }
    
    private void defineOrientador() {
        
        inputDocente = new JFrame();
        inputDocente.setTitle("Selecionar Docente");
        inputDocente.setSize(800, 100);
        inputDocente.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputDocente.setLayout(new GridLayout(3, 1));

        confirmarInvestigador = new JButton("Confirmar");
        insere = -1;
        listaP1 = new JLabel(listaInvestigadores());
        input = new JTextField(10);

        inputDocente.setLayout(new BorderLayout());
        inputDocente.add(listaP1, BorderLayout.NORTH);
        inputDocente.add(input, BorderLayout.CENTER);
        inputDocente.add(confirmarInvestigador, BorderLayout.SOUTH);

        confirmarInvestigador.addActionListener(new ConfirmaDocente());
        inputDocente.setVisible(true);
    }
    private class ConfirmaDocente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           inputDocente.setVisible(true);
           
           for (Investigador i: investigadores){
               if (i.getNome().contains(input.getText())){
                   if (bolseiro == 1){
                       try {
                           newLicenciado.setOrientador(i);
                       } catch (Exception ex) {
                           opcao = 3;
                           ERRO = 1;
                           mensagem = new JLabel(ex.getMessage());
                       }
                   }
                   else if (bolseiro == 2){
                       try {
                           newMestre.setOrientador(i);
                       } catch (Exception ex) {
                           opcao = 3;
                           ERRO = 1;
                           mensagem = new JLabel(ex.getMessage());
                       }
                   }
               }
           }
        }
    }
    
    
    public void listarProjectosCISUC() {

        //set frame
        listas = new JFrame();
        listas.setTitle("Lista de Projectos");
        listas.setSize(1000, 300);
        listas.setLayout(new GridLayout(1, 2));

        listaProjectosCISUC = new JList(projectos.toArray());
        listaProjectosCISUC.addListSelectionListener(new valueChanged());
        listas.add(new JScrollPane(listaProjectosCISUC));

        listas.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        listas.setVisible(true);
    }
    
    public void listarTarefasTodas() {

        //set atributes
        datainicioT = new JTextField();
        datafimT = new JTextField();
        duracaoT = new JTextField();
        perConcT = new JTextField();
        responsavelT = new JTextField();
        datafimT.setEditable(false);
        duracaoT.setEditable(false);
        perConcT.setEditable(false);
        responsavelT.setEditable(false);
        datainicioT.setEditable(false);
        
        //set frame
        listas = new JFrame();
        listas.setTitle("Lista de Tarefas - página 5.A");
        listas.setSize(800, 300);
        listas.setLayout(new GridLayout(1, 2));
        
        //if action selected is list satages os a certain project, get the instance of object Projecto corresponding to the input.getText()
        if (subseccao > 4 && subseccao < 9){
            for (Projecto p : projectos) {
                if (p.getNome().contains(input.getText())) {
                    newProject = p;
                    
                    //clear arraylists t reuse
                    tarefasNaoIniciadas.clear();
                    tarefasForaPrazo.clear();
                    tarefasConcluidas.clear();
                    
                    //search for specific type of stage
                    for (Tarefa t: newProject.tarefas){
                        if (t.getPercentagemConclusao() == 0){
                            tarefasNaoIniciadas.add(t);
                        }
                        if (t.getPercentagemConclusao() == 100 && t.getDataDeConclusao().after(t.getDataFim())){
                            tarefasConcluidas.add(t);
                            tarefasForaPrazo.add(t);
                        }
                        else if (t.getPercentagemConclusao() == 100) {
                            tarefasConcluidas.add(t);
                        }
                    }
                    break;
                }
            }
            switch (subseccao) {
                case 5:
                    projectoTarefas = new JList(tarefasNaoIniciadas.toArray());
                    break;
                case 6:
                    projectoTarefas = new JList(tarefasForaPrazo.toArray());
                    break;
                case 7:
                    projectoTarefas = new JList(tarefasConcluidas.toArray());
                    break;
                case 8:
                    projectoTarefas = new JList(newProject.tarefas.toArray());
                    break;
            }
            projectoTarefas.addListSelectionListener(new valueChanged());
            listas.add(new JScrollPane(projectoTarefas));
        }
        else if (subseccao == 9){
            jtarefas = new JList(tarefas.toArray());
            jtarefas.addListSelectionListener(new valueChanged());
            listas.add(new JScrollPane(jtarefas));
        }
        
        //set panel
        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new GridLayout(5, 2));
        painelInfo.add(new JLabel("Data inicio:"));
        painelInfo.add(datainicioT);
        painelInfo.add(new JLabel("Data fim:"));
        painelInfo.add(datafimT);
        painelInfo.add(new JLabel("Duracao estimada (semanas):"));
        painelInfo.add(duracaoT);
        painelInfo.add(new JLabel("Percentagem conc.:"));
        painelInfo.add(perConcT);
        painelInfo.add(new JLabel("Nome investigador:"));
        painelInfo.add(responsavelT);
        
        listas.add(painelInfo);
        listas.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        listas.setVisible(true);
    }
    private class valueChanged implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                
                if (subseccao < 9) {
                    Tarefa tarefa = projectoTarefas.getSelectedValue();
                    datainicioT.setText(DATE_FORMAT.format(tarefa.getDataInicio()));
                    datafimT.setText(DATE_FORMAT.format(tarefa.getDataFim()));
                    duracaoT.setText(String.valueOf(tarefa.getDuracaoEst()));
                    perConcT.setText(String.valueOf(tarefa.getPercentagemConclusao()));
                    if (tarefa.getResponsavel() != null) {
                        responsavelT.setText(tarefa.getResponsavel().getNome());
                    } else {
                        responsavelT.setText("Sem investigador atribuido");
                    }
                } 
                else if (subseccao == 9) {
                    Tarefa tarefa = jtarefas.getSelectedValue();
                    datainicioT.setText(DATE_FORMAT.format(tarefa.getDataInicio()));
                    datafimT.setText(DATE_FORMAT.format(tarefa.getDataFim()));
                    duracaoT.setText(String.valueOf(tarefa.getDuracaoEst()));
                    perConcT.setText(String.valueOf(tarefa.getPercentagemConclusao()));
                    if (tarefa.getResponsavel() != null) {
                        responsavelT.setText(tarefa.getResponsavel().getNome());
                    } else {
                        responsavelT.setText("Sem investigador atribuido");
                    }
                }
                else if (subseccao == 10){
                    Projecto p = listaProjectosCISUC.getSelectedValue();
                }
            }
        }
    }
    
    private void criarTarefa() {
        //creates a stage
        //labels and button setings
        tipoTarefaL = new JLabel("Tipo de tarefa: ");
        nomeL = new JLabel("Nome: ");
        datainicioL = new JLabel("Data de inicio (dd/MM/yyyy): ");
        datafimL = new JLabel("Data de finalização (dd/MM/yyyy): ");
        blank = new JLabel("");
        confirmarTarefa = new JButton("Confirmar dados");

        //text fields seting
        tipoTarefaT = new JTextField(10);
        nomeT = new JTextField(10);
        datainicioT = new JTextField(5);
        datafimT = new JTextField(10);

        //wiondow to seting
        pagina5B = new JFrame();
        pagina5B.setTitle("Criar Tarefa - página 5.B");
        pagina5B.setSize(400, 200);
        pagina5B.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagina5B.setLayout(new GridLayout(5, 2));
        pagina5B.add(tipoTarefaL);
        pagina5B.add(tipoTarefaT);
        pagina5B.add(nomeL);
        pagina5B.add(nomeT);
        pagina5B.add(datainicioL);
        pagina5B.add(datainicioT);
        pagina5B.add(datafimL);
        pagina5B.add(datafimT);
        pagina5B.add(blank);
        pagina5B.add(confirmarTarefa);
        confirmarTarefa.addActionListener(new BotaoValidaInputs());
        pagina5B.setVisible(true);
        //for method validaInputs()
        acao = 2;
    }
    
    private void eliminaTarefa() {
        //labels and button setings
        confirmarTarefa = new JButton("Confirmar");
        listaTarefasOpcao = 2;
        nomeL = new JLabel(listaTarefas());
        input = new JTextField(10);
        
        //wiondow to seting
        pagina5C = new JFrame();
        pagina5C.setTitle("Eliminar Tarefa - página 5.C");
        pagina5C.setSize(600, 100);
        pagina5C.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagina5C.setLayout(new BorderLayout());
        pagina5C.add(nomeL, BorderLayout.NORTH);
        pagina5C.add(input, BorderLayout.CENTER);
        pagina5C.add(confirmarTarefa, BorderLayout.SOUTH);
        confirmarTarefa.addActionListener(new BotaoValidaInputs());
        pagina5C.setVisible(true);
        //for method validaInputs()
        acao = 3;
    }
    
    private void atribuiTarefas() {
        
        //obtain input project
        for (Projecto p : projectos) {
            if (p.getNome().contentEquals(nomeProjecto)) {
                newProject = p;
            }
        }
        
        //open window to associate investigator in method validaInputs
        pagina5D = new JFrame();
        pagina5D.setTitle("Selecionar Investigador e Tarefa");
        pagina5D.setSize(900, 200);
        pagina5D.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagina5D.setLayout(new GridLayout(5,1));
        
        confirmaAtribuicao = new JButton("Confirmar");
        //to get the available investigators to associate to a project
        insere = 2;
        listaP1 = new JLabel(listaInvestigadores());
        listaTarefasOpcao = 3;
        listaP2 = new JLabel(listaTarefas());
        nomeT = new JTextField(10);
        tarefaT = new JTextField(10);

        pagina5D.setLayout(new GridLayout(5,1));
        pagina5D.add(listaP1);
        pagina5D.add(nomeT);
        pagina5D.add(listaP2);
        pagina5D.add(tarefaT);
        pagina5D.add(confirmaAtribuicao);
        confirmaAtribuicao.addActionListener(new BotaoValidaInputs());
        pagina5D.setVisible(true);
        //to use in metho validaInputs
        acao = 4;
    }
    
    private void atualizaPercentagem() {
        
        inputTarefa = new JFrame();
        inputTarefa.setTitle("Selecionar uma tarefa");
        inputTarefa.setSize(500, 100);
        inputTarefa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        confirmarTarefa = new JButton("Confirmar");
        listaTarefasOpcao = 1;
        listaP1 = new JLabel(listaTarefas());
        input = new JTextField(10);
        
        inputTarefa.setLayout(new BorderLayout());
        inputTarefa.add(listaP1, BorderLayout.NORTH);
        
        if ( !(listaTarefas().equals("Não há tarefas por concluir com investigador atribuido!")) ){
            inputTarefa.add(input, BorderLayout.CENTER);
            inputTarefa.add(confirmarTarefa, BorderLayout.SOUTH);
            confirmarTarefa.addActionListener(new ConfirmaTarefa());
        }
        
        inputTarefa.setVisible(true);
    }
    private class ConfirmaTarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            if ( !(listaTarefas().contains(input.getText())) || input.getText().length() == 0) {
                mensagemErro();
                //for method limpar
                opcao = 3;
            } 
            else {
                inputTarefa.dispose();
                inputTarefa = new JFrame();
                inputTarefa.setTitle("Introduzir percentagem");
                inputTarefa.setSize(310, 100);
                inputTarefa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                for (Tarefa t : newProject.tarefas) {
                    if (t.getNome().contains(input.getText())){
                        newTarefa = t;
                        listaP1 = new JLabel("Percentagem atual: "+String.valueOf(t.getPercentagemConclusao()));
                    }
                }
                input = new JTextField(10);
                confirmarTarefa = new JButton("Confirmar");

                inputTarefa.setLayout(new GridLayout(3,1));
                inputTarefa.add(listaP1, BorderLayout.NORTH);
                inputTarefa.add(input, BorderLayout.CENTER);
                inputTarefa.add(confirmarTarefa, BorderLayout.SOUTH);
                confirmarTarefa.addActionListener(new ConfirmaPercentagem());
                inputTarefa.setVisible(true);
            }
        }
    }
    private class ConfirmaPercentagem implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                inputTarefa.dispose();
                newTarefa.atualizaConclusaoTarefa(newTarefa, Integer.parseInt(input.getText()));
                if (Integer.parseInt(input.getText()) == 100){
                    pedeEsforcoReal();
                }
                guardaObjectos();
            } 
            catch (Exception ex) {
                opcao = 3;
                mensagemErro();
            }
        }
    }
    
    private void pedeEsforcoReal() {
        
        inputTaxa = new JFrame();
        inputTaxa.setTitle("Introduza Taxa de esfoço em horas/dia");
        inputTaxa.setSize(500, 100);
        inputTaxa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        confirmarTaxa = new JButton("Confirmar");
        listaP1 = new JLabel("Taxa de esforço esperada: "+String.valueOf(newTarefa.getTaxaEsforco()));
        input = new JTextField(10);

        inputTaxa.setLayout(new GridLayout(3,1));
        inputTaxa.add(listaP1);
        inputTaxa.add(input);
        inputTaxa.add(confirmarTaxa);
        confirmarTaxa.addActionListener(new ConfirmaTaxa());
        inputTaxa.setVisible(true);
    }
    private class ConfirmaTaxa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Double.parseDouble(input.getText()) <= newTarefa.getTaxaEsforco()){
                inputTaxa.dispose();
                newTarefa.setEsforcoReal(Double.parseDouble(input.getText()));
            }
            else {
                opcao = 3;
                mensagemErro();
            }
        }
    }
    
    private class ChamaGereTarefas implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pergunta.dispose();
            gereTarefas();
        }
    }
    
    /**
     * manages stage operations
     * @param subseccao 
     */
    private void gereTarefas() {
        if (subseccao == 1) {
            criarTarefa();
        } 
        else if (subseccao == 2) {
            eliminaTarefa();
        } 
        else if (subseccao == 3) {
            atribuiTarefas();
        } 
        else if (subseccao == 4) {
            atualizaPercentagem();
        } 
        else if (subseccao >=5) {
            listarTarefasTodas();
        }
    }
    
    private class BotaoValidaInputs implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                validaInputs();
            } catch (ParseException ex) {}
        }
    }
    /**
     * has 4 diferent functions:
     * if acao = 1:  receives the input data neede to create a new project
     * if acao = 2: receives the input data needed to create a new stage inside a project
     * if acao = 3: removes a stage from a project
     * if acao = 4: associates an investigator to a stage of a project
     * in every case, firts validates if input data is valid, if not, the action is not completed
     * @throws java.text.ParseException
     */
    public void validaInputs() throws ParseException {

        //when creating a new project, (acao = 1) is for input of project variables
        if (acao == 1) {
            
            //call method validaDatas from Tarefa class to validate dates
            int valida = newDesignTarefa.validaDatas(datainicioT.getText(), datafimT.getText());
            int validaComp = newProject.validaCompInput(nomeT.getText().length(), acronimoT.getText().length());
            
            if (validaComp == -1 || valida == -1) {
                mensagemErro();
                //for method limpar
                opcao = 1;
            } 
            else {
                //if valid input save data
                newProject.setNome(nomeT.getText());
                newProject.setAcronimo(acronimoT.getText());
                newProject.setDataInicio(DATE_FORMAT.parse(datainicioT.getText()));
                newProject.setDataFim(DATE_FORMAT.parse(datafimT.getText()));
                newProject.setDuracaoEst();//only after input dates are valid
                projectos.add(newProject);
                pagina1.dispose();
                guardaObjectos();
                
                //call method to add head researcher
                selecionarIP();
            }
        } 
        
        //(acao > 1) is to create or remove or, asociate an investigator to a stage
        else if (acao > 1) {

            //if method was called through window gerir tarefas -- option atribuir tarefa
            if (insere == 2) {
                for (Projecto p : projectos) {
                    if (p.getNome().contentEquals(nomeProjecto)) {
                        newProject = p;
                    }
                }
            }
            
            switch (acao) {
                //to create new stages and associate them to a project
                case 2:
                    //call metho to validate Dates
                    int valida = newDesignTarefa.validaDatas(datainicioT.getText(), datafimT.getText());
                    int validaDuracao = newProject.validaDatasTarefa(datainicioT.getText(), datafimT.getText());
                    int validaComp = newProject.validaCompInput(nomeT.getText().length(), 1);
                    int validTipo = newDesignTarefa.validaTipoInput(tipoTarefaT.getText());
                    
                    if (valida == -1 || validaDuracao == -1 || validaComp == -1 || validTipo == -1) {
                        mensagemErro();
                        //for method limpar
                        opcao = 2;
                    } 
                    else {
                        switch (tipoTarefaT.getText()) {
                            case "Design": {
                                //if valid input save data
                                newDesignTarefa.setNome(nomeT.getText());
                                newDesignTarefa.setDataInicio(DATE_FORMAT.parse(datainicioT.getText()));
                                newDesignTarefa.setDataFim(DATE_FORMAT.parse(datafimT.getText()));
                                newDesignTarefa.setPercentagemConclusao(0);
                                tarefas.add(newDesignTarefa);
                                newProject.tarefas.add(newDesignTarefa);
                                break;
                            }
                            case "Desenvolvimento": {
                                //if valid input save data
                                newDesenvolvimentonTarefa.setNome(nomeT.getText());
                                newDesenvolvimentonTarefa.setDataInicio(dataInicio);
                                newDesenvolvimentonTarefa.setDataFim(dataFim);
                                newDesenvolvimentonTarefa.setDataInicio(DATE_FORMAT.parse(datainicioT.getText()));
                                newDesenvolvimentonTarefa.setDataFim(DATE_FORMAT.parse(datafimT.getText()));
                                newDesenvolvimentonTarefa.setPercentagemConclusao(0);
                                tarefas.add(newDesenvolvimentonTarefa);
                                newProject.tarefas.add(newDesenvolvimentonTarefa);
                                break;
                            }
                            case "Documentacao": {
                                //if valid input save data
                                newDocumentacaoTarefa.setNome(nomeT.getText());
                                newDocumentacaoTarefa.setDataInicio(dataInicio);
                                newDocumentacaoTarefa.setDataFim(dataFim);
                                newDocumentacaoTarefa.setDataInicio(DATE_FORMAT.parse(datainicioT.getText()));
                                newDocumentacaoTarefa.setDataFim(DATE_FORMAT.parse(datafimT.getText()));
                                newDocumentacaoTarefa.setPercentagemConclusao(0);
                                tarefas.add(newDocumentacaoTarefa);
                                newProject.tarefas.add(newDocumentacaoTarefa);
                                break;
                            }
                        }
                        pagina5B.dispose();
                        guardaObjectos();
                        
                        pergunta = new JFrame("Criar outra tarefa?");
                        sim = new JButton("Sim");
                        nao = new JButton("Não");
                        pergunta.setSize(300, 100);
                        pergunta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        pergunta.setLayout(new BorderLayout());
                        pergunta.add(sim, BorderLayout.NORTH);
                        pergunta.add(nao, BorderLayout.SOUTH);
                        sim.addActionListener(new ChamaGereTarefas());
                        nao.addActionListener(new fechaFrame());
                        pergunta.setVisible(true);
                        subseccao = 1;
                    }
                    guardaObjectos();
                    break;
                    
                case 3:
                    //to remove a stage from a project and from arraylist of stages of CISUC
                    
                    //call mehod to validate input
                    int valindaNome = newProject.validaNomeTarefa(input.getText());
                    if ( valindaNome == -1) {
                        mensagemErro();
                        opcao = 3;
                    }
                    else {
                        pagina5C.dispose();
                        for (Tarefa t : newProject.tarefas) {
                            if (t.getNome().contains(input.getText())) {
                                newProject.tarefas.remove(t);
                                break;
                            }
                        }
                        for (Tarefa t: tarefas){
                            if (t.getNome().contains(input.getText())){
                                tarefas.remove(t);
                                break;
                            }
                        }
                        guardaObjectos();
                    }   
                    break;
                    
                case 4:
                    //to associate an ivestigator to a stage of a project
                    
                    int validaNomeTarefa = 0;
                    //validate name of input stage to verify if it exist in arraylist tarefas of class CISUC
                    for (Tarefa t: tarefas){
                        if ( t.getNome().contains(tarefaT.getText()) && tarefaT.getText().length() != 0){
                            validaNomeTarefa = 0;
                            break;
                        }
                        else validaNomeTarefa = -1;
                    }
                    int validaInvestigador = newProject.validaNomeInvestigador(nomeT.getText());
                    
                    //if input is invalid
                    if (validaNomeTarefa == -1 || validaInvestigador == -1){
                        mensagemErro();
                        opcao = 4;
                    }
                    else{
                        pagina5D.dispose();
                        
                        //associate investigator to stage
                        for (Investigador i: investigadores){
                            
                            if ( i.getNome().contains(nomeT.getText())){
                                
                                for (Tarefa t: tarefas){
                                    
                                    if (t.getNome().contains(tarefaT.getText())){
                                        try {
                                            t.setResponsavel(i);
                                            guardaObjectos();
                                            break;
                                            
                                        } catch (Exception e){
                                            opcao = 4;
                                            limpar = new JButton("OK");
                                            mensagem = new JLabel(e.getMessage());
                                            erro = new JFrame("Operação inválida");
                                            erro.setSize(600, 100);
                                            erro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                            erro.setLayout(new BorderLayout());
                                            erro.add(mensagem, BorderLayout.CENTER);
                                            erro.add(limpar, BorderLayout.SOUTH);
                                            limpar.addActionListener(new limpar());
                                            erro.setVisible(true);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                break;
            }
        }
    }
    
    /**
     * returns string with projects finished within incial date
     * @return 
     */
    public String listarProjectosConcluidosNaData() {
        String lista = "";
        for (Projecto p : projectos) {
            if ( p.getDataDeConclusao()!= null && (p.getDataFim().after(p.getDataDeConclusao()) || p.getDataFim() == p.getDataDeConclusao())) {
                lista += p.getNome() + "; ";
            }
        }
        if (lista.equals("")){
            lista = "Não há projetos concluidos na data estimada, ou antes!";
        }
        return lista;
    }
    
    /**
     * returns string with projects not finished within incial date
     * @return 
     */
    public String listarProjectosNaoConcluidosNaData() {
        String lista = "";
        for (Projecto p : projectos) {
            if (p.getDataDeConclusao() != null && p.getDataFim().before(p.getDataDeConclusao())) {
                lista += p.getNome() + "; ";
            } 
        }
        if (lista.equals("")){
            lista = "Não há projectos concluidos depois da data estimada.";
        }
        return lista;
    }
    
    /**
     * create string with project names
     * @return 
     */
    private String listaProjectos() {
        String lista = "";
        for (Projecto p : projectos) {
            //if a project ha been concluded it wont be available for edition anymore
            if (p.getDataDeConclusao() == null){
                lista += p.getNome() + "; ";
            }
        }
        return lista;
    }
    
    /**
     * returns string of with all stages without an investigator associated
     * @return 
     */
    private String listaTarefas() {
        String lista = "";
        
        //to update % of conclusion of a stage of a project, that isn't finish and as an investigator associated
        if (listaTarefasOpcao == 1){
            for (Projecto p: projectos){
                if (p.getNome().contains(nomeProjecto)){
                    newProject = p;
                    for (Tarefa t : newProject.tarefas) {
                        if (t.getPercentagemConclusao() < 100 && t.getResponsavel() != null) {
                            lista += t.getNome() + "; ";
                        }
                    }
                    break;
                }
            }
        }
        else if (listaTarefasOpcao == 2){
            for (Projecto p: projectos){
                if (p.getNome().contains(nomeProjecto)){
                    newProject = p;
                    for (Tarefa t : newProject.tarefas) {
                        lista += t.getNome() + "; ";
                    }
                    if (lista.equals("")) lista = "Este projecto não tem tarefas associadas!";
                    break;
                }
            }
        }
        else if (listaTarefasOpcao == 3){
            for (Tarefa t : tarefas) {
                if (t.getResponsavel() == null) {
                    lista += t.getNome() + "; ";
                }
            }
        }
        else{
            lista = "Não há tarefas por concluir com investigador atribuido!";
        }
        return lista;
    }
    
    /**
     * if (insere == 3) returns a string of all teachers that can be associated to as project as head resercher, in case variable
     * if (insere == 4) returns a string of all investigators that can be associated to a jroject
     * if (insere == 1) returns a string of all investigator of a projecto that can be associated to a stage
     * if (insere == -1) returns a string with all the teachers available to be teacher advisor
     * @return 
     */
    public String listaInvestigadores(){
        String lista = "";
        
        if (insere >  2){
            
            for (Investigador i : investigadores) {
                
                //to selecte IP: if investigator is teacher and isn't already heade research of a projecto
                if (insere == 3) {
                    if (i.getCustoBolsa() == 0) {
                        //cast investigator to Docente
                        Docente d = (Docente) i;
                        if (d.getDocenteIP() == null) {
                            lista += d.getNome() + "; ";
                        }
                    }
                } 
                
                //to associate investigators to project: if investigator is a scholar and isn't associated with any project yet
                else if (insere == 4) {
                    if (i.getCustoBolsa() != 0) {

                        //casto to Bolseiro
                        Bolseiro b = (Bolseiro) i;

                        //scholar isn't associated to any project yet
                        if (b.projectos.isEmpty()) {
                            lista += b.getNome() + "; ";
                        }
                    }
                    if (i.getCustoBolsa() == 0) {
                        Docente d = (Docente) i;

                        //if teacher isn't head researcher of current created Project, and if isn't associated to 3 projects yet
                        if (d.getDocenteIP() != newProject && d.projectos.size() < 3) {
                            lista += d.getNome() + "; ";
                        }
                    }
                }
            }
        }
        
        else if (insere == 2) {
            for (Investigador i : newProject.investigadores) {
                
                if (i.getNumTarefas() < i.totalTarefas){
                    lista += i.getNome() + "; ";
                }
            }
        }
        else if (insere == -1){
            //reut
            for (Investigador i: investigadores) {
                if (i.getCustoBolsa() == 0){
                    lista += i.getNome() + "; ";
                }
            }
        }
        
        if (lista.equals("")) 
            lista += "Não há investigadores disponíveis, nas condições pretendidas!";
        
        return lista;
    }
    
    public void mensagemErro(){
        if (ERRO != 1){
            mensagem = new JLabel("Input inválido!");
        }
        limpar = new JButton("Tentar novamente!");
        erro = new JFrame("Erro");
        erro.setSize(300, 100);
        erro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        erro.setLayout(new BorderLayout());
        erro.add(mensagem, BorderLayout.CENTER);
        erro.add(limpar, BorderLayout.SOUTH);
        limpar.addActionListener(new limpar());
        erro.setVisible(true);
        ERRO = 0;
    }
    
    private class limpar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (opcao) {
                case 1:
                    nomeT.setText(null);
                    acronimoT.setText(null);
                    datainicioT.setText(null);
                    datafimT.setText(null);
                    erro.dispose();
                    break;
                case 2:
                    //to clear input info of stages
                    tipoTarefaT.setText(null);
                    nomeT.setText(null);
                    datainicioT.setText(null);
                    datafimT.setText(null);
                    erro.dispose();
                    break;
                case 3:
                    //to clear input info of stages
                    input.setText(null);
                    erro.dispose();
                    break;
                case 4:
                    //to clear input info of stages and investigators
                    nomeT.setText(null);
                    tarefaT.setText(null);
                    erro.dispose();
                    break;
            }
        }
    }
    
    private class fechaFrame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pergunta.dispose();
        }
    }
}