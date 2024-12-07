package it.unibo.oop.workers02;

//import java.lang.annotation.Inherited;
import java.util.List;
import java.util.stream.IntStream;
/**
 * primary class.
 */
public final class MultiThreadedSumMatrix implements SumMatrix {
    private final int nThreads;

    /**
     * @param nThreads number of thread performing the sum.
     */
    public MultiThreadedSumMatrix(final int nThreads) {
        this.nThreads = nThreads;
    }

    private static class Worker extends Thread {
        private final List<Double> list;
        private final int startpos;
        private final int nelem;
        private double res;

        /**
         * @param list the list to sum
         * 
         * @param startpos the initial position for this worker
         * 
         * @param nelem the no. of elems to sum up for this worker
         */
        Worker(final List<Double> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                this.res += this.list.get(i);
            }
        }
        /**
         * @return the sum of every element in the array
         */
        public double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final MatrixToArray transformer = new MatrixToArray(matrix, this.nThreads);
        final int size = transformer.getSize();
        return IntStream
                .iterate(0, start -> start + size)
                .limit(this.nThreads)
                .mapToObj(start -> new Worker(transformer.getList(), start, size))
                // Start them
                .peek(Thread::start)
                // Join them
                .peek(MultiThreadedSumMatrix::joinUninterruptibly)
                 // Get their result and sum
                .mapToDouble(Worker::getResult)
                .sum();
    }

    @SuppressWarnings("PMD.AvoidPrintStackTrace")
    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
