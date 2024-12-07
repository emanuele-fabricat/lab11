package it.unibo.oop.workers02;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * A class that is made to transform the matrix in a array.
 * Also extract size an utility field that used in MultiThreadedSumMatrix.sum.
 */
public class MatrixToArray {
    private final List<Double> list;
    private final int size;

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
    /**
     * 
     * @return the size.
     */
    public int getSize() {
        return size;
    }
    /**
     * 
     * @return the list.
     */
    public List<Double> getList() {
        return List.copyOf(list);
    }
}
