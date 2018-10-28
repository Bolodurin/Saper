package sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {


    private static final int WID = 800;
    private static final int HEIG = 600;
    private static final int TILE_SIZE = 40;
    private static final int X_TILES = WID/TILE_SIZE;
    private static final int Y_TILES = HEIG/TILE_SIZE;

    Scene root;

    private Tile[][] grid = new Tile[X_TILES][Y_TILES];

    private Parent createRoot(){
        Pane root = new Pane();
        root.setPrefSize(WID, HEIG);

        for(int y = 0; y < Y_TILES; y++){
            for(int x = 0; x < X_TILES; x++){
                Tile tile = new Tile(x, y,
                        Math.random() < 0.2);
                grid[x][y] = tile;

                root.getChildren().add(tile);
            }
        }

        for(int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = grid[x][y];

                if(tile.isBomb) {
                    continue;
                }

                int countBombs = 0;
                for(Tile i : getSosed(tile)){
                    if(i.isBomb){
                        countBombs++;
                    }
                }
                if(countBombs > 0)
                    tile.text.setText(
                        toString().valueOf(countBombs));
            }
        }
        return root;
    }

    private List<Tile> getSosed(Tile tile){
        List<Tile> result = new ArrayList<>();

        int[] steps = new int[]{
                -1, 1,
                -1, 0,
                -1, -1,
                0, 1,
                0, -1,
                1, -1,
                1, 0,
                1, 1
        };
        for (int i = 0; i < steps.length; i++){
            int dx = steps[i];
            int dy = steps[++i];
            int nowX = tile.xCoord + dx;
            int nowY = tile.yCoord + dy;
            if ((nowX >= 0 && nowX < X_TILES) &&
                    (nowY >= 0 && nowY < Y_TILES))
                result.add(grid[nowX][nowY]);
        }
        return result;
    }

    private class Tile extends StackPane{
        private int xCoord;
        private int yCoord;
        private boolean isBomb;
        private boolean isOpen = false;

        private Rectangle border = new Rectangle(
                TILE_SIZE, TILE_SIZE);

        private Text text = new Text();

        public Tile(int xCoord, int yCoord,
                    boolean isBomb){
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.isBomb = isBomb;
            border.setStroke(Color.GRAY);

            text.setFill(Color.GREEN);
            text.setFont(Font.font(18));
            text.setText(isBomb ? "X" : "");
            text.setVisible(false);

            this.getChildren().addAll(border, text);

            setTranslateX(xCoord*TILE_SIZE);
            setTranslateY(yCoord*TILE_SIZE);

            setOnMouseClicked(e->open());
        }

        public void open(){
            if(isOpen) {
                return;
            }

            if(isBomb){
                border.setFill(null);
                root.setRoot(createRoot());
                System.out.println("GameOver");

                return;
            }

            isOpen = true;
            text.setVisible(true);
            border.setFill(null);

            if(text.getText().isEmpty()){
                getSosed(this).forEach(Tile::open);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Saper");

        root = new Scene(createRoot());
        primaryStage.setScene(root);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
