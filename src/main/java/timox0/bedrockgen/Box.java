package timox0.bedrockgen;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class Box<T> implements Serializable {
    static final long serialVersionUID = 33L;
    private ArrayList<T> data;
    private int sx, sy, sz;

    public Box(int x, int y, int z, T init) {
        sx = x;
        sy = y;
        sz = z;
        int size = x * y * z;
        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(init);
        }
    }

    public Box(int x, int y, int z, Getter<T> getter) {
        sx = x;
        sy = y;
        sz = z;
        int size = x * y * z;
        data = new ArrayList<>(size);
        for (int ix = 0; ix < sx; ix++) {
            for (int iy = 0; iy < sy; iy++) {
                for (int iz = 0; iz < sz; iz++) {
                    data.add(getter.get(ix, iy, iz));
                }
            }
        }
    }

    public void build(Setter<T> setter) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                for (int z = 0; z < sz; z++) {
                    setter.set(x, y, z, get(x, y, z));
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
        int i = z % sz + y * sz % sy + x * sz * sy % sx;
        data.set(i, value);
    }

    public T get(int x, int y, int z) {
        int i = z % sz + y % sy * sz + x % sx * sz * sy;
        return data.get(i);
    }

    public T getWarp(int x, int y, int z) {
        return get(x % sx, y % sy, z % sz);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < sx; x++) {
            sb.append("[\n");
            for (int y = 0; y < sy; y++) {
                sb.append("\t[");
                for (int z = 0; z < sz; z++) {
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

    public Box<T> flipX() {
        return new Box<T>(sx, sy, sz, (x, y, z) -> get(sx - 1 - x, y, z));
    }

    public Box<T> flipY() {
        return new Box<T>(sx, sy, sz, (x, y, z) -> get(x, sy - 1 - y, z));
    }

    public Box<T> flipZ() {
        return new Box<T>(sx, sy, sz, (x, y, z) -> get(x, y, sz - 1 - z));
    }

    public int sizeX() {
        return sx;
    }

    public int sizeY() {
        return sy;
    }

    public int sizeZ() {
        return sz;
    }

    public Box(Box<T> box) {
        sx = box.sx;
        sy = box.sy;
        sz = box.sz;
        data = new ArrayList<>(box.data.size());
        data.addAll(box.data);
    }

    public <N> Box<N> transform(Function<T, N> func) {
        return new Box<N>(sizeX(), sizeY(), sizeZ(), (x, y, z) -> func.apply(get(x, y, z)));
    }
}
