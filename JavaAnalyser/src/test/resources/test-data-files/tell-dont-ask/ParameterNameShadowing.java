class ParameterNameShadowing {
    String a;

    void a(String x) {
        x = "5";
        this.a = x;
    }
}