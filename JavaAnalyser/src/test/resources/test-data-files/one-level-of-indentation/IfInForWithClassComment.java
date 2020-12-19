/**
 * This is a class comment {{{{
 */
public class IfInForWithClassComment {
    public int notOk() {
        int i = 0;
        int j = 2;
        for (int k = 0; k < 5; k++) {
            j++;
            if (k == 3) {
                j = 3;
            }
        }
        return j;
    }
}