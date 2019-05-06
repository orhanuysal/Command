package sample;

import javafx.util.Pair;
import java.util.ArrayList;

public class Player {
    private ArrayList<Pair<Double,Double>> coords;
    public int hasTurn;
    public int pawns;
    public int pawnsToPlace;
    Player(){
        coords = new ArrayList<>();
        pawns = 0;
        pawnsToPlace = 5;
    }

    private void addPawn(double x, double y){
        coords.add(new Pair<>(x, y));
    }

    public ArrayList<Pair<Double,Double>> getCoords(){
        return coords;
    }
}
