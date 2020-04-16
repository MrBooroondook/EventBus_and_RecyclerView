package mr.booroondook.eventbus_and_recyclerview;

class SurnameLoadEvent {
    private String surname;

    SurnameLoadEvent(String surname) {
        this.surname = surname;
    }

    String getSurname() {
        return surname;
    }
}
