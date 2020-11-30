package br.com.finalcraft.unesp.java.cg.data.colorutil;

public class ColorUtil {

    public static int INTERN_BOUNDS = 50;

    //Reference https://stackoverflow.com/questions/3407942/rgb-values-of-visible-spectrum
    public static double[] spectralColor(double l){// RGB <0,1> <- lambda l <0,1> [nm]
        double r = 0;
        double g = 0;
        double b = 0;
        double t;

        l = 400 + INTERN_BOUNDS + ( l * (300 - (INTERN_BOUNDS * 2)));
        //Redução de intervalo, embora fosse de 400 a 700,
        //após alguns testes é preferivel usar um intervalo interno a esse
        //um valor de 50 para o INTERN_BOUNDS implica que o espectro
        //visíveil será reduzido para o intervalo de (400 + 50) - (700 - 50)

        if ((l>=400.0)&&(l<410.0)) { t=(l-400.0)/(410.0-400.0); r=    +(0.33*t)-(0.20*t*t); }
        else if ((l>=410.0)&&(l<475.0)) { t=(l-410.0)/(475.0-410.0); r=0.14         -(0.13*t*t); }
        else if ((l>=545.0)&&(l<595.0)) { t=(l-545.0)/(595.0-545.0); r=    +(1.98*t)-(     t*t); }
        else if ((l>=595.0)&&(l<650.0)) { t=(l-595.0)/(650.0-595.0); r=0.98+(0.06*t)-(0.40*t*t); }
        else if ((l>=650.0)&&(l<700.0)) { t=(l-650.0)/(700.0-650.0); r=0.65-(0.84*t)+(0.20*t*t); }
        if ((l>=415.0)&&(l<475.0)) { t=(l-415.0)/(475.0-415.0); g=             +(0.80*t*t); }
        else if ((l>=475.0)&&(l<590.0)) { t=(l-475.0)/(590.0-475.0); g=0.8 +(0.76*t)-(0.80*t*t); }
        else if ((l>=585.0)&&(l<639.0)) { t=(l-585.0)/(639.0-585.0); g=0.84-(0.84*t)           ; }
        if ((l>=400.0)&&(l<475.0)) { t=(l-400.0)/(475.0-400.0); b=    +(2.20*t)-(1.50*t*t); }
        else if ((l>=475.0)&&(l<560.0)) { t=(l-475.0)/(560.0-475.0); b=0.7 -(     t)+(0.30*t*t); }
        return new double[]{r, g, b};
    }

}
