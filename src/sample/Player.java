package sample;

import javafx.util.Pair;
import java.util.ArrayList;

public class Player {
    private ArrayList<Pair<Double,Double>> coords;
    public int hasTurn;
    public ArrayList<Pawn> pawns;
    public int pawnsToPlace;
    Player(){
        coords = new ArrayList<>();
        pawns = new ArrayList<>();
        pawnsToPlace = 3;
    }

    public void addPawn( Pawn pawn ){
        pawns.add( pawn );
    }

    public ArrayList<Pair<Double,Double>> getCoords(){
        return coords;
    }

    public void erase(Cell nex) {

        System.out.println( "Size1: " + pawns.size() );
        for(Pawn p: pawns) {
            if( p.c.equals( nex ) ) {
                p.pawnImage.setVisible( false );
                pawns.remove(p);
                break;
            }
        }
        System.out.println( "Size2: " + pawns.size() );
        nex.contains = 0;

    }
}
