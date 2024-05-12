package picture;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;


public class LoadPicture{
	public static InputFile readImage(){
		InputFile toDo = new InputFile();
		toDo.setMedia(new File("java/picture/ToDo"));
		return toDo;
	}
	
	
	
	
}
