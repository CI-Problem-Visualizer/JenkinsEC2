class AssignmentToFieldButNotSetter {
    private X x;

    public void doSomething(Y y) {
        y.doSomething(x);
        X x = new X(y);
        this.x = x;
    }
}