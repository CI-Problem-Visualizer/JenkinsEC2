class MethodThatLooksLikeGetterButIsNot {
    private Scissors scissors;

    public Curtains getCurtains() {
        return new Curtains().trimUsing(scissors);
    }
}