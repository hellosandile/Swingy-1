package za.co.WeThinkCode_.swingy.control;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import za.co.WeThinkCode_.swingy.model.player.Hero;
import za.co.WeThinkCode_.swingy.model.player.Hunter;
import za.co.WeThinkCode_.swingy.model.player.Titan;
import za.co.WeThinkCode_.swingy.model.player.Warlock;
import za.co.WeThinkCode_.swingy.view.Iview;
import za.co.WeThinkCode_.swingy.view.console.ConsoleDisplay;
import java.util.Random;
import java.util.Scanner;

@Getter
@Setter
@Builder
public class LogicController {

    public static enum Stage{
        MAIN_MENU,
        NEW_CHARACTER_MENU,
        LOAD_CHARACTER_MENU,
        PLAY_MOVE,
        FIGHT,
        FIGHT_WON,
        FIGHT_LOST,
        END_ROUND,
        END_ROUND_DING,
        QUIT_VERIFY,
        QUIT
    };
    @Builder.Default
    private Stage gameStage = Stage.MAIN_MENU;
    public String gameView;
    private Iview view;
    public Hero hero;

    public void runGame(){

        Scanner scanner = new Scanner(System.in);
        if(gameView.equalsIgnoreCase("-console")){
            view = ConsoleDisplay.builder().controller(this).build();
        } else {
            //TODO: guibuilder
        }

        while (true){

            switch (gameStage){
                case MAIN_MENU:
                    view.startMenu();
                    break;

                case NEW_CHARACTER_MENU:
                    view.createCharacterMenu();
                    break;

                case LOAD_CHARACTER_MENU:
                    //TODO
                    System.out.println("Not implemented... going back to main menu");
                    try { Thread.sleep(1000);} catch (InterruptedException ex) {ex.printStackTrace();}
                    gameStage = Stage.MAIN_MENU;
                    break;

                case PLAY_MOVE:
                    view.moveCharacterScreen();
                    break;

                case FIGHT:
                    view.fightScreen();
                    break;

                case FIGHT_WON:
                    view.fightWonScreen();
                    gameStage = Stage.PLAY_MOVE;
                    break;

                case FIGHT_LOST:
                    view.fightLostScreen();
                    hero.setCoordinates(new int[]{0,0});
                    gameStage = Stage.MAIN_MENU;
                    break;

                case END_ROUND:
                    view.endRound();
                    gameStage = Stage.PLAY_MOVE;
                    break;

                case END_ROUND_DING:
                    view.endRoundDing();
                    gameStage = Stage.PLAY_MOVE;
                    break;

                case QUIT_VERIFY:
                    view.quitVerify();
                    break;

                case QUIT:
                    view.quit();
                    System.exit(1);
            }
        }
    }

    public void handleInput(String[] input){
        switch (gameStage){

            case MAIN_MENU:
                switch (input[0]){
                    case "1": //New Game
                        gameStage = Stage.NEW_CHARACTER_MENU;
                        break;
                    case "2": //Load Game
                        gameStage = Stage.LOAD_CHARACTER_MENU;
                        break;
                    case "3": //Quit
                        gameStage = Stage.QUIT_VERIFY;
                        break;
                    case "4":
                        gameStage = Stage.PLAY_MOVE;
                        break;
                    default:
                        System.out.println("Something went terribly wrong??");
                        System.exit(-1);
                }
                break;

            case NEW_CHARACTER_MENU:
                switch (input[0]){
                    case "1":
                        try {
                            this.hero = Warlock.builder().name(input[1]).build();
                        } catch (NullPointerException e){System.out.println("Woops nullpointer in inputhandler");}
                        gameStage = Stage.PLAY_MOVE;
                        break;
                    case "2":
                        try {
                            this.hero = Titan.builder().name(input[1]).build();
                        } catch (NullPointerException e){System.out.println("Woops nullpointer in inputhandler");}
                        gameStage = Stage.PLAY_MOVE;
                        break;
                    case "3":
                        try {
                            this.hero = Hunter.builder().name(input[1]).build();
                        } catch (NullPointerException e){System.out.println("Woops nullpointer in inputhandler");}
                        gameStage = Stage.PLAY_MOVE;
                        break;
                    case "4":
                        gameStage = Stage.MAIN_MENU;
                        break;
                }
                break;

            case PLAY_MOVE:
                switch (input[0]){
                    case "5":
                        gameStage = Stage.MAIN_MENU;
                        hero.setCoordinates(new int[]{0,0});
                        break;
                    default:
                        gameStage = hero.move(input[0]);
                        break;
                }
                break;

            case FIGHT:
                switch (input[0]){
                    case "1":
                        gameStage = hero.fight();
                        break;
                    case "2":
                        if (new Random().nextInt(1000) > 700) {
                            view.ranNotAway();
                            gameStage = hero.fight();
                        } else {
                            view.ranAway();
                            gameStage = Stage.PLAY_MOVE;
                        }
                        break;
                }
                break;

            case QUIT_VERIFY:
                switch (input[0]){
                    case "1":
                        gameStage = Stage.QUIT;
                        break;
                    case "2":
                        gameStage = Stage.MAIN_MENU;
                        break;
                    default:
                        System.out.println("Something went terribly wrong");
                        System.exit(-1);
                }
                break;
        }
    }
}
