/**
 * {{{
 */
public class BracesInComments {
    /**
     * {{{{
     */
    public int ok1() {
        // {{{
        int i = 0;
        return i;
    }

    // {{{{
    public int ok2() {
        int i = 1;
        return i;
    }
}