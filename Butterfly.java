import mpi.*;
import java.text.NumberFormat;

public class Butterfly {
    static double f(double x) {
        return 2*(Math.pow(x, 3.0) + Math.pow(x, 2.0) + x + 1.0);
    }

    static double integrar(double a, double b, double h) {
        int i;
        double n = (b - a)/h;
        double soma = 0.5 * (f(a) + f(b));    // area
        for (i = 1; i < n; i++) {
           double x = a + h * i;
           soma = soma + f(x);
        }
        return soma * h;
    }

    public static void main(String[] args) throws Exception{    
        double a, b, n, a_local, b_local, n_local;
        double h, tempoInicio, tempoFinal;
        int idProcesso, quantProcesso, metade, m;
        Status status;
        long time;
        double val[] = new double[3];
        double soma[] = new double[1];
        double somatotal;
        
        //verifica se número de processos é potencia de 2
        if (!((Integer.parseInt(args[1]) > 0) && ((Integer.parseInt(args[1]) & (Integer.parseInt(args[1]) - 1)) == 0))){
            System.out.println("Quantidade de processos deve ser potencia de 2");
            System.exit(0);
        } 

        MPI.Init(args); //Inícia parte distribuída/paralelo
        idProcesso = MPI.COMM_WORLD.Rank(); //Pega o id do processo
        quantProcesso  = MPI.COMM_WORLD.Size(); //Pega quantos processos têm no distribuído
        tempoInicio = MPI.Wtime(); //Obter o tempo            
        
        //Ler e enviar dados
        if (idProcesso == 0){
            System.out.println("iniciou o mestre"); 
            //Para receber os valores (Intervalo e discretização)   
            a = Double.parseDouble(args[3]);
            b = Double.parseDouble(args[4]);
            h = Double.parseDouble(args[5]);
            val[0] = a;
            val[1] = b;
            val[2] = h;
            //For para enviar os elementos
            for(m = 1; m <= quantProcesso-1; m++){
                MPI.COMM_WORLD.Send(val, 0, 3, MPI.DOUBLE, m, 5);
            }
        }
        else{
            status = MPI.COMM_WORLD.Recv(val, 0 , 3, MPI.DOUBLE, 0, 5);
        }

        n = (val[1]-val[0])/val[2];
        n_local = n/quantProcesso;
        a_local = val[0] + idProcesso*n_local*val[2];
        b_local = a_local + n_local*val[2];
        somatotal = integrar(a_local, b_local, val[2]);
        metade = quantProcesso;

        do {
            metade = metade/2; 
            soma[0] = somatotal;
            if (idProcesso >= metade){
                if (idProcesso == idProcesso + metade) 
                    break;
                System.out.println("\n------------------------------------------\n");
                System.out.println("ENVIANDO SOMA: " +soma[0] + "\n");
                System.out.println("PROCESSO ORIGEM: " +idProcesso + "\n");
                System.out.println("PROCESSO DESTINO: " +(idProcesso - metade) + "\n");
                //Enviando
                MPI.COMM_WORLD.Send(soma, 0, 1, MPI.DOUBLE, idProcesso - metade, 5);
                System.out.println("SOMA: " + soma[0] + "ENVIADA PARA PROCESSO: " + (idProcesso - metade) + "\n");
                System.out.println("\n------------------------------------------\n");
            }
            else{
                System.out.println("\n------------------------------------------\n");
                System.out.println("ESPERANDO SOMA DO PROCESSO:  " + (idProcesso + metade) + "\n");
                //Recebendo
                status = MPI.COMM_WORLD.Recv(soma, 0 , 1, MPI.DOUBLE, MPI.ANY_SOURCE, 5);
                System.out.println("SOMA PARCIAL: " + soma[0] + " DO PROCESSO " + status.source + " RECEBIDA \n");
                System.out.println("\n------------------------------------------\n");
                somatotal = somatotal + soma[0];
            }
        } while((idProcesso < metade) && (metade > 1));

        if (idProcesso == 0) {
            tempoFinal = MPI.Wtime();
            
            // Formata o número para exibir
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(6);
            nf.setMinimumFractionDigits(6);

            time = (long) (tempoFinal - tempoInicio);	
            System.out.println("SOMA TOTAL: " + somatotal);
            System.out.println("TEMPO: " + nf.format((double)time) + " em segs");
        }
        //finaliza a parte distribuída 
        MPI.Finalize();  
    }

    
}