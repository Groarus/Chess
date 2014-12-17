/**
 * Created by Eric on 12/16/2014.
 */
public class Turn {
    private Colour turn;
    public Turn(){
        turn = Colour.WHITE;
        //True for White, False for Black
    }
    public Colour getTurn(){
        return this.turn;
    }
    public void next(){
        if (this.turn == Colour.WHITE){
            this.turn = Colour.BLACK;
        }else{
            this.turn = Colour.WHITE;
        }
    }
}
