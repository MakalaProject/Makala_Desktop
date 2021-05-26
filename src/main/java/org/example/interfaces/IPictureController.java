package org.example.interfaces;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.Picture;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface IPictureController {
    default void checkIndex(){
        getDeletButton().setVisible(getFiles().size() != 0 || getImageIndex() != 0);
        if(getFiles().size()<=1 && getImageIndex()==0){
            getNextPicture().setVisible(false);
            getPreviousPicture().setVisible(false);
        }
        else if(getImageIndex() == getFiles().size()-1){
            getNextPicture().setVisible(false);
            getPreviousPicture().setVisible(true);
        }
        else if(getImageIndex() == 0){
            getNextPicture().setVisible(true);
            getPreviousPicture().setVisible(false);
        }else{
            getPreviousPicture().setVisible(true);
            getNextPicture().setVisible(true);
        }
        if(getFiles().size() == 0){
            Image image = new Image(getDefaultPicture());
            getImage().setImage(image);
            return;
        }
        if(getFiles().get(getImageIndex()).contains("http://res.cloudinary.com")){
            Image image = new Image(getFiles().get(getImageIndex()), 300, 300, false, false);
            getImage().setImage(image);
        }else{
            File file = new File(getFiles().get(getImageIndex()));
            Image  image = new Image(file.toURI().toString(),300, 300, false, false);
            getImage().setImage(image);
        }
    }

    default List<String> deletePicture(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar imagen");
        alert.setContentText("¿Seguro quieres eliminarla?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            getDeleteFiles().add(getFiles().get(getImageIndex()));
            List<String> files = getFiles();
            String fileToRemove = files.get(getImageIndex());
            getPictures().removeIf(p -> fileToRemove.equals(p.getPath()));
            files.remove(getImageIndex());
            if(getImageIndex()!=0){
                decreaseIndex();
            }
            checkIndex();
            return files;
        }else{
            return getFiles();
        }
    }

    default String getDefaultPicture(){
        return getClass().getResource("/Images/product.png").toString();
    }

    default List<String> uploadImage(Stage s){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterJPEG
                = new FileChooser.ExtensionFilter("JPEG files (*.JPEG)", "*.JPEG");
        FileChooser.ExtensionFilter extFilterjpeg
                = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll();
        //Show open file dialog
        File file = fileChooser.showOpenDialog(s);
        if (file != null){
            Image img = new Image(file.toURI().toString(), 300, 300, false, false);
            List<String> files = getFiles();
            files.add(file.getPath());
            getPictures().add(new Picture(file.getPath()));
            getImage().setImage(img);
            updateIndex();
            checkIndex();
            return files;
        }else{
            return getFiles();
        }
    }

    void decreaseIndex();

    void updateIndex();

    List<String> getFiles();

    List<Picture> getPictures();

    List<String> getDeleteFiles();

    FontAwesomeIconView getNextPicture();

    FontAwesomeIconView getPreviousPicture();

    FontAwesomeIconView getDeletButton();

    ImageView getImage();

    int getImageIndex();

}
