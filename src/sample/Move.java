package sample;

public class Move {
    private type type;

    public Move(type type) {
        this.type = type;
    }

    public enum type{
        REDIRECT,
        GUARD,
        BURN,
        SPEED,
        PORTAL,
        ROTATE,
        RANGE
    }

    public void executeMove(){
        switch (type) {
            case REDIRECT:
                break;
            case GUARD:
                break;
            case BURN:
                break;
            case SPEED:
                break;
            case PORTAL:
                break;
            case ROTATE:
                break;
            case RANGE:
                break;
        }
    }

    public type getType(){
        return type;
    }

    @Override
    public String toString(){
        return type.name();
    }
}
