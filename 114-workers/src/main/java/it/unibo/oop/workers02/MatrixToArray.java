package it.unibo.oop.workers02;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixToArray {
    final private List<Double> list;
    final private int size;

    /**
     * 
     * @param matrix a matrix to extract the array.
     * 
     * @param nThread the number of thread used.
     */
    public MatrixToArray(final double[][] matrix, final int nThread) {
        this.list = Arrays.stream(matrix)
                .flatMapToDouble(Arrays :: stream)
                .boxed()
                .collect(Collectors.toList());
        this.size = list.size() % nThread + list.size() / nThread;
    }

    public int getSize() {
        return size;
    }

    public List<Double> getList() {
        return list;
    }

    
}
