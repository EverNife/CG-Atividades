package br.com.finalcraft.unesp.java.cg.data;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    //Retorna uma lista de coordenadas de pixels para os que pertencem a reta
    // do modo [[1,2][4,5][5,6]] etc
    public static List<Integer[]> getAllPixelsBetween(int x1, int y1, int x2, int y2){

        double theta  = Math.atan2(y2-y1, x2-x1); //Angulo do vetor resultante entre os pontos
        theta = Math.abs(Math.toDegrees(theta)); // -180 --> 180

        int x = x1;
        int y = y1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        List<Integer[]> selectedPixels = new ArrayList<>();
        selectedPixels.add(new Integer[]{x,y});

        int incX = x1 < x2 ? 1 : -1;
        int incY = y1 < y2 ? 1 : -1;

        int dBase = dx - dy;
        int d;

        while (true){
            selectedPixels.add(new Integer[]{x,y});

            if (x == x2 && y == y2)
                break;

            d = 2 * dBase;
            if (d > -dy){
                dBase = dBase - dy;
                x = x + incX;
            }

            if (d < dx){
                dBase = dBase + dx;
                y = y + incY;
            }
        }
        return selectedPixels;

        /*
        if (isInInterval(theta, 0, 45)
                || isInInterval(theta, 135, 180)){
            System.out.println("Estou Aqui");
            int incX = 1;
            if (x1 > x2){
                dE = -dE;
                dNE = -dNE;
                incX = -incX;
            }
            while (x != x2){
                if (d < 0) { // escolhe E
                    d = d + dE;
                    x = x + incX;
                }
                else {
                    d = d + dNE; // escolhe NE
                    x = x + incX;
                    y = y + incX;
                }
                selectedPixels.add(new Integer[]{x,y});
            }
        }*/
    }

}
