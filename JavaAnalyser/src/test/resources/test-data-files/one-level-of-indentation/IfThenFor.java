public class IfThenFor {
    public int ok() {
        int i = 0;
        int j = 2;
        if (i == 3) {
            j = 3;
        }
        for (int i = 0; i < 5; i++) {
            j++;
        }
        return j;
    }
}