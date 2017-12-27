import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

public class GreyScale extends Component{


    private JButton grayscaleButton;
    private JPanel mainPanel;
    private JLabel imgLabel;
    private JTextField textField1;
    private JSlider slider1;
    private ImageIcon imageIcon;
    private File file = new File("/Users/tomek/Desktop/eye.jpg");
    private BufferedImage bufferedImage = ImageIO.read(file);
    @Override
    public void paint(Graphics g) {
        g.drawImage(bufferedImage,0,0,null);
    }

    private GreyScale() throws IOException {
        imageIcon = new ImageIcon(bufferedImage);
        imgLabel.setIcon(imageIcon);
//        grayscaleButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //greyScaleAlgorithm(bufferedImage);
//                //inverseColorAlgorithm(bufferedImage);
//            }
//        });
//        slider1.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent event) {
//                int value = slider1.getValue();
//                changeBrightnessAlgorithm(bufferedImage, slider1);
//            }
//        });
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    int treshold = Integer.parseInt(textField1.getText());
                    linearContrastChange(bufferedImage,treshold);
                }
            }
        });
    }
    public void greyScaleAlgorithm(BufferedImage img)
    {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                int rgb = img.getRGB(x,y);
                int r = getR(rgb);
                int g = getG(rgb);
                int b = getB(rgb);
                int avgRBG = (r+b+g)/3;
                int newRGB = (avgRBG<<16) | (avgRBG<<8) | avgRBG;
                img.setRGB(x,y,newRGB);
            }
        }
        try {
            ImageIO.write(img, "jpeg",new File("/Users/tomek/Desktop/out.jpg"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon grayImageIcon = new ImageIcon(img);
        imgLabel.setIcon(grayImageIcon);
    }
    public void inverseColorAlgorithm(BufferedImage img)
    {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                int rgb = img.getRGB(x,y);
                int r = inverseChannel(getR(rgb));
                int g = inverseChannel(getG(rgb));
                int b = inverseChannel(getB(rgb));
                int newRGB = toRGB(r,g,b);
                img.setRGB(x,y,newRGB);
            }
        }
        try {
            ImageIO.write(img, "jpeg",new File("/Users/tomek/Desktop/outInverse.jpg"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon grayImageIcon = new ImageIcon(img);
        imgLabel.setIcon(grayImageIcon);
    }
    public void changeBrightnessAlgorithm(BufferedImage img, JSlider jSlider)
    {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        for(int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                int rgb = img.getRGB(x, y);
                int r = changeBrightness(getR(rgb),jSlider.getValue());
                int g = changeBrightness(getG(rgb),jSlider.getValue());
                int b = changeBrightness(getB(rgb),jSlider.getValue());
                int newRGB = toRGB(r,g,b);
                img.setRGB(x,y,newRGB);
            }
        }
        try {
            ImageIO.write(img, "jpeg",new File("/Users/tomek/Desktop/outBrightness.jpg"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ImageIcon grayImageIcon = new ImageIcon(img);
        imgLabel.setIcon(grayImageIcon);
    }
    private void linearContrastChange(BufferedImage img, int contrastValue)
    {
        double contrastFactor = Math.pow((100.0 + contrastValue/10)/100.0,2.0);
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        for(int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                int rgb = img.getRGB(x, y);
                int prevR = getR(rgb);
                int prevG = getG(rgb);
                int prevB = getB(rgb);
                double r = (((( prevR / 255.0 ) - 0.5 ) * contrastFactor ) + 0.5 ) * 255.0;
                double g = ((((prevG / 255.0) - 0.5) * contrastFactor) + 0.5) * 255.0;
                double b = ((((prevB / 255.0) - 0.5) * contrastFactor) + 0.5) * 255.0;
                if(r>255) r = 255;
                if(r<0) r = 0;
                if(g>255) g = 255;
                if(g<0) g = 0;
                if(b>255) b = 255;
                if(b<0) b = 0;
                int newRGB = toRGB((int)r,(int)g,(int)b);
                img.setRGB(x,y,newRGB);
            }
        }
        try {
            ImageIO.write(img, "jpeg",new File("/Users/tomek/Desktop/outContrast.jpg"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon grayImageIcon = new ImageIcon(img);
        imgLabel.setIcon(grayImageIcon);
    }
    public static void main (String[] args)
    {
        JFrame jFrame = new JFrame("Change Contrast");
        try {
            jFrame.setContentPane(new GreyScale().mainPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jFrame.setSize(500,500);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
    public void tresholding(BufferedImage img, int tresholdLevel)
    {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        for(int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                int rgb = img.getRGB(x,y);
                int r = getR(rgb);
                int g = getG(rgb);
                int b = getB(rgb);
                int avgRBG = (r+b+g)/3;
                int newRGB = (avgRBG<<16) | (avgRBG<<8) | avgRBG;
                if(getR(newRGB) < tresholdLevel || getB(newRGB) < tresholdLevel || getR(newRGB) < tresholdLevel)
                {
                    newRGB = toRGB(0,0,0);
                }
                else{
                    newRGB = toRGB(255,255,255);
                }
                img.setRGB(x,y,newRGB);
            }
        }
        try {
            ImageIO.write(img, "jpeg",new File("/Users/tomek/Desktop/outBinarization.jpg"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon grayImageIcon = new ImageIcon(img);
        imgLabel.setIcon(grayImageIcon);
    }
    private static int getR(int in) {
        return (int)((in << 8) >> 24) & 0xff;
    }
    private static int getG(int in) {
        return (int)((in << 16) >> 24) & 0xff;
    }
    private static int getB(int in) {
        return (int)((in << 24) >> 24) & 0xff;
    }
    private static int toRGB(int r,int g,int b) {
        return (int)((((r << 8)|g) << 8)|b);
    }
    private static int inverseChannel(int value)
    {
        return 255-value;
    }
    private static int changeBrightness(int value,int sliderValue)
    {
        if(value + sliderValue <= 255)
            return value+sliderValue;
        else
            return 255;
    }
    //TODO:horizontal projection i vertival projection
}
