package ua.panchuk.solaris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable {

    public static Settings INSTANCE = new Settings();

    private static boolean music;

    public static boolean isSounds() {
        return sounds;
    }

    public static void setSounds(boolean sounds) {
        INSTANCE.sounds = sounds;
    }

    private static boolean sounds;

    private Settings() {
        music = true;
        sounds = true;
    }

    public static boolean isMusic() {
        return music;
    }

    public static void setMusic(boolean music) {
        INSTANCE.music = music;
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    public static void save() {
        FileHandle file = Gdx.files.local("settings.dat");
        try {
            file.writeBytes(serialize(INSTANCE), false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (Gdx.files.local("settings.dat").exists()) {
            FileHandle file = Gdx.files.local("settings.dat");
            try {
                INSTANCE = (Settings) deserialize(file.readBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }
}
