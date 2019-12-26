package solver;

import java.util.List;

public class Probability {

    static double calculate(double a, double b){
        return 1 - (1 - a) * (1 - b);
    }


    static void correct(List<Double> list, double multiplier){
        double sum = 0;
        for (Double elem : list) sum += elem;
        for (int i = 0; i < list.size(); i++){
            double res = list.get(i) * multiplier / sum;
            list.set(i, res);
        }
    }
}
