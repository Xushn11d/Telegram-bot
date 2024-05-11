package org.example.backend.service;

import org.example.backend.model.MyUser;
import org.example.backend.repository.FileWriterAndLoader;
import org.example.backend.statics.PathConstants;

import java.util.List;
import java.util.Objects;

public class UserService implements PathConstants {
    FileWriterAndLoader<MyUser> fileWriterOrLoader;

    public UserService() {
        this.fileWriterOrLoader = new FileWriterAndLoader<>(USERS_PATH);
    }
    public void save(MyUser myUser){
        List<MyUser> users= fileWriterOrLoader.load(MyUser.class);
        for (int i = 0; i < users.size(); i++) {
            MyUser cur= users.get(i);
            if (Objects.equals(myUser.getId(),cur.getId())){
                users.set(i,myUser);
                fileWriterOrLoader.write(users);
                return;
            }
        }
        users.add(myUser);
        fileWriterOrLoader.write(users);
    }
    public MyUser get(Long id){
        List<MyUser> users = fileWriterOrLoader.load(MyUser.class);
        for (int i = 0; i < users.size(); i++) {
            MyUser cur = users.get(i);
            if (Objects.equals(cur.getId(),id)){
                return cur;
            }
        }
        return null;
    }


}
