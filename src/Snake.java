import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Snake {
    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public static int [][] map = new int[8][8];
    private static final AtomicReference<Direction> direction = new AtomicReference<>(Direction.RIGHT);
    public static int snakeSize = 4;

    public static void main(String[] args) {

        Thread inputThread = getInputThread();
        inputThread.start();


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = 0;
            }
        }
        map[4][1] = 1;
        map[4][2] = 2;
        map[4][3] = 3;
        map[4][4] = snakeSize;
        map[4][6] = -1;

        FrontEnd(ConvertValues());

        while (MoveSnake()) {
            FrontEnd(ConvertValues());
            if (map[4][7] == snakeSize) direction.set(Direction.UP);
            try {
                TimeUnit.MILLISECONDS.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Game over");
    }

    private static Thread getInputThread() {
        Thread inputThread = new Thread(() -> {
            try {
                while (true) {
                    int input = System.in.read();
                    if (input == 'w' && direction.get() != Direction.DOWN) {
                        direction.set(Direction.UP);
                    } else if (input == 's' && direction.get() != Direction.UP) {
                        direction.set(Direction.DOWN);
                    } else if (input == 'a' && direction.get() != Direction.RIGHT) {
                        direction.set(Direction.LEFT);
                    } else if (input == 'd' && direction.get() != Direction.LEFT) {
                        direction.set(Direction.RIGHT);
                    }
                }
            } catch (IOException ignored) {
            }
        });
        inputThread.setDaemon(true);
        return inputThread;
    }

    public static boolean MoveSnake(){
        boolean AteApple = false;
        int ranX = (int) (Math.random() * 7);
        int ranY = (int) (Math.random() * 7);
        while (map[ranX][ranY] > 0){
            ranX = (int) (Math.random() * 7);
            ranY = (int) (Math.random() * 7);
        }

        CheckHead:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map[i][j] == snakeSize){
                    switch (direction.get()){
                        case UP -> {
                            if (i-1 == -1 || map[i-1][j] > 1) return false;
                            if (map[i-1][j] == -1) {
                                snakeSize++;
                                AteApple = true;
                            }
                            if (!AteApple) map[i-1][j] = snakeSize+1; else map[i-1][j] = snakeSize;
                        }
                        case DOWN -> {
                            if (i+1 == 8 || map[i+1][j] > 1) return false;
                            if (map[i+1][j] == -1) {
                                snakeSize++;
                                AteApple = true;
                                map[i+1][j] = snakeSize;
                                break CheckHead;
                            }
                            map[i + 1][j] = snakeSize + 1;
                        }
                        case LEFT -> {
                            if (j-1 == -1 || map[i][j-1] > 1) return false;
                            if (map[i][j-1] == -1) {
                                snakeSize++;
                                AteApple = true;
                            }
                            if (!AteApple) map[i][j-1] = snakeSize+1; else map[i][j-1] = snakeSize;
                        }
                        case RIGHT -> {
                            if (j+1 == 8 || map[i][j+1] > 1) return false;
                            if (map[i][j+1] == -1) {
                                snakeSize++;
                                AteApple = true;
                            }
                            if (!AteApple) map[i][j+1] = snakeSize+1; else map[i][j+1] = snakeSize;
                        }
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map[i][j] > 0 && !AteApple) {
                    map[i][j] -= 1;
                }
                if (AteApple) map[ranX][ranY] = -1;

            }
        }
        return true;
    }
    public static char[][] ConvertValues(){
        char[][] charMap = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (map[i][j]){
                    case 0 -> charMap[i][j] = ' ';
                    case -1 -> charMap[i][j] = '△';
                    case 1 -> charMap[i][j] = '⚬';
                    default -> {
                        if (map[i][j] == snakeSize){
                          charMap[i][j] = '⊚';
                        } else charMap[i][j] = '◯';
                    }
                }
            }
        }
        return charMap;
    }
    public static void FrontEnd(char[][] map){
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("┌──────────────────────┐");
        for (int i = 0; i < 8; i++) {
            System.out.println("│" + map[i][0] + "  " + map[i][1] + "  " + map[i][2] + "  " + map[i][3] + "  " + map[i][4] + "  " + map[i][5] + "  " + map[i][6] + "  " + map[i][7] + "│");
        }
        System.out.println("└──────────────────────┘");
    }
}