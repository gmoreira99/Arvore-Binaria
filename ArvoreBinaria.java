// Guilherme Moreira de Carvalho, 25/06/2021 - Para Laboratório de Algoritmos e Estruturas de Dados II, 2021/1
// Classe ArvoreBinaria: implementa a árvore; contém um atributo privado do tipo No "raiz" e 
// dois atributos estáticos: "count" inteiro para contar quantas comparações são feitas na pesquisa e "generator" do tipo SecureRandom para gerar inteiros aleátorios na construção da árvore;
// insere e pesquisa por itens nos nós.
// Classe No: implementa um nó da árvore; contém um atributo do tipo Item "reg" e dois nos filhos "esq" e "dir".

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

public class ArvoreBinaria {
    private static class No {
        Item reg;
        No esq, dir;

        No(Item reg) {
            this.reg = reg;
            esq = null; dir = null;
        }
    }

    private No raiz;

    static int count = 0;
    static SecureRandom generator = new SecureRandom();

    public ArvoreBinaria() {
        this.raiz = null;
    }

    public No getRaiz() {
        return raiz;
    }
    
    private No insere(Item reg, No p) {
        if (p == null)                      /*condição de parada: se o nó corrente estiver vazio*/
            p = new No(reg);                /*insere o resgistro nele*/
        else if (reg.compara(p.reg) < 0)    /*se o registro for menor que o do nó corrente*/
            p.esq = insere(reg, p.esq);     /*o insere na sub-árvore esquerda*/
        else if (reg.compara(p.reg) > 0)    /*se o registro for maior que o do nó corrente*/
            p.dir = insere(reg, p.dir);     /*o insere na sub-árvore direita*/
        /*else
            System.out.println("ERRO - REGISTRO DUPLICADO");*/

        return p;
    }

    private Item pesquisa(Item reg, No p) {
        if (p == null) {                    /*condição de parada: se o registro corrente estiver vazio*/
            return null;                    /*retorna null - registro não encontrado*/
        }
        else if (reg.compara(p.reg) < 0) {  /*se o registro for menor que o nó corrente*/
            count++;                        /*soma uma comparação ao contador*/
            return pesquisa(reg, p.esq);    /*retorna o registro encontrado na sub-árvore esquerda*/
        }
        else if (reg.compara(p.reg) > 0) {  /*se o registro for maior que o nó corrente*/
            count++;                        /*soma uma comparação ao contador*/
            return pesquisa(reg, p.dir);    /*retorna o registro encontrado na sub-árvore direita*/
        }
        return p.reg;
    }

    public static void main(String[] args) {
        String out = "";    /*saída para um arquivo de texto - resultado das comparações e tempo gasta em cada caso*/
        Item reg;           /*o registro a ser inserido ou procurado*/
        No root_ord, root_ran;  /*nós das árvores geradas em ordem e aleatóriamente*/
        long tempoInicial, tempoFinal; /*tempos em nano segundos inicial e final da pesquisa por um registro*/

        for(int n = 1000; n <= 9000; n += 1000) {   /*árvores com n variando de 1000 a 9000 com passo 1000 - 9x2 árvores*/
            out += "n = " + n + "\n";
            ArvoreBinaria tree_ord = new ArvoreBinaria();   /*árvore ordenada*/
            ArvoreBinaria tree_ran = new ArvoreBinaria();   /*árvore aleatória*/

            reg = new Item(0);  /*raíz da aŕvore ordenada (0)*/
            root_ord = tree_ord.insere(reg, tree_ord.getRaiz());

            reg = new Item(generator.nextInt(n));   /*raíz da aŕvore aleatória (?)*/
            root_ran = tree_ran.insere(reg, tree_ran.getRaiz());

            for(int i = 1; i < n; i++) {    /*insere n nós nas árvores*/
                reg = new Item(i);  /*0 - n-1*/
                tree_ord.insere(reg, root_ord);

                reg = new Item(generator.nextInt(n)); /*aleatórios de 0 - n-1*/
                tree_ran.insere(reg, root_ran);
            }

            /*procura por itens inexistentes na árvore (-1 e n+1)*/
            count = 0;
            reg = new Item(-1);
            tempoInicial = System.nanoTime();
            tree_ord.pesquisa(reg, root_ord);
            tempoFinal = System.nanoTime();
            out += "ERRO:REGISTRO NAO ENCONTRADO NA ARVORE ORDENADA (-1)\n";
            out += "Tempo gasto: " + (tempoFinal-tempoInicial) + " ns - Comparacoes: " + count + "\n";

            count = 0;
            reg = new Item(n+1);
            tempoInicial = System.nanoTime();
            tree_ord.pesquisa(reg, root_ord);
            tempoFinal = System.nanoTime();
            out += "ERRO:REGISTRO NAO ENCONTRADO NA ARVORE ORDENADA (n+1)\n";
            out += "Tempo gasto: " + (tempoFinal-tempoInicial) + " ns - Comparacoes: " + count + "\n";

            count = 0;
            reg = new Item(-1);
            tempoInicial = System.nanoTime();
            tree_ran.pesquisa(reg, root_ran);
            tempoFinal = System.nanoTime();
            out += "ERRO:REGISTRO NAO ENCONTRADO NA ARVORE RANDOMICA(-1)\n";
            out += "Tempo gasto: " + (tempoFinal-tempoInicial) + " ns - Comparacoes: " + count + "\n";

            count = 0;
            reg = new Item(n+1);
            tempoInicial = System.nanoTime();
            tree_ran.pesquisa(reg, root_ran);
            tempoFinal = System.nanoTime();
            out += "ERRO:REGISTRO NAO ENCONTRADO NA ARVORE RANDOMICA(n+1)\n";
            out += "Tempo gasto: " + (tempoFinal-tempoInicial) + " ns - Comparacoes: " + count + "\n";
        }

        /*escreve a saída em um arquivo "pratica01.txt"*/
        try {
            FileWriter fp = new FileWriter("ArvoreBinaria_Saida.txt");
            fp.write(out);
            fp.close();
        } catch (IOException e) {
            System.out.println("ERRO:NAO FOI POSSIVEL CRIAR O ARQUIVO");
        }
    }
}
