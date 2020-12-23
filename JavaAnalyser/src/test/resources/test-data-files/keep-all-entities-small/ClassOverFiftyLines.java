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

    public int number() {
        int i = 4;
        int k = 2;
        if (i + k < 2) {
            k++;
        }
        return k * 6;
    }

    public Desk buildDesk(Nails nails, IkeaManual manual) {
        if (!manual.hasPages()) {
            throw new RuntimeException("The IKEA manual is fucked, we " +
                    "can't go on.");
        }
        RawMaterials rawMaterials = new RawMaterials();
        HalfAssumbledThing abomination = manual.instructionSet()
                .applyTo(rawMaterials, nails);
        DeskFactory factory = deskFactory();
        return factory.assembleFrom(abomination);
    }

    private DeskFactory deskFactory() {
        AirConditioningUnit airCon = AirConditioningUnit.getInstance();
        airCon.makeRoomHotter();
        complain();
        airCon.makeRoomCooler();
        complain();
        if (Ikea.isOpen()) {
            return new IkeaDeskFactory();
        } else {
            return new ImprovisedDeskFactory(
                    new GcseDtKnowledge(),
                    new Spanner(),
                    new Incompetence());
        }
    }

    private void complain() {
        System.out.println("This aircon sucks");
    }
}