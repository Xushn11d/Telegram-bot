package org.example.backend.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.backend.LocalDateTimeJsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileWriterAndLoader<M> {
    private Path path;
    private Gson gson;
    public FileWriterAndLoader(String path) {
        this.path = Path.of(path);
        this.gson = buildGson();
    }

    public synchronized void write(List<M> list){
        String json = gson.toJson(list);
        try {
            Files.writeString(path,json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public synchronized List<M> load(Class<M> mClass) {
		try {
			String json = Files.readString(path);
			Type type = TypeToken.getParameterized(List.class, mClass).getType();
			ArrayList<M> arrayList = gson.fromJson(json, type);
			return arrayList != null ? arrayList : new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	
	
	
	private Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
//                .setDateFormat("dd/MM/yyyy HH:mm:ss")
//                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonSerializer())
//                .serializeNulls()
                .create();
    }


}
