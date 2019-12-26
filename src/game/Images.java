package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Images {

    public static final int imageW = 50;
    public static final int imageH = 50;

    static Image MINE_IMAGE;
    static Image FLAG_IMAGE;
    static Image ONE_IMAGE;
    static Image TWO_IMAGE;
    static Image THREE_IMAGE;
    static Image FOUR_IMAGE;
    static Image FIVE_IMAGE;
    static Image SIX_IMAGE;
    static Image SEVEN_IMAGE;
    static Image EIGHT_IMAGE;
    static Image NINE_IMAGE;
    static Image GREY_IMAGE;
    static Image CAMO_IMAGE;

    static ImageView MINE_IMAGEVIEW;
    static ImageView FLAG_IMAGEVIEW;
    static ImageView ONE_IMAGEVIEW;
    static ImageView TWO_IMAGEVIEW;
    static ImageView THREE_IMAGEVIEW;
    static ImageView FOUR_IMAGEVIEW;
    static ImageView FIVE_IMAGEVIEW;
    static ImageView SIX_IMAGEVIEW;
    static ImageView SEVEN_IMAGEVIEW;
    static ImageView EIGHT_IMAGEVIEW;
    static ImageView NINE_IMAGEVIEW;
    static ImageView GREY_IMAGEVIEW;
    static ImageView CAMO_IMAGEVIEW;

    public static void load() {
        try {
            //-----------------------------Images-----------------------------------------------------
            MINE_IMAGE = new Image(new FileInputStream("./images/mine.png"));
            FLAG_IMAGE = new Image(new FileInputStream("./images/flag.png"));
            ONE_IMAGE = new Image(new FileInputStream("./images/one.png"));
            TWO_IMAGE = new Image(new FileInputStream("./images/two.png"));
            THREE_IMAGE = new Image(new FileInputStream("./images/three.png"));
            FOUR_IMAGE = new Image(new FileInputStream("./images/four.png"));
            FIVE_IMAGE = new Image(new FileInputStream("./images/five.png"));
            SIX_IMAGE = new Image(new FileInputStream("./images/six.png"));
            SEVEN_IMAGE = new Image(new FileInputStream("./images/seven.png"));
            EIGHT_IMAGE = new Image(new FileInputStream("./images/eight.png"));
            NINE_IMAGE = new Image(new FileInputStream("./images/nine.png"));
            GREY_IMAGE = new Image(new FileInputStream("./images/grey.jpg"));
            CAMO_IMAGE = new Image(new FileInputStream("./images/camo.png"));

            //----------------------------------------------------------------------------------------

            //------------------------------ImageViews--------------------------------------------------
            MINE_IMAGEVIEW = new ImageView(MINE_IMAGE);
            FLAG_IMAGEVIEW = new ImageView(FLAG_IMAGE);
            ONE_IMAGEVIEW = new ImageView(ONE_IMAGE);
            TWO_IMAGEVIEW = new ImageView(TWO_IMAGE);
            THREE_IMAGEVIEW = new ImageView(THREE_IMAGE);
            FOUR_IMAGEVIEW = new ImageView(FOUR_IMAGE);
            FIVE_IMAGEVIEW = new ImageView(FIVE_IMAGE);
            SIX_IMAGEVIEW = new ImageView(SIX_IMAGE);
            SEVEN_IMAGEVIEW = new ImageView(SEVEN_IMAGE);
            EIGHT_IMAGEVIEW = new ImageView(EIGHT_IMAGE);
            NINE_IMAGEVIEW = new ImageView(NINE_IMAGE);
            GREY_IMAGEVIEW = new ImageView(GREY_IMAGE);
            CAMO_IMAGEVIEW = new ImageView(CAMO_IMAGE);
            //------------------------------------------------------------------------------------------
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
