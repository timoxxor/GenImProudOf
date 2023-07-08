package timox0.bedrockgen;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Box<T extends Serializable> implements Serializable {
    static final long serialVersionUID = 33L;
    private ArrayList<ArrayList<ArrayList<T>>> data;

    public Box(int x, int y, int z, T init) {
        data = new ArrayList<>(x);
        for (int ix = 0; ix < x; ix++) {
            data.add(new ArrayList<>(y));
            for (int iy = 0; iy < x; iy++) {
                data.get(ix).add(new ArrayList<>(z));
                for (int iz = 0; iz < x; iz++) {
                    data.get(ix).get(iy).add(init);
                }
            }
        }
    }

    public Box(int x, int y, int z, Getter<T> getter) {
        data = new ArrayList<>(x);
        for (int ix = 0; ix < x; ix++) {
            data.add(new ArrayList<>(y));
            for (int iy = 0; iy < x; iy++) {
                data.get(ix).add(new ArrayList<>(z));
                for (int iz = 0; iz < x; iz++) {
                    data.get(ix).get(iy).add(getter.get(ix, iy, iz));
                }
            }
        }
    }

    public void build(Setter<T> setter) {
        for (int x = 0; x < data.size(); x++) {
            for (int y = 0; y < data.get(0).size(); y++) {
                for (int z = 0; z < data.get(0).get(0).size(); z++) {
                    setter.set(x, y, z, data.get(x).get(y).get(z));
                }
            }
        }
    }

    static Object loadFormFile(File file) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(new FileInputStream(file)).readObject();
    }

    public void saveToFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    public void set(int x, int y, int z, T value) {
        data.get(x).get(y).set(z, value);
    }

    public T get(int x, int y, int z) {
        return data.get(x).get(y).get(z);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < data.size(); x++) {
            sb.append("[\n");
            for (int y = 0; y < data.get(0).size(); y++) {
                sb.append("\t[");
                for (int z = 0; z < data.get(0).get(0).size(); z++) {
                    sb.append(get(x, y, z));
                    sb.append(',');
                }
                sb.append("],\n");
            }
            sb.append("],\n");
        }
        return sb.toString();
    }

    @FunctionalInterface
    public interface Setter<Y> {
        void set(int x, int y, int z, Y value);
    }

    @FunctionalInterface
    public interface Getter<Y> {
        Y get(int x, int y, int z);
    }

    public void FlipX() {
        Collections.reverse(data);
    }
    public void FlipY() {
        data.forEach(Collections::reverse);
    }
    public void FlipZ() {
        data.forEach(l -> l.forEach(Collections::reverse));
    }

    public int sizeX() {
        return data.size();
    }

    public int sizeY() {
        return data.get(0).size();
    }

    public int sizeZ() {
        return data.get(0).get(0).size();
    }

    public Box<T> copy() {
        return new Box<T>(sizeX(),sizeY(),sizeZ(), this::get);
    }

}
