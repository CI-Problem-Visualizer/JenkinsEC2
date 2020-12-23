class ClassOverFiftyLines {
    public void thing() {
        if (1 == 2) {
            System.out.println("Hello");
        }
        int j = 0;
        for (int i = 5; i < 10; i++) {
            j += i;
        }
    }

    /**
     * This is a big fuck-off comment.
     * <p>
     * <p>
     * <p>
     * <p>
     * Stuff is happening in here.
     * <p>
     * Like using
     * up
     * <p>
     * so many
     * <p>
     * <p>
     * lines of this source file
     * <p>
     * to make it long for a test
     */
    public int number() {
        int i = 4;
        int k = 2;
        if (i + k < 2) {
            k++;
        }
        return k * 6;
    }

    // D
    // a
    // n
    // k
    //
    // M
    // e
    // m
    // e
    // s
    private void complain() {
        System.out.println("This aircon sucks");
    }
}