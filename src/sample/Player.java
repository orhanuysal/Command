package sample;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {
    private ArrayList<Pair<Double,Double>> coords;
    public int hasTurn;
    public ArrayList<Pawn> pawns;
    public int pawnsToPlace;
    private ArrayList<Move> initialMoves;
    private int moveIndex;
    private final int MOVESTOGET = 4;
    private int count = 0;
    public Base base;

    Player(){
        coords = new ArrayList<>();
        pawns = new ArrayList<>();
        pawnsToPlace = 5;
        initialMoves = new ArrayList<>();
        initializeMoves();
        moveIndex = 0;
        base = new Base();
    }

    private void initializeMoves(){
        initialMoves.add(new Move(Move.type.REDIRECT));
        initialMoves.add(new Move(Move.type.REDIRECT));
        initialMoves.add(new Move(Move.type.REDIRECT));
        initialMoves.add(new Move(Move.type.REDIRECT));
        initialMoves.add(new Move(Move.type.BURN));
        initialMoves.add(new Move(Move.type.BURN));
        initialMoves.add(new Move(Move.type.GUARD));
        initialMoves.add(new Move(Move.type.GUARD));
        initialMoves.add(new Move(Move.type.PORTAL));
        initialMoves.add(new Move(Move.type.RANGE));
        initialMoves.add(new Move(Move.type.ROTATE));
        initialMoves.add(new Move(Move.type.SPEED));

        Collections.shuffle(initialMoves);
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

    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> res = new ArrayList<>();
        for(int i = 0; i < MOVESTOGET; i++){
            res.add(initialMoves.get(i));
        }
        System.out.println("Possible moves: " + Arrays.toString(res.toArray()));
        Collections.rotate(initialMoves, MOVESTOGET);
        count++;
        if((initialMoves.size()/MOVESTOGET) == count){
            count = 0;
            Collections.shuffle(initialMoves);
        }
        return res;
    }
}
