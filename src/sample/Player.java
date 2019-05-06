package sample;

import javafx.util.Pair;
import java.util.ArrayList;

public class Player {
    private ArrayList<Pair<Double,Double>> coords;
    public int hasTurn;
    private int remainingPawns;
    Player(){
        coords = new ArrayList<>();
        remainingPawns = 5;
    }

    private void addPawn(double x, double y){
        coords.add(new Pair<>(x, y));
    }

    public ArrayList<Pair<Double,Double>> getCoords(){
        return coords;
    }
}
