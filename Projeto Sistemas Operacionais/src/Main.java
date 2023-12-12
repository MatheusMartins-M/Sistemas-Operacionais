import java.util.Scanner;
import java.util.Random;

public class Main {
    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static int n_processos = 3;
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        int[] tempo_execucao = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];

        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        System.out.println();

        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        int escolha = -1;
        while (escolha != 0) {
            menu();
            escolha = teclado.nextInt();

            switch (escolha) {
                case 1 -> FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 2 -> SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 3 -> SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 4 -> PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 5 -> PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 6 -> RoundRobin(tempo_execucao, tempo_espera, tempo_restante);
                case 7 -> imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 8 -> {
                    popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                    imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                }
                default -> System.out.println("\nOpção inválida. Tente novamente\n");
            }
        }
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada, int[] prioridade) {
        Random random = new Random();
        Scanner teclado = new Scanner(System.in);
        int aleatorio;

        System.out.println("Será aleatório?");
        System.out.println("1) Sim");
        System.out.println("2) Não");
        System.out.print("Escolha: ");
        aleatorio = teclado.nextInt();

        System.out.println();

        for (int i = 0; i < n_processos; i++) {
            if (aleatorio == 1) {
                tempo_execucao[i] = random.nextInt(10) + 1;
                tempo_chegada[i] = random.nextInt(10) + 1;
                prioridade[i] = random.nextInt(10) + 1;
            } else {
                System.out.print("Digite o tempo de execução do processo[" + i + "]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo[" + i + "]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo[" + i + "]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];
        }

        System.out.println();
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada, int[] prioridade) {
        for (int i = 0; i < n_processos; i++) {
            System.out.println("Process[" + i + "]: tempo_execucao=" + tempo_execucao[i] + " tempo_restante=" + tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade=" + prioridade[i]);
        }
    }

    public static void imprime_stats(int[] espera) {
        int[] tempo_espera = espera.clone();
        double tempo_total = 0;

        System.out.println();

        for (int i = 0; i < n_processos; i++) {
            System.out.println("Process[" + i + "]: tempo_espera=" + tempo_espera[i]);
            tempo_total = tempo_espera[i] + tempo_total;
        }
        System.out.println("Tempo medio de espera: " + tempo_total / n_processos);
    }

    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int tempoAtual = 0;

        for (int i = 0; i < n_processos; i++) {
            tempo_espera[i] = tempoAtual;

            while (tempo_restante[i] > 0) {
                System.out.println("tempo[" + (tempoAtual + 1) + "]: processo[" + i + "] restante=" + (tempo_restante[i] - 1));
                tempo_restante[i]--;
                tempoAtual++;
            }
        }

        imprime_stats(tempo_espera);
    }

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int menor_tempo_restante = MAXIMO_TEMPO_EXECUCAO;
        int processo_em_execucao = -1;
        int proc_terminados = 0;

        for (int tempo = 1; tempo <= 1000; tempo++) {
            if ((preemptivo) || ((!preemptivo) && (processo_em_execucao == -1))) {
                for (int proc = 0; proc < n_processos; proc++) {
                    if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
                        if (tempo_restante[proc] < menor_tempo_restante) {
                            menor_tempo_restante = tempo_restante[proc];
                            processo_em_execucao = proc;
                        }
                    }
                }
            }

            if (processo_em_execucao == -1)
                System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
            else {
                if (tempo_restante[processo_em_execucao] == tempo_execucao[processo_em_execucao])
                    tempo_espera[processo_em_execucao] = tempo - tempo_chegada[processo_em_execucao];

                tempo_restante[processo_em_execucao]--;

                System.out.println("tempo[" + tempo + "]: processo[" + processo_em_execucao + "] restante=" + (tempo_restante[processo_em_execucao]));

                if (tempo_restante[processo_em_execucao] == 0) {
                    processo_em_execucao = -1;
                    menor_tempo_restante = MAXIMO_TEMPO_EXECUCAO;
                    proc_terminados++;

                    if (proc_terminados == n_processos)
                        break;
                }
            }
        }

        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] _prioridade = prioridade.clone();

        int maior_prioridade = 0;
        int processo_em_execucao = -1;
        int proc_terminados = 0;

        for (int tempo = 1; tempo <= 1000; tempo++) {
            if ((preemptivo) || ((!preemptivo) && (processo_em_execucao == -1))) {
                for (int proc = 0; proc < n_processos; proc++) {
                    if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
                        if (_prioridade[proc] > maior_prioridade) {
                            maior_prioridade = _prioridade[proc];
                            processo_em_execucao = proc;
                        }
                    }
                }
            }

            if (processo_em_execucao == -1)
                System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
            else {
                if (tempo_restante[processo_em_execucao] == tempo_execucao[processo_em_execucao])
                    tempo_espera[processo_em_execucao] = tempo - tempo_chegada[processo_em_execucao];

                tempo_restante[processo_em_execucao]--;

                System.out.println("tempo[" + tempo + "]: processo[" + processo_em_execucao + "] restante=" + (tempo_restante[processo_em_execucao]));

                if (tempo_restante[processo_em_execucao] == 0) {
                    processo_em_execucao = -1;
                    maior_prioridade = 0;
                    proc_terminados++;

                    if (proc_terminados == n_processos)
                        break;
                }
            }
        }

        imprime_stats(tempo_espera);
    }

    public static void RoundRobin(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        int processo_em_execucao = 0;
        int proc_terminados = 0;
        int time_sliceCount = 1;

        System.out.println("Escolha o tempo de corte: ");
        int tempoCorte = teclado.nextInt();

        for (int tempo = 1; tempo <= 1000; tempo++) {
            System.out.println("tempo[" + tempo + "]: processo[" + processo_em_execucao + "] restante=" + tempo_restante[processo_em_execucao]);
            tempo_restante[processo_em_execucao]--;

            if (tempo_restante[processo_em_execucao] == 0) {
                proc_terminados++;
                time_sliceCount = 1;

                if (proc_terminados == n_processos) {
                    break;
                }

                do {
                    processo_em_execucao = (processo_em_execucao + 1) % n_processos;
                } while (tempo_restante[processo_em_execucao] == 0);

            } else if (time_sliceCount == tempoCorte) {
                time_sliceCount = 1;

                do {
                    processo_em_execucao = (processo_em_execucao + 1) % n_processos;
                } while (tempo_restante[processo_em_execucao] == 0);

            } else {
                time_sliceCount++;
            }
        }
    }

    static void menu() {
        System.out.println();
        System.out.println("=========== Escolha a Opção ===========");
        System.out.println("1) FCFS");
        System.out.println("2) SJF Preemptivo");
        System.out.println("3) SJF não-preemptivo");
        System.out.println("4) Prioridade não-preemptivo");
        System.out.println("5) Prioridade preemptivo");
        System.out.println("6) Round Robin");
        System.out.println("7) Imprime lista de processos");
        System.out.println("8) Popular processos novamente");
        System.out.println("=========================================");
        System.out.print("Escolha: ");
    }
}
