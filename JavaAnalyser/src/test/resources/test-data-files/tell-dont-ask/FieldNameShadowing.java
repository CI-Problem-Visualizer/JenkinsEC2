class FieldNameShadowing {
    private Oppo p;

    public Phone upgrade() {
        Upgrade upgrade = new Upgrade();
        Samsung p = upgrade.applyTo(this.p);
        p.deliverToCustomer();
        return p;
    }
}