package Galton;

import java.util.Random;

public class TreeFunc{

    //con 10 filas va a haber 66 nodos (teniendo en cuenta los de la fila 11 para recibir las bolas al final)
    public GaltonNode[] nodes = new GaltonNode[66];

    public GaltonTree onTreeCreate(){
        GaltonTree arbre = new GaltonTree();
        arbre.root.id = 0;
        arbre.nbNode =1;
        arbre.root.rang=1;
        //System.out.println("coucou");
        arbre.root.leftpadre=null;
        arbre.root.rightpadre=null;
        arbre.root.leftchild=null;
        arbre.root.rightchild=null;
        nodes[0] = arbre.root;
        arbre = onTreeConstruct(arbre, arbre.root);
        return arbre;
    }

    public GaltonNode CreateNode(int id, int rang){
        GaltonNode node = new GaltonNode();
        node.id = id;
        node.rang = rang;
        node.leftpadre=null;
        node.rightpadre=null;
        node.leftchild=null;
        node.rightchild=null;
        nodes[id] = node;
        return node;
    }

    public GaltonNode Getleftchild(GaltonNode current){
        if (nodes[current.id+current.rang] == null) {
            return CreateNode(current.id+current.rang, current.rang+1);
        }
        return nodes[current.id+current.rang];
    }
    public GaltonNode Getrightchild(GaltonNode current){
        if (nodes[current.id+current.rang+1] == null) {
            return CreateNode(current.id+current.rang+1, current.rang+1);
        }
        return nodes[current.id+current.rang+1];
    }

    public GaltonTree onTreeConstruct(GaltonTree gt, GaltonNode current){
        //vamos a usar 10 filas de node en esta maquina + 1 para recibir las bolas
        for (int i = 1; i < 66; i++){
            if (current.rang != 11) {
                current.leftchild = Getleftchild(current);
                current.rightchild = Getrightchild(current);
                current.leftchild.rightpadre = current;
                current.rightchild.leftpadre = current;
            }
            current = nodes[current.id+1];
        }
        return gt;
    }

    public int ParcoursResult(GaltonTree tree){
        GaltonNode current = tree.root;

        while (current.rang!=11){
            //System.out.println(current.id);
            //vamos a eligir left o right gracias al boolean
            Random randomNumbers = new Random();
            boolean left = randomNumbers.nextBoolean();
            if (left) {
                //System.out.println("left");
                current = current.leftchild;
            }
            else {
                //System.out.println("rigt");
                current = current.rightchild;
            }
        }
        return current.id - 54;
    }

    public void printNodes() {
        for (GaltonNode node : nodes) {
            if (node != null) { // Vérifiez que le nœud n'est pas nul
                int parentLeftId = (node.leftpadre != null) ? node.leftpadre.id+1 : -1;
                int parentRightId = (node.rightpadre != null) ? node.rightpadre.id+1 : -1;
                int leftChildId = (node.leftchild != null) ? node.leftchild.id +1: -1;
                int rightChildId = (node.rightchild != null) ? node.rightchild.id+1 : -1;

                System.out.println(node.id+1 + ", " + parentLeftId + ", " + parentRightId + ", " + leftChildId + ", " + rightChildId + ", " + node.rang);
            }
        }
    }
}
