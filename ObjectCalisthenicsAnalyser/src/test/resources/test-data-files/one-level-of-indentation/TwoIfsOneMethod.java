public class TwoIfsOneMethod {
    public int ok1() {
        int i = 0;
        if (true) {
            i = 1;
        }
        if (true) {
            i = 2;
        }
        return i;
    }
}