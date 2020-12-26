class ReturnsFieldButNotGetter {
    private Wire w = new Wire();

    // This is not a getter
    public Wire liveOne() {
        w.createPotentialDifference();
        return w;
    }
}