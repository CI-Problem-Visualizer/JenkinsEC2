class SetterParameterNameNotSameAsFieldName {
    private Bed bed;

    // This is a setter
    public void useKingSize(Bed kingSize) {
        this.bed = kingSize;
    }
}